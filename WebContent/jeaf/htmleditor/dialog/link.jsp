<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/link">
<script>
	window.onload = function() {
		var dialogArguments = DialogUtils.getDialogArguments();
		var link = DomUtils.getParentNode(dialogArguments.selectedElement, 'a');
		if(!link) {
			return;
		}
		var href = link.getAttribute("href", 2);
		if(href) {
			document.getElementsByName('url')[0].value = href;
		}
		var target = link.target;
		if(!link.target) {
			document.getElementsByName('targetName')[0].value = '';
			target = 'none';
		}
		else {
			document.getElementsByName('targetName')[0].value = target;
			if(',_self,_parent,_top,_blank,'.indexOf(',' + target + ',')==-1) {
				target = 'other';
			}
		}
		DropdownField.setValue('target', target);
	}
	function doOK() {
		var url = document.getElementsByName('url')[0].value;
		if(url=='') {
			alert('链接地址不能为空');
			return;
		}
		var dialogArguments = DialogUtils.getDialogArguments();
		dialogArguments.editor.saveUndoStep();
		var link = DomUtils.getParentNode(dialogArguments.selectedElement, 'a');
		if(!link) {
			try {
				link = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
			}
			catch(e) {
			
			}
			if(!link) {
				alert('链接创建失败');
				return;
			}
		}
		link.href = url;
		if(link.innerHTML=="" || link.innerHTML.toLowerCase()=="<br>" || link.innerHTML.toLowerCase()=="<br/>") { //没有内容
			var indexBegin = url.lastIndexOf('/') + 1;
			var indexEnd = url.indexOf('.', indexBegin);
			link.appendChild(dialogArguments.document.createTextNode(StringUtils.utf8Decode(indexBegin==indexEnd ? url : url.substring(indexBegin, indexEnd==-1 ? url.length : indexEnd))));
		}
		//设置链接打开方式
		var target = document.getElementsByName('targetName')[0].value;
		if(target=="") {
			link.removeAttribute('target');
		}
		else {
			link.target = target;
		}
		DialogUtils.closeDialog();
	}
	function selectAttachment() {
		var dialogArguments = DialogUtils.getDialogArguments();
		var selectorURL = dialogArguments.editor.getAttachmentSelectorURL("attachments");
		DialogUtils.openDialog(selectorURL, 640, 400);
	}
	function setUrl(url) {
		document.getElementsByName('url')[0].value = url;
	}
	function onTargetChanged() {
		var target = document.getElementsByName('target')[0].value;
		if(target=="none") {
			document.getElementsByName('targetName')[0].value = "";
		}
		else if(target!="other") {
			document.getElementsByName('targetName')[0].value = target;
		}
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<tr>
		<td nowrap="nowrap" align="right">链接地址：</td>
		<td width="100%"><ext:field property="url"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">链接目标：</td>
		<td width="100%">
			<table border="0" width="100%" cellspacing="0" cellpadding="0px">
				<tr>
					<td width="100px"><ext:field property="target" onchange="onTargetChanged()"/></td>
					<td style="padding-left: 5px"><ext:field property="targetName"/></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</ext:form>