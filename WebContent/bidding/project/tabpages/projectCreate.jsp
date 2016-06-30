<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<jsp:include flush="true" page="../admin/tabpages/projectCreate.jsp"/>

<center>
	<br>
	<ext:button name="完成登记" onclick="if(confirm('是否确定已经完成登记？'))FormUtils.doAction('completeCreate');"/>
</center>