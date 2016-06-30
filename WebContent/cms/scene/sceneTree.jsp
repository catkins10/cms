<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/js/subPageCssImport.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/tree/js/tree.js"></script>
</head>
<body leftmargin="3" topmargin="3" rightmargin="3" bottommargin="3" style="border-style:none; overflow-x:hidden; overflow-y:hidden;">
<ext:form action="/sceneTree">
	<div id="divTree"></div>
	<ext:tree property="tree" parentElementId="divTree"/>
	<script>
		window.tree.onGetUrlForListChildNodes = function(nodeId) { //事件:获取URL,以得到子节点列表
			var url = "<ext:write property="listChildNodesUrl" filter="false"/>";
			url += (url.lastIndexOf("?")==-1 ? "?" : "&") + "parentNodeId=" + nodeId + "&selectNodeTypes=<ext:write property="selectNodeTypes"/>";
			return url;
		};
		window.tree.onNodeSeleced = function(nodeId, nodeText, nodeType) { //事件:选中节点
			var actionName;
			if(nodeType=="service") {
				actionName = "sceneService";
			}
			else if(nodeType=="link") {
				window.open(window.tree.getNodeAttribute(window.tree.getNodeById(nodeId), "url"));
				return;
			}
			else {
				actionName = "scene";
			}
			var url = parent.document.getElementById("sceneList").src;
			parent.document.getElementById("sceneList").src = url.substring(0, url.lastIndexOf("/") + 1) + actionName + ".shtml?id=" + nodeId + "&siteId=<%=com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId")%>";
		};
		var attachmentEvent = function() { //窗口加载
			var sceneListIframe = parent.document.getElementById("sceneList");
			if(!sceneListIframe) {
				top.window.setTimeout(attachmentEvent, 1000);
				return;
			}
			var onSceneListLoad = function() {
				var url = parent.frames["sceneList"].location.search;
				window.tree.expand(window.tree.getNodeById(StringUtils.getPropertyValue(url, "id")), true);
			};
			//绑定事件
			EventUtils.addEvent(sceneListIframe, 'load', onSceneListLoad);
		};
		top.window.setTimeout(attachmentEvent, 300);
	</script>
</ext:form>
</body>
</html>