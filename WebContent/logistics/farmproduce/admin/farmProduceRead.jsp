<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">农产品名称</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">数量</td>
		<td class="tdcontent"><ext:field writeonly="true" property="quantity"/></td>
		<td class="tdtitle" nowrap="nowrap">单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="unit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单价</td>
		<td class="tdcontent"><ext:field writeonly="true" property="freight"/></td>
		<td class="tdtitle" nowrap="nowrap">地点</td>
		<td class="tdcontent"><ext:field writeonly="true" property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>联系人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkman"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkmanTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>登记人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>