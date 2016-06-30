<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doCopy">
	<script language="JavaScript" charset="utf-8" src="js/site.js"></script>
   	<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col valign="middle" width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">被拷贝站点/栏目</td>
			<td class="tdcontent"><ext:field property="fromDirectoryName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">拷贝到</td>
			<td class="tdcontent"><ext:field property="toDirectoryName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">新站点/栏目名称列表(用逗号分隔)</td>
			<td class="tdcontent"><ext:field property="newDirectoryName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">是否拷贝模板</td>
			<td class="tdcontent"><ext:field property="copyTemplate"/></td>
		</tr>
	</table>
</ext:form>