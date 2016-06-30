<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="150px" align="right">
	<col width="50%">
	<col width="110px" align="right">
	<col width="50%">
	<tr>
		<td>参建单位名称：</td>
		<td colspan="3"><ext:field writeonly="true" property="projectUnit.name"/></td>
	</tr>
	<tr>
		<td>单位类型：</td>
		<td><ext:field writeonly="true" property="projectUnit.type"/></td>
		<td>资质等级：</td>
		<td><ext:field writeonly="true" property="projectUnit.qualificationLevel"/></td>
	</tr>
	<tr>
		<td>承担合同段名称：</td>
		<td><ext:field writeonly="true" property="projectUnit.partName"/></td>
		<td>合同金额(万元)：</td>
		<td><ext:field writeonly="true" property="projectUnit.contractAmount"/></td>
	</tr>
	<tr>
		<td>合同开始时间：</td>
		<td><ext:field writeonly="true" property="projectUnit.contractBegin"/></td>
		<td>合同结束时间：</td>
		<td><ext:field writeonly="true" property="projectUnit.contractEnd"/></td>
	</tr>
	<tr>
		<td>项目经理(总监/设总)：</td>
		<td><ext:field writeonly="true" property="projectUnit.projectManager"/></td>
		<td>联系电话：</td>
		<td><ext:field writeonly="true" property="projectUnit.tel"/></td>
	</tr>
	<tr>
		<td>单位总部电话：</td>
		<td><ext:field writeonly="true" property="projectUnit.unitTel"/></td>
		<td>单位总部邮编：</td>
		<td><ext:field writeonly="true" property="projectUnit.unitPostcode"/></td>
	</tr>
	<tr>
		<td>参建单位总部地址：</td>
		<td colspan="3"><ext:field writeonly="true" property="projectUnit.unitAddress"/></td>
	</tr>
	<tr>
		<td>参建单位现场邮编：</td>
		<td colspan="3"><ext:field writeonly="true" property="projectUnit.workingPostcode"/></td>
	</tr>
	<tr>
		<td>参建单位现场地址：</td>
		<td colspan="3"><ext:field writeonly="true" property="projectUnit.workingAddress"/></td>
	</tr>
	<tr>
		<td>填报人：</td>
		<td><ext:field writeonly="true" property="projectUnit.reporter"/></td>
		<td>填报时间：</td>
		<td><ext:field writeonly="true" property="projectUnit.reportDate"/></td>
	</tr>
</table>