package com.getqiu.event.pipeline;

public interface TagMapper {

	public Tag getById(long id);
	public void insertTag(Tag t);
	public Long tagExists(String label);
	public Tag getByLabel(String label);

}
