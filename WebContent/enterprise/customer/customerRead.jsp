<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">客户名称</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">客户分类</td>
		<td class="tdcontent"><ext:field writeonly="true" property="type"/></td>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">区域</td>
		<td class="tdcontent"><ext:field writeonly="true" property="area"/></td>
		<td class="tdtitle" nowrap="nowrap">编号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="customerNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkman"/></td>
		<td class="tdtitle" nowrap="nowrap">移动电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="mobile"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="tel"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field writeonly="true" property="fax"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">职务</td>
		<td class="tdcontent"><ext:field writeonly="true" property="jobTitle"/></td>
		<td class="tdtitle" nowrap="nowrap">所属部门</td>
		<td class="tdcontent"><ext:field writeonly="true" property="department"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开户银行</td>
		<td class="tdcontent"><ext:field writeonly="true" property="depositBank"/></td>
		<td class="tdtitle" nowrap="nowrap">帐户名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="accountName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">帐号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="account"/></td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
</table>