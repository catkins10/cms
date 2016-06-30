<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#2D5C7A">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">机构名称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="fax"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">邮编</td>
		<td class="tdcontent"><ext:field property="postalcode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field property="legalRepresentative"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">性质</td>
		<td class="tdcontent"><ext:field property="kind"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">许可证号</td>
		<td class="tdcontent"><ext:field property="licenseNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">详细介绍</td>
		<td class="tdcontent"><ext:field property="summarize"/></td>
	</tr>
</table>