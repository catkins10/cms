<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/editElementHtml">
	<script>
		var toEditElement;
		var currentElement;
		window.onload = function() {
			currentElement = DialogUtils.getDialogArguments().selectedElement;
			toEditElement = currentElement;
			if(currentElement) {
				document.getElementsByName("elementHTML")[0].value = currentElement.outerHTML;
			}
		};
		function doOk() {
			var dialogArguments = DialogUtils.getDialogArguments();
			dialogArguments.editor.saveUndoStep();
			if(currentElement) {
				try {
					var html = document.getElementsByName("elementHTML")[0].value;
					DomUtils.setElementHTML(currentElement, html, document.getElementsByName("editInnerHTML")[0].checked);
				}
				catch(e) {
					alert(currentElement.tagName + "不允许直接修改。");
					return;
				}
			}
			DialogUtils.closeDialog();
		}
		function getParentElementHTML() {
			if(currentElement && currentElement.parentNode.tagName!="BODY") {
				currentElement = currentElement.parentElement;
				document.getElementsByName("elementHTML")[0].value = document.getElementsByName("editInnerHTML")[0].checked ? currentElement.innerHTML : currentElement.outerHTML;
				findText(toEditElement.outerHTML, true); //选中要配置元素的HTML
			}
		}
		function doEditInnerHTML() { //编辑内部HTML
			if(currentElement) {
				document.getElementsByName("elementHTML")[0].value = document.getElementsByName("editInnerHTML")[0].checked ? currentElement.innerHTML : currentElement.outerHTML;
				findText(toEditElement.outerHTML, true);
			}
		}
		function findText(text, fromBeginPosition) { //查找文本
			if(fromBeginPosition) {
				var range = DomSelection.createRange(document, document.getElementsByName("elementHTML")[0]);
				range.collapse(true);
				DomSelection.selectRange(window, range);
			}
			else {
				document.getElementsByName("elementHTML")[0].focus();
				var range = DomSelection.getRange(window);
				range.collapse(false);
			}
			if(range.findText) {
				if(range.findText(text)) {
					range.select();
				}
			}
			else if(window.find) {
				window.find(text);
			}
			
		}
	</script>
	<table border="0" cellpadding="3px" cellspacing="0" width="100%">
		<tr>
			<td nowrap="nowrap">源代码：</td>
			<td nowrap="nowrap"><input type="button" class="button" onclick="getParentElementHTML()" value="获取父元素源代码" style="width:110px"></td>
			<td nowrap="nowrap"><ext:field property="key" style="width:150px"/></td>
			<td nowrap="nowrap"><input type="button" class="button" onclick="findText(document.getElementsByName('key')[0].value)" value="查找" style="width:40px"></td>
			<td nowrap="nowrap"><ext:field onclick="doEditInnerHTML();" property="editInnerHTML"/></td>
			<td width="100%"></td>
		</tr>
	</table>
	<ext:field property="elementHTML"/>
</ext:form>