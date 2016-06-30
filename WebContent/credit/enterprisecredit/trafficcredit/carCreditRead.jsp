<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">处罚决定书案号</td>
		<td class="tdcontent" ><ext:wtite property="bookCode"/></td>
		<td class="tdtitle" nowrap="nowrap">单位</td>
		<td class="tdcontent" ><ext:wtite property="unit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">当事人</td>
		<td class="tdcontent"><ext:wtite property="person"/></td>
		<td class="tdtitle" nowrap="nowrap">车号</td>
		<td class="tdcontent"><ext:wtite property="carNo"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">案由</td>
		<td class="tdcontent"><ext:wtite property="reson"/></td>
		<td class="tdtitle" nowrap="nowrap">案发时间</td>
		<td class="tdcontent"><ext:wtite property="happenTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">缴款日期</td>
		<td class="tdcontent" ><ext:wtite property="payTime"/></td>
		<td class="tdtitle" nowrap="nowrap">强制措施</td>
		<td class="tdcontent" ><ext:wtite property="auditor"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">移交中队</td>
		<td class="tdcontent" ><ext:wtite property="transfer"/></td>
		<td class="tdtitle" nowrap="nowrap">处罚依据</td>
		<td class="tdcontent" ><ext:wtite property="basis"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">处罚机关</td>
		<td class="tdcontent" ><ext:wtite property="department"/></td>
		<td class="tdtitle" nowrap="nowrap">罚款金额</td>
		<td class="tdcontent" ><ext:wtite property="money"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:wtite property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:wtite property="created"/></td>
	</tr>
</table>