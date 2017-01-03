package com.getqiu.event.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.getqiu.event.dao.po.EventTag;

@Repository
public interface EventTagMapper {

	public void connect(EventTag eventTag);
	public Long relationExist(@Param("eventId") long eventId,@Param("tagId") long tagId);
}
