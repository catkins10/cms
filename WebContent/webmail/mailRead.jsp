<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">发件人</td>
		<td class="tdcontent"><ext:write property="mailFrom"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">收件人</td>
		<td class="tdcontent"><ext:write property="mailTo"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">抄送</td>
		<td class="tdcontent"><ext:write property="mailCc"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent"><ext:write property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">附件</td>
		<td class="tdcontent"><ext:field writeonly="true" property="attachments"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">正文</td>
		<td class="tdcontent"><ext:field property="htmlBody" readonly="true" style="height:280px"/></td>
	</tr>
</table>