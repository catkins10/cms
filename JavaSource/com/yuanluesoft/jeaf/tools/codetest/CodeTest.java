package com.yuanluesoft.jeaf.tools.codetest;

import com.yuanluesoft.jeaf.database.dialect.oracle.OracleDatabaseDialect;

/**
 * 
 * @author linchuan
 *
 */
public class CodeTest {
	
	public static void main(String[] args) throws Exception {
		System.out.println(OracleDatabaseDialect.class.getDeclaredField("threadLocal"));
	}
}