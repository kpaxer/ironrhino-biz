package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.compass.core.CompassHit;
import org.compass.core.support.search.CompassSearchResults;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.Region;
import org.ironrhino.common.support.RegionTreeControl;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Station;
import com.ironrhino.biz.service.StationManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class StationAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Station station;

	private ResultPage<Station> resultPage;

	private Long regionId;

	@Inject
	private transient StationManager stationManager;

	@Inject
	private transient RegionTreeControl regionTreeControl;

	@Inject
	private transient CompassSearchService compassSearchService;

	public ResultPage<Station> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Station> resultPage) {
		this.resultPage = resultPage;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	@Override
	public String execute() {
		if (StringUtils.isBlank(keyword)) {
			DetachedCriteria dc = stationManager.detachedCriteria();
			Region region = null;
			if (regionId != null) {
				region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(regionId);
				if (region != null && !region.isRoot()) {
					if (region.isLeaf()) {
						dc.add(Restrictions.eq("region", region));
					} else {
						dc.add(Restrictions.in("region", region
								.getDescendantsAndSelf()));
					}
				}
			}
			if (resultPage == null)
				resultPage = new ResultPage<Station>();
			resultPage.setDetachedCriteria(dc);
			resultPage.addOrder(org.hibernate.criterion.Order.asc("id"));
			resultPage = stationManager.findByResultPage(resultPage);
			for (Station c : resultPage.getResult())
				if (c.getRegion() != null)
					c.setRegion(regionTreeControl.getRegionTree()
							.getDescendantOrSelfById(c.getRegion().getId()));
		} else {
			String query = keyword.trim();
			CompassCriteria cc = new CompassCriteria();
			cc.setQuery(query);
			cc.setAliases(new String[] { "station" });
			if (resultPage == null)
				resultPage = new ResultPage<Station>();
			cc.setPageNo(resultPage.getPageNo());
			cc.setPageSize(resultPage.getPageSize());
			CompassSearchResults searchResults = compassSearchService
					.search(cc);
			int totalHits = searchResults.getTotalHits();
			CompassHit[] hits = searchResults.getHits();
			if (hits != null) {
				List<Station> list = new ArrayList<Station>(hits.length);
				for (CompassHit ch : searchResults.getHits()) {
					Station c = (Station) ch.getData();
					c = stationManager.get(c.getId());
					if (c != null) {
						if (c.getRegion() != null)
							c.setRegion(regionTreeControl.getRegionTree()
									.getDescendantOrSelfById(
											c.getRegion().getId()));
						list.add(c);
					} else {
						totalHits--;
					}
				}
				resultPage.setResult(list);
			} else {
				resultPage.setResult(Collections.EMPTY_LIST);
			}
			resultPage.setTotalRecord(totalHits);
		}
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNumeric(id))
			station = stationManager.get(Long.valueOf(id));
		if (station == null)
			station = new Station();
		else {
			Region region = station.getRegion();
			if (region != null) {
				region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(region.getId());
				if (region != null) {
					regionId = region.getId();
					station.setRegion(region);
				}
			}
		}
		return INPUT;
	}

	@Override
	public String save() {
		if (station == null)
			return INPUT;
		if (station.isNew()) {
			if (stationManager.findByNaturalId(station.getName()) != null) {
				addFieldError("station.name",
						getText("validation.already.exists"));
				return INPUT;
			}
			if (regionId != null) {
				Region region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(regionId);
				station.setRegion(region);
			}
		} else {
			Station temp = station;
			station = stationManager.get(temp.getId());
			if (!station.getName().equals(temp.getName())) {
				if (stationManager.findByNaturalId(temp.getName()) != null) {
					addFieldError("station.name",
							getText("validation.already.exists"));
					return INPUT;
				}
			}
			if (regionId != null) {
				Region r = station.getRegion();
				if (r == null || !r.getId().equals(regionId)) {
					Region region = regionTreeControl.getRegionTree()
							.getDescendantOrSelfById(regionId);
					station.setRegion(region);
				}
			}
			if (temp.getAddress() == null)
				temp.setAddress(station.getAddress());
			if (temp.getMemo() == null)
				temp.setMemo(station.getMemo());
			BeanUtils.copyProperties(temp, station);
		}
		stationManager.save(station);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String view() {
		String id = getUid();
		if (StringUtils.isNumeric(id)) {
			station = stationManager.get(Long.valueOf(id));
			if (station.getRegion() != null)
				station.setRegion(regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(station.getRegion().getId()));
		} else {
			return ACCESSDENIED;
		}
		return VIEW;
	}

	@Override
	public String delete() {
		String[] _id = getId();
		if (_id != null) {
			Long[] id = new Long[_id.length];
			for (int i = 0; i < _id.length; i++)
				id[i] = Long.valueOf(_id[i]);
			List<Station> list;
			if (id.length == 1) {
				list = new ArrayList<Station>(1);
				list.add(stationManager.get(id[0]));
			} else {
				DetachedCriteria dc = stationManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = stationManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				boolean deletable = true;
				for (Station c : list) {
					if (!stationManager.canDelete(c)) {
						deletable = false;
						addActionError(c.getName() + "有订单,不能删除只能合并到其他客户");
						break;
					}
				}
				if (deletable) {
					for (Station station : list)
						stationManager.delete(station);
					addActionMessage(getText("delete.success"));
				}
			}
		}
		return SUCCESS;
	}

	public String merge() {
		String[] id = getId();
		if (id != null && id.length == 2) {
			Station source = stationManager.findByNaturalId(id[0].trim());
			Station target = stationManager.findByNaturalId(id[1].trim());
			stationManager.merge(source, target);
			addActionMessage(getText("operate.success"));
		}
		return SUCCESS;
	}

	@JsonConfig(root = "station")
	public String json() {
		String id = getUid();
		if (StringUtils.isNumeric(id)) {
			station = stationManager.get(Long.valueOf(id));
		} else if (StringUtils.isNotBlank(id)) {
			id = id.trim();
			station = stationManager.findByNaturalId(id);
			if (station == null) {
				CompassCriteria cc = new CompassCriteria();
				cc.setQuery(id);
				cc.setAliases(new String[] { "station" });
				cc.setPageNo(1);
				cc.setPageSize(10);
				CompassSearchResults searchResults = compassSearchService
						.search(cc);
				int hits = searchResults.getTotalHits();
				if (hits == 1) {
					station = (Station) searchResults.getHits()[0].getData();
				} else if (hits > 1) {
					StringBuilder sb = new StringBuilder();
					for (CompassHit ch : searchResults.getHits())
						sb.append(((Station) ch.getData()).getName()).append(
								",");
					sb.deleteCharAt(sb.length() - 1);
					station = new Station();
					station.setName(sb.toString());
				}
			}
		}
		return JSON;
	}

}