package com.yuanluesoft.jeaf.database.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

import org.apache.commons.lang.ObjectUtils;

import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class StringClobType implements UserType {
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.UserType#sqlTypes()
	 */
	public int[] sqlTypes()	{
		return new int[] { Types.CLOB };
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.UserType#returnedClass()
	 */
	public Class returnedClass() {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals(Object x, Object y) {
		return ObjectUtils.equals(x, y);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		return Environment.getDatabaseDialect().readClobText(rs, names[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if(value == null) {
			value = "";
		}
		Environment.getDatabaseDialect().writeClobText(st, (String)value, index);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.UserType#deepCopy(java.lang.Object)
	 */
	public Object deepCopy(Object value) {
		if (value == null) {
			return "";
		}
		return new String((String) value);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.UserType#isMutable()
	 */
	public boolean isMutable() {
		return false;
	}
}