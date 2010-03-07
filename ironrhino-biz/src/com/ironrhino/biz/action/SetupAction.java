package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.ironrhino.common.model.Region;
import org.ironrhino.common.util.RegionParser;
import org.ironrhino.core.aop.AopContext;
import org.ironrhino.core.aop.PublishAspect;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.model.SimpleElement;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.AuthzUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Brand;
import com.ironrhino.biz.model.Category;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.model.Spec;
import com.ironrhino.biz.model.User;
import com.ironrhino.biz.service.UserManager;

@AutoConfig
public class SetupAction extends BaseAction {

	private static final long serialVersionUID = 7038201018786069091L;

	private transient BaseManager baseManager;

	@Inject
	private transient UserManager userManager;

	boolean region;

	public void setRegion(boolean region) {
		this.region = region;
	}

	public void setBaseManager(BaseManager baseManager) {
		this.baseManager = baseManager;
	}

	@Override
	public String execute() {
		int cnt = userManager.countAll();
		if (cnt == 0) {
			initUser();
			if (region)
				initRegion();
			initProduct();
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

	private void initProduct() {

		String[] brandNames = "湘陵,云阳山,武仙,福瑞,伊品,玉香,美味缘,武夷,圣花,信乐,三九".split(",");
		Map<String, Brand> brands = new HashMap<String, Brand>();
		for (int i = 0; i < brandNames.length; i++) {
			Brand b = new Brand(brandNames[i]);
			b.setDisplayOrder(i);
			baseManager.save(b);
			brands.put(b.getName(), b);
		}

		String[] categoryNames = "味精,鸡精,食用油".split(",");
		Map<String, Category> cates = new HashMap<String, Category>();
		for (int i = 0; i < categoryNames.length; i++) {
			Category c = new Category(categoryNames[i]);
			c.setDisplayOrder(i);
			baseManager.save(c);
			cates.put(c.getName(), c);
		}

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

		for (String s : brandNames) {
			for (String name : "粗晶,中晶,小晶,粉晶".split(",")) {
				Product p = new Product();
				p.setName(name);
				p.setBrand(brands.get(s));
				p.setCategory(cates.get("味精"));
				p.setSpec(s25);
				baseManager.save(p);
			}
		}

		String[] xiangling99cb = "2500*4,1000*10,908*10,50*200,40*200"
				.split(",");
		String[] xiangling99cblb = "500*20,480*20,450*22,400*25,380*25,360*25,350*28,340*28,340*25,320*30,300*32,280*35,260*35,250*40,227*40,200*50,180*50,170*56,160*60,150*64,140*70,130*70,100*100,80*100,70*120"
				.split(",");
		String[] xiangling80 = "2500*5,500*20,480*20,454*22,400*25,380*25,360*25,350*27,340*28,320*30,300*32,280*35,260*35,250*40,227*40,200*50,180*50,170*56,160*60,150*64,140*70,130*70,100*100,80*100,70*120,60*150,50*200,40*200"
				.split(",");
		String[] yunyang99 = "2500*4,1000*10,908*10,500*20,480*20,454*20,400*25,380*25,360*25,350*27,250*40,227*40,200*50,180*50,100*100,80*100"
				.split(",");
		String[] yunyang80 = "2500*5,500*20,480*20,454*20,400*25,380*25,360*25,350*27,340*28,320*30,200*50,180*50,170*56,160*60,80*100,70*120"
				.split(",");
		Set<String> specNames = new LinkedHashSet<String>();
		specNames.addAll(Arrays.asList(xiangling99cb));
		specNames.addAll(Arrays.asList(xiangling99cblb));
		specNames.addAll(Arrays.asList(xiangling80));
		specNames.addAll(Arrays.asList(yunyang99));
		specNames.addAll(Arrays.asList(yunyang80));

		Map<String, Spec> specs = new HashMap<String, Spec>();
		for (String s : specNames) {
			String[] arr = s.split("\\*");
			Spec temp = new Spec();
			temp.setBasicPackName("包");
			temp.setBasicQuantity(Double.valueOf(arr[0]));
			temp.setBasicMeasure("g");
			temp.setBaleQuantity(Integer.valueOf(arr[1]));
			temp.setBalePackName("件");
			baseManager.save(temp);
			specs.put(s, temp);
		}

		for (String s : xiangling99cb) {
			Product p = new Product();
			p.setName("纯版99%");
			p.setBrand(brands.get("湘陵"));
			p.setCategory(cates.get("味精"));
			p.setSpec(specs.get(s));
			baseManager.save(p);
		}

		for (String s : xiangling99cblb) {
			Product p = new Product();
			p.setName("纯版99%");
			p.setBrand(brands.get("湘陵"));
			p.setCategory(cates.get("味精"));
			p.setSpec(specs.get(s));
			baseManager.save(p);
			p = new Product();
			p.setName("蓝版99%");
			p.setBrand(brands.get("湘陵"));
			p.setCategory(cates.get("味精"));
			p.setSpec(specs.get(s));
			baseManager.save(p);
		}

		for (String s : xiangling80) {
			Product p = new Product();
			p.setName("80%");
			p.setBrand(brands.get("湘陵"));
			p.setCategory(cates.get("味精"));
			p.setSpec(specs.get(s));
			baseManager.save(p);
		}

		for (String s : yunyang99) {
			Product p = new Product();
			p.setName("99%");
			p.setBrand(brands.get("云阳山"));
			p.setCategory(cates.get("味精"));
			p.setSpec(specs.get(s));
			baseManager.save(p);
		}
		for (String s : yunyang80) {
			Product p = new Product();
			p.setName("80%");
			p.setBrand(brands.get("云阳山"));
			p.setCategory(cates.get("味精"));
			p.setSpec(specs.get(s));
			baseManager.save(p);
		}

	}

	protected void initRegion() {
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
		AopContext.setBypass(PublishAspect.class);
		baseManager.save(region);
		List<Region> list = new ArrayList<Region>();
		for (Region child : region.getChildren())
			list.add(child);
		Collections.sort(list);
		for (Region child : list)
			save(child);
	}
}
