<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newFeesItem() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/msa/fees/feesItem.shtml?id=<ext:write property="id"/>', 500, 300);
}
function openFeesItem(feesItemId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/msa/fees/feesItem.shtml?id=<ext:write property="id"/>&item.id=' + feesItemId, 500, 300);
}
</script>
<div style="padding-bottom:5px">
	<ext:equal value="true" name="editabled">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td nowrap="nowrap">规费名称：</td>
				<td width="200px"><ext:field property="name"/></td>
				<td>&nbsp;<input type="button" class="button" value="添加细项" onclick="newFeesItem()"></td>
			</tr>
		</table>	
	</ext:equal>
	<ext:notEqual value="true" name="editabled">
		规费名称：<ext:field property="name" writeonly="true"/>
	</ext:notEqual>
</div>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="120px">期数</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">考生人数</td>
		<td class="tdtitle" nowrap="nowrap" width="80">费用(元)</td>
		<td class="tdtitle" nowrap="nowrap" width="100">缴费方式</td>
		<td class="tdtitle" nowrap="nowrap"l width="100%">负责单位</td>
		<td class="tdtitle" nowrap="nowrap"l width="80">考试时间</td>
		<td class="tdtitle" nowrap="nowrap"l width="110">完成时间</td>
	</tr>
	<ext:iterate id="feesItem" indexId="feesItemId" property="items">
		<tr style="cursor:pointer" valign="top" onclick="openFeesItem('<ext:write name="feesItem" property="id"/>')" align="center">
			<td class="tdcontent"><ext:writeNumber name="feesItemId" plus="1"/></td>
			<td class="tdcontent"><ext:write name="feesItem" property="period"/></td>
			<td class="tdcontent"><ext:write name="feesItem" property="examineeNumber" format="#"/></td>
			<td class="tdcontent"><ext:write name="feesItem" property="charge" format="#"/></td>
			<td class="tdcontent"><ext:write name="feesItem" property="paymentMode"/></td>
			<td class="tdcontent" align="left"><ext:write name="feesItem" property="unitName"/></td>
			<td class="tdcontent"><ext:write name="feesItem" property="examTime" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="feesItem" property="completeTime" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
	</ext:iterate>
</table>