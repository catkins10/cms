<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html:html>
<head>
	<title>政务信息封面</title>
	<style>
		 .print {
		 	font-family:仿宋_GB2312; font-size:24px
		 }
	</style>
</head>
<body onload="window.print()" style="margin:0px">
<center>
    <ext:iterate name="adminExportPublicInfo" property="publicInfos" id="info">
    	<br/>
    	<br/>
    	<br/>
    	<br/>
    	<br/>
    	<table border="1" width="600px" style="border-collapse:collapse;" cellpadding="10" bordercolor="#000000">
    		<col width="150px" align="center">
    		<col>
    		<tr height="80px">
    			<td class="print">索引号</td>
    			<td class="print"><ext:write name="info" property="infoIndex"/></td>
    		</tr>
    		<tr height="280px">
    			<td class="print">名<br><br><br>称</td>
    			<td class="print"><ext:write name="info" property="subject"/></td>
    		</tr>
    		<tr height="160px">
    			<td class="print">发布机构</td>
    			<td class="print"><ext:write name="info" property="infoFrom"/></td>
    		</tr>
    		<tr height="160px">
    			<td class="print">内容概述</td>
    			<td class="print"><ext:write name="info" property="summarize"/></td>
    		</tr>
    		<tr height="80px">
    			<td class="print">生成日期</td>
    			<td class="print"><ext:write name="info" property="generateDate" format="yyyy-MM-dd"/></td>
    		</tr>
    		<tr height="160px">
    			<td class="print">备注/文号</td>
    			<td class="print"><ext:write name="info" property="mark"/></td>
    		</tr>
    	</table>
    	<p style="page-break-after: always">&nbsp;</p>
    </ext:iterate>
</center>
</body>
</html:html>