<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="type"/></td>
		<td class="tdtitle" nowrap="nowrap">发送部门</td>
		<td class="tdcontent"><ext:write property="department"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td colspan="3" class="tdcontent"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">内容</td>
		<td colspan="3" class="tdcontent"><ext:field property="content"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">编号</td>
		<td class="tdcontent"><ext:write property="sn"/></td>
		<td class="tdtitle" nowrap="nowrap">诉求时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">事件辖区</td>
		<td class="tdcontent"><ext:write property="popedom" /></td>
		<td class="tdtitle" nowrap="nowrap">事件地点</td>
		<td class="tdcontent"><ext:write property="area" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">事件时间</td>
		<td class="tdcontent"><ext:field property="happenTime" writeonly="true"/></td>
		<td class="tdtitle" nowrap="nowrap">是否允许公开</td>
		<td class="tdcontent"><ext:field property="isPublic" writeonly="true"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">诉求人姓名</td>
		<td class="tdcontent"><ext:write property="creator" /></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:write property="creatorMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:write property="creatorTel"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:write property="creatorFax"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:write property="creatorMobile" /></td>
		<td class="tdtitle" nowrap="nowrap">IP地址</td>
		<td class="tdcontent"><ext:write property="creatorIP" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在单位</td>
		<td class="tdcontent"><ext:write property="creatorUnit" /></td>
		<td class="tdtitle" nowrap="nowrap">职业</td>
		<td class="tdcontent"><ext:write property="creatorJob" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="attachment"/></td>
	</tr>
</table>