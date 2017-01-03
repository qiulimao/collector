package com.getqiu.event.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.getqiu.event.dao.mapper.EventMapper;
import com.getqiu.event.dao.po.Event;

@Service("eventDAO")
public class EventDAO implements EventMapper{

	@Resource
	private EventMapper eventMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void insertEvent(Event e) {
		eventMapper.insertEvent(e);
		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Long eventExists(String hash) {
		return eventMapper.eventExists(hash);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Event getById(long id) {
		return eventMapper.getById(id);
	}

}
