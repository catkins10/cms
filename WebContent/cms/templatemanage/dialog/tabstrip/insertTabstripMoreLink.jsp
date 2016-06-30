<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertTabstripMoreLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			//设置选项卡列表
			var tabstripNames = "";
			var elements = dialogArguments.document.getElementsByTagName("A");
			for(var i=0; i<elements.length; i++) {
				if(elements[i].id=="tabstripBody") {
					tabstripNames += (tabstripNames=="" ? "" : "\0 ") + StringUtils.getPropertyValue(elements[i].getAttribute("urn"), "name");
				}
			}
			DropdownField.setListValues("tabstripName", tabstripNames);
			
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj && obj.id.indexOf("tabstripMoreLink_")==0) {
				DropdownField.setValue("tabstripName", obj.getAttribute("urn")); //隶属选项卡
				//解析打开方式
				setLinkOpenMode(obj.target);
			}
		}
		function doOk() {
			var tabstripName = document.getElementsByName("tabstripName")[0].value;
			if(tabstripName=='') {
				alert("隶属选项卡不能为空");
				return false;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					alert('请重新选择插入的位置');
					return;
				}
			}
			//打开方式
			obj.target = getLinkOpenMode();
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = "更多...";
			}
			obj.id = "tabstripMoreLink_" + tabstripName;
			obj.setAttribute("urn", tabstripName);
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">隶属选项卡：</td>
			<td><ext:field property="tabstripName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>