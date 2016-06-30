<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="<%=((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getPath()%>">
	<link href="<%=request.getContextPath()%>/jeaf/tree/css/tree.css" rel="stylesheet" type="text/css" />
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/tree/js/tree.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/dialog/js/selectDialog.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/dialog/treedialog/js/treeDialog.js"></script>
<%	request.setAttribute("hideConditionBar", "false");
	request.setAttribute("dataPage", "/jeaf/dialog/treedialog/dataPage.jsp"); %>
	<jsp:include page="/jeaf/dialog/selectDialogCommon.jsp" />
	<script>
		window.tree.onGetUrlForListChildNodes = function(nodeId) { //事件:获取URL,以得到子节点列表
			var url = "<ext:write property="listChildNodesUrl" filter="false"/>";
			url += (url.lastIndexOf("?")==-1 ? "?" : "&") + "parentNodeId=" + nodeId + "&selectNodeTypes=<ext:write property="selectNodeTypes"/>";
			return url;
		};
		window.tree.onDblClickNode = function(nodeId, nodeText, nodeType, leafNode) { //事件:节点被双击,从tree.js继承
			onDblClickData();
		};
		function validTargetType(node) { //类型校验
			if(!node) {
				return false;
			}
			if(<ext:write property="leafNodeOnly"/> && !window.tree.isLeafNode(node)) { //只允许选择叶节点
				return false;
			}
			var selectTypes = '<ext:write property="selectNodeTypes"/>';
			if(selectTypes=='' || selectTypes=='all') {
				return true;
			}
			var selectedType = window.tree.getNodeAttribute(node, "nodeType");
			return ("," + selectTypes + ",").indexOf("," + selectedType + ",")!=-1;
		}
	</script>
</ext:form>