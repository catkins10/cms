<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>工作流实例</title>
	<style>
		.tip {
			 filter: alpha(opacity=90);
			 opacity:0.90;
		}
		.tip td {
			font-size: 12px;
			line-height: 15px;
		}
	</style>
	<script language="JavaScript" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script>
		var dimensions = new Array();
		var coordinates = new Array();
		window.onload = function() {
			var maxWidth = 0;
			var maxHeight = 0;
			for(var i=1; i<dimensions.length; i++) {
				maxWidth = Math.max(maxWidth, dimensions[i][0]);
				maxHeight = Math.max(maxHeight, dimensions[i][1]);
			}
			try {
				var width = Math.max(document.body.scrollWidth, Math.max(dimensions[0][0], maxWidth) + 10);
				var height = document.body.scrollHeight + 10 + (maxHeight<dimensions[0][1] ? 0 : maxHeight-dimensions[0][1]);
				window.setTimeout("if(window.frameElement.offsetWidth<" + width + "){window.frameElement.style.width='" + width + "px';}if(window.frameElement.offsetHeight<" + height + "){window.frameElement.style.height='" + height + "px';}", 100);
			}
			catch(e) {
				
			}
		};
		var currentElement = null;
		var workflowTip = null;
		function mouseMoveOnView(srcElement, viewIndex, offsetX, offsetY, x, y) {
			var processElement = null;
			for(var i=0; i<coordinates[viewIndex].length; i++) {
				var element = document.getElementById("process_" + viewIndex + "_" + coordinates[viewIndex][i][0]);
				if(!element) {
					continue;
				}
				var startX = Math.min(coordinates[viewIndex][i][1], coordinates[viewIndex][i][3]);
				var endX = Math.max(coordinates[viewIndex][i][1], coordinates[viewIndex][i][3]);
				var startY = Math.min(coordinates[viewIndex][i][2], coordinates[viewIndex][i][4]);
				var endY = Math.max(coordinates[viewIndex][i][2], coordinates[viewIndex][i][4]);
				if(startX<=offsetX && endX>=offsetX && startY<=offsetY && endY>=offsetY) {
					processElement = element;
					break;
				}
			}
			if(processElement==null) {
				hideTip();
			}
			else if(currentElement!=processElement) {
				currentElement = processElement;
				showTip(srcElement, x, y, currentElement.innerHTML);
			}
		}
		function mouseOutView(viewIndex) {
			hideTip();
		}
		function showTip(srcElement, x, y, html) {
			if(workflowTip==null) {
				workflowTip = new FormField.Picker('', '<html><body style="margin:0px">' + html + '</body></html>', null, 100, 800, false, true, false, true);
			}
			else {
				workflowTip.pickerFrame.document.body.innerHTML = html;
			}
			var pos = DomUtils.getAbsolutePosition(srcElement, null, true);
			workflowTip.show(x + pos.left, y + pos.top - 29);
		}
		function hideTip() {
			if(workflowTip!=null) {
				currentElement = null;
				workflowTip.hide();
			}
		}
		CssUtils.cloneStyle(parent.document, document);
	</script>
</head>
<body style="margin:0; overflow:hidden; border-style: none; background-color: transparent">
	<ext:form action="/workflowInstanceViewer">
		<ext:tab/>
	</ext:form>
</body>
</html:html>