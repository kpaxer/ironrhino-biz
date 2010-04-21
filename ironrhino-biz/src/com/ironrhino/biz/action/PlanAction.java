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
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Plan;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.service.PlanManager;
import com.ironrhino.biz.service.ProductManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR + "," + Constants.ROLE_HR)
public class PlanAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Plan plan;

	private ResultPage<Plan> resultPage;

	private Product product;

	private List<Product> productList;

	@Inject
	private transient PlanManager planManager;

	@Inject
	private transient ProductManager productManager;

	@Inject
	private transient CompassSearchService compassSearchService;

	public ResultPage<Plan> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Plan> resultPage) {
		this.resultPage = resultPage;
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
		if (StringUtils.isBlank(keyword)) {
			DetachedCriteria dc = planManager.detachedCriteria();
			if (resultPage == null)
				resultPage = new ResultPage<Plan>();
			resultPage.setDetachedCriteria(dc);
			if (product != null && product.getId() != null)
				dc.createAlias("product", "c").add(
						Restrictions.eq("c.id", product.getId()));
			resultPage.addOrder(org.hibernate.criterion.Order.desc("planDate"));
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
			CompassCriteria cc = new CompassCriteria();
			cc.setQuery(query);
			cc.setAliases(new String[] { "plan" });
			if (resultPage == null)
				resultPage = new ResultPage<Plan>();
			cc.setPageNo(resultPage.getPageNo());
			cc.setPageSize(resultPage.getPageSize());
			CompassSearchResults searchResults = compassSearchService
					.search(cc);
			int totalHits = searchResults.getTotalHits();
			CompassHit[] hits = searchResults.getHits();
			if (hits != null) {
				List<Plan> list = new ArrayList<Plan>(hits.length);
				for (CompassHit ch : searchResults.getHits()) {
					Plan p = (Plan) ch.data();
					p.setProduct(productManager.get(p.getProduct().getId()));
					list.add(p);
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
		productList = productManager.findAll(org.hibernate.criterion.Order
				.asc("displayOrder"));
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

}
