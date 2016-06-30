<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">注册号</td>
		<td class="tdcontent" ><ext:field property="registCode"/></td>
		<td class="tdtitle" nowrap="nowrap">字号名称</td>
		<td class="tdcontent" ><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">经营者</td>
		<td class="tdcontent"><ext:field property="person"/></td>
		<td class="tdtitle" nowrap="nowrap">经营场所</td>
		<td class="tdcontent"><ext:field property="addr"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">成立日期</td>
		<td class="tdcontent"><ext:field property="buildDate"/></td>
		<td class="tdtitle" nowrap="nowrap">登记机关</td>
		<td class="tdcontent"><ext:field property="registrar"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">工商所</td>
		<td class="tdcontent"><ext:field property="aic"/></td>
		<td class="tdtitle" nowrap="nowrap">片区</td>
		<td class="tdcontent"><ext:field property="area"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">信用类别</td>
		<td class="tdcontent"><ext:field property="creditType"/></td>
		<td class="tdtitle" nowrap="nowrap">信用分值</td>
		<td class="tdcontent"><ext:field property="creditScore"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
		<td class="tdtitle" nowrap="nowrap">经营范围</td>
		<td class="tdcontent"><ext:field property="businessScope"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">经营期限起</td>
		<td class="tdcontent"><ext:field property="startDate"/></td>
		<td class="tdtitle" nowrap="nowrap">经营期限止</td>
		<td class="tdcontent"><ext:field property="endDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>