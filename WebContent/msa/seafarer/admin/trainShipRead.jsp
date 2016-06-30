<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#2D5C7A">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">船籍港</td>
		<td class="tdcontent"><ext:field writeonly="true" property="port"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">船舶种类</td>
		<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">总吨位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="tonnage"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">功率</td>
		<td class="tdcontent"><ext:field writeonly="true" property="power"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">船舶所有人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="shipBelong"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">船上培训/见习单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="practiceOrg"/></td>
	</tr><tr>
		<td class="tdtitle" nowrap="nowrap">所属海事机构</td>
		<td class="tdcontent"><ext:field writeonly="true" property="orgBelong"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>