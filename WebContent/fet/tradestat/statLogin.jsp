<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/statLogin" applicationName="fet/tradestat" pageName="statQuery">
	<script>
	function doLogin() {
		var userName;
		var userType;
		var prompt;
		var form = document.forms[0];
		var radios = document.getElementsByName("userType");
		if(radios[0].checked) {
			userType = "company";
			userName = document.getElementsByName("companyCode")[0].value;
			if(userName=="") {
				alert("请输入企业海关代码");
				return;
			}
		}
		else if(radios[1].checked) {
			userType = "county";
			userName = document.getElementsByName("county")[0].value;
			if(userName=="") {
				alert("请输入或选择区县");
				return;
			}
		}
		else if(radios[2] && radios[2].checked) {
			userType = "developmentArea";
			userName = document.getElementsByName("developmentArea")[0].value;
			if(userName=="") {
				alert("请输入或选择开发区");
				return;
			}
		}
		document.getElementsByName("userName")[0].value = userName;
		if(document.getElementsByName("password")[0].value=="") {
			alert("请输入口令");
			return;
		}
		form.action = "<%=request.getContextPath()%>/jeaf/sso/submitlogin.shtml";
		var expireDate = new Date();
		expireDate.setFullYear(expireDate.getFullYear() + 1);
		document.cookie = "lastLoginFetUser=" + StringUtils.utf8Encode(userName) + "; expires=" + expireDate.toGMTString() + "; path=/";
		document.cookie = "lastUserType=" + userType + "; expires=" + expireDate.toGMTString() + "; path=/";
		form.submit();
	}
	function onSelectUserType(type) {
		document.getElementById("trCompany").style.display = (type=="company" ? "" : "none");
		document.getElementById("trCounty").style.display = (type=="county" ? "" : "none");
		document.getElementById("trDevelopmentArea").style.display = (type=="developmentArea" ? "" : "none");
	}
	</script>
	<table border="0" cellpadding="0" cellspacing="0" bgcolor="#ffffff" align="center" width="300px">
	  <tr>
	  	<td width="100px" height="35" align="right" nowrap="nowrap">用户类型：</td>
	    <td height="35"><ext:field onclick="onSelectUserType(value)" property="userType"/></td>
	  </tr>
	  <tr id="trCompany" style="<ext:notEqual property="userType" value="company">display:none</ext:notEqual>">
	    <td height="35" align="right" nowrap="nowrap">企业海关代码：</td>
	    <td height="35" align="left"><ext:field property="companyCode"/></td>
	  </tr>
	  <tr id="trCounty" style="<ext:notEqual property="userType" value="county">display:none</ext:notEqual>">
	    <td height="35" align="right" nowrap="nowrap">区县名称：</td>
	    <td height="35" align="left"><ext:field property="county"/></td>
	  </tr>
	  <tr id="trDevelopmentArea" style="<ext:notEqual property="userType" value="developmentArea">display:none</ext:notEqual>">
	    <td height="35" align="right" nowrap="nowrap">开发区名称：</td>
	    <td height="35" align="left"><ext:field property="developmentArea"/></td>
	  </tr>
	  <tr>
	    <td height="35" align="right" nowrap="nowrap">口令：</td>
	    <td height="35" align="left"><ext:field property="password" onkeypress="if(event.keyCode==13)doLogin()"/></td>
	  </tr>
	  <tr>
	  	<td></td>
	    <td height="35">
	    	<input type="button" class="button" onclick="doLogin()" value="登 录"/>
	    	<a href="company.shtml?siteId=<%=request.getParameter("siteId")%>">注册企业</a>
	    </td>
	  </tr>
	  <tr>
	    <td height="35" colspan="2" align="center">
	    	<font color="#FF0000"><jsp:include page="/jeaf/form/promptAsText.jsp"/></font>
	    </td>
	  </tr>
	</table>
	<script>
		<ext:write property="changePasswordScript" filter="false"/>
	</script>
</ext:form>