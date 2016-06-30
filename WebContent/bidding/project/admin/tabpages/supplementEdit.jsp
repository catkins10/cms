<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div>
	<ext:field property="supplement.body"/>
	
	<ext:iterate id="supplement" property="supplements">
		<ext:notEmpty name="supplement" property="publicBeginTime">
			<br><br>
			<b>补充通知(<ext:write name="supplement" property="publicBeginTime" format="yyyy-MM-dd"/>)：</b><br>
			<ext:write name="supplement" property="body" filter="false"/>
		</ext:notEmpty>
	</ext:iterate>
</div>