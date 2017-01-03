package com.getqiu.event.dao.mapper;

import org.springframework.stereotype.Repository;

import com.getqiu.event.dao.po.Tag;

@Repository
public interface TagMapper {

	public Tag getById(long id);
	public void insertTag(Tag t);
	public Long tagExists(String label);
	public Tag getByLabel(String label);

}
