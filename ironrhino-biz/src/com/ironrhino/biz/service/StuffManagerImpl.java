package com.ironrhino.biz.service;

import javax.inject.Named;
import javax.inject.Singleton;

import org.ironrhino.core.service.BaseManagerImpl;

import com.ironrhino.biz.model.Stuff;

@Singleton
@Named
public class StuffManagerImpl extends BaseManagerImpl<Stuff> implements
		StuffManager {

}
