<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">违法当事人</td>
		<td class="tdcontent" ><ext:field property="person"/></td>
		<td class="tdtitle" nowrap="nowrap">案由</td>
		<td class="tdcontent" ><ext:field property="reason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">违法地点</td>
		<td class="tdcontent" ><ext:field property="addr"/></td>
		<td class="tdtitle" nowrap="nowrap">批准立案时间</td>
		<td class="tdcontent" ><ext:field property="filingTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">处罚依据</td>
		<td class="tdcontent" ><ext:field property="basis"/></td>
		<td class="tdtitle" nowrap="nowrap">处罚情况</td>
		<td class="tdcontent" ><ext:field property="result"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">处罚时间</td>
		<td class="tdcontent" colspan="3"><ext:field property="punishTime"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>