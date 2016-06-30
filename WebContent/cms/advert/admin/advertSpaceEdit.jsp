<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("editabled", "true");%>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col align="left">
	<col width="50%">
	<col align="left">
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td colspan="3" class="tdcontent"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否浮动广告</td>
		<td colspan="3" class="tdcontent">
			<ext:equal value="create" property="act">
				<ext:field property="isFloat"/>
			</ext:equal>
			<ext:notEqual value="create" property="act">
				<ext:field property="isFloat" writeonly="true"/>
			</ext:notEqual>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">宽度</td>
		<td class="tdcontent"><ext:field property="width"/></td>
		<td class="tdtitle" nowrap="nowrap">高度</td>
		<td class="tdcontent"><ext:field property="height"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">报价</td>
		<td colspan="3" class="tdcontent"><ext:field property="price"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">无广告时显示内容</td>
		<td colspan="3" class="tdcontent"><ext:field property="freeContent"/></td>
	</tr>
</table>