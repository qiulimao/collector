package com.getqiu.event.dao.mapper;

import org.springframework.stereotype.Repository;

import com.getqiu.event.dao.po.Event;

@Repository
public interface EventMapper {

	public void insertEvent(Event e);
	public Long eventExists(String hash);
	public Event getById(long id);
	
}
