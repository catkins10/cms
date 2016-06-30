<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">农产品名称</td>
		<td class="tdcontent" colspan="3"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">数量</td>
		<td class="tdcontent"><ext:field property="quantity"/></td>
		<td class="tdtitle" nowrap="nowrap">单位</td>
		<td class="tdcontent"><ext:field property="unit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单价</td>
		<td class="tdcontent"><ext:field property="freight"/></td>
		<td class="tdtitle" nowrap="nowrap">地点</td>
		<td class="tdcontent"><ext:field property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>联系人</td>
		<td class="tdcontent"><ext:field property="linkman"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="linkmanTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
	</tr>
</table>