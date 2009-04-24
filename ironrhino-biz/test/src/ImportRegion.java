
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ironrhino.core.service.BaseManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ironrhino.biz.model.Region;
import com.ironrhino.biz.util.RegionParser;

public class ImportRegion {
	static int count;

	static BaseManager baseManager;

	public static void main(String... strings) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "resources/spring/applicationContext-base.xml",
						"resources/spring/applicationContext-hibernate.xml",
						"resources/spring/applicationContext-aop.xml" });
		baseManager = (BaseManager) ctx.getBean("baseManager");
		baseManager.bulkUpdate("delete from Region");
		List<Region> regions = RegionParser.parse();
		for (Region region : regions) {
			save(region);
		}
		System.out.println(count);
	}

	public static void save(Region region) {
		count++;
		baseManager.save(region);
		List<Region> list = new ArrayList<Region>();
		for (Region child : region.getChildren())
			list.add(child);
		Collections.sort(list);
		for (Region child : list)
			save(child);
	}

	public static void treewalker(Region region) {
		count++;
		for (int i = 0; i < region.getLevel(); i++)
			System.out.print("--");
		System.out.println();
		System.out.println(region.getName() + " " + region.getDisplayOrder());
		List<Region> list = new ArrayList<Region>();
		for (Region child : region.getChildren())
			list.add(child);
		Collections.sort(list);
		for (Region child : list)
			treewalker(child);
	}
}
