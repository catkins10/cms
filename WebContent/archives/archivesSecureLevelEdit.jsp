<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" style=" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap" class="tdtitle">编号</td>
		<td class="tdcontent"><ext:field property="secureLevelCode"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">密级</td>
		<td class="tdcontent"><ext:field property="secureLevel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">访问者</td>
		<td class="tdcontent"><ext:field property="visitors.visitorNames"/></td>
	</tr>
</table>