<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>
<%@ page import="org.apache.struts.taglib.TagUtils" %>
<%@ page import="org.apache.struts.taglib.html.Constants" %>

<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
ViewPackage viewPackage = (ViewPackage)TagUtils.getInstance().lookup(pageContext, Constants.BEAN_KEY, viewPackageName, null);
%>

<div class="viewPageBar" style="width:100%; float:right">
	<div class="viewRecordCount" style="float:left">
		共<ext:equal value="true" property="<%=viewPackageName + ".searchMode"%>">搜索到 </ext:equal> <span style="color:red"><ext:write property="<%=viewPackageName + ".recordCount"%>"/></span> 条记录 <span style="color:red"><ext:write property="<%=viewPackageName + ".pageCount"%>"/></span> 页 每页 <span style="color:red"><ext:write property="<%=viewPackageName + ".view.pageRows"%>"/></span> 条记录
	</div>
	<div class="viewPageSwitch" style="float:right">
		<ext:equal value="1" property="<%=viewPackageName + ".curPage"%>">
			<div class="firstPageLinkImageDisable" style="float:left" title="第一页"></div>
			<div class="firstPageLinkTextDisable" style="float:left">第一页</div>
			<div class="previousPageLinkImageDisable" style="float:left" title="上一页"></div>
			<div class="previousPageLinkTextDisable" style="float:left">上一页</div>
		</ext:equal>
		<ext:notEqual value="1" property="<%=viewPackageName + ".curPage"%>">
			<a href="javascript:gotopage('<%=viewPackageName%>', 'first')" title="第一页">
				<div class="firstPageLinkImage" style="float:left"></div>
				<div class="firstPageLinkText" style="float:left">第一页</div>
			</a>
			<a href="javascript:gotopage('<%=viewPackageName%>', 'prev')" title="上一页">
				<div class="previousPageLinkImage" style="float:left"></div>
				<div class="previousPageLinkText" style="float:left">上一页</div>
			</a>
		</ext:notEqual>
	<%	//int beginPage = (viewPackage.getCurPage() / 10) * 10;
		int beginPage = viewPackage.getCurPage() -4;
		int endPage = 0;
		if(beginPage<1){
			beginPage = 1;
		}
		//if(beginPage==0) {
			//beginPage = 1;
		//}
		endPage = (beginPage+8 > viewPackage.getPageCount() ? viewPackage.getPageCount() :	beginPage + 8);
		if(beginPage!=1) { %>
			<div class="pageNo" style="float:left"><a href="javascript:FormUtils.doAction(FormUtils.getCurrentAction(), '<%=viewPackageName%>.currentViewAction=gotoPage&viewPackageName=<%=viewPackageName%>&<%=viewPackageName%>.page=<%=1 %>')"><%=1 %></a></div>
			<div class="pageNo" style="float:left">...</div>
		<%}
		for(int i=beginPage; i<=endPage; i++)	{
			if(i==viewPackage.getCurPage()) { %>
				<div class="curPageNo" style="float:left"><%=i%></div>
	<%		}
			else { %>
				<div class="pageNo" style="float:left"><a href="javascript:FormUtils.doAction(FormUtils.getCurrentAction(), '<%=viewPackageName%>.currentViewAction=gotoPage&viewPackageName=<%=viewPackageName%>&<%=viewPackageName%>.page=<%=i %>')"><%=i %></a></div>
	<%		}
		}
		if(endPage!=viewPackage.getPageCount()){%>
			<div class="pageNo" style="float:left">...</div>
			<div class="pageNo" style="float:left"><a href="javascript:FormUtils.doAction(FormUtils.getCurrentAction(), '<%=viewPackageName%>.currentViewAction=gotoPage&viewPackageName=<%=viewPackageName%>&<%=viewPackageName%>.page=<%=viewPackage.getPageCount() %>')"><%=viewPackage.getPageCount() %></a></div>
		<%}%>
		<ext:equal propertyCompare="<%=viewPackageName + ".pageCount"%>" property="<%=viewPackageName + ".curPage"%>">
			<div class="nextPageLinkImageDisable" style="float:left" title="下一页"></div>
			<div class="nextPageLinkTextDisable" style="float:left">下一页</div>
			<div class="lastPageLinkImageDisable" style="float:left" title="最后一页"></div>
			<div class="lastPageLinkTextDisable" style="float:left">最后一页</div>
		</ext:equal>
		<ext:notEqual propertyCompare="<%=viewPackageName + ".pageCount"%>" property="<%=viewPackageName + ".curPage"%>">
			<a href="javascript:gotopage('<%=viewPackageName%>', 'next')" title="下一页">
				<div class="nextPageLinkImage" style="float:left"></div>
				<div class="nextPageLinkText" style="float:left">下一页</div>
			</a>
			<a href="javascript:gotopage('<%=viewPackageName%>', 'last')" title="最后一页">
				<div class="lastPageLinkImage" style="float:left"></div>
				<div class="lastPageLinkText" style="float:left">最后一页</div>
			</a>
		</ext:notEqual>
		<div class="pageNumber" style="float:left">
			&nbsp;跳转至第&nbsp;<input class="pageNumberInput" value="<ext:write property="<%=viewPackageName + ".curPage"%>"/>/<ext:write property="<%=viewPackageName + ".pageCount"%>"/>" class="text" size="5" onfocus="oldValue=value;value=''" onblur="value=oldValue;" onkeypress="gotopage('<%=viewPackageName%>', 'keypress', event)"/>&nbsp;页&nbsp;
		</div>
	</div>
</div>