<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col valign="middle">
			<col valign="middle" width="50%">
			<col valign="middle">
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
				<td class="tdtitle" nowrap="nowrap">所在班级</td>
				<td class="tdcontent" colspan="3">
					<html:hidden property="orgId"/>
					<ext:write property="orgFullName"/>
				</td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">座号</td>
				<td class="tdcontent"><html:text property="seatNumber"/></td>
				<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
				<td class="tdcontent"><html:text property="mailAddress"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">家庭地址</td>
				<td class="tdcontent"><html:text property="familyAddress"/></td>
				<td class="tdtitle" nowrap="nowrap">电话</td>
				<td class="tdcontent"><html:text property="telFamily"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">手机</td>
				<td class="tdcontent"><html:text property="mobile"/></td>
				<td class="tdtitle" nowrap="nowrap">是否停用</td>
				<td class="tdcontent">
					<html:radio property="halt" value="1" styleClass="radio" styleId="haltEnable"/><label for="haltEnable">&nbsp;是</label>
					<html:radio property="halt" value="0" styleClass="radio" styleId="haltDisable"/><label for="haltDisable">&nbsp;否</label>
				</td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">注册人</td>
				<td class="tdcontent"><ext:write property="creator"/></td>
				<td class="tdtitle" nowrap="nowrap">注册时间</td>
				<td class="tdcontent"><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">备注</td>
				<td class="tdcontent" colspan="3"><html:text property="remark"/></td>
			</tr>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="genearch">
		<table class="table" width="100%" border="0" cellpadding="3" cellspacing="1">
			<tr>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">姓名</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">称呼</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100%">手机</td>
			</tr>
			<ext:iterate id="genearch" name="student" property="genearches">
				<tr>
					<td class="tdcontent">
						<a href="javascript:editPerson(<ext:write name="genearch" property="genearch.id"/>, '<ext:write name="genearch" property="genearch.type"/>', 'width=720,height=480')"><ext:write name="genearch" property="genearch.name"/></a>
					</td>
					<td class="tdcontent"><ext:write name="genearch" property="relation"/></td>
					<td class="tdcontent"><ext:write name="genearch" property="genearch.mobile"/></td>
				</tr>
			</ext:iterate>
		</table>
	</ext:tabBody>
</ext:tab>