<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位名称</td>
		<td class="tdcontent"><ext:field property="unitName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">网址</td>
		<td class="tdcontent"><ext:field property="siteUrl"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">公众号名称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">帐号类型</td>
		<td class="tdcontent"><ext:field property="accountType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">微信认证</td>
		<td class="tdcontent"><ext:field property="certificate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">Token</td>
		<td class="tdcontent"><ext:field property="token"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">消息接收URL</td>
		<td class="tdcontent"><ext:field property="receiveMessageURL"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">用户唯一凭证</td>
		<td class="tdcontent"><ext:field property="appId"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">用户唯一凭证密钥</td>
		<td class="tdcontent"><ext:field property="appSecret"/></td>
	</tr>
</table>