<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">被评论页面主题</td>
		<td colspan="3" class="tdcontent"><ext:write property="pageTitle"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">被评论页面地址</td>
		<td colspan="3" class="tdcontent"><ext:write property="pageUrl"/>&nbsp;<a href="<ext:write property="pageUrl"/>" target="_blank">打开</a></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">评论内容</td>
		<td colspan="3" class="tdcontent"><ext:write property="content" filter="false"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评论人</td>
		<td class="tdcontent"><ext:write property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">评论时间</td>
		<td class="tdcontent"><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:write property="creatorTel" /></td>
		<td class="tdtitle" nowrap="nowrap">IP地址</td>
		<td class="tdcontent"><ext:write property="creatorIP" /></td>
	</tr>
</table>