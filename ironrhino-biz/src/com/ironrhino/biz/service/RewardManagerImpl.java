package com.ironrhino.biz.service;

import javax.inject.Named;
import javax.inject.Singleton;

import org.ironrhino.core.service.BaseManagerImpl;

import com.ironrhino.biz.model.Reward;

@Singleton
@Named("rewardManager")
public class RewardManagerImpl extends BaseManagerImpl<Reward> implements
		RewardManager {

}
