<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>养路费查询</title>
	<link href="<%=request.getContextPath()%>/cms/css/application.css" rel="stylesheet" type="text/css" />
</head>
<body style="border-style:none; overflow:hidden" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
    <ext:form action="/doQuery">
	<div style="float:left;padding-top:8px">车牌：</div>
	<div style="float:left;padding-right:5px;padding-top:3px">
		<html:select property="plateNumberPrefix" style="font-size:12px">
			<html:option value="闽A">闽A</html:option>
			<html:option value="闽B">闽B</html:option>
			<html:option value="闽C">闽C</html:option>
			<html:option value="闽D">闽D</html:option>
			<html:option value="闽E">闽E</html:option>
			<html:option value="闽F">闽F</html:option>
			<html:option value="闽G">闽G</html:option>
			<html:option value="闽H">闽H</html:option>
			<html:option value="闽J">闽J</html:option>
			<html:option value="闽K">闽K</html:option>
			<html:option value="闽O">闽O</html:option>
		</html:select>
	</div>
	<div style="float:left;width:120px;padding-right:5px;padding-top:3px">
		<html:text property="plateNumber" size="12"/>
	</div>
	<div style="float:left;padding-top:3px">
		<html:select property="vehicleType" style="font-size:12px">
	    	<html:option value="蓝">小型车</html:option>
	        <html:option value="黄">大型车</html:option>
	        <html:option value="黑">外籍车</html:option>
		</html:select>
	</div>
	<div>
		&nbsp;<ext:button name="查询" width="40" onclick="submit()"/>
	</div>
	<br>
	<div>数据最后更新日期为:<ext:write property="lastUpdated" format="yyyy-M-d HH:mm"/><div>
	<TABLE align="center" bgcolor="#b2d0f4" border="0" cellpadding="1" cellspacing="1" width="100%">
		<TR>
			<TD align="center" bgcolor="#69a7f2" height="20">车号</TD>
			<TD align="center" bgcolor="#69a7f2">厂牌型号</TD>
			<TD align="center" bgcolor="#69a7f2">是否报停</TD>
			<TD align="center" bgcolor="#69a7f2">有效期限</TD>
			<TD align="center" bgcolor="#69a7f2">所属稽征所</TD>
		</TR>
		<ext:notEmpty property="fullPlateNumber">
			<TR>
				<TD align="center" bgcolor="#ffffff" height="22"><ext:write property="fullPlateNumber"/></TD>
				<TD align="center" bgcolor="#ffffff"><ext:write property="type"/></TD>
				<TD align="center" bgcolor="#ffffff"><ext:write property="halt"/></TD>
				<TD align="center" bgcolor="#ffffff"><ext:write property="expensesDate" format="yyyy-M-d"/></TD>
				<TD align="center" bgcolor="#ffffff"><ext:write property="collection"/></TD>
			</TR>
		</ext:notEmpty>
		<ext:empty property="fullPlateNumber">
			<TR>
				<TD align="center" bgcolor="#ffffff" height="22">-</TD>
				<TD align="center" bgcolor="#ffffff">-</TD>
				<TD align="center" bgcolor="#ffffff">-</TD>
				<TD align="center" bgcolor="#ffffff">-</TD>
				<TD align="center" bgcolor="#ffffff">-</TD>
			</TR>
		</ext:empty>
	</TABLE>
</ext:form>
</body>
</html:html>
