package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.SearchService.Mapper;
import org.ironrhino.core.search.compass.CompassSearchCriteria;
import org.ironrhino.core.search.compass.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.model.Plan;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.PlanManager;
import com.ironrhino.biz.service.ProductManager;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR)
public class PlanAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Plan plan;

	private ResultPage<Plan> resultPage;

	private Product product;

	private List<Product> productList;

	private List<Plan> uncompletedPlans;

	@Inject
	private transient PlanManager planManager;

	@Inject
	private transient ProductManager productManager;

	@Autowired(required = false)
	private transient CompassSearchService compassSearchService;

	public ResultPage<Plan> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Plan> resultPage) {
		this.resultPage = resultPage;
	}

	public List<Plan> getUncompletedPlans() {
		return uncompletedPlans;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String execute() {
		if (StringUtils.isBlank(keyword) || compassSearchService == null) {
			DetachedCriteria dc = planManager.detachedCriteria();
			Criterion filtering = CriterionUtils.filter(plan, "id", "planDate",
					"completeDate", "completed");
			if (filtering != null)
				dc.add(filtering);
			if (StringUtils.isNotBlank(keyword))
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE, "memo"));
			if (product != null && product.getId() != null)
				dc.createAlias("product", "c").add(
						Restrictions.eq("c.id", product.getId()));
			dc.addOrder(org.hibernate.criterion.Order.desc("planDate"));
			if (resultPage == null)
				resultPage = new ResultPage<Plan>();
			resultPage.setCriteria(dc);
			resultPage = planManager.findByResultPage(resultPage);
		} else {
			String query = keyword.trim();
			if (query.matches("^\\d{4}-\\d{2}-\\d{2}$"))
				query = "planDate:" + query;
			if (query.matches("^\\d{4}年\\d{2}月\\d{2}日$")) {
				StringBuilder sb = new StringBuilder();
				sb.append("planDate:");
				sb.append(query.substring(0, 4));
				sb.append('-');
				sb.append(query.substring(5, 7));
				sb.append('-');
				sb.append(query.substring(8, 10));
				query = sb.toString();
			}
			CompassSearchCriteria criteria = new CompassSearchCriteria();
			criteria.addSort("planDate", true);
			criteria.setQuery(query);
			criteria.setAliases(new String[] { "plan" });
			if (resultPage == null)
				resultPage = new ResultPage<Plan>();
			resultPage.setCriteria(criteria);
			resultPage = compassSearchService.search(resultPage, new Mapper() {
				public Object map(Object source) {
					Plan p = (Plan) source;
					p.setProduct(productManager.get(p.getProduct().getId()));
					return p;
				}
			});
		}
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNotBlank(id))
			plan = planManager.get(id);
		if (plan == null) {
			plan = new Plan();
			if (product != null && product.getId() != null) {
				product = productManager.get(product.getId());
			}
		} else {
			product = plan.getProduct();
		}
		productList = productManager.findAll();
		return INPUT;
	}

	@Override
	public String save() {
		if (plan == null)
			return INPUT;
		if (plan.isNew()) {
			plan.setProduct(productManager.get(product.getId()));
		} else {
			Plan temp = plan;
			plan = planManager.get(temp.getId());
			if (plan.isCompleted()) {
				plan.setMemo(temp.getMemo());
			} else {
				BeanUtils.copyProperties(temp, plan);
				if (product != null
						&& !plan.getProduct().getId().equals(product.getId())) {
					plan.setProduct(productManager.get(product.getId()));
				}
			}
		}
		planManager.save(plan);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String delete() {
		String[] id = getId();
		if (id != null) {
			List<Plan> list;
			if (id.length == 1) {
				list = new ArrayList<Plan>(1);
				list.add(planManager.get(id[0]));
			} else {
				DetachedCriteria dc = planManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = planManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				boolean deletable = true;
				for (Plan temp : list) {
					if (!planManager.canDelete(temp)) {
						deletable = false;
						addActionError("已经完成的计划不能删除");
						break;
					}
				}
				if (deletable)
					for (Plan temp : list) {
						planManager.delete(temp);
					}
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

	public String complete() {
		String[] id = getId();
		if (id != null) {
			List<Plan> list;
			if (id.length == 1) {
				list = new ArrayList<Plan>(1);
				list.add(planManager.get(id[0]));
			} else {
				DetachedCriteria dc = planManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = planManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				for (Plan temp : list)
					planManager.complete(temp);
				addActionMessage(getText("operate.success"));
			}
		}
		return REFERER;
	}

	public String uncompleted() {
		DetachedCriteria dc = planManager.detachedCriteria();
		dc.add(Restrictions.eq("completed", false));
		uncompletedPlans = planManager.findListByCriteria(dc);
		return "uncompleted";
	}

}
