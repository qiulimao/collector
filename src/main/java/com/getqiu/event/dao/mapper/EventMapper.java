package com.getqiu.event.dao.mapper;

import com.getqiu.event.dao.po.Event;

public interface EventMapper {

	public void insertEvent(Event e);
	public Long eventExists(String hash);
	public Event getById(long id);
	
}
