package com.ironrhino.biz.action;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.HtmlUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Category;
import com.ironrhino.biz.support.CategoryTreeControl;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class CategoryAction extends BaseAction {

	private static final long serialVersionUID = -2735040460711165402L;

	private Category category;

	private Long parentId;

	private Collection<Category> list;

	@Inject
	private transient CategoryTreeControl categoryTreeControl;

	private transient BaseManager<Category> baseManager;

	private boolean async;

	private long root;

	public long getRoot() {
		return root;
	}

	public void setRoot(long root) {
		this.root = root;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public Collection<Category> getList() {
		return list;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setBaseManager(BaseManager<Category> baseManager) {
		this.baseManager = baseManager;
		this.baseManager.setEntityClass(Category.class);
	}

	@Override
	public String execute() {
		if (parentId != null && parentId > 0) {
			category = baseManager.get(parentId);
		} else {
			category = new Category();
			DetachedCriteria dc = baseManager.detachedCriteria();
			dc.add(Restrictions.isNull("parent"));
			dc.addOrder(Order.asc("displayOrder"));
			dc.addOrder(Order.asc("name"));
			category.setChildren(baseManager.findListByCriteria(dc));
		}
		list = category.getChildren();
		return LIST;
	}

	@Override
	public String input() {
		if (getUid() != null)
			category = baseManager.get(Long.valueOf(getUid()));
		if (category == null)
			category = new Category();
		return INPUT;
	}

	@Override
	public String save() {
		if (category.isNew()) {
			if (baseManager.findByNaturalId("name", category.getName()) != null) {
				addFieldError("category.name",
						getText("validation.already.exists"));
				return INPUT;
			}
			if (parentId != null) {
				Category parent = baseManager.get(parentId);
				category.setParent(parent);
			}
		} else {
			Category temp = category;
			category = baseManager.get(temp.getId());
			if (!category.getName().equals(temp.getName())
					&& baseManager.findByNaturalId("name", temp.getName()) != null) {
				addFieldError("category.name",
						getText("validation.already.exists"));
				return INPUT;
			}
			category.setName(temp.getName());
			category.setDisplayOrder(temp.getDisplayOrder());
		}
		baseManager.save(category);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	public String move() {
		String id = getUid();
		if (StringUtils.isNotBlank(id)) {
			category = baseManager.get(Long.valueOf(getUid()));
			if (parentId != null && parentId > 0
					&& !id.equals(parentId.toString()))
				category.setParent(baseManager.get(parentId));
			else
				category.setParent(null);
			baseManager.save(category);
		}
		return "tree";
	}

	@Override
	public String delete() {
		String[] arr = getId();
		Long[] id = new Long[arr.length];
		for (int i = 0; i < id.length; i++)
			id[i] = Long.valueOf(arr[i]);
		if (id != null) {
			DetachedCriteria dc = baseManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Category> list = baseManager.findListByCriteria(dc);
			if (list.size() > 0) {
				for (Category category : list)
					baseManager.delete(category);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

	public String tree() {
		if (!async) {
			if (root < 1)
				category = categoryTreeControl.getCategoryTree();
			else
				category = categoryTreeControl.getCategoryTree()
						.getDescendantOrSelfById(root);
			list = category.getChildren();
		}
		return "tree";
	}

	@JsonConfig(root = "list")
	public String children() {
		Category category;
		if (root < 1)
			category = categoryTreeControl.getCategoryTree();
		else
			category = categoryTreeControl.getCategoryTree()
					.getDescendantOrSelfById(root);
		list = category.getChildren();
		return JSON;
	}

	public String getTreeViewHtml() {
		return HtmlUtils.getTreeViewHtml(list, async);
	}
}
