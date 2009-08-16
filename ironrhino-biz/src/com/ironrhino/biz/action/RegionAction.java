package com.ironrhino.biz.action;

import java.util.Collection;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.util.HtmlUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.ext.struts.BaseAction;
import org.ironrhino.core.service.BaseManager;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Region;
import com.ironrhino.biz.support.RegionTreeControl;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class RegionAction extends BaseAction {

	private Region region;

	private Integer parentId;

	private transient BaseManager<Region> baseManager;

	private Collection<Region> list;

	private Collection<Region> children;

	private transient RegionTreeControl regionTreeControl;

	private boolean async = true;

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

	public Collection<Region> getChildren() {
		return children;
	}

	public void setRegionTreeControl(RegionTreeControl regionTreeControl) {
		this.regionTreeControl = regionTreeControl;
	}

	public Collection<Region> getList() {
		return list;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public void setBaseManager(BaseManager baseManager) {
		baseManager.setEntityClass(Region.class);
		this.baseManager = baseManager;
	}

	public String execute() {
		if (parentId != null && parentId > 0) {
			region = baseManager.get(parentId);
		} else {
			region = new Region();
			DetachedCriteria dc = baseManager.detachedCriteria();
			dc.add(Restrictions.isNull("parent"));
			dc.addOrder(Order.asc("displayOrder"));
			dc.addOrder(Order.asc("name"));
			region.setChildren(baseManager.getListByCriteria(dc));
		}
		list = region.getChildren();
		return LIST;
	}

	public String input() {
		if (getUid() != null)
			region = baseManager.get(new Integer(getUid()));
		if (region == null)
			region = new Region();
		return INPUT;
	}

	public String save() {
		if (region.isNew()) {
			if (parentId != null) {
				Region parent = baseManager.get(parentId);
				region.setParent(parent);
			}
		} else {
			Region temp = region;
			region = baseManager.get(temp.getId());
			if (temp.getLatitude() != null) {
				region.setLatitude(temp.getLatitude());
				region.setLongitude(temp.getLongitude());
			} else {
				region.setName(temp.getName());
				region.setDisplayOrder(temp.getDisplayOrder());
			}
		}
		baseManager.save(region);
		addActionMessage(getText("save.success", "save {0} successfully",
				new String[] { region.getName() }));
		return SUCCESS;
	}

	public String delete() {
		String[] arr = getId();
		Integer[] id = new Integer[arr.length];
		for (int i = 0; i < id.length; i++)
			id[i] = new Integer(arr[i]);
		if (id != null) {
			DetachedCriteria dc = baseManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Region> list = baseManager.getListByCriteria(dc);
			if (list.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("(");
				for (Region region : list) {
					baseManager.delete(region);
					sb.append(region.getName() + ",");
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

	@Authorize(ifAnyGranted = Constants.ROLE_BUILTIN_ANONYMOUS)
	public String tree() {
		if (!async) {
			Region region;
			if (root < 1)
				region = regionTreeControl.getRegionTree();
			else
				region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(root);
			children = region.getChildren();
		}
		return "tree";
	}

	@Authorize(ifAnyGranted = Constants.ROLE_BUILTIN_ANONYMOUS)
	@JsonConfig(root = "children")
	public String children() {
		Region region;
		if (root < 1)
			region = regionTreeControl.getRegionTree();
		else
			region = regionTreeControl.getRegionTree().getDescendantOrSelfById(
					root);
		children = region.getChildren();
		ServletActionContext.getResponse().setHeader("Cache-Control",
				"max-age=86400");
		return JSON;
	}

	public String getTreeViewHtml() {
		return HtmlUtils.getTreeViewHtml(children, async);
	}

}
