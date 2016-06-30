<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" class="table">
	<col align="left">
	<col width="50%">
	<col align="left">
	<col width="50%">
    <tr>
		<td nowrap="nowrap" class="tdTitle">姓名</td>
		<td class="tdContent"><ext:field property="name" /></td>
		<td nowrap="nowrap" class="tdTitle">性别</td>
		<td class="tdContent"><ext:field property="sex"/></td>		
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdTitle">社会保障号</td>
		<td class="tdContent"><ext:field property="securityNumber"/></td>
		<td nowrap="nowrap" class="tdTitle">批号</td>
		<td class="tdContent"><ext:field property="batchNumber"/></td>		
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdTitle">制卡类型</td>
		<td class="tdContent"><ext:field property="cardType"/></td>
		<td nowrap="nowrap" class="tdTitle">参保辖区</td>
		<td class="tdContent"><ext:field property="jurisdiction"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdTitle">接收数据日期</td>
		<td class="tdContent"><ext:field property="receiveDate"/></td>
		<td nowrap="nowrap" class="tdTitle">制卡日期</td>
		<td class="tdContent"><ext:field property="makeCardDate"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdTitle">移出卡片日期</td>
		<td  class="tdContent"><ext:field property="removedCardDate"/></td>
		<td nowrap="nowrap" class="tdTitle">数据日期</td>
		<td  class="tdContent"><ext:field property="mark"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdTitle">登记人</td>
		<td class="tdContent"><ext:field property="creator"/></td>
		<td nowrap="nowrap" class="tdTitle">登记时间</td>
		<td class="tdContent"><ext:field property="created"/></td>
	</tr>
</table>