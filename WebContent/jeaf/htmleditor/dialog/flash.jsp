<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/flash">
<script>
	window.onload = function() {
		var dialogArguments = DialogUtils.getDialogArguments();
		var embed = DomUtils.getParentNode(dialogArguments.selectedElement, 'embed');
		if(!embed) {
			return;
		}
		var src = embed.getAttribute("src", 2);
		document.getElementsByName('url')[0].value = src ? src : "";
		var width = embed.getAttribute("width", 2);
		document.getElementsByName('width')[0].value = width ? width : "";
		var height = embed.getAttribute("height", 2);
		document.getElementsByName('height')[0].value = height ? height : "";
		preview(); //预览
	}
	function doOK() {
		var url = document.getElementsByName('url')[0].value;
		if(url=='') {
			alert('源文件不能为空');
			return;
		}
		var dialogArguments = DialogUtils.getDialogArguments();
		dialogArguments.editor.saveUndoStep();
		var embed = DomUtils.getParentNode(dialogArguments.selectedElement, 'embed');
		if(!embed) {
			DomSelection.selectRange(dialogArguments.window, dialogArguments.range);
			var random = ("" + Math.random()).substring(2);
			dialogArguments.document.execCommand("InsertImage", false, random);
			var images = dialogArguments.document.getElementsByTagName("img");
			for(var i=0; i<images.length; i++) {
				if(images[i].src.indexOf(random)!=-1) {
					embed = images[i];
				}
			}
			if(!embed) {
				alert('FLASH创建失败');
				return;
			}
		}
		embed.parentNode.replaceChild(createFlash(dialogArguments.document), embed);
		DialogUtils.closeDialog();
	}
	function selectFlash() {
		var dialogArguments = DialogUtils.getDialogArguments();
		var selectorURL = dialogArguments.editor.getAttachmentSelectorURL("flashs");
		DialogUtils.openDialog(selectorURL, 640, 400);
	}
	function setUrl(url) {
		document.getElementsByName('url')[0].value = url;
		preview();
	}
	function preview() {
		var previewDiv = document.getElementById('previewDiv');
		previewDiv.style.width = previewDiv.offsetWidth + "px";
		var url = document.getElementsByName('url')[0].value;
		previewDiv.innerHTML = "";
		if(url=="") {
			return;
		}
		previewDiv.appendChild(createFlash(document));
	}
	function createFlash(doc) {
		var embed = doc.createElement("embed");
		embed.menu = true;
		embed.loop = true;
		embed.play = true;
		embed.type = "application/x-shockwave-flash";
		embed.src = document.getElementsByName('url')[0].value;
		embed.pluginsPage = "http://www.macromedia.com/go/getflashplayer";
		var width = document.getElementsByName('width')[0].value;
		if(width!="") {
			embed.width = width;
		}
		var height = document.getElementsByName('height')[0].value;
		if(height!="") {
			embed.height = height;
		}
		return embed;
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<tr>
		<td nowrap="nowrap" align="right">源文件：</td>
		<td><ext:field property="url" onchange="preview()"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">宽度：</td>
		<td>
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="60px"><ext:field property="width" onchange="preview()"/></td>
					<td>&nbsp;高度：</td>
					<td width="60px"><ext:field property="height" onchange="preview()"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right" valign="top">预览：</td>
		<td>
			<div id="previewDiv" style="border:#909090 1px solid; overflow: auto; height: 200px; padding: 1px 1px 1px 1px;"></div>
		</td>
	</tr>
</table>
</ext:form>