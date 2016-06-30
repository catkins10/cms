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
<ext:form action="/directoryTree">
	<div id="divTree"></div>
	<ext:tree property="tree" parentElementId="divTree"/>
	<script>
		window.tree.onGetUrlForListChildNodes = function(nodeId) { //事件:获取URL,以得到子节点列表
			var url = "<ext:write property="listChildNodesUrl" filter="false"/>";
			url += (url.lastIndexOf("?")==-1 ? "?" : "&") + "parentNodeId=" + nodeId + "&selectNodeTypes=<ext:write property="selectNodeTypes"/>";
			return url;
		};
		window.tree.onNodeSeleced = function(nodeId, nodeText, nodeType) { //事件:选中节点
			//设置目录名称
			var directoryLocation = parent.document.getElementById("infoDirectoryLocation");
			var urn = directoryLocation.getAttribute("urn");
			if(directoryLocation) {
				directoryLocation.innerHTML = (StringUtils.getPropertyValue(urn, "selfOnly")=="true" ? this.getSelectedNodeText() : this.getSelectedNodeFullText(StringUtils.getPropertyValue(urn, "separator")));
			}
			var url = parent.document.getElementById("publicInfoList").src;
			parent.document.getElementById("publicInfoList").contentWindow.location = url.substring(0, url.lastIndexOf("/") + 1) + "publicInfoView.shtml?directoryId=" + nodeId + "&rootDirectoryId=<ext:write property="rootDirectoryId"/><%=(request.getParameter("siteId")==null ? "" : "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId"))%>" + "&countPublicInfo=true&seq=" + Math.random();
		};
	</script>
</ext:form>
</body>
</html>