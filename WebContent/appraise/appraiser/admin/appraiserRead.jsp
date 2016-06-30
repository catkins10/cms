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
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位或居住地</td>
		<td class="tdcontent"><ext:field writeonly="true" property="unit"/></td>
		<td class="tdtitle" nowrap="nowrap">通讯地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">区域</td>
		<td class="tdcontent"><ext:field writeonly="true" property="area"/></td>
		<td class="tdtitle" nowrap="nowrap">乡镇或街道</td>
		<td class="tdcontent"><ext:field writeonly="true" property="street"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">手机号码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="mobileNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">身份</td>
		<td class="tdcontent"><ext:field writeonly="true" property="job"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">人大政协级别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="npcLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">级别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="level"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">服务或管理项目、内容</td>
		<td class="tdcontent"><ext:field writeonly="true" property="serviceContent"/></td>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">有效期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="expire"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">状态</td>
		<td class="tdcontent"><ext:field writeonly="true" property="status"/></td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>