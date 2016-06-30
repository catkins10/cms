<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<ext:form siteFormServiceName="bbsFormService" applicationName="bbs" pageName="bbsPrompt">
	<div align="center">
		删除完成。<br><br>
		<ext:button name="关闭窗口" onclick="window.close()" width="60"/>
	</div>
</ext:form>