<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="120px" class="tdtitle">
	<col valign="middle" width="100%" class="tdcontent">
	<tr>
		<td class="tdtitle" nowrap="nowrap">运营商</td>
		<td class="tdcontent"><ext:write property="carrierName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">金额</td>
		<td class="tdcontent"><ext:write property="money"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">充值卡名称</td>
		<td class="tdcontent"><ext:write property="cardName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">充值卡面额</td>
		<td class="tdcontent"><ext:write property="cardMoney"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">充值卡数量</td>
		<td class="tdcontent"><ext:write property="cardNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">服务绑定</td>
		<td class="tdcontent"><ext:write property="servicePriceNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">服务超过一个时</td>
		<td class="tdcontent">
			<ext:equal property="serviceSelect" value="1">单选</ext:equal>
			<ext:equal property="serviceSelect" value="2">多选</ext:equal>
			<ext:equal property="serviceSelect" value="3">全选</ext:equal>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">充值截止日期</td>
		<td class="tdcontent"><ext:write property="topUpEnd" format="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:write property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:write property="created" format="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:write property="remark"/></td>
	</tr>
</table>