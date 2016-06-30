<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@page import="com.yuanluesoft.jeaf.view.sqlview.model.SqlViewPackage"%>
<%@ page import="org.apache.struts.taglib.TagUtils" %>
<%@ page import="org.apache.struts.taglib.html.Constants" %>

<%
String viewPackageName = (String)request.getAttribute(SqlViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
SqlViewPackage viewPackage = (SqlViewPackage)TagUtils.getInstance().lookup(pageContext, Constants.BEAN_KEY, viewPackageName, null);
%>
<div class="viewPageBar" style="float:right">
	<div class="viewRecordCount" style="float:left">
		共 <ext:write property="<%=viewPackageName + ".recordCount"%>"/> 条记录
	</div>
	<ext:equal value="1" property="<%=viewPackageName + ".curPage"%>">
		<div class="firstPageLinkImageDisable" style="float:left" title="第一页"></div>
		<div class="firstPageLinkTextDisable" style="float:left">第一页</div>
		<div class="previousPageLinkImageDisable" style="float:left" title="上一页"></div>
		<div class="previousPageLinkTextDisable" style="float:left">上一页</div>
	</ext:equal>
	<ext:notEqual value="1" property="<%=viewPackageName + ".curPage"%>">
		<a href="" onclick="gotopage('<%=viewPackageName%>', 'first');return false;" title="第一页">
			<div class="firstPageLinkImage" style="float:left"></div>
			<div class="firstPageLinkText" style="float:left">第一页</div>
		</a>
		<a href="" onclick="gotopage('<%=viewPackageName%>', 'prev');return false;" title="上一页">
			<div class="previousPageLinkImage" style="float:left"></div>
			<div class="previousPageLinkText" style="float:left">上一页</div>
		</a>
	</ext:notEqual>
	<div class="pageNumber" style="float:left">
		<input class="pageNumberInput" value="<ext:write property="<%=viewPackageName + ".curPage"%>"/>/<ext:write property="<%=viewPackageName + ".pageCount"%>"/>" class="text" size="5" onfocus="oldValue=value;value=''" onblur="value=oldValue;" onkeypress="gotopage('<%=viewPackageName%>', 'keypress', event)"/>
	</div>
<%	int beginPage = (viewPackage.getCurPage() / 10) * 10;
	int endPage = 0;
	if(beginPage==0) {
		beginPage = 1;
	}
	endPage = (beginPage+10 > viewPackage.getPageCount() ? viewPackage.getPageCount() :	beginPage + 9);
	for(int i=beginPage; i<=endPage; i++)	{
		if(i==viewPackage.getCurPage()) { %>
			<div class="curPageNo" style="float:left"><%=i%></div>
<%		}
		else { %>
			<div class="pageNo" style="float:left"><a href="" onclick="FormUtils.doAction(FormUtils.getCurrentAction(), '<%=viewPackageName%>.currentViewAction=gotoPage&viewPackageName=<%=viewPackageName%>&<%=viewPackageName%>.page=<%=i %>');return false;"><%=i %></a></div>
<%		}
	} %>
	<ext:equal propertyCompare="<%=viewPackageName + ".pageCount"%>" property="<%=viewPackageName + ".curPage"%>">
		<div class="nextPageLinkImageDisable" style="float:left" title="下一页"></div>
		<div class="nextPageLinkTextDisable" style="float:left">下一页</div>
		<div class="lastPageLinkImageDisable" style="float:left" title="最后一页"></div>
		<div class="lastPageLinkTextDisable" style="float:left">最后一页</div>
	</ext:equal>
	<ext:notEqual propertyCompare="<%=viewPackageName + ".pageCount"%>" property="<%=viewPackageName + ".curPage"%>">
		<a class="link" href="" onclick="gotopage('<%=viewPackageName%>', 'next');return false;" title="下一页">
			<div class="nextPageLinkImage" style="float:left"></div>
			<div class="nextPageLinkText" style="float:left">下一页</div>
		</a>
		<a class="link" href="" onclick="gotopage('<%=viewPackageName%>', 'last');return false;" title="最后一页">
			<div class="lastPageLinkImage" style="float:left"></div>
			<div class="lastPageLinkText" style="float:left">最后一页</div>
		</a>
	</ext:notEqual>
</div>