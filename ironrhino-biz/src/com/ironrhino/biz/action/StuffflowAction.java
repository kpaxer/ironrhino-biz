package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.compass.core.CompassHit;
import org.compass.core.support.search.CompassSearchResults;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.Stuffflow;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.StuffManager;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR)
public class StuffflowAction extends BaseAction {

	private static final long serialVersionUID = 3919769173051324935L;

	private boolean out;

	private Stuff stuff;

	private List<Stuff> stuffList;

	private Stuffflow stuffflow;

	private ResultPage<Stuffflow> resultPage;

	private transient BaseManager baseManager;

	@Inject
	private transient StuffManager stuffManager;

	@Autowired(required = false)
	private transient CompassSearchService compassSearchService;

	public boolean isOut() {
		return out;
	}

	public void setOut(boolean out) {
		this.out = out;
	}

	public Stuff getStuff() {
		return stuff;
	}

	public void setStuff(Stuff stuff) {
		this.stuff = stuff;
	}

	public List<Stuff> getStuffList() {
		return stuffList;
	}

	public Stuffflow getStuffflow() {
		return stuffflow;
	}

	public void setStuffflow(Stuffflow stuffflow) {
		this.stuffflow = stuffflow;
	}

	public ResultPage<Stuffflow> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Stuffflow> resultPage) {
		this.resultPage = resultPage;
	}

	public void setBaseManager(BaseManager baseManager) {
		this.baseManager = baseManager;

	}

	@Override
	public String execute() {
		if (StringUtils.isBlank(keyword) || compassSearchService == null) {
			baseManager.setEntityClass(Stuffflow.class);
			DetachedCriteria dc = baseManager.detachedCriteria();
			if (StringUtils.isNotBlank(keyword))
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE, "memo"));
			if (stuff != null && stuff.getId() != null)
				dc.createAlias("stuff", "s").add(
						Restrictions.eq("s.id", stuff.getId()));
			dc.addOrder(Order.desc("date"));
			if (resultPage == null)
				resultPage = new ResultPage<Stuffflow>();
			resultPage.setDetachedCriteria(dc);
			resultPage = baseManager.findByResultPage(resultPage);
		} else {
			String query = keyword.trim();
			if (query.matches("^\\d{4}-\\d{2}-\\d{2}$"))
				query = "date:" + query;
			if (query.matches("^\\d{4}年\\d{2}月\\d{2}日$")) {
				StringBuilder sb = new StringBuilder();
				sb.append("date:");
				sb.append(query.substring(0, 4));
				sb.append('-');
				sb.append(query.substring(5, 7));
				sb.append('-');
				sb.append(query.substring(8, 10));
				query = sb.toString();
			}
			CompassCriteria cc = new CompassCriteria();
			cc.setQuery(query);
			cc.setAliases(new String[] { "stuffflow" });
			cc.addSort("date", null, true);
			if (resultPage == null)
				resultPage = new ResultPage<Stuffflow>();
			cc.setPageNo(resultPage.getPageNo());
			cc.setPageSize(resultPage.getPageSize());
			CompassSearchResults searchResults = compassSearchService
					.search(cc);
			int totalHits = searchResults.getTotalHits();
			CompassHit[] hits = searchResults.getHits();
			if (hits != null) {
				List<Stuffflow> list = new ArrayList<Stuffflow>(hits.length);
				for (CompassHit ch : searchResults.getHits())
					list.add((Stuffflow) ch.data());
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
		stuff = stuffManager.get(stuff.getId());
		stuffflow = new Stuffflow();
		return INPUT;
	}

	@Override
	public String save() {
		stuff = stuffManager.get(stuff.getId());
		if (out)
			stuffflow.setQuantity(-stuffflow.getQuantity());
		stuffflow.setStuff(stuff);
		stuff.setStock(stuff.getStock() + stuffflow.getQuantity());
		if (out && stuff.getStock() < 0) {
			addFieldError("stuffflow.quantity", "库存不够");
		}
		stuffManager.save(stuff);
		baseManager.save(stuffflow);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

}
