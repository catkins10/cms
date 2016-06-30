<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="100px" align="right">
	<col width="100%">
	<tr>
		<td>合同名称：</td>
		<td><ext:field property="contract.contractName"/></td>
	</tr>
	<tr>
		<td>合同单位：</td>
		<td><ext:field property="contract.contractUnit"/></td>
	</tr>
	<tr>
		<td>合同编号：</td>
		<td><ext:field property="contract.contractNo"/></td>
	</tr>
	<tr>
		<td>合同金额(元)：</td>
		<td><ext:field property="contract.contractValue"/></td>
	</tr>
	<tr>
		<td >附件：</td>
		<td><ext:field property="contract.attachments"/></td>
	</tr>
	<ext:equal property="contractCreated" value="false">
		<tr>
			<td>选择模板：</td>
			<td>
				<div style="width:180px; float:left;"><ext:field property="selectedContractTemplateName"/></div>
				<div style="float:left;">
					&nbsp;<input type="button" value="起草合同" onclick="DocumentUtils.openRemoteDocument('createDocument', '', 'FormUtils.doAction(&quot;saveProjectContractBody&quot;)', 'editProjectContractBody')" class="button">
				</div>
			</td>
		</tr>
	</ext:equal>
	<ext:equal property="contractCreated" value="true">
		<tr>
			<td></td>
			<td><input type="button" value="修改合同" onclick="DocumentUtils.openRemoteDocument('editDocument', '', 'FormUtils.doAction(&quot;saveProjectContractBody&quot;)', 'editProjectContractBody')" class="button"></td>
		</tr>
	</ext:equal>
	<tr>
		<td valign="top">合同正文预览：</td>
		<td><ext:field property="contract.body" readonly="true" style="height: 150px"/></td>
	</tr>
	<tr>
		<td>备注：</td>
		<td><ext:field property="contract.remark"/></td>
	</tr>
</table>