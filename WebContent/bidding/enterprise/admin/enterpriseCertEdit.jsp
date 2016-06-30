<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<tr>
		<td width="100px" align="right">资质类型：</td>
		<td>
			<ext:equal value="其它" property="cert.type">
				<ext:field property="cert.type"/>
			</ext:equal>
			<ext:notEqual value="其它" property="cert.type">
				<ext:field writeonly="true" property="cert.type"/>
				<html:hidden property="cert.type"/>
			</ext:notEqual>
		</td>
	</tr>
	<tr>
		<td align="right">资质证书：</td>
		<td><ext:field property="cert.certPicture"/></td>
	</tr>
	<tr>
		<td align="right">取得资质时间：</td>
		<td><ext:field property="cert.approvalDate"/></td>
	</tr>
	<tr>
		<td align="right">资质证书编号：</td>
		<td><ext:field property="cert.certificateNumber"/></td>
	</tr>
	<tr>
		<td align="right">资质等级：</td>
		<td><ext:field property="cert.level"/></td>
	</tr>
	<tr>
		<td align="right">批准文号：</td>
		<td><ext:field property="cert.approvalNumber"/></td>
	</tr>
	<ext:equal value="招标代理" property="cert.type">
		<tr>
			<td align="right" valign="top">代理名录库：</td>
			<td><ext:field property="cert.libArray"/></td>
		</tr>
	</ext:equal>
</table>