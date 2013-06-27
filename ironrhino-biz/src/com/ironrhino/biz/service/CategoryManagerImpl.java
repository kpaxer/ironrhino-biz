package com.ironrhino.biz.service;

import javax.inject.Named;
import javax.inject.Singleton;

import org.ironrhino.core.service.BaseManagerImpl;

import com.ironrhino.biz.model.Category;

@Singleton
@Named("categoryManager")
public class CategoryManagerImpl extends BaseManagerImpl<Category> implements
		CategoryManager {

}
