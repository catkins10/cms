<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>场景目录</title>
	<style>
		body {
			font-size:12px;
			font-family:Simsun;
		}
		.workspace { 
			width: 100%; 
			height: 100%;
			overflow: auto;
			background: #e9f5f7;
			border: 1px solid #dbdce3;
			border-left-style: none;
			padding-left: 6px;
		    padding-top: 3px;
		    scrollbar-face-color: #F5F8FA;
			scrollbar-highlight-color: #F5F8FA;
			scrollbar-3dlight-color: #D0E3E8;
			scrollbar-darkshadow-color: #D0E3E8;
			scrollbar-Shadow-color: #F5F8FA;
			scrollbar-arrow-color: #9BC4CC;
			scrollbar-track-color: #FAFAFA;
		}
	</style>
</head>
<body leftmargin="0px" rightmargin="0px" topmargin="0" bottommargin="0" style="overflow:hidden; border:none">
<ext:form action="/admin/displaySceneTree" style="height:100%">
	<div class="workspace"><jsp:include page="/jeaf/tree/tree.jsp" /></div>
	<script>
		//事件:节点被选中
		window.tree.onNodeSeleced = function(nodeId, nodeText, nodeType, leafNode) {
			var formName;
			if(nodeType=="service") {
				formName = "sceneService";
			}
			else if(nodeType=="scene") {
				formName = "scene";
			}
			else if(nodeType=="content") {
				formName = "sceneContent";
			}
			else if(nodeType=="link") {
				formName = "sceneLink";
			}
			top.frames["contentFrame"].location.href = formName + ".shtml?act=edit&id=" + nodeId;
		};
	</script>
</ext:form>
</body>
</html:html>