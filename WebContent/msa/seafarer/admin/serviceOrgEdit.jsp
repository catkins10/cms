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
		<td class="tdtitle" nowrap="nowrap">分类</td>
		<td class="tdcontent"><ext:field property="category"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">服务内容</td>
		<td class="tdcontent"><ext:field property="serviceContent"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">许可证号</td>
		<td class="tdcontent"><ext:field property="licenseNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系地址</td>
		<td class="tdcontent"><ext:field property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field property="legalRepresentative"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
	</tr>
</table>