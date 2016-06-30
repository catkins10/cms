<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="javascript" src="<%=request.getContextPath()%>/cms/scene/js/scene.js"></script>
<jsp:include page="actionBar.jsp" />
<hr style="color: #cccccc; height:1px">
<ext:subForm/>
<jsp:include page="/jeaf/form/warn.jsp" />
<script>
	function copySceneDirectory(actionName) { //拷贝目录
		selectScene(500, 320, false, "copyToDirectoryId{id}", "FormUtils.doAction('" + actionName + "')", "", "", "scene,service", "<ext:write property="sceneServiceId"/>");
	}
</script>