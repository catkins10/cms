<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/listExportPublicInfos">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/view/js/view.js"></script>
	<script>
		function exportPublicInfo() {
			var form = document.forms[0];
			form.action = "<%=request.getContextPath()%>/cms/infopublic/admin/doExportPublicInfo.shtml";
			form.submit();
		}
	</script>
	<table border="0" cellpadding="3" cellspacing="0">
		<tr>
			<td nowrap>目录：</td>
   			<td width="200px"><ext:field property="directoryName"/></td>
			<td>开始日期:</td>
			<td width="100px"><ext:field property="beginDate"/></td>
			<td>结束日期:</td>
			<td width="100px"><ext:field property="endDate"/></td>
			<td><input type="button" class="button" style="width:80px" value="获取信息列表" onclick="FormUtils.doAction('listExportPublicInfos')"></td>
			<td><input type="button" class="button" style="width:100px" value="导出正文和目录" onclick="exportPublicInfo()"></td>
			<td><input type="button" class="button" style="width:80px" value="打印封面" onclick="FormUtils.doAction('printPublicInfoCover')"></td>
		</tr>
	</table>
 	<jsp:include page="/jeaf/view/viewCommon.jsp" />
 	<%request.setAttribute("sortDisabled", "true");%>
	<jsp:include page="/jeaf/view/view.jsp" />
	<div class="viewPageAndCategoryBottomBar">
		<jsp:include page="/jeaf/view/viewPageBar.jsp" />
	</div>
</ext:form>