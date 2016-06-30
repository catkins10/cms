package com.yuanluesoft.jeaf.tools.blob2text.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.tools.blob2text.forms.Blob2Text;

/**
 * 
 * @author linchuan
 *
 */
public class Convert extends BaseAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Blob2Text blob2Text = (Blob2Text)form;
		try {
			connection = getConnection(blob2Text);
			//读取表
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from " + blob2Text.getTableName();
			resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				byte[] blob = resultSet.getBytes(blob2Text.getFromBlobField());
				if(blob!=null) {
					resultSet.updateString(blob2Text.getToTextField(), new String(blob, "utf-8"));
					resultSet.updateRow();
				}
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			try { resultSet.close();}catch (Exception e) {}
			try { statement.close();}catch (Exception e) {}
			try { connection.close();}catch (Exception e) {}
		}
		response.getWriter().write("convert compelete");
		return null;
	}
	
	/**
	 * 获取连接
	 * @param blob2Text
	 * @return
	 * @throws Exception
	 */
	private Connection getConnection(Blob2Text blob2Text) throws Exception {
		String dbmsType = blob2Text.getDbmsType().toLowerCase();
		String className = null;
		String url = null;
		if("mysql".equals(dbmsType)) {
			className = "org.gjt.mm.mysql.Driver";
			url = "jdbc:mysql://" + blob2Text.getServerIp() + ":" + (blob2Text.getServerPort()==null || "".endsWith(blob2Text.getServerPort()) ? "3306" : blob2Text.getServerPort()) + "/" + blob2Text.getDbName() + "?useUnicode=true&characterEncoding=GBK";
		}
		else if("oracle".equals(dbmsType)) {
			className = "oracle.jdbc.driver.OracleDriver";
			url = "jdbc:oracle:thin:@" + blob2Text.getServerIp() + ":" + (blob2Text.getServerPort()==null || "".endsWith(blob2Text.getServerPort()) ? "1521" : blob2Text.getServerPort()) + ":" + blob2Text.getDbName();
		}
		Class.forName(className);
		return DriverManager.getConnection(url, blob2Text.getUserName(), blob2Text.getPassword());
	}
}
