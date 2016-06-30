<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<title>分类</title>
</head>
<body style="margin:0px">
	<%request.setAttribute("writeFormPrompt", "true");%>
    <ext:form action="/categoryTree">
    	<ext:equal property="actionResult" value="NOSESSIONINFO">
    		<script language="javascript">top.location.reload();</script>
		</ext:equal>
		<ext:equal property="actionResult" value="SUCCESS">
    		<jsp:include page="/jeaf/tree/tree.jsp" />
    		<script>
    			window.tree.onNodeSeleced = function(nodeId, nodeText, nodeType) { //事件:节点被选中,从tree.js继承
					parent.setTimeout("onCategoryChanged('" + nodeId + "')", 1);
				};
    		</script>
    	</ext:equal>
    </ext:form>
</body>
</html>