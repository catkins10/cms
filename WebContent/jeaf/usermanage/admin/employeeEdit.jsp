<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">登录用户名(英文)</td>
		<td class="tdcontent">
			<ext:equal value="1" property="preassign">
				<ext:write property="loginName"/>
			</ext:equal>
			<ext:notEqual value="1" property="preassign">
				<ext:field property="loginName"/>
			</ext:notEqual>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:field property="sex"/></td>
		<td class="tdtitle" nowrap="nowrap">口令</td>
		<td class="tdcontent"><ext:field property="password"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent"><ext:field property="mailAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">所在单位/部门</td>
		<td class="tdcontent"><ext:field property="orgFullName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属其他部门</td>
		<td class="tdcontent" colspan="3"><ext:field property="otherOrgNames"/></td>
	</tr>
	<jsp:include page="personSupervise.jsp"/>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办公室电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:field property="mobile"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">家庭地址</td>
		<td class="tdcontent"><ext:field property="familyAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="telFamily"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否停用</td>
		<td class="tdcontent"><ext:field property="halt"/></td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field property="remark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">注册人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">注册时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>