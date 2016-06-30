<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("editabled", "true");%>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td  class="tdtitle" nowrap="nowrap">项目</td>
		<td class="tdcontent"><ext:field writeonly="true" property="directoryName"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">评价项目</td>
		<td class="tdcontent"><ext:field writeonly="true" property="parentDirectoryName"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">资料上传频率</td>
		<td class="tdcontent"><ext:field property="dataCycle"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">资料上传截止时间</td>
		<td class="tdcontent"><ext:field property="dataCycleEnd" title="按周:1～7；按月:1~28；按年:日期，如:1-1"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">资料上传催办提前天数</td>
		<td class="tdcontent"><ext:field property="dataUrgency"/></td>
	</tr>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
</table>