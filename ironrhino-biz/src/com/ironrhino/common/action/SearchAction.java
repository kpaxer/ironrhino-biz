package com.ironrhino.common.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.compass.core.CompassHit;
import org.compass.core.support.search.CompassSearchResults;
import org.ironrhino.common.model.Page;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@AutoConfig
public class SearchAction extends BaseAction {

	private static final long serialVersionUID = 3969977471985368095L;

	@Inject
	private transient CompassSearchService compassSearchService;

	private ResultPage resultPage;

	private String q;

	public ResultPage getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage resultPage) {
		this.resultPage = resultPage;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	@Override
	public String execute() {
		if (StringUtils.isBlank(q))
			return null;
		String query = q.trim();
		CompassCriteria cc = new CompassCriteria();
		cc.setQuery(query + " AND tags:product");
		cc.setAliases(new String[] { "page" });
		if (resultPage == null)
			resultPage = new ResultPage();
		cc.setPageNo(resultPage.getPageNo());
		cc.setPageSize(resultPage.getPageSize());
		CompassSearchResults searchResults = compassSearchService.search(cc);
		int totalHits = searchResults.getTotalHits();
		CompassHit[] hits = searchResults.getHits();
		if (hits != null) {
			List list = new ArrayList(hits.length);
			for (CompassHit ch : searchResults.getHits()) {
				list.add(ch.getData());
			}
			resultPage.setResult(list);
		} else {
			resultPage.setResult(Collections.EMPTY_LIST);
		}
		resultPage.setTotalRecord(totalHits);
		
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
		
		return SUCCESS;
	}

}
