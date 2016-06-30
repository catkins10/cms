<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="90px" class="tdtitle">
	<col valign="middle" width="50%" class="tdcontent">
	<tr>
		<td>服务项目</td>
		<td colspan="3"><ext:write property="serviceItem"/></td>
	</tr>
	<tr>
		<td>用户名</td>
		<td colspan="3"><ext:write property="personName"/></td>
	</tr>
	<tr>
		<td>首次使用时间</td>
		<td><ext:write property="beginTime" format="yyyy-MM-dd HH:mm"/></td>
	</tr>
</table>