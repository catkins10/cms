<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">错误页面主题</td>
		<td colspan="3" class="tdcontent"><ext:write property="pageTitle"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">错误页面地址</td>
		<td colspan="3" class="tdcontent"><ext:write property="pageUrl"/>&nbsp;<a href="<ext:write property="pageUrl"/>" target="_blank">打开</a></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">错误描述</td>
		<td colspan="3" class="tdcontent"><ext:write property="content" filter="false"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提交人</td>
		<td class="tdcontent"><ext:write property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">提交时间</td>
		<td class="tdcontent"><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:write property="creatorTel" /></td>
		<td class="tdtitle" nowrap="nowrap">IP地址</td>
		<td class="tdcontent"><ext:write property="creatorIP" /></td>
	</tr>
</table>