<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<b>ISO文件上传：</b>
<ext:empty property="projectTeam.stage">
	<ext:field property="isoFile"/>
</ext:empty>
<ext:notEmpty property="projectTeam.stage">
	<ext:field property="isoDesignFile"/>
</ext:notEmpty>
<br><br>
<jsp:include page="isoFileRead.jsp" />