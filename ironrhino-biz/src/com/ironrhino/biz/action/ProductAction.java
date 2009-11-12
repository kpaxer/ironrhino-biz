package com.ironrhino.biz.action;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Category;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.model.Spec;
import com.ironrhino.biz.service.ProductManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class ProductAction extends BaseAction {

	private static final long serialVersionUID = -7021713504816843968L;

	private Product product;

	private ResultPage<Product> resultPage;

	private Long categoryId;

	private String specId;

	private List<Spec> specList;

	private transient BaseManager baseManager;

	@Autowired
	private transient ProductManager productManager;

	public ResultPage<Product> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Product> resultPage) {
		this.resultPage = resultPage;
	}

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	public List<Spec> getSpecList() {
		return specList;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setBaseManager(BaseManager baseManager) {
		this.baseManager = baseManager;

	}

	public String execute() {
		DetachedCriteria dc = productManager.detachedCriteria();
		Category category = null;
		if (categoryId != null) {
			category = (Category) baseManager.get(categoryId);
			if (category != null)
				dc.add(Restrictions.eq("category", category));
		}
		if (resultPage == null)
			resultPage = new ResultPage<Product>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(Order.asc("name"));
		resultPage = productManager.getResultPage(resultPage);
		return LIST;
	}

	public String input() {
		baseManager.setEntityClass(Spec.class);
		specList = baseManager.getAll();
		product = productManager.get(getUid());
		if (product == null)
			product = new Product();
		if (product != null) {
			if (product.getCategory() != null)
				categoryId = product.getCategory().getId();
			if (product.getSpec() != null)
				specId = product.getSpec().getId();
		} else {
			product = new Product();
		}
		return INPUT;
	}

	public String save() {
		if (product == null)
			return INPUT;
		if (product.isNew()) {
			Spec spec = null;
			baseManager.setEntityClass(Spec.class);
			if ((spec = (Spec) baseManager.get(specId)) != null)
				product.setSpec(spec);
			if (productManager.getByNaturalId("name", product.getName(),
					"spec", product.getSpec()) != null) {
				addActionError(getText("validation.already.exists"));
				return INPUT;
			}
			if (categoryId != null) {
				baseManager.setEntityClass(Category.class);
				Category category = (Category) baseManager.get(categoryId);
				product.setCategory(category);
			}
		} else {
			Product temp = product;
			product = productManager.get(temp.getId());
			if (product == null)
				return ERROR;
			product.setCriticalStock(temp.getCriticalStock());
		}
		productManager.save(product);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	public String delete() {
		String[] id = getId();
		if (id != null) {
			DetachedCriteria dc = productManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Product> list = productManager.getListByCriteria(dc);
			if (list.size() > 0) {
				for (Product product : list)
					productManager.delete(product);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

	public String category() {
		product = productManager.get(getUid());
		if (product != null && categoryId != null) {
			baseManager.setEntityClass(Category.class);
			product.setCategory((Category) baseManager.get(categoryId));
			productManager.save(product);
			addActionMessage(getText("operation.success"));
		}
		return "category";
	}

}
