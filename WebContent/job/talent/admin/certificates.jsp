<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">获得时间</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">证书名称</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">成绩</td>
	</tr>
	<ext:iterate id="certificate" property="certificates">
		<tr align="center">
			<td class="tdcontent"><ext:field writeonly="true" name="certificate" property="gained"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="certificate" property="certificateName"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="certificate" property="mark"/></td>
		</tr>
	</ext:iterate>
</table>