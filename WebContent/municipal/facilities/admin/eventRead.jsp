<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">案件来源</td>
		<td class="tdcontent"><ext:write property="source"/></td>
		<td class="tdtitle" nowrap="nowrap">所属片区</td>
		<td class="tdcontent"><ext:write property="zone"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">上报人</td>
		<td class="tdcontent"><ext:write property="observer"/></td>
		<td class="tdtitle" nowrap="nowrap">上报号码</td>
		<td class="tdcontent"><ext:write property="observerNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">举报人</td>
		<td class="tdcontent"><ext:write property="reporter"/></td>
		<td class="tdtitle" nowrap="nowrap">联系方式</td>
		<td class="tdcontent"><ext:write property="contect"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否需回复</td>
		<td class="tdcontent">
			<ext:equal property="isReceipt" value="1">需要</ext:equal>
			<ext:notEqual property="isReceipt" value="1">不需要</ext:notEqual>
		</td>
		<td class="tdtitle" nowrap="nowrap">回复方式</td>
		<td class="tdcontent"><ext:write property="receiptMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">案件等级</td>
		<td class="tdcontent"><ext:write property="level"/></td>
		<td class="tdtitle" nowrap="nowrap">案件分类</td>
		<td class="tdcontent"><ext:write property="childCategory"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">重复案件</td>
		<td class="tdcontent"><ext:write property="duplicate"/></td>
		<td class="tdtitle" nowrap="nowrap">案件定位</td>
		<td class="tdtitle" nowrap="nowrap">
			X:<ext:write property="XPos"/>
			Y:<ext:write property="YPos"/>
			<input type="button" class="button" value="查看" onclick="displayEvent(' ', '<ext:write property="XPos"/>', '<ext:write property="YPos"/>', 2)">
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">来件时间</td>
		<td class="tdcontent"><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
		<td class="tdtitle" nowrap="nowrap">案件编号</td>
		<td class="tdcontent"><ext:write property="eventNumber"/></td>
	</tr>
	<tr>
		<td valign="top">登记人</td>
		<td colspan="3" class="tdcontent"><ext:write property="creator"/></td>
	</tr>
	<tr>
		<td valign="top">事发位置</td>
		<td colspan="3" class="tdcontent"><ext:write property="position"/></td>
	</tr>
	<tr>
		<td valign="top">案件描述</td>
		<td colspan="3" class="tdcontent"><ext:write property="description"/></td>
	</tr>
	<tr>
		<td valign="top">处理结果</td>
		<td colspan="3" class="tdcontent"><ext:equal value="1" property="isTruly">情况属实</ext:equal class="tdcontent"><ext:equal value="0" property="isTruly">情况不属实</ext:equal></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">处理情况描述</td>
		<td colspan="3" class="tdcontent"><ext:write property="truthDescription"/></td>
	</tr>
	<tr>
		<td valign="top">备注</td>
		<td colspan="3" class="tdcontent"><ext:write property="remark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">照片上传</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="images"/></td>
	</tr>
</table>