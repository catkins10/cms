<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:iterate id="action" property="formActions">
	<input type="button" class="button" style="width: <%=((com.yuanluesoft.jeaf.form.model.FormAction)pageContext.getAttribute("action")).getTitle().length() * 14 + 6 %>" onclick="<ext:write name="action" property="execute"/>" value="<ext:write name="action" property="title"/>">&nbsp;
</ext:iterate>