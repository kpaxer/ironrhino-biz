package com.ironrhino.biz.support;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.ironrhino.core.event.EntityOperationEvent;
import org.ironrhino.core.event.EntityOperationType;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.util.BeanUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.ironrhino.biz.model.Region;
import com.ironrhino.biz.util.RegionUtils;

public class RegionTreeControl implements ApplicationListener {

	private Region regionTree;

	private BaseManager<Region> baseManager;

	public void setBaseManager(BaseManager baseManager) {
		this.baseManager = baseManager;
	}

	public void buildRegionTree() {
		baseManager.setEntityClass(Region.class);
		regionTree = baseManager.loadTree();
	}

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		buildRegionTree();
	}

	public Region getRegionTree() {
		return regionTree;
	}

	public Region parseByHost(String host) {
		return RegionUtils.parseByHost(host, regionTree);
	}

	public Region parseByAddress(String address) {
		return RegionUtils.parseByAddress(address, regionTree);
	}

	private void create(Region region) {
		Region parent;
		if (region.getParent() == null)
			parent = regionTree;
		else
			parent = regionTree.getDescendantOrSelfById(region.getParent()
					.getId());
		Region r = new Region();
		BeanUtils.copyProperties(region, r,
				new String[] { "parent", "children" });
		r.setParent(parent);
		parent.getChildren().add(r);
		Collections.sort((List<Region>) parent.getChildren());
	}

	private void update(Region region) {
		Region r = regionTree.getDescendantOrSelfById(region.getId());
		if (!r.getFullId().equals(region.getFullId())) {
			r.getParent().getChildren().remove(r);
			Region newParent;
			if (region.getParent() == null)
				newParent = regionTree;
			else
				newParent = regionTree.getDescendantOrSelfById(region
						.getParent().getId());
			r.setParent(newParent);
			newParent.getChildren().add(r);
		}
		BeanUtils.copyProperties(region, r,
				new String[] { "parent", "children" });
		Collections.sort((List<Region>) r.getParent().getChildren());
	}

	private void delete(Region region) {
		Region r = regionTree.getDescendantOrSelfById(region.getId());
		r.getParent().getChildren().remove(r);
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (regionTree == null)
			return;
		if (event instanceof EntityOperationEvent) {
			EntityOperationEvent ev = (EntityOperationEvent) event;
			if (ev.getEntity() instanceof Region) {
				Region region = (Region) ev.getEntity();
				if (ev.getType() == EntityOperationType.CREATE)
					create(region);
				else if (ev.getType() == EntityOperationType.UPDATE)
					update(region);
				else if (ev.getType() == EntityOperationType.DELETE)
					delete(region);
			}
		}

	}
}
