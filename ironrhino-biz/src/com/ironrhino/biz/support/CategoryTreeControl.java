package com.ironrhino.biz.support;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.ironrhino.common.util.BeanUtils;
import org.ironrhino.core.event.EntityOperationEvent;
import org.ironrhino.core.event.EntityOperationType;
import org.ironrhino.core.service.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.ironrhino.biz.model.Category;

public class CategoryTreeControl implements ApplicationListener {

	private Category categoryTree;

	@Autowired
	private BaseManager<Category> baseManager;

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		baseManager.setEntityClass(Category.class);
		categoryTree = baseManager.loadTree();
	}

	public Category getFullCategoryTree() {
		return this.categoryTree;
	}

	public Category getCategoryTree() {
		return categoryTree;
	}

	private void create(Category cate) {
		Category parent;
		if (cate.getParent() == null)
			parent = categoryTree;
		else
			parent = categoryTree.getDescendantOrSelfById(cate.getParent()
					.getId());
		Category c = new Category();
		BeanUtils
				.copyProperties(cate, c, new String[] { "parent", "children" });
		c.setParent(parent);
		parent.getChildren().add(c);
		Collections.sort((List<Category>) parent.getChildren());
	}

	private void update(Category cate) {
		Category c = categoryTree.getDescendantOrSelfById(cate.getId());
		if (!c.getFullId().equals(cate.getFullId())) {
			c.getParent().getChildren().remove(c);
			Category newParent;
			if (cate.getParent() == null)
				newParent = categoryTree;
			else
				newParent = categoryTree.getDescendantOrSelfById(cate
						.getParent().getId());
			c.setParent(newParent);
			newParent.getChildren().add(c);
		}
		BeanUtils
				.copyProperties(cate, c, new String[] { "parent", "children" });
		Collections.sort((List<Category>) c.getParent().getChildren());
	}

	private void delete(Category cate) {
		Category c = categoryTree.getDescendantOrSelfById(cate.getId());
		c.getParent().getChildren().remove(c);
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (categoryTree == null)
			return;
		if (event instanceof EntityOperationEvent) {
			EntityOperationEvent ev = (EntityOperationEvent) event;
			if (ev.getEntity() instanceof Category) {
				Category cate = (Category) ev.getEntity();
				if (ev.getType() == EntityOperationType.CREATE)
					create(cate);
				else if (ev.getType() == EntityOperationType.UPDATE)
					update(cate);
				else if (ev.getType() == EntityOperationType.DELETE)
					delete(cate);
			}
		}

	}

}
