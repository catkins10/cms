<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/js/subPageCssImport.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/tree/js/tree.js"></script>
</head>
<body leftmargin="3" topmargin="3" rightmargin="3" bottommargin="3" style="border-style:none; overflow-x:hidden; overflow-y:hidden">
<ext:form action="/plantDirectoryTree">
	<div id="divTree"></div>
	<ext:tree property="tree" parentElementId="divTree"/>
	<script>
		window.tree.onGetUrlForListChildNodes = function(nodeId) { //事件:获取URL,以得到子节点列表
			var url = "<ext:write property="listChildNodesUrl" filter="false"/>";
			url += (url.lastIndexOf("?")==-1 ? "?" : "&") + "parentNodeId=" + nodeId + "&selectNodeTypes=<ext:write property="selectNodeTypes"/>";
			return url;
		};
		window.tree.onNodeSeleced(nodeId, nodeText, nodeType) { //事件:选中节点
			var contentWindow = parent.document.getElementById("content").contentWindow;
			contentWindow.location = RequestUtils.getContextPath() + "/chd/evaluation/directory.shtml?directoryId=" + nodeId + (contentWindow.location.href.indexOf("selfEval=true")!=-1 ? "&selfEval=true" : "") + "&siteId=<%=com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId")%>";
		};
	</script>
</ext:form>
</body>
</html>