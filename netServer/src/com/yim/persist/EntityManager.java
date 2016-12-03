package com.yim.persist;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 数据持久化管理器,通过此接口来进行数据的持久化工作(包括存储数据到数据库，从数据库取出数据，删除数据等等)
 * @author Jeffrey
 *
 */
public interface EntityManager {
	
	/**
	 * 获取指定类型指定key的对象
	 * @param entityClass 指定类型
	 * @param key 指定的主键
	 * @return 返回指定数据，如果没找到返回null
	 */
	public <T> T find(Class<T> entityClass, Serializable key);
	
	
	/**
	 * 创建对象,将对象持久化到数据库中
	 * @param entity 需要被创建的对象
	 */
	public void create(Object entity);
	
	/**
	 * 批量创建对象
	 * @param entities
	 * @param batch_size
	 */
	public void create(Collection<?> entities, int batch_size);
	/**
	 * 将指定对象从数据库中删除
	 * @param entity 被删除的对象
	 */
	public void delete(Object entity);
	
	/**
	 * 批量删除对象
	 * @param entities
	 * @param batch_size
	 */
	public void delete(Collection<?> entities, int batch_size);
	
	/**
	 * 更新指定对象
	 * @param entity 需要被更新的对象
	 */
	public void update(Object entity);
	/**
	 * 保存或者更新对象
	 * @param entity 需要被更新的对象
	 */
	public void saveOrUpdate(Object entity);
	/**
	 * 批量更新对象
	 * @param entities
	 * @param batch_size
	 */
	public void update(Collection<?> entities, int batch_size);
	/**
	 * 批量保存或者更新对象
	 * @param entities
	 * @param batch_size
	 */
	public void saveOrUpdate(Collection<?> entities, int batch_size);
	/**
	 * 根据指定条件查询指定的对象，注意此方法只返回单个对象，如果查询条件返回多个对象，此方法将抛出异常
	 * @param klass 指定的类型
	 * @param hql 查询的hql语句
	 * @param values 查询需要的参数值，如果没有参数可以为null
	 */
	public <T> T fetch(Class<T> klass, String hql, Object... values);
	
	/**
	 * 根据指定条件查询对象列表
	 * @param klass 指定的类型
	 * @param hql 查询的hql语句
	 * @values 查询需要的参数值，如果没有参数可以为null
	 */
	public <T> List<T> query(Class<T> klass, String hql, Object... values);
	
	/**
	 * 根据指定条件查询指定数量的对象列表
	 * @param klass 指定的类型
	 * @param hql 查询的hql语句
	 * @param start 开始位置
	 * @param count 数量
	 * @param values 查询需要的参数值，如果没有参数可以为null
	 */
	public <T> List<T> limitQuery(Class<T> klass, String hql, int start, int count, Object... values);
	
	
	/**
	 * 根据条件，返回数据库中符合条件的对象数量
	 * @param hql 查询的hql语句，语句中必须要 有"select count(*)"类似的语句
	 * @param values 查询需要的参数，如果没有参数可以为null
	 * @return
	 */
	public long count(String hql, Object... values);
	
	/**
	 * 执行特殊的查询，查询的结果并不是某个映射的对象或者映射对象的集合，而是对象的某些字段或者sum,avg等计算字段
	 * @param hql
	 * @param values
	 * @return
	 */
	public List nativeQuery(String hql, Object... values);
}
