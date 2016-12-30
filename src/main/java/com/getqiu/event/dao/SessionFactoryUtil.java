package com.getqiu.event.dao;

import java.io.IOException;
import java.io.InputStream;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SessionFactoryUtil {

	//必须设置为 volatile 不然会发生双重检查锁定BUG
	private volatile static SqlSessionFactory sqlSessionFactory = null;
	
	private static String mybatisConfigureFileName = "com/getqiu/event/dao/mybatis-config.xml";
	
	private SessionFactoryUtil(){}
	
	public static void setMybatisCofingureFile(String filePath)
	{
		mybatisConfigureFileName = filePath;
	}
	
	public static SqlSessionFactory init()
	{
		
		synchronized (SessionFactoryUtil.class) {
			
			
			if(sqlSessionFactory == null)
			{
				try
				{
					InputStream configureStream = Resources.getResourceAsStream(mybatisConfigureFileName);
					sqlSessionFactory = new SqlSessionFactoryBuilder().build(configureStream);
				}
				catch(IOException ioe)
				{
					System.out.println("error in reading mybatis-config.xml");
				}
			}			
		}
		
		return sqlSessionFactory;
		
	}
	
	public static SqlSession openSession()
	{
		if(sqlSessionFactory == null)
		{
			init();
		}
		return sqlSessionFactory.openSession();
	}
}
