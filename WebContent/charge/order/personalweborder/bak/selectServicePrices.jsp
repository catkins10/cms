<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean" %>
<%
	com.yuanluesoft.charge.order.forms.PersonalWebOrder personalWebOrder = (com.yuanluesoft.charge.order.forms.PersonalWebOrder)request.getAttribute("personalWebOrder"); 
%>

<html:html>
<head>
	<title>选择服务</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<link href="<%=request.getContextPath()%>/edu/css/application.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
	<script>
	    function formOnSubmit() { //检查是否选中服务
			var serviceIds = document.getElementsByTagName("input");
			if(!serviceIds) {
				return false;
			}
			var ids = "";
			for(var i=0; i<serviceIds.length; i++) {
				if(serviceIds[i].checked) {
					ids += (ids=="" ? "" : ",") + serviceIds[i].value;
				}
			}
			if(ids=="") {
				alert("未选择服务,订购不能完成！");
				return false;
			}
			document.getElementsByName("selectedServicePriceIds")[0].value = ids;
			return true;
	    }
	</script>
</head>
<body>
<ext:form action="/submitSelectedServicePrices" onsubmit="return formOnSubmit();">
<center>
	<table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="#CCCCCC">
<%		long previousServiceId = 0;
		for(java.util.Iterator iterator = personalWebOrder.getServicePrices().iterator(); iterator.hasNext();) {
			com.yuanluesoft.charge.servicemanage.pojo.ServicePrice servicePrice = (com.yuanluesoft.charge.servicemanage.pojo.ServicePrice)iterator.next();
			if(servicePrice.getServiceId()!=previousServiceId) {
				previousServiceId = servicePrice.getServiceId();%>
				<tr valign="middle">
					<td  bgcolor="#e9f5f7"><b><%=servicePrice.getService().getServiceName()%>(<%=servicePrice.getService().getItemTitles()%>)</b></td>
				</tr>
<%			}%>
			<tr valign="middle">
				<td height="20" bgcolor="#FFFFFF">
					&nbsp;&nbsp;<input type="radio" class="radio" name="service_<%=servicePrice.getServiceId()%>" value="<%=servicePrice.getId()%>">
					<%=servicePrice.getPrice()%>
				</td>
			</tr>
<%		}%>
	</table>
	<br/>
	<div style="width:80px">
		<input type="button" class="button" value="下一步" onclick="FormUtils.submitForm()"/>
	</div>
</center>
<html:hidden property="selectedServicePriceIds"/>
</ext:form>
</body>
</html:html>