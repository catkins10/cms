package com.yuanluesoft.jeaf.database.hibernate.dialect;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.dialect.StandardSQLFunction;

/**
 * 
 * @author linchuan
 *
 */
public class PostgreSQLDialect extends net.sf.hibernate.dialect.PostgreSQLDialect {

	public PostgreSQLDialect() {
		super();
		registerFunction("date_part", new StandardSQLFunction(Hibernate.INTEGER));
		registerFunction("to_char", new StandardSQLFunction(Hibernate.STRING));
		registerFunction("substring", new StandardSQLFunction(Hibernate.STRING));
	}
}