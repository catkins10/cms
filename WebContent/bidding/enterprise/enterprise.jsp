<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveEnterprise" applicationName="bidding/enterprise" pageName="enterpriseRegist">
   	<ext:tab/>
   	<ext:equal property="subForm" value="enterpriseEdit.jsp">
	   	<br>
		<center>
			<ext:button name="提交" onclick="if(confirm((document.getElementById('tableJobholders').rows.length==1 ? '尚未注册企业人员，' : '') + '是否确定提交？'))FormUtils.doAction('submitEnterprise')"/>
		</center>
	</ext:equal>
</ext:form>