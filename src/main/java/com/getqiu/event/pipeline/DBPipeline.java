package com.getqiu.event.pipeline;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.getqiu.event.dao.EventDAO;
import com.getqiu.event.dao.EventTagDAO;
import com.getqiu.event.dao.TagDAO;
import com.getqiu.event.dao.po.Event;
import com.getqiu.event.dao.po.EventTag;
import com.getqiu.event.dao.po.Tag;
import com.getqiu.event.utils.CharactorSegmentor;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component("pipeline")
public class DBPipeline implements Pipeline{

	private Logger logger = Logger.getLogger("collector.pipeline");
	
	@Resource
	private EventDAO eventDAO;
	
	@Resource
	private TagDAO tagDAO;
	
	@Resource
	private EventTagDAO eventTagDAO;
	
	private Lock lock = new ReentrantLock();
	
	@Override
	public void process(ResultItems resultItems, Task task) {

		Event event= new Event();
		event.setTitle(resultItems.get("title").toString());
		event.setCategory(resultItems.get("category").toString());
		event.setContent(resultItems.get("content").toString());
		event.setCover(resultItems.get("cover").toString());
		event.setOrigin(resultItems.get("origin").toString());
		event.setTime(resultItems.get("time").toString());
		event.setUrl(resultItems.get("url").toString());
		event.setRank(resultItems.get("rank"));
		event.setPublisher(resultItems.get("publisher").toString());
		
		Long existsId = eventDAO.eventExists(event.getHash());

		if(existsId == null)
		{
			eventDAO.insertEvent(event);
			/*--------要开始分词处理title---------*/
			createTagIndex(event);
			logger.info("++++ ["+event.getTitle()+"] is added to the database.");
		}
		else{
			logger.info("||||| ["+existsId+" "+event.getTitle()+"] has already been saved.");
		}
	}

	/**
	 * 建立tag标签，并关联相应的event
	 * */
	private void createTagIndex(Event event){
		String title = event.getTitle();
		Set<String> tags = CharactorSegmentor.seperate(title);

		for(String t:tags){
			Tag tag = tagDAO.getByLabel(t);
			/**
			 *创建tag的时候应该去加锁，保证不会重复插入tag
			 *因为在某一时刻，可能n个线程检测到tag数据库都是null，所以同时插入。
			 *数据库因为unique_constraint 就会报错
			 * */
			if (tag == null)
			{
				lock.lock();

				try{
					tag = tagDAO.getByLabel(t);
					/**
					 * 双重检查，很有可能是自己被同步锁阻塞了，回过头来，其实别人已经插入了。
					 * */
					if(tag == null){
						tag = new Tag();
						tag.setLabel(t);
						tagDAO.insertTag(tag);								
					}
				}
				finally {
					lock.unlock();
				}
			}
			connectTagWithEvent(event, tag);
		}
	}
	
	/**
	 * 建立event和tag之间的关系
	 * */
	private void connectTagWithEvent(Event e,Tag t){
		EventTag relation = new EventTag();
		relation.setEventId(e.getId());
		relation.setTagId(t.getId());
		eventTagDAO.connect(relation);
	}
	
}
