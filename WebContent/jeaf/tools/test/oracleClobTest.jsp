<%@page import="java.io.PrintWriter"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="java.sql.SQLException"%>
<%@page import="net.sf.hibernate.HibernateException"%>
<%@page import="net.sf.hibernate.Session"%>
<%@page import="org.springframework.orm.hibernate.HibernateCallback"%>
<%@page import="org.springframework.orm.hibernate.HibernateTemplate"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@page import="com.yuanluesoft.jeaf.database.DatabaseService"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>

<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	try {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		final PrintWriter writer = response.getWriter();
		final HibernateTemplate hibernateTemplate = (HibernateTemplate)PropertyUtils.getProperty(databaseService, "hibernateTemplate");
		final Object databaseDialect = PropertyUtils.getProperty(databaseService, "databaseDialect");
		final Object extend = Class.forName("com.yuanluesoft.workflow.server.engin.pojo.WorkflowDefinitionExtend").newInstance();
		StringBuffer a = new StringBuffer();
		for(int i = 0; i < 100000; i++) {
			a.append("123456789012<br>");
		}
		a.append("." + a.length());
		PropertyUtils.setProperty(extend, "configure", a.toString());
		PropertyUtils.setProperty(extend, "id", new Long(12388));
		hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session.save(extend);
				    session.flush();
				    Method freeTemporaryClobMethod = databaseDialect.getClass().getMethod("freeTemporaryClob", null);
				    freeTemporaryClobMethod.invoke(databaseDialect, null);
				}
				catch(Exception e) {
					e.printStackTrace(writer);
				}
			    return null;
			}
		});
		out.println(databaseService.findRecordByHql("select WorkflowDefinitionExtend.configure from WorkflowDefinitionExtend WorkflowDefinitionExtend where WorkflowDefinitionExtend.id=12388"));
		databaseService.deleteRecordsByHql("from WorkflowDefinitionExtend WorkflowDefinitionExtend where WorkflowDefinitionExtend.id=12388");
	}
	catch(Exception e) {
		out.print("<pre>");
		e.printStackTrace(response.getWriter());
		out.print("</pre>");
	}
%>