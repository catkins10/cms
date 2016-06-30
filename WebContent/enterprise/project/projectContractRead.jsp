<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="100px" align="right">
	<col width="100%">
	<tr>
		<td>合同名称：</td>
		<td><ext:field writeonly="true" property="contract.contractName"/></td>
	</tr>
	<tr>
		<td>合同单位：</td>
		<td><ext:field writeonly="true" property="contract.contractUnit"/></td>
	</tr>
	<tr>
		<td>合同编号：</td>
		<td><ext:field writeonly="true" property="contract.contractNo"/></td>
	</tr>
	<tr>
		<td>合同金额(元)：</td>
		<td><ext:field writeonly="true" property="contract.contractValue"/></td>
	</tr>
	<tr>
		<td >附件：</td>
		<td><ext:field writeonly="true" property="contract.attachments"/></td>
	</tr>
	<tr>
		<td></td>
		<td>
			<input type="button" value="查看合同" onclick="DocumentUtils.openRemoteDocument('viewDocument', '', null, 'editProjectContractBody')" class="button">
		</td>
	</tr>
	<tr>
		<td valign="top">合同正文预览：</td>
		<td><ext:field property="contract.body" readonly="true" style="height: 150px"/></td>
	</tr>
	<tr>
		<td>备注：</td>
		<td><ext:field writeonly="true" property="contract.remark"/></td>
	</tr>
</table>