<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<ext:equal name="effectUnicomCard" property="step" value="0">
	<jsp:include page="/charge/unicom/effectUnicomCardI.jsp" />
</ext:equal>

<ext:equal name="effectUnicomCard" property="step" value="1">
	<jsp:include page="/charge/unicom/effectUnicomCardII.jsp" />
</ext:equal>