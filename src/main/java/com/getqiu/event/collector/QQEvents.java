package com.getqiu.event.collector;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.selector.Selectable;

public class QQEvents  extends BaseSpider{

	private final String useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/55.0.2883.87 Chrome/55.0.2883.87 Safari/537.36";
	private Site site = Site.me().setRetryTimes(3).setSleepTime(100).setTimeOut(1000*20);
	private String allowedUrlType = "(http://([a-z]{1,6}\\.)?[a-z]{3,10}\\.qq\\.com/a/\\d{4}\\d{4}/\\d{6}\\.html?)";
	
	/**
	 * 分类映射
	 * */
	private HashMap<String, String> categoryMapping = new HashMap<>();
	
	public QQEvents() {
		categoryMapping.put("news", "headline");
		categoryMapping.put("finance", "finance");
		categoryMapping.put("stock", "finance");
		categoryMapping.put("ent", "entertainment");
		categoryMapping.put("mobile", "technology");
		categoryMapping.put("sports", "sports");
		categoryMapping.put("auto", "car");
		categoryMapping.put("home", "home");
		categoryMapping.put("mil", "military");
		categoryMapping.put("discovery", "technology");
		categoryMapping.put("digi", "technology");
		categoryMapping.put("lady", "lady");
		categoryMapping.put("edu", "education");
		categoryMapping.put("city", "city");
		categoryMapping.put("jiankang", "healthy");
		categoryMapping.put("travel", "travel");
		categoryMapping.put("house", "house");
		categoryMapping.put("other", "other");		
	}
	
	@Override
	public Site getSite() {
		return site.setUserAgent(useragent);
	}
	
	/**
	 * 首页的解析工作
	 * **/
	@Override
	public void parse(Page page) {
		List<String> nextUrls = page.getHtml().links().regex(allowedUrlType).all();
		int rank = 0;
		for(String url:nextUrls){
			rank += 1;
			Request r = new AdvanceRequest(url, "processSingleEvent");
			r.putExtra("rank", rank);
			page.addTargetRequest(r);
		}
		
		page.setSkip(true);
		
	}	
	
	/**
	 * 具体内容的解析工作
	 * */
	protected void processSingleEvent(Page page){
		
		page.putField("title", titleCleaner(page));
		page.putField("url", page.getUrl());
		page.putField("rank", page.getRequest().getExtra("rank"));
		page.putField("publisher", publisherCleaner(page));
		page.putField("cover", findCover(page));
		/**
		 * 根据url获取分类和时间
		 * */
		EventMeta eventMeta = parseTimeFromUrl(page.getUrl().toString());
		
		String mappedCategory = categoryMapping.get(eventMeta.getCategory());
		page.putField("category",mappedCategory == null ? "other" : mappedCategory);
		
		page.putField("time", eventMeta.getDate());
		page.putField("origin", eventMeta.getSite());
		/**
		 * 这个正文抽取部分直接去除了p标签，根据换行，把这个标签添加回去
		 * */
		String content = page.getHtml().smartContent().toString();
		
		if(content == null || content.equals("")){
			page.setSkip(true);
		}		
		content = content.replaceAll("^", "<p>");
		content = content.replaceAll("$", "</p>");
		content = content.replaceAll("\n", "</p><p>");
		page.putField("content", content);

		

	}
	
	/**
	 * 去除title的小尾巴
	 * */
	private String titleCleaner(Page page){
		Selectable title = page.getHtml().xpath("/html/head/title/text()");
		if (title.toString() == null){
			title = page.getHtml().xpath("//div[@class='qq_article']/div[@class='hd']/h1/text()");
		}
		return title.toString().replaceAll("_.+$", "");
	}
	
	private class EventMeta{
		
		private String category;
		private String date;
	
		public EventMeta(String category,String date){
			this.category = category;
			this.date = date;
		}
		public String getCategory() {
			return category;
		}
		
		public String getSite() {
			return "qq.com";
		}
		public String getDate() {
			return date;
		}
	
		@Override
		public String toString() {
			return this.getCategory() + this.getDate();
		}

		
	}
	/**
	 * 提取publisher，可能没有，填充默认
	 * */
	private String publisherCleaner(Page page){
		Selectable publisher = page.getHtml().xpath("//div[@class='a_Info']/span[@class='a_source']/a/text()");
		if(publisher.toString() == null){
			return "qq.com";
		}
		else{
			return publisher.toString();
		}
	}
	/**
	 * http://news.qq.com/a/20161229/001746.htm
	 * */
	private EventMeta parseTimeFromUrl(String url){
		String category;
		String year;
		String month;
		String date;
		
		Pattern p = Pattern.compile("http://([a-z]{1,6}\\.)?(?<category>[a-z]{3,10})\\.qq\\.com/a/(?<year>\\d{4})(?<month>\\d{2})(?<date>\\d{2})/\\d{6}\\.html?");  
		Matcher m = p.matcher(url);
		while(m.find())
		{
			category = m.group("category");
			year  = m.group("year");
			month = m.group("month");
			date  = m.group("date");
			return new EventMeta(category,year+"-"+month+"-"+date);
		}
		return null;
	}


	/**
	 * 查找一张图片作为cover
	 * */
	public String findCover(Page page){
		Selectable images = page.getHtml().xpath("//div[@id='Cnt-Main-Article-QQ']/p/img[starts-with(@src,'http')]/@src");
		List<String> covers = images.all();
		if(covers.isEmpty()){
			return "/static/image/default-cover.jpg";
		}
		else{
			return covers.get(0);
		}
		
	}
}
