package com.ironrhino.common.action;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.ironrhino.common.model.Page;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.SearchService.Mapper;
import org.ironrhino.core.search.elasticsearch.ElasticSearchCriteria;
import org.ironrhino.core.search.elasticsearch.ElasticSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@AutoConfig
public class SearchAction extends BaseAction {

	private static final long serialVersionUID = 3969977471985368095L;

	@Inject
	private transient ElasticSearchService<Page> elasticSearchService;

	private ResultPage<Page> resultPage;

	private String q;

	public ResultPage<Page> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Page> resultPage) {
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
			return REDIRECT;
		String query = q.trim();
		ElasticSearchCriteria criteria = new ElasticSearchCriteria();
		criteria.setQuery(query + " AND tags:product");
		criteria.setTypes(new String[] { "page" });
		if (resultPage == null)
			resultPage = new ResultPage<Page>();
		resultPage.setCriteria(criteria);
		resultPage = elasticSearchService.search(resultPage,
				new Mapper<Page>() {
					public Page map(Page source) {
						Page p = (Page) source;
						Document doc = Jsoup.parse(p.getContent());
						Elements elements = doc.select("img");
						if (elements.size() > 0) {
							Element ele = elements.get(0);
							Page page = new Page();
							page.setPagepath(p.getPagepath());
							page.setTitle(p.getTitle());
							page.setContent(ele.attr("src"));
							return page;
						} else
							return null;
					}
				});
		return SUCCESS;
	}

}
