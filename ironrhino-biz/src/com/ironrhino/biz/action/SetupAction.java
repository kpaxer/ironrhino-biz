package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.ironrhino.common.model.Region;
import org.ironrhino.common.util.RegionParser;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.model.SimpleElement;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.AuthzUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Category;
import com.ironrhino.biz.model.Spec;
import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.User;
import com.ironrhino.biz.model.Vendor;
import com.ironrhino.biz.service.UserManager;

@AutoConfig
public class SetupAction extends BaseAction {

	private static final long serialVersionUID = 7038201018786069091L;

	private transient BaseManager baseManager;

	@Inject
	private transient UserManager userManager;

	public void setBaseManager(BaseManager baseManager) {
		this.baseManager = baseManager;
	}

	@Override
	public String execute() {
		int cnt = userManager.countAll();
		if (cnt == 0) {
			initUser();
			initStuff();
			initProduct();
			initRegion();
		} else {
			if (!AuthzUtils.getRoleNames().contains(Constants.ROLE_SUPERVISOR))
				return ACCESSDENIED;
		}
		targetUrl = "/";
		return REDIRECT;
	}

	private void initUser() {

		// init user
		User admin = new User();
		admin.setUsername("admin");
		admin.setLegiblePassword("password");
		admin.setName(Constants.ROLE_SUPERVISOR);
		admin.getRoles().add(new SimpleElement(Constants.ROLE_SUPERVISOR));
		userManager.save(admin);
	}

	private void initStuff() {
		// init vendor
		Vendor yx = new Vendor("玉香");
		baseManager.save(yx);
		Vendor yp = new Vendor("伊品");
		baseManager.save(yp);
		Vendor xl = new Vendor("湘陵");
		baseManager.save(xl);

		// init spec
		Spec s25 = new Spec();
		s25.setBasicPackName("包");
		s25.setBasicQuantity(25.0);
		s25.setBasicMeasure("kg");
		baseManager.save(s25);
		Spec s50 = new Spec();
		s50.setBasicPackName("包");
		s50.setBasicQuantity(50.0);
		s50.setBasicMeasure("kg");
		baseManager.save(s50);

		// init stuff
		Stuff yxcj = new Stuff("玉香粗晶", s25);
		yxcj.setVendor(yx);
		baseManager.save(yxcj);
		Stuff ypcj = new Stuff("伊品粗晶", s25);
		ypcj.setVendor(yp);
		baseManager.save(ypcj);
		Stuff ypzj = new Stuff("伊品中晶", s25);
		ypzj.setVendor(yp);
		baseManager.save(ypzj);
		Stuff tjj = new Stuff("添加剂", s50);
		baseManager.save(tjj);
	}

	private void initProduct() {
		// init cateogry
		Category wj = new Category("味精");
		baseManager.save(wj);
		Category jj = new Category("鸡精");
		baseManager.save(jj);
		Category syy = new Category("食用油");
		baseManager.save(syy);
		// init product

	}

	private void initRegion() {
		List<Region> regions = null;
		try {
			regions = RegionParser.parse();
		} catch (Exception e) {

		}
		if (regions != null)
			for (Region region : regions) {
				save(region);
			}
	}

	private void save(Region region) {
		baseManager.save(region);
		List<Region> list = new ArrayList<Region>();
		for (Region child : region.getChildren())
			list.add(child);
		Collections.sort(list);
		for (Region child : list)
			save(child);
	}
}
