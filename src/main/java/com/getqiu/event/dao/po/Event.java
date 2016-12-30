package com.getqiu.event.dao.po;

import com.getqiu.event.utils.HashTools;

public class Event {
	private Long id;
	private String title;
	private String time;
	private String publisher;
	private Integer rank; 
	private String url;
	private String cover;
	private String content;
	private String category;
	/**
	 * 来自哪儿，比如来自qq.com
	 * */
	private String origin;
	private String hash;

	
	/**
	 * getters and setters
	 * */
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getHash() {
		if(this.hash == null){
			return HashTools.md5(title+time);
		}
		return this.hash;
		
		
	}
	public void setHash(String hash) {
		this.hash = hash;
	}

	
}
