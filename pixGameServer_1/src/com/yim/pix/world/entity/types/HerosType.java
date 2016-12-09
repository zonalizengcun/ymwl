package com.yim.pix.world.entity.types;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import com.yim.pix.world.entity.HeroContainer;

public class HerosType implements UserType,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int[] TYPES = new int[] { Types.BLOB };

	@Override
	public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
		return null;
	}

	@Override
	public Object deepCopy(Object arg0) throws HibernateException {
		return arg0;
	}

	@Override
	public Serializable disassemble(Object arg0) throws HibernateException {
		return null;
	}

	@Override
	public boolean equals(Object arg0, Object arg1) throws HibernateException {
		return arg0 == arg1;
	}

	@Override
	public int hashCode(Object arg0) throws HibernateException {
		return arg0.hashCode();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object nullSafeGet(ResultSet resultSet, String[] arg1, Object arg2) throws HibernateException, SQLException {
		byte[] bytes = resultSet.getBytes(arg1[0]);
		try {
			return HeroContainer.parse(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int i) throws HibernateException, SQLException {
		if (value != null) {
			HeroContainer heroContainer = (HeroContainer)value;
			byte[] data = heroContainer.getBytes();
			preparedStatement.setBytes(i, data);
		}
		
	}

	@Override
	public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
		return null;
	}

	@Override
	public Class returnedClass() {
		return HeroContainer.class;
	}

	@Override
	public int[] sqlTypes() {
		return TYPES;
	}

}
