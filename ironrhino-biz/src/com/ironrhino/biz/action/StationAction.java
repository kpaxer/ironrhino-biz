package com.ironrhino.biz.action;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.Region;
import org.ironrhino.common.support.RegionTreeControl;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.SearchService.Mapper;
import org.ironrhino.core.search.elasticsearch.ElasticSearchCriteria;
import org.ironrhino.core.search.elasticsearch.ElasticSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.model.Station;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.StationManager;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR + ","
		+ UserRole.ROLE_CUSTOMERMANAGER)
public class StationAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Station station;

	private ResultPage<Station> resultPage;

	private Long regionId;

	@Inject
	private transient StationManager stationManager;

	@Inject
	private transient RegionTreeControl regionTreeControl;

	@Autowired(required = false)
	private transient ElasticSearchService<Station> elasticSearchService;

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

	public Map<String, String> getCashConditionMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		int type = Calendar.DAY_OF_WEEK;
		map.put(type + "," + Calendar.MONDAY, "每周一");
		map.put(type + "," + Calendar.TUESDAY, "每周二");
		map.put(type + "," + Calendar.WEDNESDAY, "每周三");
		map.put(type + "," + Calendar.THURSDAY, "每周四");
		map.put(type + "," + Calendar.FRIDAY, "每周五");
		map.put(type + "," + Calendar.SATURDAY, "每周六");
		map.put(type + "," + Calendar.SUNDAY, "每周天");
		type = Calendar.DAY_OF_MONTH;
		for (int i = 1; i <= 31; i++)
			map.put(type + "," + i, "每月" + i + "号");
		return map;
	}

	@Override
	public String execute() {
		if (StringUtils.isBlank(keyword) || elasticSearchService == null) {
			DetachedCriteria dc = stationManager.detachedCriteria();
			Criterion filtering = CriterionUtils.filter(station, "id", "name");
			if (filtering != null)
				dc.add(filtering);
			if (StringUtils.isNotBlank(keyword))
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE, "name",
						"phone", "mobile", "address", "destination"));
			Region region = null;
			if (regionId != null) {
				region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(regionId);
				if (region != null && !region.isRoot()) {
					if (region.isLeaf()) {
						dc.add(Restrictions.eq("region", region));
					} else {
						dc.add(Restrictions.in("region",
								region.getDescendantsAndSelf()));
					}
				}
			}
			dc.addOrder(org.hibernate.criterion.Order.asc("id"));
			if (resultPage == null)
				resultPage = new ResultPage<Station>();
			resultPage.setCriteria(dc);
			resultPage = stationManager.findByResultPage(resultPage);
			for (Station c : resultPage.getResult())
				if (c.getRegion() != null)
					c.setRegion(regionTreeControl.getRegionTree()
							.getDescendantOrSelfById(c.getRegion().getId()));
		} else {
			String query = keyword.trim();
			ElasticSearchCriteria criteria = new ElasticSearchCriteria();
			criteria.setQuery(query);
			criteria.setTypes(new String[] { "station" });
			if (resultPage == null)
				resultPage = new ResultPage<Station>();
			resultPage.setCriteria(criteria);
			resultPage = elasticSearchService.search(resultPage,
					new Mapper<Station>() {
						public Station map(Station source) {
							Station s = (Station) source;
							if (s.getRegion() != null)
								s.setRegion(regionTreeControl.getRegionTree()
										.getDescendantOrSelfById(
												s.getRegion().getId()));
							return s;
						}
					});
		}
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNotBlank(id))
			if (StringUtils.isNumeric(id))
				station = stationManager.get(Long.valueOf(id));
			else
				station = stationManager.findByNaturalId(id);
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
	@Validations(requiredStrings = { @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "station.name", trim = true, key = "validation.required") })
	public String save() {
		if (!makeEntityValid())
			return INPUT;
		stationManager.save(station);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	public String checkavailable() {
		return makeEntityValid() ? NONE : INPUT;
	}

	private boolean makeEntityValid() {
		if (station == null) {
			addActionError(getText("access.denied"));
			return false;
		}
		if (station.isNew()) {
			if (StringUtils.isNotBlank(station.getName())) {
				if (stationManager.findByNaturalId(station.getName()) != null) {
					addFieldError("station.name",
							getText("validation.already.exists"));
					return false;
				}
			}
			if (regionId != null) {
				Region region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(regionId);
				station.setRegion(region);
			}
		} else {
			Station temp = station;
			station = stationManager.get(temp.getId());
			if (StringUtils.isNotBlank(temp.getName())
					&& !station.getName().equals(temp.getName())) {
				if (stationManager.findByNaturalId(temp.getName()) != null) {
					addFieldError("station.name",
							getText("validation.already.exists"));
					return false;
				}
			}
			if (regionId != null) {
				Region r = station.getRegion();
				if (r == null || !r.getId().equals(regionId)) {
					Region region = regionTreeControl.getRegionTree()
							.getDescendantOrSelfById(regionId);
					station.setRegion(region);
				}
			} else if (ServletActionContext.getRequest().getParameter(
					"regionId") != null)
				station.setRegion(null);
			if (temp.getAddress() == null)
				temp.setAddress(station.getAddress());
			if (temp.getCashCondition() == null)
				temp.setCashCondition(station.getCashCondition());
			if (temp.getMemo() == null)
				temp.setMemo(station.getMemo());
			BeanUtils.copyProperties(temp, station);
			stationManager.evict(station);
		}
		return true;
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
		String[] id = getId();
		if (id != null) {
			stationManager.delete((Serializable[]) id);
			addActionMessage(getText("delete.success"));
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
				ElasticSearchCriteria criteria = new ElasticSearchCriteria();
				criteria.setQuery(id);
				criteria.setTypes(new String[] { "station" });
				List<Station> list = elasticSearchService.search(criteria);
				if (list.size() == 1) {
					station = list.get(1);
				} else {
					StringBuilder sb = new StringBuilder();
					for (Station s : list)
						sb.append(s.getName()).append(",");
					sb.deleteCharAt(sb.length() - 1);
					station = new Station();
					station.setName(sb.toString());
				}
			}
		}
		return JSON;
	}

}
