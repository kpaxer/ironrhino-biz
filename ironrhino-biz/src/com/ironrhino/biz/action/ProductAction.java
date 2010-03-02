package com.ironrhino.biz.action;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Brand;
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

	private Long brandId;

	private Long specId;

	private List<Category> categoryList;

	private List<Brand> brandList;

	private List<Spec> specList;

	private transient BaseManager baseManager;

	@Inject
	private transient ProductManager productManager;

	public ResultPage<Product> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Product> resultPage) {
		this.resultPage = resultPage;
	}

	public Long getSpecId() {
		return specId;
	}

	public void setSpecId(Long specId) {
		this.specId = specId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public List<Spec> getSpecList() {
		return specList;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public List<Brand> getBrandList() {
		return brandList;
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

	@Override
	public String execute() {
		DetachedCriteria dc = productManager.detachedCriteria();
		if (categoryId != null)
			dc.createAlias("category", "c").add(
					Restrictions.eq("c.id", categoryId));
		if (brandId != null)
			dc.createAlias("brand", "b").add(
					Restrictions.eq("b.id", brandId));
		if (resultPage == null)
			resultPage = new ResultPage<Product>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(Order.asc("name"));
		resultPage = productManager.findByResultPage(resultPage);
		return LIST;
	}

	@Override
	public String input() {
		baseManager.setEntityClass(Spec.class);
		specList = baseManager.findAll();
		Collections.sort(specList);
		baseManager.setEntityClass(Category.class);
		categoryList = baseManager.findAll();
		Collections.sort(categoryList);
		baseManager.setEntityClass(Brand.class);
		brandList = baseManager.findAll();
		Collections.sort(brandList);
		String id = getUid();
		if (StringUtils.isNumeric(id))
			product = productManager.get(Long.valueOf(id));
		if (product != null) {
			if (product.getSpec() != null)
				specId = product.getSpec().getId();
			if (product.getCategory() != null)
				categoryId = product.getCategory().getId();
			if (product.getBrand() != null)
				brandId = product.getBrand().getId();
		} else {
			product = new Product();
		}
		return INPUT;
	}

	@Override
	public String save() {
		if (product == null)
			return ACCESSDENIED;
		if (product.isNew()) {
			if (specId != null) {
				baseManager.setEntityClass(Spec.class);
				Spec spec = (Spec) baseManager.get(specId);
				product.setSpec(spec);
			}
			if (categoryId != null) {
				baseManager.setEntityClass(Category.class);
				Category category = (Category) baseManager.get(categoryId);
				product.setCategory(category);
			}
			if (brandId != null) {
				baseManager.setEntityClass(Brand.class);
				Brand brand = (Brand) baseManager.get(brandId);
				product.setBrand(brand);
			}
		} else {
			Product temp = product;
			product = productManager.get(temp.getId());
			if (product == null)
				return ERROR;
			BeanUtils.copyProperties(temp, product);
			if (categoryId != null) {
				Category category = product.getCategory();
				if (category == null || !category.getId().equals(categoryId)) {
					baseManager.setEntityClass(Category.class);
					product.setCategory((Category) baseManager.get(categoryId));
				}
			}
			if (brandId != null) {
				Brand brand = product.getBrand();
				if (brand == null || !brand.getId().equals(brandId)) {
					baseManager.setEntityClass(Brand.class);
					product.setBrand((Brand) baseManager.get(brandId));
				}
			}
			if (specId != null) {
				Spec spec = product.getSpec();
				if (spec == null || !spec.getId().equals(specId)) {
					baseManager.setEntityClass(Spec.class);
					product.setSpec((Spec) baseManager.get(specId));
				}
			}
		}
		productManager.save(product);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String delete() {
		String[] _id = getId();
		if (_id != null) {
			Long[] id = new Long[_id.length];
			for (int i = 0; i < _id.length; i++)
				id[i] = Long.valueOf(_id[i]);
			DetachedCriteria dc = productManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Product> list = productManager.findListByCriteria(dc);
			if (list.size() > 0) {
				for (Product product : list)
					productManager.delete(product);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

}
