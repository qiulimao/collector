package com.getqiu.event.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.getqiu.event.dao.mapper.TagMapper;
import com.getqiu.event.dao.po.Tag;

@Service("tagDAO")
public class TagDAO implements TagMapper{

	@Resource
	private TagMapper tagMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Tag getById(long id) {
		return tagMapper.getById(id);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void insertTag(Tag t) {
		tagMapper.insertTag(t);
		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)	
	public Long tagExists(String label) {
		return tagMapper.tagExists(label);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)	
	public Tag getByLabel(String label) {
		return tagMapper.getByLabel(label);
	}

	
}
