<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">网友发言审核顺序</td>
		<td class="tdcontent"><ext:field property="speakFlow"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主持人发言审核顺序</td>
		<td class="tdcontent"><ext:field property="compereSpeakFlow"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">嘉宾发言审核顺序</td>
		<td class="tdcontent"><ext:field property="guestsSpeakFlow"/></td>
	</tr>
</table>