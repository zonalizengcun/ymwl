package com.yim.persist.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.yim.persist.DataAccessException;
import com.yim.persist.EntityManager;
/**
 * 用于数据库操作
 */
public class HibernateEntityManager implements EntityManager {

	private Configuration configuration;
	private SessionFactory sessionFactory;

	public HibernateEntityManager(Configuration configuration) {
		this.configuration = configuration;
		this.sessionFactory = configuration.buildSessionFactory();
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public <T> T find(Class<T> entityClass, Serializable key) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			T ret = (T) session.get(entityClass, key);
			session.clear();
			tx.commit();
			return ret;
		} catch (Exception ex) {
			tx.rollback();
			throw new DataAccessException(ex);
		}
	}

	@Override
	public void create(Object entity) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(entity);
			tx.commit();
		} catch (Exception ex) {
			tx.rollback();
			throw new DataAccessException(ex);
		}
	}
	
	public void create(Collection<?> entities, int batch_size) {
		List<Object> cache = new ArrayList<Object>(entities);
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			for (int i = 0; i < cache.size(); i++) {
				Object entity = cache.get(i);
				if (entity == null) {
					continue;
				}
				try {
					session.save(entity);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if ((i + 1) % batch_size == 0) {
					try{
						tx.commit();
					}catch (Exception e) {
						tx.rollback();
						e.printStackTrace();
					}
					session = getSession();
					tx = session.beginTransaction();
				}
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Object entity) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.delete(entity);
			tx.commit();
		} catch (Exception ex) {
			tx.rollback();
			throw new DataAccessException(ex);
		}
	}
	
	public void delete(Collection<?> entities, int batch_size) {
		List<Object> cache = new ArrayList<Object>(entities);
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			for (int i = 0; i < cache.size(); i++) {
				Object entity = cache.get(i);
				if (entity == null) {
					continue;
				}
				try {
					session.delete(entity);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if ((i + 1) % batch_size == 0) {
					try{
						tx.commit();
					}catch (Exception e) {
						tx.rollback();
						e.printStackTrace();
					}
					session = getSession();
					tx = session.beginTransaction();
				}
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
	}
	

	@Override
	public void saveOrUpdate(Object entity) {
		if (entity == null) {
			return;
		}
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.saveOrUpdate(entity);
			tx.commit();
		} catch (Exception ex) {
			tx.rollback();
			throw new DataAccessException(ex);
		}
	}
	@Override
	public void update(Object entity) {
		if (entity == null) {
			return;
		}
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.update(entity);
			tx.commit();
		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			throw new DataAccessException(ex);
		}
	}
	
	@Override
	public void saveOrUpdate(Collection<?> entities, int batch_size) {
		List<Object> cache = new ArrayList<Object>(entities);
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			for (int i = 0; i < cache.size(); i++) {
				Object entity = cache.get(i);
				if (entity == null) {
					continue;
				}
				try {
					session.saveOrUpdate(entity);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if ((i + 1) % batch_size == 0) {
					try{
						tx.commit();
					}catch (Exception e) {
						tx.rollback();
						e.printStackTrace();
					}
					session = getSession();
					tx = session.beginTransaction();
				}
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
	}
	@Override
	public void update(Collection<?> entities, int batch_size) {
		List<Object> cache = new ArrayList<Object>(entities);
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			for (int i = 0; i < cache.size(); i++) {
				Object entity = cache.get(i);
				if (entity == null) {
					continue;
				}
				try {
					session.update(entity);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if ((i + 1) % batch_size == 0) {
					try{
						tx.commit();
					}catch (Exception e) {
						tx.rollback();
						e.printStackTrace();
					}
					session = getSession();
					tx = session.beginTransaction();
				}
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
	}
	@Override
	public <T> T fetch(Class<T> klass, String hql, Object... values) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery(hql);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
			}
			T result = (T) query.uniqueResult();
			session.clear();
			tx.commit();
			return result;
		} catch (Exception ex) {
			tx.rollback();
			throw new DataAccessException(ex);
		}
	}

	@Override
	public <T> List<T> query(Class<T> klass, String hql, Object... values) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery(hql);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
			}
			List<T> result = query.list();
			session.clear();
			tx.commit();
			return result;
		} catch (Exception ex) {
			tx.rollback();
			throw new DataAccessException(ex);
		}
	}

	@Override
	public <T> List<T> limitQuery(Class<T> klass, String hql, int start, int count, Object... values) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery(hql);
			query.setFirstResult(start);
			query.setMaxResults(count);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
			}
			List<T> result = query.list();
			session.clear();
			tx.commit();
			return result;
		} catch (Exception ex) {
			tx.rollback();
			throw new DataAccessException(ex);
		}
	}

	@Override
	public long count(String hql, Object... values) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery(hql);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
			}
			Long count = (Long) query.uniqueResult();
			session.clear();
			tx.commit();
			return count;
		} catch (Exception ex) {
			tx.rollback();
			throw new DataAccessException(ex);
		}
	}

	/**
	 * 执行特殊的查询，查询的结果并不是某个映射的对象或者映射对象的集合，而是对象的某些字段或者sum,avg等计算字段
	 * @param hql
	 * @param values
	 * @return
	 */
	public List nativeQuery(String hql, Object... values) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery(hql);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
			}
			List ret = query.list();
			session.clear();
			tx.commit();
			return ret;
		} catch (Exception e) {
			tx.rollback();
			throw new DataAccessException(e);
		}
	}
	
	public Object uniqueResult(String hql,Object... values){
		Session session = getSession();
    	Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery(hql);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
			}
			Object ret = query.uniqueResult();
			session.clear();
			tx.commit();
			return ret;
		} catch (Exception ex) {
			tx.rollback();
			throw new RuntimeException(ex);
		}
    }
}
