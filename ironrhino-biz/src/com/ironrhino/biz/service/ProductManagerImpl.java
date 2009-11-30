package com.ironrhino.biz.service;

import javax.inject.Named;
import javax.inject.Singleton;

import org.ironrhino.core.service.BaseManagerImpl;

import com.ironrhino.biz.model.Product;

@Singleton
@Named("productManager")
public class ProductManagerImpl extends BaseManagerImpl<Product> implements
		ProductManager {

}
