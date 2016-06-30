<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标人</td>
		<td class="tdcontent"><ext:field property="notice.pitchonEnterprise"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标金额(元)</td>
		<td class="tdcontent"><ext:field property="notice.pitchonPrice"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">正文</td>
		<td class="tdcontent"><ext:field property="notice.body"/></td>
	</tr>
</table>