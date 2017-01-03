package com.getqiu.event.collector;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Hello world!
 *
 */
public class App 
{
	@Resource
	private Pipeline pipeline;
	
	@Resource 
	private BaseSpider spider;
	
	private String[] startsUrls;

	private int workerNum;
	
	public void setStartsUrls(String[] urls){
		this.startsUrls = urls;
	}
	
	public String[] getStartsUrls(){
		return this.startsUrls;
	}
	
    public int getWorkerNum() {
		return workerNum;
	}

	public void setWorkerNum(int workerNum) {
		this.workerNum = workerNum;
	}

	public static void main( String[] args )
    {

    	@SuppressWarnings("resource")
		ApplicationContext beanContainer = new ClassPathXmlApplicationContext("spring-beans.xml");
    	App app = beanContainer.getBean("app",App.class);
    	app.run();
    }
    
    public void run(){
    	Spider.create(spider).addUrl(startsUrls).thread(workerNum).addPipeline(pipeline).run();
    }
}
