<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap" class="tdtitle">全宗名称(单位名称)</td>
		<td class="tdcontent"><ext:field property="fondsName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">全宗号</td>
		<td class="tdcontent"><ext:field property="fondsCode"/></td>
	</tr>
</table>