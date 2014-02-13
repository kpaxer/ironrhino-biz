package com.ironrhino.common.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ironrhino.common.action.ColumnPageAction;
import org.ironrhino.common.model.Page;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.model.ResultPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@AutoConfig
public class ProductAction extends ColumnPageAction {

	private static final long serialVersionUID = 45081098210120504L;

	@Override
	public String list() {
		if (resultPage == null)
			resultPage = new ResultPage<Page>();
		resultPage.setPageSize(12);
		String result = super.list();
		if (result.equals("columnpage"))
			return result;
		Collection<Page> _list = resultPage.getResult();
		List<Page> list = new ArrayList<Page>();
		for (Page p : _list) {
			Document doc = Jsoup.parse(p.getContent());
			Elements elements = doc.select("img");
			if (elements.size() > 0) {
				Element ele = elements.get(0);
				Page page = new Page();
				page.setPath(p.getPath());
				page.setTitle(p.getTitle());
				page.setContent(ele.attr("src"));
				list.add(page);
			}
		}
		resultPage.setResult(list);
		return result;
	}
}
