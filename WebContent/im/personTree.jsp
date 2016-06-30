<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/js/clientPage.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/js/subPageCssImport.js"></script>
</head>
<body>
	<html:form action="/personTree">
		<jsp:include page="/jeaf/tree/tree.jsp" />
		<script>
			window.tree.onDblClickNode = function(nodeId, nodeText, nodeType) { //事件:节点被双击
				if(",employee,teacher,student,genearch,".indexOf("," + nodeType + ",")!=-1 && nodeId!="<ext:write property="userId" name="SessionInfo" scope="session"/>") {
					parent.setTimeout('doHtmlDialogAction("CREATECHAT", "personId="' + nodeId + '")', 1);
				}
			};
		</script>
	</html:form>
</body>
</html>