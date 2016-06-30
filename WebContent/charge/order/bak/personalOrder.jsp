<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<html:html>
<head>
	<title>订购服务</title>
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
</head>
<body>
<ext:form action="/displayPersonalOrder">
    <jsp:include page="/edu/skins/v2/default/detailFormPrompt.jsp"/>
	<table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#CCCCCC">
		<tr align="center" valign="middle">
			<td bgcolor="#e9f5f7">订购的服务</td>
			<td width="100px" bgcolor="#e9f5f7">服务截止时间</td>
			<td width="100px" bgcolor="#e9f5f7">退订时间</td>
			<td width="100px" bgcolor="#e9f5f7"></td>
		</tr>
		<ext:iterate id="orderItem" property="orderItems">
			<tr valign="middle">
				<td bgcolor="#FFFFFF"><ext:write name="orderItem" property="servicePriceName"/></td>
			    <td align="center" bgcolor="#FFFFFF"><ext:write name="orderItem" property="orderItemEnd" format="yyyy-MM-dd"/></td>
			    <td align="center" bgcolor="#FFFFFF"><ext:write name="orderItem" property="cancelTime" format="yyyy-MM-dd"/></td>
			    <td align="center" bgcolor="#FFFFFF">
			    	<ext:empty name="orderItem" property="serviceEnd">
				    	<a href="cancelOrderItem.shtml?orderItemId=<ext:write name="orderItem" property="id"/>">退订</a>
			    	</ext:empty>
			    </td>
			</tr>
		</ext:iterate>
	</table>
	<br>
	订购时间：<ext:write property="orderTime" format="yyyy-MM-dd HH:mm"/>&nbsp;&nbsp;
</ext:form>
</body>
</html:html>