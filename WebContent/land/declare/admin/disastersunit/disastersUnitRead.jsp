<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<table width="100%" border="0" cellpadding="3" cellspacing="3" >
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td  nowrap="nowrap">勘察单位</td>
		<td  colspan="3"><ext:field property="investigationUnit" writeonly="true"/></td>
	</tr>
	<tr>
	    <td  nowrap="nowrap">资质类型</td>
		<td colspan="3"><ext:field property="qualificationType" writeonly="true"/></td>
	</tr>
	<tr>
		<td  nowrap="nowrap">资质等级</td>
		<td  ><ext:field property="qualificationLevel" writeonly="true"/></td>
		<td  nowrap="nowrap">审批时间</td>
		<td ><ext:field property="approvalTime" writeonly="true"/></td>
	</tr>
</table>