<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>

<%
	String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
	if(viewPackageName==null || viewPackageName.equals("")) {
		viewPackageName = "viewPackage";
	}
%>

<div class="viewPageBar">
	<div class="viewRecordCount" style="float:left">
		共<ext:equal value="true" property="<%=viewPackageName + ".searchMode"%>">搜索到 </ext:equal> <ext:write property="<%=viewPackageName + ".recordCount"%>"/> <%=(request.getAttribute("recordTitle")==null ? "条记录" : request.getAttribute("recordTitle"))%>
	</div>
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
	<div class="pageNumber" style="float:left">
		<input class="pageNumberInput" value="<ext:write property="<%=viewPackageName + ".curPage"%>"/>/<ext:write property="<%=viewPackageName + ".pageCount"%>"/>" size="5" onfocus="oldValue=value;value=''" onblur="value=oldValue;" onkeypress="gotopage('<%=viewPackageName%>', 'keypress', event)"/>
	</div>
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
</div>