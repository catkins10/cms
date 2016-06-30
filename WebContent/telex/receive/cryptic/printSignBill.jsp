<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@page import="com.yuanluesoft.telex.receive.cryptic.forms.Sign"%>
<%Sign signForm = (Sign)request.getAttribute("sign");%>

<html:html>
<head>
	<title>电报签收单</title>
	<style>
		body,td{font-family:宋体;font-size:12px}
		pre{font-size:12px; font-family:宋体;line-height:16px;margin:0;word-wrap:break-word}
	</style>
</head>
<body onload="window.print()">
<center>
	<div style="font-family:黑体;font-size:22px">电报签收单</div>
</center>
<br>
<table width="100%" style="table-layout:fixed; border;border-collapse:collapse;" border="1" cellpadding="5" cellspacing="0" bordercolor="#000000">
	<col width="55px">
	<col width="40px">
	<col width="100%">
	<col width="72px">
	<col width="72px">
	<col width="72px">
	<col width="60px">
	<col width="72px">
	<tr>
		<td align="center"><b>签收人</b></td>
		<td colspan="3"><ext:write name="sign" property="signPersonName"/><ext:equal value="true" name="sign" property="agent">(代签收)</ext:equal></td>
		<td align="center"><b>所在单位</b></td>
		<td colspan="3"><ext:write name="sign" property="signPersonOrgFullName"/></td>
	</tr>
	<tr align="center">
		<td rowspan="<%=(signForm.getSignTelegrams().size() + 2)%>"><b>签<br>收<br>电<br>报<br>列<br>表</b></td>
		<td><b>原号</b></td>
		<td><b>标题</b></td>
		<td><b>接收人</b></td>
		<td><b>发电单位</b></td>
		<td><b>发往单位</b></td>
		<td><b>页数</b></td>
		<td><b>接收时间</b></td>
	</tr>
	<ext:iterate id="telegramSign"  name="sign" property="signTelegrams">
		<tr>
			<td align="center"><ext:write name="telegramSign" property="telegram.sn"/></td>
			<td><ext:write name="telegramSign" property="telegram.subject"/></td>
			<td align="center"><ext:write name="telegramSign" property="receiverName"/></td>
			<td align="center"><ext:write name="telegramSign" property="telegram.fromUnitNames"/></td>
			<td align="center"><ext:write name="telegramSign" property="telegram.toUnitNames"/></td>
			<td align="center"><ext:write name="telegramSign" property="telegram.pages"/></td>
			<td align="center"><ext:write name="telegramSign" property="telegram.receiveTime" format="yyyy-MM-dd"/></td>
		</tr>
	</ext:iterate>
	<tr>
		<td colspan="7" height="<%=Math.max(20, 100-signForm.getSignTelegrams().size()*16)%>px"></td>
	</tr>
	<ext:iterate id="telegramSign"  name="sign" property="signTelegrams" length="1">
		<tr>
			<td align="center"><b>值班人</b></td>
			<td colspan="2"><ext:write name="telegramSign" property="signOperatorName"/></td>
			<td align="center"><b>签收时间</b></td>
			<td><ext:write name="telegramSign" property="signTime" format="yyyy-MM-dd"/></td>
			<td align="center"><b>打印时间</b></td>
			<td colspan="2"><ext:write name="sign" property="printTime" format="yyyy-MM-dd"/></td>
		</tr>
	</ext:iterate>
	<tr height="50px">
		<td align="center"><b>签收人<br>签字</b></td>
		<td colspan="8"></td>
	</tr>
</table>
</body>
</html:html>