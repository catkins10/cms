<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">秘密等级</td>
		<td class="tdcontent"><ext:field property="secureLevel" title="提示：用逗号分隔秘密等级列表" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保密期限</td>
		<td class="tdcontent"><ext:field property="secureTerm" title="提示：用逗号分隔保密期限列表" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">紧急程度</td>
		<td class="tdcontent"><ext:field property="priority" title="提示：用逗号分隔紧急程度列表" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">公文种类</td>
		<td class="tdcontent"><ext:field property="docType" title="提示：用逗号分隔公文种类列表" /></td>
	</tr>
</table>