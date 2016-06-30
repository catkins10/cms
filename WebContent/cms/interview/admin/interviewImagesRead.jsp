<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<center>
	<ext:iterate id="image" property="interviewImages">
		<img src="<ext:write name="image" property="imageUrl"/>"><br>
		<ext:write name="image" property="subject"/><br><br>
	</ext:iterate>
</center>