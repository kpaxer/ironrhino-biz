package com.ironrhino.biz.action;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.ironrhino.core.hibernate.CriteriaState;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.model.Persistable;
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

	@Autowired
	private transient CategoryManager categoryManager;

	@Autowired
	private transient BrandManager brandManager;

	@Autowired
	private transient ProductManager productManager;

	@Autowired(required = false)
	private transient ElasticSearchService<Product> elasticSearchService;

	public Class<? extends Persistable<?>> getEntityClass() {
		return Product.class;
	}

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
			CriteriaState criteriaState = CriterionUtils.filter(dc,
					getEntityClass());
			if (StringUtils.isNotBlank(keyword))
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE, "name"));
			dc.addOrder(Order.asc("displayOrder"));
			if (criteriaState.getAliases().containsKey("brand"))
				dc.addOrder(Order.asc(criteriaState.getAliases().get("brand")
						+ ".name"));
			else
				dc.createAlias("brand", "b").addOrder(Order.asc("b.name"));
			if (criteriaState.getAliases().containsKey("category"))
				dc.addOrder(Order.asc(criteriaState.getAliases()
						.get("category") + ".name"));
			else
				dc.createAlias("category", "c").addOrder(Order.asc("c.name"));
			if (criteriaState.getOrderings().isEmpty())
				dc.addOrder(Order.asc("name"));
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
			resultPage = elasticSearchService.search(resultPage,
					new Mapper<Product>() {
						@Override
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
		if (StringUtils.isNumeric(id))
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
		String[] id = getId();
		if (id != null) {
			productManager.delete((Serializable[]) id);
			addActionMessage(getText("delete.success"));
		}
		return SUCCESS;
	}

	@JsonConfig(root = "product")
	public String json() {
		String id = getUid();
		if (StringUtils.isNumeric(id)) {
			product = productManager.get(Long.valueOf(id));
		}
		return JSON;
	}

}
