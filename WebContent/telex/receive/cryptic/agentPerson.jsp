<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveAgentPerson">
  	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">姓名</td>
			<td class="tdcontent"><ext:write property="name"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">所在单位</td>
			<td class="tdcontent"><ext:write property="orgName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">证件名称</td>
			<td class="tdcontent"><ext:write property="certificateName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">证件号码</td>
			<td class="tdcontent"><ext:write property="certificateCode"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">创建人</td>
			<td class="tdcontent"><ext:write property="creator"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">创建时间</td>
			<td class="tdcontent"><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
	</table>
</ext:form>