package com.ironrhino.biz.action;

import org.ironrhino.core.util.AuthzUtils;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.service.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Category;
import com.ironrhino.biz.model.Role;
import com.ironrhino.biz.model.Spec;
import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.User;
import com.ironrhino.biz.model.Vendor;
import com.ironrhino.biz.service.UserManager;

@AutoConfig
public class MiscAction extends BaseAction {

	private static final long serialVersionUID = 7038201018786069091L;

	private transient BaseManager baseManager;

	@Autowired
	private transient UserManager userManager;

	public void setBaseManager(BaseManager baseManager) {
		this.baseManager = baseManager;
	}

	public String execute() {
		return NONE;
	}

	public String setup() throws Exception {

		int cnt = userManager.countAll();
		if (cnt == 0) {
			initUser();
			initStuff();
			initProduct();

		} else {
			if (!AuthzUtils.getRoleNames().contains(Constants.ROLE_SUPERVISOR))
				return ACCESSDENIED;
		}
		targetUrl = "/";
		return REDIRECT;
	}

	private void initUser() {
		// init role
		Role supervisor = new Role(Constants.ROLE_SUPERVISOR);
		baseManager.save(supervisor);
		Role ceo = new Role(Constants.ROLE_CEO);
		baseManager.save(ceo);
		Role cfo = new Role(Constants.ROLE_CFO);
		baseManager.save(cfo);
		Role warehouseman = new Role(Constants.ROLE_WAREHOUSEMAN);
		baseManager.save(warehouseman);
		Role packer = new Role(Constants.ROLE_PACKER);
		baseManager.save(packer);
		Role deliveryman = new Role(Constants.ROLE_DELIVERYMAN);
		baseManager.save(deliveryman);
		Role salesman = new Role(Constants.ROLE_SALESMAN);
		baseManager.save(salesman);
		// init user
		User admin = new User();
		admin.setUsername("admin");
		admin.setLegiblePassword("password");
		admin.setName(Constants.ROLE_SUPERVISOR);
		admin.getRoles().add(supervisor);
		userManager.save(admin);
	}

	private void initStuff() {
		// init vendor
		Vendor yx = new Vendor("玉香");
		baseManager.save(yx);
		Vendor yp = new Vendor("伊品");
		baseManager.save(yp);

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

}
