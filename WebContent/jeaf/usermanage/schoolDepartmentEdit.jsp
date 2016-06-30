<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col valign="middle">
			<col valign="middle" width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">名称</td>
				<td colspan="3" class="tdcontent"><html:text property="name" styleClass="field required"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所在单位/上级部门</td>
				<td colspan="3" class="tdcontent"><ext:write property="parentOrgFullName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">注册人</td>
				<td colspan="3" class="tdcontent"><ext:write property="creator"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">注册时间</td>
				<td colspan="3" class="tdcontent"><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
			</tr>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="teacher">
		<table class="table" width="100%" border="0" cellpadding="3" cellspacing="1">
			<tr height="22px">
				<td align="center" nowrap="nowrap" class="tdtitle" width="50px">姓名</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">电话</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">手机</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100%">电子邮件</td>
			</tr>
			<ext:iterate id="teacher" property="teachers">
				<tr>
					<td class="tdcontent">
						<a href="javascript:PageUtils.editrecord('jeaf/usermanage','teacher','<ext:write name="teacher" property="id"/>','width=720,height=480')"><ext:write name="teacher" property="name"/></a>
					</td>
					<td class="tdcontent"><ext:write name="teacher" property="tel"/></td>
					<td class="tdcontent"><ext:write name="teacher" property="mobile"/></td>
					<td class="tdcontent"><ext:write name="teacher" property="mailAddress"/></td>
				</tr>
			</ext:iterate>
		</table>
	</ext:tabBody>
</ext:tab>
<html:hidden property="parentOrgId"/>