<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="78px" align="right">
	<col width="100%">
	<tr>
		<td>年份：</td>
		<td><ext:field property="photo.photoYear"/></td>
	</tr>
	<tr>
		<td>月份：</td>
		<td><ext:field property="photo.photoMonth"/></td>
	</tr>
	<tr>
		<td>拍摄时间：</td>
		<td><ext:field property="photo.shotTime"/></td>
	</tr>
	<tr>
		<td>图片标题：</td>
		<td><ext:field property="photo.photoSubject"/></td>
	</tr>
	<tr>
		<td>图片：</td>
		<td><ext:field property="photo.images"/></td>
	</tr>
</table>