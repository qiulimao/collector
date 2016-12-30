package com.getqiu.event.pipeline;
import java.util.Set;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.getqiu.event.utils.CharactorSegmentor;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class DBPipeline implements Pipeline{

	private Logger logger = Logger.getLogger("collector.pipeline");
	
	@Override
	public void process(ResultItems resultItems, Task task) {
		SqlSession sqlSession = SessionFactoryUtil.openSession();
		try{
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
			
			EventMapper eventMapper = sqlSession.getMapper(EventMapper.class);
			Long existsId = eventMapper.eventExists(event.getHash());

			if(existsId == null)
			{
				eventMapper.insertEvent(event);
				sqlSession.commit();
				/*--------要开始分词处理title---------*/
				createTagIndex(event, sqlSession);
				logger.info("++++ ["+event.getTitle()+"] is added to the database.");
			}
			else{
				logger.info("||||| ["+existsId+" "+event.getTitle()+"] has already been saved.");
			}

		}catch(Exception e){
			logger.error("xxxxx ["+resultItems.get("url").toString()+"] doesn't collect enough infomation.");
			e.printStackTrace();
		}
		finally{
			sqlSession.close();
		}
		
	}

	/**
	 * 建立tag标签，并关联相应的event
	 * */
	private void createTagIndex(Event event,SqlSession sqlSession){
		String title = event.getTitle();
		Set<String> tags = CharactorSegmentor.seperate(title);
		TagMapper tagMapper = sqlSession.getMapper(TagMapper.class);
		
		for(String t:tags){
			Tag tag = tagMapper.getByLabel(t);
			if (tag == null)
			{
				try{
					tag = new Tag();
					tag.setLabel(t);
					tagMapper.insertTag(tag);
					sqlSession.commit();					
				}catch (PersistenceException e) {
					// 有些时候，unqiue error。
					//虽然查到当前没有这个tag，那是因为多个现成发生了竞争。某个线程查的时候，还没有这个tag
					//但是等它插入的时候，这个时候另外一个现成已经完成tag的写入
					sqlSession.rollback();
					tag = tagMapper.getByLabel(t);
				}

			}
			
			connectTagWithEvent(event, tag, sqlSession);
		}
	}
	
	/**
	 * 建立event和tag之间的关系
	 * */
	private void connectTagWithEvent(Event e,Tag t,SqlSession sqlSession){
		EventTag relation = new EventTag();
		relation.setEventId(e.getId());
		relation.setTagId(t.getId());
		
		EventTagMapper eventTagRelationMapper = sqlSession.getMapper(EventTagMapper.class);
		eventTagRelationMapper.connect(relation);
		sqlSession.commit();
	}
	
}
