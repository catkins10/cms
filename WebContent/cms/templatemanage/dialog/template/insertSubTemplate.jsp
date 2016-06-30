<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertSubTemplate">
	<script>
		function doOk() {
			var subTemplateDocument = document.getElementById('frameQuery').contentWindow.document;
			if(subTemplateDocument.body.innerHTML=="") {
				alert('子模板未设置或者尚未加载完成！');
				return;
			}
			var dialogArguments = DialogUtils.getDialogArguments();
			if(subTemplateDocument.body.innerHTML.indexOf("ID=" + dialogArguments.editor.document.getElementsByName("id")[0].value)!=-1) {
				alert('不允许循环嵌套！');
				document.getElementsByName("subTemplateName")[0].value = "";
				return;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, 'div');
			if(obj) {
				while(obj.id!="subTemplate" && obj.tagName.toLowerCase()!="body") {
					obj = obj.offsetParent;
				}
			}
			if(obj && obj.id=="subTemplate") {
				obj.innerHTML = "";
			}
			else {
				obj = DomUtils.createElement(dialogArguments.window, dialogArguments.range, 'div');
			}
			obj.id = 'subTemplate';
			obj.title = "子模板=" + document.getElementsByName("subTemplateName")[0].value + "&ID=" + document.getElementsByName("subTemplateId")[0].value + "&禁止继承=" + (document.getElementsByName("inheritDisabled")[0].checked);
			obj.style.cssText = "border-width:1; border-style:dotted; border-color:#0000cd;";
			obj.innerHTML = subTemplateDocument.body.innerHTML;
			//复制头部的link、style和script
			var head = subTemplateDocument.getElementsByTagName('head')[0];
			if(head) {
				var editorHead = dialogArguments.document.getElementsByTagName('head')[0];
				for(var i=0; i<head.childNodes.length; i++) {
					if(head.childNodes[i].id && head.childNodes[i].id.indexOf("subTemplateHeadElement_")==0) {
						editorHead.appendChild(head.childNodes[i]);
						i--;
					}
				}
			}
			DialogUtils.closeDialog();
		}
		function selectSubTemplate() {
			var dialogArguments = DialogUtils.getDialogArguments();
			var url = RequestUtils.getContextPath() + "/cms/templatemanage/selectSubTemplate.shtml";
			url += "?multiSelect=false"; 
			url += "&param=subTemplateId{id},subTemplateName{name}"; 
			url += "&selectNodeTypes=subTemplate";
			url += "&themeId=" + dialogArguments.editor.document.getElementsByName("themeId")[0].value; 
			url += "&script=retrieveSubTemplate()";
			DialogUtils.openDialog(url, 600, 400);
		}
		function retrieveSubTemplate() { //获取模板HTML
			var dialogArguments = DialogUtils.getDialogArguments();
			var templateId = document.getElementsByName("subTemplateId")[0].value;
			if(dialogArguments.editor.document.getElementsByName("id")[0].value==templateId) {
				alert('不允许插入子模板本身！');
				document.getElementsByName("subTemplateName")[0].value = "";
				return;
			}
			document.getElementById("frameQuery").src = RequestUtils.getContextPath() + "/cms/templatemanage/retrieveSubTemplate.shtml?templateId=" + templateId;
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap" align="right">选择子模板：</td>
			<td><ext:field property="subTemplateName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">子站/子栏目：</td>
			<td><ext:field property="inheritDisabled"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right" valign="top">预览：</td>
			<td><iframe id="frameQuery" src="about:blank" frameborder="0" style="border:#909090 1px solid; width:100%; height: 300px"></iframe></td>
		</tr>
	</table>
</ext:form>