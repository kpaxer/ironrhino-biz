package com.ironrhino.biz.action;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.ironrhino.core.hibernate.CriteriaState;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.Persistable;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.elasticsearch.ElasticSearchCriteria;
import org.ironrhino.core.search.elasticsearch.ElasticSearchService;
import org.ironrhino.core.service.EntityManager;
import org.ironrhino.core.struts.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.Stuffflow;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.StuffManager;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR + ","
		+ UserRole.ROLE_WAREHOUSEMAN)
public class StuffflowAction extends BaseAction {

	private static final long serialVersionUID = 3919769173051324935L;

	private boolean out;

	private Stuff stuff;

	private Stuffflow stuffflow;

	private ResultPage<Stuffflow> resultPage;

	private transient EntityManager<Stuffflow> entityManager;

	@Autowired
	private transient StuffManager stuffManager;

	@Autowired(required = false)
	private transient ElasticSearchService<Stuffflow> elasticSearchService;

	public Class<? extends Persistable<?>> getEntityClass() {
		return Stuffflow.class;
	}

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

	public void setEntityManager(EntityManager<Stuffflow> entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public String execute() {
		if (StringUtils.isBlank(keyword) || elasticSearchService == null) {
			entityManager.setEntityClass(Stuffflow.class);
			DetachedCriteria dc = entityManager.detachedCriteria();
			CriteriaState criteriaState = CriterionUtils.filter(dc,
					getEntityClass());
			if (StringUtils.isNotBlank(keyword))
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE, "memo"));
			if (criteriaState.getOrderings().isEmpty())
				dc.addOrder(Order.desc("date"));
			if (resultPage == null)
				resultPage = new ResultPage<Stuffflow>();
			resultPage.setCriteria(dc);
			resultPage = entityManager.findByResultPage(resultPage);
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
			ElasticSearchCriteria criteria = new ElasticSearchCriteria();
			criteria.setQuery(query);
			criteria.setTypes(new String[] { "stuffflow" });
			criteria.addSort("date", true);
			if (resultPage == null)
				resultPage = new ResultPage<Stuffflow>();
			resultPage.setCriteria(criteria);
			resultPage = elasticSearchService.search(resultPage);
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
		entityManager.save(stuffflow);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

}
