<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent" ><ext:write property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">种类</td>
		<td class="tdcontent" ><ext:write property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">简介</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="introduction"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">最高额度</td>
		<td class="tdcontent" colspan="3"><ext:write property="maxMoney"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">适用对象</td>
		<td class="tdcontent" colspan="3"><ext:write property="forWho"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">贷款条件</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="condition"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请资料</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="material"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请流程</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="how"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:write property="linkMan"/></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:write property="email"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:write property="faxes"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:write property="tel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:write property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:write property="created"/></td>
	</tr>
</table>