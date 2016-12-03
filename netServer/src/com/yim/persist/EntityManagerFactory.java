package com.yim.persist;

import org.hibernate.cfg.Configuration;

import com.yim.persist.impl.HibernateEntityManager;




public class EntityManagerFactory {
	
	private static EntityManager manager;
	
	public static EntityManager createManager(){
		if(manager==null){
			Configuration configuration = new Configuration().configure();
			manager = new HibernateEntityManager(configuration);
		}
		return manager;
	}
}
