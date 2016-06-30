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
				<td class="tdcontent"><ext:write property="name"/></td>
				<td class="tdtitle" nowrap="nowrap">登录用户名</td>
				<td class="tdcontent"><ext:write property="loginName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">性别</td>
				<td class="tdcontent" colspan="3">
					<ext:equal value="M" property="sex">男</ext:equal><ext:equal value="F" property="sex">女</ext:equal>
				</td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所在班级</td>
				<td class="tdcontent">
					<html:hidden property="orgId"/>
					<ext:write property="orgFullName"/>
				</td>
				<td class="tdtitle" nowrap="nowrap">座号</td>
				<td class="tdcontent"><ext:write property="seatNumber"/></td>
			</tr>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="genearch">
		<table class="table" width="100%" border="0" cellpadding="3" cellspacing="1">
			<tr height="22px">
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">姓名</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">称呼</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100%">手机</td>
			</tr>
			<ext:iterate id="genearch" name="adminStudent" property="genearches">
				<tr bordercolor="E1E8F5" height="20px">
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