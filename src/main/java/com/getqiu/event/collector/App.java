package com.getqiu.event.collector;

import com.getqiu.event.pipeline.DBPipeline;

import us.codecraft.webmagic.Spider;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

    	String[] start_urls = {"http://news.qq.com","http://mil.qq.com/mil_index.htm","http://sports.qq.com/",
                    "http://ent.qq.com/","http://finance.qq.com/","http://stock.qq.com/",
                    "http://auto.qq.com/","http://tech.qq.com/","http://digi.tech.qq.com/",
                    "http://cd.house.qq.com/","http://bj.house.qq.com/","http://edu.qq.com/",
                    "http://news.qq.com/world_index.shtml","http://news.qq.com/society_index.shtml",
    	};

    	//String[] start_urls = {"http://news.qq.com"};
    	
        Spider.create(new QQEvents()).addUrl(start_urls)
        	  .thread(10).addPipeline(new DBPipeline()).run();
    }
}
