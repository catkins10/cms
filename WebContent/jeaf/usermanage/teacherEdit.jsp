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
		<td class="tdcontent"><html:text property="name" styleClass="field required"/></td>
		<td class="tdtitle" nowrap="nowrap">登录用户名(英文)</td>
		<td class="tdcontent">
			<ext:equal value="1" property="preassign">
				<ext:write property="loginName"/>
			</ext:equal>
			<ext:notEqual value="1" property="preassign">
				<html:text property="loginName" styleClass="field required"/>
			</ext:notEqual>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent">
			<html:radio property="sex" value="M" styleClass="radio" styleId="male"/><label for="male">&nbsp;男</label>
			<html:radio property="sex" value="F" styleClass="radio" styleId="female"/><label for="female">&nbsp;女</label>
		</td>
		<td class="tdtitle" nowrap="nowrap">口令</td>
		<td class="tdcontent"><html:text property="password" styleClass="field required"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在学校/部门</td>
		<td class="tdcontent" colspan="3">
			<html:hidden property="orgId"/>
			<ext:write property="orgFullName"/>
		</td>
	</tr>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">所属其他部门</td>
		<td class="tdcontent" colspan="3">
			<html:hidden property="otherOrgIds"/>
			<ext:field property="otherOrgNames" readonly="true" onSelect="DialogUtils.selectOrg(500,300,true,'otherDepartmentIds{id},otherDepartmentNames{name|部门名称|100%}')"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办公室电话</td>
		<td class="tdcontent"><html:text property="tel"/></td>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><html:text property="mobile"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">家庭地址</td>
		<td class="tdcontent"><html:text property="familyAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><html:text property="telFamily"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent"><html:text property="mailAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">是否停用</td>
		<td class="tdcontent">
			<html:radio property="halt" value="1" styleClass="radio" styleId="haltEnable"/><label for="haltEnable">&nbsp;是</label>
			<html:radio property="halt" value="0" styleClass="radio" styleId="haltDisable"/><label for="haltDisable">&nbsp;否</label>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><html:text property="remark"/></td>
	</tr>
	<tr>
		<td>注册人</td>
		<td class="tdcontent"><ext:write property="creator"/></td>
		<td>注册时间</td>
		<td class="tdcontent"><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
	</tr>
</table>