package com.ironrhino.biz.service;

import javax.inject.Named;
import javax.inject.Singleton;

import org.ironrhino.core.service.BaseManagerImpl;

import com.ironrhino.biz.model.Brand;

@Singleton
@Named("brandManager")
public class BrandManagerImpl extends BaseManagerImpl<Brand> implements
		BrandManager {

}
