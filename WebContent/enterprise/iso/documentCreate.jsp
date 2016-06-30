<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件名称</td>
		<td class="tdcontent" colspan="3"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在目录</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
		<td class="tdtitle" nowrap="nowrap">文件编号</td>
		<td class="tdcontent"><ext:field property="documentNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件类别</td>
		<td class="tdcontent"><ext:field property="category"/></td>
		<td class="tdtitle" nowrap="nowrap">关键词</td>
		<td class="tdcontent"><ext:field property="keywords"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">版本号</td>
		<td class="tdcontent"><ext:field property="version"/></td>
		<td class="tdtitle" nowrap="nowrap">受控状态</td>
		<td class="tdcontent"><ext:field property="control"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">紧急程度</td>
		<td class="tdcontent"><ext:field property="urgency"/></td>
		<td class="tdtitle" nowrap="nowrap">文件密级</td>
		<td class="tdcontent"><ext:field property="security"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保存期限</td>
		<td class="tdcontent"><ext:field property="storage"/></td>
		<td class="tdtitle" nowrap="nowrap">文件状态</td>
		<td class="tdcontent"><ext:field property="status"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">编制人</td>
		<td class="tdcontent"><ext:field property="writer"/></td>
		<td class="tdtitle" nowrap="nowrap">编制日期</td>
		<td class="tdcontent"><ext:field property="writeDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">管理部门</td>
		<td class="tdcontent"><ext:field property="storageDepartment"/></td>
		<td class="tdtitle" nowrap="nowrap">管理员</td>
		<td class="tdcontent"><ext:field property="manager"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">正文和附件</td>
		<td class="tdcontent" colspan="3"><ext:field property="attachments"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">内容概述</td>
		<td class="tdcontent" colspan="3"><ext:field property="summary"/></td>
	</tr>
	<ext:equal value="1" property="isModify">
		<tr>
			<td class="tdtitle" valign="top">修改说明</td>
			<td class="tdcontent" colspan="3"><ext:field property="modifyDescription"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
	</tr>
</table>