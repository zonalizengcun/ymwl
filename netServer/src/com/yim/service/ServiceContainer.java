package com.yim.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServiceContainer {
	
	private static ServiceContainer container;
	
	protected Map<Class<? extends IService>,IService> services = new LinkedHashMap<Class<? extends IService>, IService>();
	
	public static ServiceContainer getInstance(){
		if (container == null) {
			container = new ServiceContainer();
		}
		return container;
	}
	
	public ServiceContainer(){
		
	}
	/**
	 * 注册一个Service到容器中，Service的startup方法将会被调用，如果在调用Service的sartup方法时抛出异常，Service将不会被加入到容器中
	 * @param serviceClass
	 * @param service
	 * @throws ServiceException
	 */
	public <T extends IService> void register(Class<T> serviceClass, T service) throws ServiceException{
		try {
			service.startup();
			services.put(serviceClass, service);
			
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * 替换一个Service到容器中
	 * @param serviceClass
	 * @param service
	 * @throws ServiceException
	 */
	public <T extends IService> void replace(Class<T> serviceClass, T service) {
		services.put(serviceClass, service);
	}
	
	/**
	 * 在系统退出的时候，应该调用此方法，方法中将会依照加入Service时的倒序调用各个Service的shutdown方法
	 */
	public void shutdown() {
		List<IService> serviceList = new ArrayList<IService>(this.services.values());
		for(int i=serviceList.size()-1;i>=0;i--){
			IService service = serviceList.get(i);
			service.shutdown();
		}
	}
	
	/**
	 * 获取Service的实例
	 * @param serviceClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends IService> T get(Class<T> serviceClass) {
		return (T)services.get(serviceClass);
	}
}
