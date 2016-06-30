<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">有效期开始时间</td>
		<td class="tdcontent"><ext:field property="beginTime"/></td>
		<td class="tdtitle" nowrap="nowrap">有效期截止时间</td>
		<td class="tdcontent"><ext:field property="endTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">运费金额</td>
		<td class="tdcontent"><ext:field property="freightAmount"/></td>
		<td class="tdtitle" nowrap="nowrap">运费单位</td>
		<td class="tdcontent"><ext:field property="freightUnit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="linkman"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="linkmanTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>载货车辆车牌</td>
		<td class="tdcontent" colspan="3"><ext:field property="freeVehicleNumbers"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>出发地点</td>
		<td class="tdcontent"><ext:field property="departureAreas"/></td>
		<td class="tdtitle" nowrap>目的地点</td>
		<td class="tdcontent"><ext:field property="destinationAreas"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap>登记人IP</td>
		<td class="tdcontent"><ext:field property="creatorIP"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
		<td class="tdtitle" nowrap>最后修改时间</td>
		<td class="tdcontent"><ext:field property="lastModified"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
	</tr>
</table>