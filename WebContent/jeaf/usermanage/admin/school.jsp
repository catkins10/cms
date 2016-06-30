<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveSchool">
   	<ext:equal value="create" name="adminSchool" property="act">
	   	<jsp:include page="schoolRegist.jsp" />
   	</ext:equal>
   	<ext:notEqual value="create" name="adminSchool" property="act">
	   	<ext:subForm/>
   	</ext:notEqual>
</ext:form>