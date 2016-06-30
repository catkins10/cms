<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent" colspan="3"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登录用户名</td>
		<td class="tdcontent"><ext:field property="loginName"/></td>
		<td class="tdtitle" nowrap="nowrap">密码</td>
		<td class="tdcontent"><ext:field property="password"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">LOGO图片</td>
		<td class="tdcontent" colspan="3"><ext:field property="logoImages"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">简介</td>
		<td class="tdcontent" colspan="3"><ext:field property="introduction"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="addr"/></td>
		<td class="tdtitle" nowrap="nowrap">邮编</td>
		<td class="tdcontent"><ext:field property="postcode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="linkMan"/></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:field property="email"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="faxes"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" colspan="4"><b>授信（保险）部门</b></td>
		
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">负责人</td>
		<td class="tdcontent"><ext:field property="person"/></td>
		<td class="tdtitle" nowrap="nowrap">经办人</td>
		<td class="tdcontent"><ext:field property="operator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">网址</td>
		<td class="tdcontent"><ext:field property="orgUrl"/></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:field property="orgEmail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="orgFaxes"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="orgTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" colspan="4" ><b>举报投诉渠道</b></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">网址</td>
		<td class="tdcontent"><ext:field property="complainUrl"/></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:field property="complainEmail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="complainFaxes"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="complainTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>