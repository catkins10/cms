<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/updateProfile" applicationName="jeaf/usermanage" pageName="member">
	<table border="0" cellpadding="3" cellspacing="0" style="color:#000000; width:100%" align="center">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td>用户名：</td>
			<td><ext:field writeonly="true" property="loginName"/></td>
		</tr>
		<tr>
			<td>真实姓名：</td>
			<td><ext:field property="name"/></td>
		</tr>
		<tr>
			<td>性别：</td>
			<td><ext:field property="sex"/></td>
		</tr>
		<tr>
			<td>电子邮箱：</td>
			<td><ext:field property="email"/></td>
		</tr>
		<tr>
			<td>证件名称：</td>
			<td><ext:field property="identityCardName"/></td>
		</tr>
		<tr>
			<td>证件号码：</td>
			<td><ext:field property="identityCard"/></td>
		</tr>
		<tr>
			<td>所属省份：</td>
			<td><ext:field property="area"/></td>
		</tr>
		<tr>
			<td nowrap>所在单位：</td>
			<td><ext:field property="company"/></td>
		</tr>
		<tr>
			<td>单位所属行业：</td>
			<td><ext:field property="organization"/></td>
		</tr>
		<tr>
			<td>地址：</td>
			<td><ext:field property="address"/></td>
		</tr>
		<tr>
			<td>邮编：</td>
			<td><ext:field property="postalcode"/></td>
		</tr>
		<tr>
			<td>所在部门：</td>
			<td><ext:field property="department"/></td>
		</tr>
		<tr style="display:none">
			<td>职务：</td>
			<td><ext:field property="duty"/></td>
		</tr>
		<tr>
			<td>传真：</td>
			<td><ext:field property="fax"/></td>
		</tr>
		<tr>
			<td>联系电话：</td>
			<td><ext:field property="telephone"/></td>
		</tr>
		<tr>
			<td>手机：</td>
			<td><ext:field property="cell"/></td>
		</tr>
		<tr>
			<td nowrap>是否公开个人资料：</td>
			<td><ext:field property="hideDetail"/></td>
		</tr>
		<tr>
			<td colspan="3" align="center" style="padding-top:8px">
				<ext:button onclick="FormUtils.submitForm()" width="50" name="确定"/>
			</td>
		</tr>
	</table>
</ext:form>