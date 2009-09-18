package com.ironrhino.biz.service;

import org.ironrhino.core.service.BaseManagerImpl;
import org.springframework.stereotype.Component;

import com.ironrhino.biz.model.Product;

@Component("productManager")
public class ProductManagerImpl extends BaseManagerImpl<Product> implements
		ProductManager {

}
