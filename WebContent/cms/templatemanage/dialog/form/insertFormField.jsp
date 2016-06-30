<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertFormField">
	<script>
		var fieldElement;
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			if(!dialogArguments.selectedElement) {
				return;
			}
			if(dialogArguments.selectedElement.alt) {
				document.getElementsByName("inputAlt")[0].value = dialogArguments.selectedElement.alt;
			}
			if(dialogArguments.selectedElement.id=='field' || dialogArguments.selectedElement.id=='fieldSpan') {
				fieldElement = dialogArguments.selectedElement;
			}
			else {
				fieldElement = DomUtils.getParentNode(dialogArguments.selectedElement, 'fieldSpan');
			}
			if(",INPUT,TEXTAREA,".indexOf(',' + dialogArguments.selectedElement.tagName + ',')!=-1) {
				document.getElementById("trStyle").style.display = "none";
			}
			var fieldName = !fieldElement ? '' : (fieldElement.id=='fieldSpan' ? fieldElement.title : fieldElement.name);
			var fields = getFields();
			for(var i=0; fieldName!='' && i<fields.length; i++) {
				if(fields[i].name==fieldName || fields[i].title==fieldName) {
					DropdownField.setValueByIndex("field", i);
					break;
				}
			}
			onFieldChanged();
		};
		function getFields() {
			var fields = DropdownField.getListValues('field').split("\0");
			for(var i=0; i<fields.length; i++) {
				eval('fields[i] = ' + fields[i].substring(fields[i].indexOf('|') + 1));
			}
			return fields;
		}
		function doOk() {
			var field = document.getElementsByName("field")[0].value;
			if(field=="") {
				alert('请选择字段');
				return;
			}
			var extendProperties = "";
			var iframe = document.getElementById('iframeTemplateExtend');
			if(iframe.style.display!='none') {
				try {
					extendProperties = iframe.contentWindow.getExtendPropertiesAsText();
				}
				catch(e) {
	
				}
				if(extendProperties=="ERROR") {
					return false;
				}
			}
			eval("var field = " + field + ";");
			dialogArguments.editor.saveUndoStep();
			var cssName = document.getElementsByName("cssName")[0].value;
			if(field.inputMode=="multibox" || field.inputMode=="radio") {
				var items = (field.itemsText=="" ? "选项1\0选项2\0选项3" : field.itemsText);
				items = items.split("\0");
				if(!fieldElement || fieldElement.id!="fieldSpan") {
					fieldElement = DomUtils.createElement(dialogArguments.window, dialogArguments.range, 'span');
				}
				fieldElement.id = "fieldSpan";
				var html = '';
				for(var k=0; k<items.length; k++) {
					html += '<input type="' + (field.inputMode=="radio" ? "radio" : "checkbox") + '" id="field" name="' + field.name +'">';
					html += '&nbsp;' + items[k].split("|")[0];
					if(k<items.length-1) {
						html += '&nbsp;';
					}
				}
				fieldElement.innerHTML = html;
				fieldElement.title = field.title;
			}
			else if(field.inputMode=="checkbox") { //单个复选框
				fieldElement = dialogArguments.selectedElement;
				if(!fieldElement || fieldElement.type!="checkbox") {
					var label = DomUtils.createElement(dialogArguments.window, dialogArguments.range, 'label');
					label.innerHTML = "&nbsp;" + field.checkboxLabel;
					label.setAttribute("id", "label_" + field.name);
					label.setAttribute("for", field.name);
					
					fieldElement = label.ownerDocument.createElement("input");
					label.parentNode.insertBefore(fieldElement, label);
					fieldElement = DomUtils.setAttribute(fieldElement, "type", "checkbox");
				}
				fieldElement.id = "field";
				fieldElement.setAttribute("value", field.checkboxValue);
				DomUtils.setAttribute(fieldElement, "name", field.name)
			}
			else if(field.inputMode=="hidden" || field.inputMode=="readonly") {
				fieldElement = DomUtils.getParentNode(dialogArguments.selectedElement, "span");
				if(!fieldElement || fieldElement.id!=field.inputMode + "Field") {
					fieldElement = DomUtils.createElement(dialogArguments.window, dialogArguments.range, 'span');
				}
				fieldElement.id = field.inputMode + "Field";
				fieldElement.title = field.name;
				fieldElement.style.cssText = style;
				fieldElement.innerHTML = "&lt;" + field.title + "&gt;";
			}
			else {
				fieldElement = dialogArguments.selectedElement;
				if(field.inputMode=="textarea" || field.inputMode=="htmleditor") {
					if(!fieldElement || fieldElement.tagName!="TEXTAREA") {
						fieldElement = DomUtils.createElement(dialogArguments.window, dialogArguments.range, 'textarea');
						if(cssName!="") {
							fieldElement.className = cssName;
						}
					}
				}
				else if(!fieldElement || !fieldElement.type || (fieldElement.type!="text" && fieldElement.type!="password")) {
					fieldElement = DomUtils.createElement(dialogArguments.window, dialogArguments.range, 'input');
					fieldElement = DomUtils.setAttribute(fieldElement, "type", "text");
					if(cssName!="") {
						fieldElement.className = cssName;
					}
				}
				if(",text,dropdown,select,date,".indexOf("," + field.inputMode + ",")!=-1) {
					fieldElement.alt = document.getElementsByName("inputAlt")[0].value;
				}
				fieldElement.id = "field";
				fieldElement.title = field.title;
				DomUtils.setAttribute(fieldElement, "name", field.name);
			}
			//设置扩展属性
			if(extendProperties=='') {
				fieldElement.removeAttribute("urn");
			}
			else {
				fieldElement.setAttribute("urn", extendProperties);
			}
			DialogUtils.closeDialog();
		}
		function onFieldChanged() {
			try {
				eval("var field = " + document.getElementsByName("field")[0].value + ";");
				document.getElementById("trInputAlt").style.display = ",text,textarea,dropdown,select,date,datetime,".indexOf("," + field.inputMode + ",")!=-1 ? "" : "none";
				var iframeTemplateExtend = document.getElementById('iframeTemplateExtend');
				iframeTemplateExtend.style.display = field.templateExtendURL ? '' : 'none';
				if(field.templateExtendURL && iframeTemplateExtend.src!=field.templateExtendURL) {
					iframeTemplateExtend.src = field.templateExtendURL;
				}
				DialogUtils.adjustDialogSize();
			}
			catch(e) {
			
			}
		}
		function onTemplateExtendLoad() { //扩展的页面加载完成
			var iframe = document.getElementById('iframeTemplateExtend');
			if(iframe.style.display=='none') {
				return;
			}
			window.setTimeout(function() {
				var extendDocument = iframe.contentWindow.document;
				CssUtils.cloneStyle(document, extendDocument);
				extendDocument.body.style.margin = '0px';
				extendDocument.body.style.padding = '0px';
				extendDocument.body.style.overflow = 'hidden';
				iframe.style.height = "1px";
				iframe.style.height = extendDocument.body.scrollHeight;
				var extendTable = extendDocument.getElementsByTagName("table")[0];
				var mainTable = document.getElementById("mainTable");
				extendTable.cellSpacing  = mainTable.cellSpacing;
				extendTable.cellPadding  = mainTable.cellPadding;
				var mainTd = mainTable.rows[0].cells[0];
				var extendTd = extendTable.rows[0].cells[0];
				mainTd.style.width = extendTd.style.width = Math.max(mainTd.offsetWidth, extendTd.offsetWidth);
				//显示扩展属性
				try {
					iframe.contentWindow.showExtendProperties(fieldElement ? fieldElement.getAttribute("urn") : "");
				}
				catch(e) {
					
				}
				DialogUtils.adjustDialogSize();
			}, 10);
		}
	</script>
	<table id="mainTable" border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">表单字段：</td>
			<td><ext:field property="field" onchange="onFieldChanged()"/></td>
		</tr>
		<tr id="trInputAlt" style="display:none">
			<td nowrap="nowrap">提示文本：</td>
			<td><ext:field property="inputAlt"/></td>
		</tr>
		<tr id="trStyle">
			<td nowrap="nowrap">样式名称：</td>
			<td><ext:field property="cssName"/></td>
		</tr>
	</table>
	<iframe style="display:none; width:100%;" id="iframeTemplateExtend" onload="onTemplateExtendLoad()" frameborder="0"></iframe>
</ext:form>