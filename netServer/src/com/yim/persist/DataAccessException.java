package com.yim.persist;

/**
 * 持久化错误抛出的Exception，这个Exception是RuntimeException，所以可以不用catch
 * @author Jeffrey
 *
 */
public class DataAccessException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public DataAccessException(Throwable cause) {
		super(cause);
	}
	
	public DataAccessException(String msg) {
		super(msg);
	}
}
