package com.getqiu.event.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.getqiu.event.dao.mapper.EventTagMapper;
import com.getqiu.event.dao.po.EventTag;

@Service("eventTagDAO")
public class EventTagDAO  implements EventTagMapper	{

	@Resource
	private EventTagMapper eventTagMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void connect(EventTag eventTag) {
		eventTagMapper.connect(eventTag);
		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Long relationExist(long eventId, long tagId) {
		return eventTagMapper.relationExist(eventId, tagId);
	}

}
