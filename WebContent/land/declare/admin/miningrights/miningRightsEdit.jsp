<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">标题</td>
		<td class="tdcontent"><ext:field property="subject"/></td>
		<td class="tdtitle" nowrap="nowrap">申请人</td>
		<td class="tdcontent"><ext:field property="applicant"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">矿山名称</td>
		<td class="tdcontent" ><ext:field property="mineName"/></td>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent" ><ext:field property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">许可证号</td>
		<td class="tdcontent"><ext:field property="licenseNum"/></td>
		<td class="tdtitle" nowrap="nowrap" >开采主矿种</td>
		<td class="tdcontent"><ext:field property="mainMinerals"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目类型</td>
		<td class="tdcontent" ><ext:field property="projectType"/></td>
		<td class="tdtitle" nowrap="nowrap">矿区面积</td>
		<td class="tdcontent"><ext:field property="miningArea"/></td>
		
	</tr>
	<tr>
	    <td class="tdtitle" nowrap="nowrap">坐标（矿区范围）</td>
		<td class="tdcontent" colspan="3"><ext:field property="coordinate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">有效期起</td>
		<td class="tdcontent"><ext:field property="validFrom"/></td>
		<td class="tdtitle" nowrap="nowrap">有效期止</td>
		<td class="tdcontent"><ext:field property="validEnd"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">采深上限</td>
		<td class="tdcontent"><ext:field property="caps"/></td>
		<td class="tdtitle" nowrap="nowrap">采深下限</td>
		<td class="tdcontent"><ext:field property="lower"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">审批时间</td>
		<td class="tdcontent"><ext:field property="approvalTime"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
</table>