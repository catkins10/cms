<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">状态</td>
		<td class="tdcontent"><ext:field property="status"/></td>
	</tr>
    <tr>
		<td class="tdtitle" nowrap="nowrap">登录用户名</td>
		<td class="tdcontent"><ext:field property="loginName"/></td>
		<td class="tdtitle" nowrap="nowrap">密码</td>
		<td class="tdcontent"><ext:field property="password"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:field property="email"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="addr"/></td>
		<td class="tdtitle" nowrap="nowrap">单位名称</td>
		<td class="tdcontent"><ext:field property="unitName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">职务</td>
		<td class="tdcontent"><ext:field property="job"/></td>
		<td class="tdtitle" nowrap="nowrap">经营范围</td>
		<td class="tdcontent"><ext:field property="buniessScope"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">审核意见</td>
		<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">注册时间</td>
		<td class="tdcontent" colspan="3"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">审核人</td>
		<td class="tdcontent"><ext:field property="approver"/></td>
		<td class="tdtitle" nowrap="nowrap">审核时间</td>
		<td class="tdcontent"><ext:field property="approveDate"/></td>
	</tr>

</table>