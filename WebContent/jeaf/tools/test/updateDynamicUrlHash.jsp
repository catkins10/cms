<%@page import="com.yuanluesoft.cms.pagebuilder.pojo.StaticPage"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@page import="com.yuanluesoft.jeaf.database.DatabaseService"%>

<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
	String hql = "from StaticPage StaticPage order by StaticPage.id";
	for(int i=0; ;i+=200) {
		List pages = databaseService.findRecordsByHql(hql, i, 200);
		for(Iterator iterator = pages==null ? null : pages.iterator(); iterator!=null && iterator.hasNext();) {
			StaticPage staticPage = (StaticPage)iterator.next();
			if(staticPage.getDynamicUrl()!=null) {
				staticPage.setDynamicUrlHash(staticPage.getDynamicUrl().hashCode());
				databaseService.updateRecord(staticPage);
			}
		}
		if(pages==null || pages.size()<200) {
			break;
		}
	}
	out.print("complete");
%>