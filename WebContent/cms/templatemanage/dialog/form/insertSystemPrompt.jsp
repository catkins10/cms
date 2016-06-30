<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertSystemPrompt">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = dialogArguments.selectedElement;
			var format = "";
			if(obj && obj.id=="systemPrompt") {
				format = obj.getAttribute("urn");
			}
			var doc = window.frames[0].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="margin:3px; font-family:宋体; font-size:12px">');
			doc.write(format);
			doc.write('</body>');
			doc.close();
		};
		function doOk() {
			var format;
			var textarea = document.getElementsByName("source")[0];
			if(textarea.style.display=='none') {
				format = window.frames['promptFormat'].document.body.innerHTML;
			}
			else {
				format = textarea.value;
			}
			if(format=="") {
				alert("格式不能为空");
				return;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, 'a');
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return;
				}
			}
			obj.innerHTML = "&lt;系统提示&gt;";
			obj.id = "systemPrompt";
			obj.setAttribute("urn", format);
			DialogUtils.closeDialog();
		}
		function insertPrompt() {
			var win = window.frames["promptFormat"];
			var range = DomSelection.getRange(win);
			if(range) {
				DomSelection.pasteHTML(win, range, '&lt;提示信息&gt;');
			}
		}
		function insertImage(selectFor) {
			window.bookmark = DomUtils.getWindowBookmark(window.frames["promptFormat"], '插入位置不正确，请重新选择');
			if(!window.bookmark) {
				return;
			}
			DialogUtils.openDialog(dialogArguments.editor.getAttachmentSelectorURL('images'), 640, 400);
		}
		function setUrl(imageURL) {
			var img = window.bookmark.selectedElement;
			if(!img || img.tagName!='IMG') {
				img = DomUtils.createElement(window.frames["promptFormat"], window.bookmark.range, "img");
			}
			if(!img) {
				alert("图片插入失败");
			}
			else {
				img.src = imageURL;
			}
		}
		function showtSource(button) {
			var textarea = document.getElementsByName("source")[0];
			var sourceMode = false;
			var doc = window.frames["promptFormat"].document;
			if(textarea.style.display=='none') {
				textarea.style.width = document.getElementById('promptFormat').offsetWidth + "px";
				textarea.value = doc.body.innerHTML;
				sourceMode = true;
			}
			else {
				doc.body.innerHTML = textarea.value;
			}
			textarea.style.display = sourceMode ? "" : "none";
			document.getElementById("promptFormat").style.display = !sourceMode ? "" : "none";
			button.value = sourceMode ? "回到编辑模式" : "查看源代码";
			document.getElementById("insertFieldButton").disabled = sourceMode;
			document.getElementById("insertImageButton").disabled = sourceMode;
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr>
			<td valign="top" style="padding-top:6px" nowrap="nowrap">提示格式：</td>
			<td>
				<input type="button" class="Button" value="插入提示信息" id="insertFieldButton" style="width:90px" onmousedown="insertPrompt()">
				<input type="button" class="Button" value="插入图片" id="insertImageButton" style="width:58px" onmousedown="insertImage()">
				<input type="button" class="Button" value="查看源代码" style="width:80px" onmousedown="showtSource(this)">
				<br><div style="height:5px;font-size:1px">&nbsp;</div>
				<iframe name="promptFormat" id="promptFormat" frameBorder="0" src="about:blank" class="frame" style="width:100%; height:120px;"></iframe>
				<textarea name="source" style="border-style:1 solid #808080; width:100%; height:120px !important; display:none"></textarea>
			</td>
		</tr>
	</table>
</ext:form>