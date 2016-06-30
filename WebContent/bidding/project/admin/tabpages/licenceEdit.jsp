<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">施工单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="notice.pitchonEnterprise"/></td>
	</tr>
</table>