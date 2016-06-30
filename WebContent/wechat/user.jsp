<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveUser">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">昵称：</td>
			<td><ext:field property="nickname"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">性别：</td>
			<td><ext:field writeonly="true" property="sex"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">所在城市：</td>
			<td><ext:field property="city"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">所在省份：</td>
			<td><ext:field property="province"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">所在国家：</td>
			<td><ext:field property="country"/></td>
		</tr>
		<ext:notEmpty property="headimgUrl">
			<tr>
				<td nowrap="nowrap">用户头像：</td>
				<td><img height="80" src="<ext:write property="headimgUrl"/>"/></td>
			</tr>
		</ext:notEmpty>
		<tr>
			<td nowrap="nowrap">关注时间：</td>
			<td><ext:field property="subscribeTime"/></td>
		</tr>
		<ext:notEmpty property="unsubscribeTime">
			<tr>
				<td nowrap="nowrap">取消关注时间：</td>
				<td><ext:field property="unsubscribeTime"/></td>
			</tr>
		</ext:notEmpty>
		<tr>
			<td nowrap="nowrap">真实姓名：</td>
			<td><ext:field property="name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">备注：</td>
			<td><ext:field property="remark"/></td>
		</tr>
	</table>
</ext:form>