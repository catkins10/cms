﻿<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveCompany" onsubmit="return formOnSubmit();" applicationName="fet/tradestat" pageName="statQuery">
	<ext:subForm/>
	<script>
   		document.title = "企业信息" + (document.title=="" ? "" : " - " + document.title);
   	</script>
</ext:form>
