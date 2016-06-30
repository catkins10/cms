<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function addEventCallback(x, y) {
	document.getElementsByName("XPos")[0].value = x;
	document.getElementsByName("YPos")[0].value = y;
}
</script>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">案件来源</td>
		<td class="tdcontent"><ext:field property="source" styleClass="field required" itemsText="信息采集员\0 PDA即办\0 PDA承办\0 12345诉求\0 12345来电\0 110联动 \0公众举报\0 短息举报\0 领导批办"/></td>
		<td class="tdtitle" nowrap="nowrap">所属片区</td>
		<td class="tdcontent"><ext:field property="zone" styleClass="field required" itemsText="鼓楼\0 台江\0 晋安\0 仓山\0 金山"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">上报人</td>
		<td class="tdcontent"><html:text property="observer"/></td>
		<td class="tdtitle" nowrap="nowrap">上报号码</td>
		<td class="tdcontent"><html:text property="observerNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">举报人</td>
		<td class="tdcontent"><html:text property="reporter"/></td>
		<td class="tdtitle" nowrap="nowrap">联系方式</td>
		<td class="tdcontent"><html:text property="contect"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否需回复</td>
		<td class="tdcontent">
			<html:radio property="isReceipt" value="1" styleClass="radio" styleId="isReceiptYes"/><label for="isReceiptYes">&nbsp;需要</label>
			<html:radio property="isReceipt" value="0" styleClass="radio" styleId="isReceiptNo"/><label for="isReceiptNo">&nbsp;不需要</label>
		</td>
		<td class="tdtitle" nowrap="nowrap">回复方式</td>
		<td class="tdcontent"><ext:field property="receiptMode" itemsText="电话回复\0 短信回复"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">案件等级</td>
		<td class="tdcontent"><ext:field property="level" itemsText="特级病害\0一级病害\0二级病害" styleClass="field required"/></td>
		<td class="tdtitle" nowrap="nowrap">案件分类</td>
		<td class="tdcontent"><ext:field property="childCategory" styleClass="field required" itemsText="道管所\0排水所\0路灯所\0桥梁所\0违章\0其他"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">重复案件</td>
		<td class="tdcontent"><ext:field property="duplicate" itemsText="非重复案件\0 1次复案件\0 2次复案件\0 3次及以上重复案件"/></td>
		<td class="tdtitle" nowrap="nowrap">案件定位</td>
		<td class="tdcontent">
			X<html:text property="XPos" style="width: 80px"/>
			Y<html:text property="YPos" style="width: 80px"/>
			<input type="button" class="button" value="定位" onclick="EventUtils.addEvent(addEventCallback)">
			<input type="button" class="button" value="查看" onclick="displayEvent(' ', document.getElementsByName('XPos')[0].value, document.getElementsByName('YPos')[0].value, 2)">
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">来件时间</td>
		<td class="tdcontent"><ext:field property="created" /></td>
		<td class="tdtitle" nowrap="nowrap">案件编号</td>
		<td class="tdcontent"><html:text property="eventNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td colspan="3" class="tdcontent"><ext:write property="creator"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">事发位置</td>
		<td colspan="3" class="tdcontent"><html:textarea property="position" rows="3"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">案件描述</td>
		<td colspan="3" class="tdcontent"><html:textarea property="description" rows="3" styleClass="field required" style="height:43px"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">处理结果</td>
		<td colspan="3" class="tdcontent"><ext:equal value="1" property="isTruly">情况属实</ext:equal><ext:equal value="0" property="isTruly">情况不属实</ext:equal></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">处理情况描述</td>
		<td colspan="3" class="tdcontent"><ext:write property="truthDescription"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="3" class="tdcontent"><html:textarea property="remark" rows="3"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">照片上传</td>
		<td colspan="3" class="tdcontent"><ext:field property="images"/></td>
	</tr>
</table>