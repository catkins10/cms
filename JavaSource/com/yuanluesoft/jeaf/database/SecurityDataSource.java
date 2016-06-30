package com.yuanluesoft.jeaf.database;

import org.apache.commons.dbcp.BasicDataSource;

import com.yuanluesoft.jeaf.util.Encoder;

/**
 * 安全的数据源,对用户名、密码、url、JDBC驱动名称加密
 * @author linchuan
 *
 */
public class SecurityDataSource extends BasicDataSource {

	/* (non-Javadoc)
	 * @see org.apache.commons.dbcp.BasicDataSource#setPassword(java.lang.String)
	 */
	public void setPassword(String password) {
		try {
			super.setPassword(Encoder.getInstance().desBase64Decode(password, "yu050718", null));
		}
		catch (Exception e) {
			super.setPassword(password);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.dbcp.BasicDataSource#setUsername(java.lang.String)
	 */
	public void setUsername(String username) {
		try {
			super.setUsername(Encoder.getInstance().desBase64Decode(username, "yu050718", null));
		}
		catch (Exception e) {
			super.setUsername(username);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.dbcp.BasicDataSource#setDriverClassName(java.lang.String)
	 */
	public void setDriverClassName(String driverClassName) {
		try {
			super.setDriverClassName(Encoder.getInstance().desBase64Decode(driverClassName, "yu050718", null));
		}
		catch (Exception e) {
			super.setDriverClassName(driverClassName);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.dbcp.BasicDataSource#setUrl(java.lang.String)
	 */
	public void setUrl(String url) {
		try {
			super.setUrl(Encoder.getInstance().desBase64Decode(url, "yu050718", null));
		}
		catch (Exception e) {
			super.setUrl(url);
		}
	}
}