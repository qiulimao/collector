package com.getqiu.event.pipeline;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.getqiu.event.dao.mapper.EventMapper;
import com.getqiu.event.dao.mapper.EventTagMapper;
import com.getqiu.event.dao.mapper.TagMapper;
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
	private EventMapper eventMapper;
	
	@Resource
	private TagMapper tagMapper;
	
	@Resource
	private EventTagMapper eventTagMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
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
		
		Long existsId = eventMapper.eventExists(event.getHash());

		if(existsId == null)
		{
			eventMapper.insertEvent(event);
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
			Tag tag = tagMapper.getByLabel(t);
			if (tag == null)
			{
				try{
					tag = new Tag();
					tag.setLabel(t);
					tagMapper.insertTag(tag);				
				}catch (PersistenceException e) {
					// 有些时候，unqiue error。
					//虽然查到当前没有这个tag，那是因为多个现成发生了竞争。某个线程查的时候，还没有这个tag
					//但是等它插入的时候，这个时候另外一个现成已经完成tag的写入
					System.out.println("the duplicate tag is: "+t);
					tag = tagMapper.getByLabel(t);
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
		
		if(eventTagMapper.relationExist(relation.getEventId(), relation.getTagId()) == null){
			//这里就有点纳闷了，因为新闻是唯一的，tag也是唯一的。怎么样的情况会出现重复关联呢？
			eventTagMapper.connect(relation);
		}
		
	}
	
}
