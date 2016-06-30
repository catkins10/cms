<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/chat">
	<ext:iterate id="talk" property="talks">
		<div>
			<ext:write name="talk" property="creatorName"/>&nbsp;<ext:write name="talk" property="created" format="yyyy-MM-dd HH:mm:ss"/>
		</div>
		<div style="padding:5px 0px 10px 8px">
			<ext:write name="talk" property="content" filter="false"/>
		</div>
	</ext:iterate>
</ext:form>