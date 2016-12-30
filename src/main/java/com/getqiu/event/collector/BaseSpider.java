package com.getqiu.event.collector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public abstract class BaseSpider implements PageProcessor{

	@Override
	public abstract Site getSite();

	public abstract void parse(Page page);
	
	@Override
	public void process(Page page) {
		Class<?> requestType = page.getRequest().getClass();
		if(requestType.equals(AdvanceRequest.class)){
			//当前是采用的AdvanceRequest
			//System.out.println("AdvanceRequest.....");
			AdvanceRequest request = (AdvanceRequest) page.getRequest();
			String callback = request.getCallback();
			try {
				Method method = this.getClass().getDeclaredMethod(callback, Page.class);
				method.invoke(this, page);
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else{
			/**
			 * 默认调用parse方法
			 * */
			this.parse(page);
		}
		
	}

}
