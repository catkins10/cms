<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/image">
<script>
	var dialogArguments = DialogUtils.getDialogArguments();
	window.onload = function() {
		document.getElementsByName('center')[0].checked = dialogArguments.editor.centerImage;
		var image = DomUtils.getParentNode(dialogArguments.selectedElement, 'img');
		if(!image) {
			return;
		}
		var src = image.getAttribute("src", 2);
		document.getElementsByName('url')[0].value = src ? src : "";
		var alt = image.getAttribute("alt", 2);
		document.getElementsByName('alt')[0].value = alt ? alt : "";
		DropdownField.setValue('align', image.align);
		preview(image.width, image.height); //预览
	}
	function doOK() {
		var url = document.getElementsByName('url')[0].value;
		if(url=='') {
			alert('图片地址不能为空');
			return;
		}
		dialogArguments.editor.saveUndoStep();
		var newImage = false;
		var image = DomUtils.getParentNode(dialogArguments.selectedElement, 'img');
		if(!image) {
			newImage = true;
			DomSelection.selectRange(dialogArguments.window, dialogArguments.range);
			var random = ("" + Math.random()).substring(2);
			dialogArguments.document.execCommand("InsertImage", false, random);
			var images = dialogArguments.document.getElementsByTagName("img");
			for(var i=0; i<images.length; i++) {
				if(images[i].src.indexOf(random)!=-1) {
					image = images[i];
				}
			}
			if(!image) {
				alert('图片创建失败');
				return;
			}
		}
		image.src = url;
		var alt = document.getElementsByName('alt')[0].value;
		if(alt=="") {
			image.removeAttribute('alt');
		}
		else {
			image.alt = alt;
		}
		//设置链接打开方式
		var align = document.getElementsByName('align')[0].value;
		if(align=="") {
			image.removeAttribute('align');
		}
		else {
			image.align = align;
		}
		image.width = document.getElementsByName('width')[0].value;
		image.height = document.getElementsByName('height')[0].value;
		if(image.width>800 && image.width==window.imageWidth && image.height==window.imageHeight) {
			image.width = 800;
			image.height = Math.round(800 * window.imageHeight / window.imageWidth);
			image.setAttribute("onclick", "window.open(this.src)");
		}
		if(newImage && document.getElementsByName('center')[0].checked) { //自动居中
			var div = dialogArguments.document.createElement('div');
			div.align = "center";
			image.parentNode.replaceChild(div, image);
			div.appendChild(image);
		}
		DialogUtils.closeDialog();
	}
	function selectImage() {
		var dialogArguments = DialogUtils.getDialogArguments();
		var selectorURL = dialogArguments.editor.getAttachmentSelectorURL("images");
		DialogUtils.openDialog(selectorURL, 640, 400);
	}
	function setUrl(url) {
		document.getElementsByName('url')[0].value = url;
		preview();
	}
	function preview(width, height) {
		var previewDiv = document.getElementById('previewDiv');
		previewDiv.style.width = previewDiv.offsetWidth + "px";
		var url = document.getElementsByName('url')[0].value;
		previewDiv.innerHTML = "";
		if(url=="") {
			document.getElementsByName('width')[0].value = "";
			document.getElementsByName('height')[0].value = "";
			return;
		}
		var image = document.createElement("img");
		previewDiv.appendChild(image);
		image.onload = function() {
			window.imageWidth = image.width;
			window.imageHeight = image.height;
			if(!width && !height) { //没有指定宽度和高度
				document.getElementsByName('width')[0].value = image.width;
				document.getElementsByName('height')[0].value = image.height;
			}
			else if(width && height) { //指定宽度和高度
				//检查是否按比例
				if(width==Math.round(height * window.imageWidth / window.imageHeight)) {
					document.getElementById('lock').src =  document.getElementById('lock').src.replace('/unlocked', '/locked');
				}
				else {
					document.getElementById('lock').src =  document.getElementById('lock').src.replace('/locked', '/unlocked');
				}
				document.getElementsByName('width')[0].value = width;
				document.getElementsByName('height')[0].value = height;
				onSizeChange(true);
			}
			else if(width) {
				document.getElementsByName('width')[0].value = width;
				onSizeChange(true);
			}
			else if(height) {
				document.getElementsByName('height')[0].value = height;
				onSizeChange(false);
			}
		};
		image.src = url;
	}
	function onSizeChange(widthChange) {
		var previewDiv = document.getElementById('previewDiv');
		var image = previewDiv.getElementsByTagName("img")[0];
		if(!image) {
			return;
		}
		var field = document.getElementsByName(widthChange ? 'width' : 'height')[0];
		var size = Number(field.value);
		if(isNaN(size) || size==0) {
			return;
		}
		size = Math.round(size);
		if(field.value!=size) {
			field.value = size;
		}
		var lock = document.getElementById('lock').src.indexOf('/locked')!=-1;
		if(lock && window.imageWidth>0 && window.imageHeight>0) {
			document.getElementsByName(!widthChange ? 'width' : 'height')[0].value = Math.round(size * (widthChange ? window.imageHeight/window.imageWidth : window.imageWidth/window.imageHeight));
		}
		image.width = document.getElementsByName('width')[0].value;
		image.height = document.getElementsByName('height')[0].value;
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<tr>
		<td nowrap="nowrap" align="right">图片地址：</td>
		<td width="100%"><ext:field property="url" onchange="preview()"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">替换文本：</td>
		<td width="100%"><ext:field property="alt"/></td>
	</tr>
</table>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
		<td nowrap="nowrap" width="180px" valign="top">
			<table border="0" cellspacing="0" cellpadding="3px">
				<tr>
					<td nowrap="nowrap" align="right">宽度：</td>
					<td nowrap="nowrap"><ext:field property="width" onchange="onSizeChange(true)" onkeyup="onSizeChange(true)"/></td>
					<td nowrap="nowrap" width="30px" rowspan="2">
						<img id="lock" onclick="this.src=(this.src.indexOf('/unlocked')==-1 ? this.src.replace('/locked', '/unlocked') : this.src.replace('/unlocked', '/locked'));" src="<%=request.getContextPath()%>/jeaf/htmleditor/images/locked.gif"/>
						<img onclick="preview()" src="<%=request.getContextPath()%>/jeaf/htmleditor/images/reset.gif"/>
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" align="right">高度：</td>
					<td><ext:field property="height" onchange="onSizeChange(false)" onkeyup="onSizeChange(false)"/></td>
				</tr>
				<tr>
					<td nowrap="nowrap" align="right">对齐方式：</td>
					<td><ext:field property="align"/></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td nowrap="nowrap" align="right">自动居中：</td>
					<td><ext:field property="center"/></td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</td>
		<td>
			预览：
			<div id="previewDiv" style="margin-top: 3px; border:#909090 1px solid; overflow: auto; height: 400px; width:98%"></div>
		</td>
	</tr>
</table>
</ext:form>