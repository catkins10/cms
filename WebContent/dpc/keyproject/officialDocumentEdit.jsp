<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="78px" align="right">
	<col width="100%">
	<tr title="批转该项目的公文标题">
		<td>标题：</td>
		<td><ext:field property="officialDocument.subject"/></td>
	</tr>
	<tr title="业主单位或者项目负责单位提交的报批文件标题" style="display:none">
		<td>报批文件：</td>
		<td><ext:field property="officialDocument.approvalDocuments"/></td>
	</tr>
	<tr title="批准该项目的公文文号">
		<td>批准文号：</td>
		<td><ext:field property="officialDocument.documentNumber"/></td>
	</tr>
	<tr title="批准公文的成文日期">
		<td>批准日期：</td>
		<td><ext:field property="officialDocument.approvalDate"/></td>
	</tr>
	<tr>
		<td valign="top">文件正文：</td>
		<td><ext:field property="officialDocument.body"/></td>
	</tr>
	<tr>
		<td>附件：</td>
		<td><ext:field property="officialDocument.attachments"/></td>
	</tr>
</table>