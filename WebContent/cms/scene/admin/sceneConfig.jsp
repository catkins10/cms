<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>

<html:html>
<head>
	<title>场景服务</title>
	<script>
		var move = false, cursorX, navigatorWidth;
		function startDrag(obj) {
			obj.setCapture();
			cursorX = event.clientX;
			navigatorWidth = document.getElementById('tdSceneTree').offsetWidth
			move = true;
		}
		function drag(obj) {
			if(move) {
				var width = navigatorWidth + (event.clientX - cursorX);
				if(width>0 && width<document.body.clientWidth - document.getElementById('tdAdjust').offsetWidth) {
					document.getElementById('tdSceneTree').width = width;
				}
			}
		}
		function stopDrag(obj) {
			obj.releaseCapture();
			move = false;
		}
	</script>
</head>
<body style="margin:0; overflow:hidden; border-style: none">
	<table border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed" height="100%" width="100%">
		<tr>
			<td id="tdSceneTree" width="230px">
				<iframe name="treeFrame" id="treeFrame" src="sceneTree.shtml?<%=request.getQueryString()%>" style="width:100%; height:100%" frameborder="0"></iframe>
			</td>
			<td id="tdAdjust" width="3px" style="cursor: e-resize; background-color:#f8f8fc" onmousedown="startDrag(this)" onmouseup="stopDrag(this)" onmousemove="drag(this)">&nbsp;</td>
			<td width="100%">
				<iframe name="contentFrame" src="sceneService.shtml?<%=request.getQueryString()%>" name="contentFrame" style="width:100%; height:100%" frameborder="0"></iframe>
			</td>
		</tr>
	</table>
</body>
</html:html>