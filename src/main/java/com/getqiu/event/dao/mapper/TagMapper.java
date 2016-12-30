package com.getqiu.event.dao.mapper;

import com.getqiu.event.dao.po.Tag;

public interface TagMapper {

	public Tag getById(long id);
	public void insertTag(Tag t);
	public Long tagExists(String label);
	public Tag getByLabel(String label);

}
