<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">事项名称</td>
		<td class="tdcontent" colspan="3"><ext:write property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办理条件</td>
		<td class="tdcontent" colspan="3"><ext:write property="condition"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办理流程</td>
		<td class="tdcontent" colspan="3"><ext:write property="workFlow"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办理时限</td>
		<td class="tdcontent" colspan="3"><ext:write property="timeLimit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办理方式</td>
		<td class="tdcontent" colspan="3"><ext:write property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办理材料</td>
		<td class="tdcontent" colspan="3"><ext:write property="material"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">收费项目及标准标准</td>
		<td class="tdcontent" colspan="3"><ext:write property="chargeStandard"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">服务承诺</td>
		<td class="tdcontent" colspan="3"><ext:write property="promise"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:write property="person"/></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:write property="mail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:write property="faxes"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:write property="tel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>