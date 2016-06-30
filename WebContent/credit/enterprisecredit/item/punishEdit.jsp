<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">案件名称</td>
		<td class="tdcontent" ><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">处罚单位或个人名称</td>
		<td class="tdcontent" ><ext:field property="unit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">案件事由</td>
		<td class="tdcontent"><ext:field property="reason"/></td>
		<td class="tdtitle" nowrap="nowrap">法定代表人（负责人）姓名</td>
		<td class="tdcontent"><ext:field property="person"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">统一社会信用代码</td>
		<td class="tdcontent"><ext:field property="code"/></td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field property="remark"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>