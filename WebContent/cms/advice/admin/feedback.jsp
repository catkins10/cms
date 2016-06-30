<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:iterate id="feedback" property="feedbacks">
	<ext:field writeonly="true" name="feedback" property="feedback"/>
</ext:iterate>