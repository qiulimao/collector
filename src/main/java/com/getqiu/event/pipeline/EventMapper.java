package com.getqiu.event.pipeline;

public interface EventMapper {

	public void insertEvent(Event e);
	public Long eventExists(String hash);
	public Event getById(long id);
	
}
