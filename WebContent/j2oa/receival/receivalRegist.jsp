<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">标题</td>
		<td colspan="3" class="tdcontent"><ext:field property="subject" onchange="parseKeyword(value, 'keyword')"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件字</td>
		<td class="tdcontent"><ext:field property="docWord"/></td>
		<td class="tdtitle" nowrap="nowrap">文件种类</td>
		<td class="tdcontent"><ext:field property="docType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">来文单位</td>
		<td class="tdcontent"><ext:field property="fromUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">主办部门</td>
		<td class="tdcontent"><ext:field property="mainDo"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题词</td>
		<td class="tdcontent"><ext:field property="keyword"/></td>
		<td class="tdtitle" nowrap="nowrap">成文日期</td>
		<td class="tdcontent"><ext:field property="signDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">收文日期</td>
		<td class="tdcontent"><ext:field property="receivalDate"/></td>
		<td class="tdtitle" nowrap="nowrap">办理期限</td>
		<td class="tdcontent"><ext:field property="transactDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">紧急程度</td>
		<td class="tdcontent"><ext:field property="priority"/></td>
		<td class="tdtitle" nowrap="nowrap">秘密等级</td>
		<td class="tdcontent"><ext:field property="secureLevel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">份数</td>
		<td class="tdcontent"><ext:field property="receivalCount"/></td>
		<td class="tdtitle" nowrap="nowrap">页数</td>
		<td class="tdcontent"><ext:field property="pageCount"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="registPerson"/></td>
		<td class="tdtitle" nowrap="nowrap">登记部门</td>
		<td class="tdcontent"><ext:field property="registDepartment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">正文</td>
		<td colspan="3" class="tdcontent"><ext:field property="body"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td colspan="3" class="tdcontent"><ext:field property="attachment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">来文摘要</td>
		<td colspan="3" class="tdcontent"><ext:field property="content"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附注</td>
		<td colspan="3" class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>