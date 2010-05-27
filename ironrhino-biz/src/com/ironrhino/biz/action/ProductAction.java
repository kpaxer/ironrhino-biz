package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.compass.core.CompassHit;
import org.compass.core.support.search.CompassSearchResults;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.model.Brand;
import com.ironrhino.biz.model.Category;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.BrandManager;
import com.ironrhino.biz.service.CategoryManager;
import com.ironrhino.biz.service.ProductManager;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR)
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

	@Inject
	private transient CompassSearchService compassSearchService;

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
		if (StringUtils.isBlank(keyword)) {
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
			resultPage.addOrder(Order.asc("displayOrder"));
			resultPage = productManager.findByResultPage(resultPage);
		} else {
			String query = keyword.trim();
			CompassCriteria cc = new CompassCriteria();
			cc.setQuery(query);
			cc.setAliases(new String[] { "product" });
			if (resultPage == null)
				resultPage = new ResultPage<Product>();
			cc.setPageNo(resultPage.getPageNo());
			cc.setPageSize(resultPage.getPageSize());
			CompassSearchResults searchResults = compassSearchService
					.search(cc);
			int totalHits = searchResults.getTotalHits();
			CompassHit[] hits = searchResults.getHits();
			if (hits != null) {
				List<Product> list = new ArrayList<Product>(hits.length);
				for (CompassHit ch : searchResults.getHits()) {
					Product c = (Product) ch.getData();
					c = productManager.get(c.getId());
					if (c != null)
						list.add(c);
					else
						totalHits--;
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
		categoryList = categoryManager.findAll(Order.asc("displayOrder"));
		brandList = brandManager.findAll(Order.asc("displayOrder"));
		String id = getUid();
		if (StringUtils.isNumeric(id))
			product = productManager.get(Long.valueOf(id));
		if (product != null) {
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
					"brand", product.getBrand(), "category", product
							.getCategory()) != null) {
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
						"brand", temp.getBrand(), "category", product
								.getCategory()) != null) {
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
		if (StringUtils.isNumeric(id)) {
			product = productManager.get(Long.valueOf(id));
		}
		return JSON;
	}

}
