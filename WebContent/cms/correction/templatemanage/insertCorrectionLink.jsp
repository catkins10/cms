<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertCorrectionLink">
	<script>
	var dialogArguments = DialogUtils.getDialogArguments();
	function doOk() {
		dialogArguments.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
		if(!obj) {
			obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
			if(!obj) {
				return false;
			}
		}
		obj.removeAttribute("target");
		if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
			obj.innerHTML = "纠错";
		}
		obj.setAttribute("onclick", "createCorrection();return false;");
		obj.href = "#";
		DialogUtils.closeDialog();
	}
	</script>
</head>
<body style="overflow: hidden; border-style:none">
	是否确定插入“纠错”链接？
</ext:form>