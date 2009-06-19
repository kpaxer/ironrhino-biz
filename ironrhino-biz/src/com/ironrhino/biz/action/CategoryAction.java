package com.ironrhino.biz.action;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.util.HtmlUtils;
import org.ironrhino.core.annotation.Authorize;
import org.ironrhino.core.annotation.JsonConfig;
import org.ironrhino.core.ext.struts.BaseAction;
import org.ironrhino.core.service.BaseManager;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Category;
import com.ironrhino.biz.support.CategoryTreeControl;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class CategoryAction extends BaseAction {

	private Category category;

	private Integer parentId;

	private transient BaseManager<Category> baseManager;

	private Collection<Category> list;

	private transient CategoryTreeControl categoryTreeControl;

	private boolean async;

	private int root;

	public int getRoot() {
		return root;
	}

	public void setRoot(int root) {
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

	public void setCategoryTreeControl(CategoryTreeControl categoryTreeControl) {
		this.categoryTreeControl = categoryTreeControl;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
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

	public String execute() {
		if (parentId != null && parentId > 0) {
			category = baseManager.get(parentId);
		} else {
			category = new Category();
			DetachedCriteria dc = baseManager.detachedCriteria();
			dc.add(Restrictions.isNull("parent"));
			dc.addOrder(Order.asc("displayOrder"));
			dc.addOrder(Order.asc("name"));
			category.setChildren(baseManager.getListByCriteria(dc));
		}
		list = category.getChildren();
		return LIST;
	}

	public String input() {
		if (getUid() != null)
			category = baseManager.get(new Integer(getUid()));
		if (category == null)
			category = new Category();
		return INPUT;
	}

	public String save() {
		if (category.isNew()) {
			if (baseManager.getByNaturalId("name", category.getName()) != null) {
				addFieldError("category.name", getText("category.name.exists"));
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
					&& baseManager.getByNaturalId("name", temp.getName()) != null) {
				addFieldError("category.name", getText("category.name.exists"));
				return INPUT;
			}
			category.setName(temp.getName());
			category.setDisplayOrder(temp.getDisplayOrder());
		}
		baseManager.save(category);
		addActionMessage(getText("save.success", "save {0} successfully",
				new String[] { category.getName() }));
		return SUCCESS;
	}

	public String move() {
		String id = getUid();
		if (StringUtils.isNotBlank(id)) {
			category = baseManager.get(new Integer(getUid()));
			if (parentId != null && parentId > 0
					&& !id.equals(parentId.toString()))
				category.setParent(baseManager.get(parentId));
			else
				category.setParent(null);
			baseManager.save(category);
		}
		return "tree";
	}

	public String delete() {
		String[] arr = getId();
		Integer[] id = new Integer[arr.length];
		for (int i = 0; i < id.length; i++)
			id[i] = new Integer(arr[i]);
		if (id != null) {
			DetachedCriteria dc = baseManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Category> list = baseManager.getListByCriteria(dc);
			if (list.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("(");
				for (Category category : list) {
					baseManager.delete(category);
					sb.append(category.getName() + ",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append(")");
				addActionMessage(getText("delete.success",
						"delete {0} successfully",
						new String[] { sb.toString() }));
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

	@JsonConfig(top = "list")
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
