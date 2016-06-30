<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<tr>
		<td class="tdtitle" nowrap="nowrap">标题</td>
		<td class="tdcontent"><ext:field writeonly="true" property="title"/></td>
		<td class="tdtitle" nowrap="nowrap">作者</td>
		<td class="tdcontent"><ext:field writeonly="true" property="writer"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="comType"/></td>
		<td class="tdtitle" nowrap="nowrap">批改人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="correctName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="attachments"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">正文</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="content"/></td>
	</tr>
</table>