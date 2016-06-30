<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">会议室名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">座位容量数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="seating"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">桌椅配备情况</td>
		<td class="tdcontent"><ext:field writeonly="true" property="tableAndChair"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">设备情况</td>
		<td class="tdcontent"><ext:field writeonly="true" property="fixture"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top" style="padding-top:5px">安排情况</td>
		<td class="tdcontent" style="line-height:16px"><pre><ext:write property="meetingsInfo"/></pre></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>