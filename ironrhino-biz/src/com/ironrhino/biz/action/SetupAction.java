package com.ironrhino.biz.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.User;

@AutoConfig
public class SetupAction extends BaseAction {

	private static final long serialVersionUID = 7038201018786069091L;

	private transient BaseManager baseManager;

	boolean region;

	public void setRegion(boolean region) {
		this.region = region;
	}

	public void setBaseManager(BaseManager baseManager) {
		this.baseManager = baseManager;
	}

	@Override
	public String execute() {
		baseManager.setEntityClass(User.class);
		int cnt = baseManager.countAll();
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
		baseManager.save(admin);
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

		String[] xiangling99 = "2500*10,2270*10,2200*4,2000*5".split(",");
		String[] xiangling99cb = "2500*4,1000*10,908*10,50*200,40*200"
				.split(",");
		String[] xiangling99cblb = "500*20,480*20,450*22,400*25,380*25,360*25,350*28,340*28,340*25,320*30,300*32,280*35,260*35,250*40,227*40,200*50,180*50,170*56,160*60,150*64,140*70,130*70,100*100,80*100,70*120"
				.split(",");
		String[] xiangling80 = "2500*5,500*20,480*20,454*22,400*25,380*25,360*25,350*27,340*28,320*30,300*32,280*35,260*35,250*40,227*40,200*50,180*50,170*56,160*60,150*64,140*70,130*70,100*100,80*100,70*120,60*150,50*200,40*200"
				.split(",");
		String[] yunyang99 = "2500*10,2500*4,2270*10,2200*4,2000*5,1000*10,908*10,500*20,480*20,454*20,400*25,380*25,360*25,350*27,250*40,227*40,200*50,180*50,100*100,80*100"
				.split(",");
		String[] yunyang80 = "2500*5,500*20,480*20,454*20,400*25,380*25,360*25,350*27,340*28,320*30,200*50,180*50,170*56,160*60,80*100,70*120"
				.split(",");

		int productDisplayOrder = 0;
		int stuffDisplayOrder = 0;
		for (int i = 0; i < brandNames.length; i++) {
			String s = brandNames[i];
			for (String name : "粗晶,中晶,小晶,粉晶".split(",")) {
				Product p = new Product();
				p.setName(name + "(25kg/包)");
				p.setBrand(brands.get(s));
				p.setCategory(cates.get("味精"));
				p.setWeight(new BigDecimal(25));
				p.setDisplayOrder(++productDisplayOrder);
				baseManager.save(p);
				if (i > 2) {
					Stuff stuff = new Stuff();
					stuff.setName(s + "味精" + name);
					stuff.setWeight(new BigDecimal(25));
					stuff.setDisplayOrder(++stuffDisplayOrder);
					baseManager.save(stuff);
				}
			}
		}

		for (String s : xiangling99) {
			String[] arr = s.split("\\*");
			Product p = new Product();
			p.setName("99%(" + arr[0] + "g*" + arr[1] + "/包)");
			p.setBrand(brands.get("湘陵"));
			p.setCategory(cates.get("味精"));
			p.setWeight(new BigDecimal(Integer.parseInt(arr[0])
					* Integer.parseInt(arr[1])).divide(new BigDecimal(1000)));
			p.setDisplayOrder(++productDisplayOrder);
			baseManager.save(p);
		}

		for (String s : xiangling99cb) {
			String[] arr = s.split("\\*");
			Product p = new Product();
			p.setName("纯版99%(" + arr[0] + "g*" + arr[1] + "/包)");
			p.setBrand(brands.get("湘陵"));
			p.setCategory(cates.get("味精"));
			p.setWeight(new BigDecimal(Integer.parseInt(arr[0])
					* Integer.parseInt(arr[1])).divide(new BigDecimal(1000)));
			p.setDisplayOrder(++productDisplayOrder);
			baseManager.save(p);
		}

		for (String s : xiangling99cblb) {
			String[] arr = s.split("\\*");
			Product p = new Product();
			p.setName("纯版99%(" + arr[0] + "g*" + arr[1] + "/包)");
			p.setBrand(brands.get("湘陵"));
			p.setCategory(cates.get("味精"));
			p.setWeight(new BigDecimal(Integer.parseInt(arr[0])
					* Integer.parseInt(arr[1])).divide(new BigDecimal(1000)));
			p.setDisplayOrder(++productDisplayOrder);
			baseManager.save(p);
			p = new Product();
			p.setName("蓝版99%(" + arr[0] + "g*" + arr[1] + "/包)");
			p.setBrand(brands.get("湘陵"));
			p.setCategory(cates.get("味精"));
			p.setWeight(new BigDecimal(Integer.parseInt(arr[0])
					* Integer.parseInt(arr[1])).divide(new BigDecimal(1000)));
			p.setDisplayOrder(++productDisplayOrder);
			baseManager.save(p);
		}

		for (String s : xiangling80) {
			String[] arr = s.split("\\*");
			Product p = new Product();
			p.setName("80%(" + arr[0] + "g*" + arr[1] + "/包)");
			p.setBrand(brands.get("湘陵"));
			p.setCategory(cates.get("味精"));
			p.setWeight(new BigDecimal(Integer.parseInt(arr[0])
					* Integer.parseInt(arr[1])).divide(new BigDecimal(1000)));
			p.setDisplayOrder(++productDisplayOrder);
			baseManager.save(p);
		}

		for (String s : yunyang99) {
			String[] arr = s.split("\\*");
			Product p = new Product();
			p.setName("99%(" + arr[0] + "g*" + arr[1] + "/包)");
			p.setBrand(brands.get("云阳山"));
			p.setCategory(cates.get("味精"));
			p.setWeight(new BigDecimal(Integer.parseInt(arr[0])
					* Integer.parseInt(arr[1])).divide(new BigDecimal(1000)));
			p.setDisplayOrder(++productDisplayOrder);
			baseManager.save(p);
		}
		for (String s : yunyang80) {
			String[] arr = s.split("\\*");
			Product p = new Product();
			p.setName("80%(" + arr[0] + "g*" + arr[1] + "/包)");
			p.setBrand(brands.get("云阳山"));
			p.setCategory(cates.get("味精"));
			p.setWeight(new BigDecimal(Integer.parseInt(arr[0])
					* Integer.parseInt(arr[1])).divide(new BigDecimal(1000)));
			p.setDisplayOrder(++productDisplayOrder);
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
