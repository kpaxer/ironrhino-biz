package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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

import com.ironrhino.biz.model.Brand;
import com.ironrhino.biz.model.Category;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.BrandManager;
import com.ironrhino.biz.service.CategoryManager;
import com.ironrhino.biz.service.ProductManager;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR + ","
		+ UserRole.ROLE_PRODUCTMANAGER)
public class ProductAction extends BaseAction {

	private static final long serialVersionUID = -7021713504816843968L;

	private Product product;

	private ResultPage<Product> resultPage;

	private Long categoryId;

	private Long brandId;

	private List<Category> categoryList;

	private List<Brand> brandList;

	@Inject
	private transient CategoryManager categoryManager;

	@Inject
	private transient BrandManager brandManager;

	@Inject
	private transient ProductManager productManager;

	@Autowired(required = false)
	private transient ElasticSearchService<Product> elasticSearchService;

	public ResultPage<Product> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Product> resultPage) {
		this.resultPage = resultPage;
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

	@Override
	public String execute() {
		if (StringUtils.isBlank(keyword) || elasticSearchService == null) {
			DetachedCriteria dc = productManager.detachedCriteria();
			Criterion filtering = CriterionUtils.filter(product, "id", "name");
			if (filtering != null)
				dc.add(filtering);
			if (StringUtils.isNotBlank(keyword))
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE, "name"));
			if (categoryId != null)
				dc.createAlias("category", "c").add(
						Restrictions.eq("c.id", categoryId));
			if (brandId != null)
				dc.createAlias("brand", "b").add(
						Restrictions.eq("b.id", brandId));
			dc.addOrder(Order.asc("displayOrder"));
			if (resultPage == null)
				resultPage = new ResultPage<Product>();
			resultPage.setCriteria(dc);
			resultPage = productManager.findByResultPage(resultPage);
		} else {
			String query = keyword.trim();
			ElasticSearchCriteria criteria = new ElasticSearchCriteria();
			criteria.setQuery(query);
			criteria.setTypes(new String[] { "product" });
			criteria.addSort("displayOrder", false);
			if (resultPage == null)
				resultPage = new ResultPage<Product>();
			resultPage.setCriteria(criteria);
			resultPage = elasticSearchService.search(resultPage, new Mapper<Product>() {
				public Product map(Product source) {
					return productManager.get(source.getId());
				}
			});
		}
		return LIST;
	}

	@Override
	public String input() {
		categoryList = categoryManager.findAll();
		brandList = brandManager.findAll();
		String id = getUid();
		if (org.ironrhino.core.util.StringUtils.isNumericOnly(id))
			product = productManager.get(Long.valueOf(id));
		if (product != null) {
			if (product.getCategory() != null && categoryId == null)
				categoryId = product.getCategory().getId();
			if (product.getBrand() != null && brandId == null)
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
		if (product.getShopStock() > 0
				&& product.getShopStock() > product.getStock()) {
			addFieldError("product.shopStock", "总库存不够");
			return INPUT;
		}
		if (product.isNew()) {
			Category category = categoryManager.get(categoryId);
			product.setCategory(category);
			Brand brand = brandManager.get(brandId);
			product.setBrand(brand);
			if (productManager.findByNaturalId("name", product.getName(),
					"brand", product.getBrand(), "category",
					product.getCategory()) != null) {
				addFieldError("product.name",
						getText("validation.already.exists"));
				return INPUT;
			}
		} else {
			Product temp = product;
			product = productManager.get(temp.getId());
			if (!product.getName().equals(temp.getName()) || brandId != null
					&& !product.getBrand().getId().equals(brandId)
					|| categoryId != null
					&& !product.getCategory().getId().equals(categoryId)) {
				if (productManager.findByNaturalId("name", temp.getName(),
						"brand", temp.getBrand(), "category",
						product.getCategory()) != null) {
					addFieldError("product.name",
							getText("validation.already.exists"));
					return INPUT;
				}
			}
			BeanUtils.copyProperties(temp, product);
			if (categoryId != null) {
				Category category = product.getCategory();
				if (category == null || !category.getId().equals(categoryId)) {
					product.setCategory(categoryManager.get(categoryId));
				}
			}
			if (brandId != null) {
				Brand brand = product.getBrand();
				if (brand == null || !brand.getId().equals(brandId)) {
					product.setBrand(brandManager.get(brandId));
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
			List<Product> list;
			if (id.length == 1) {
				list = new ArrayList<Product>(1);
				list.add(productManager.get(id[0]));
			} else {
				DetachedCriteria dc = productManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = productManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				boolean deletable = true;
				for (final Product product : list) {
					if (!productManager.canDelete(product)) {
						deletable = false;
						addActionError(product.getName() + "有订单,不能删除");
						break;
					}
				}
				if (deletable) {
					for (Product product : list)
						productManager.delete(product);
					addActionMessage(getText("delete.success"));
				}
			}
		}
		return SUCCESS;
	}

	@JsonConfig(root = "product")
	public String json() {
		String id = getUid();
		if (org.ironrhino.core.util.StringUtils.isNumericOnly(id)) {
			product = productManager.get(Long.valueOf(id));
		}
		return JSON;
	}

}
