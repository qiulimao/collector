package com.getqiu.event.collector;

import us.codecraft.webmagic.Request;

public class AdvanceRequest extends Request{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdvanceRequest(String url){
		super(url);
		this.setCallback("parse");
	}
	
	public AdvanceRequest(String url,String callback){
		super(url);
		this.setCallback(callback);
	}
	
	public String getCallback(){
		return (String) this.getExtra("callback");
	}
	
	public void setCallback(String callback){
		this.putExtra("callback", callback);
	}

}
