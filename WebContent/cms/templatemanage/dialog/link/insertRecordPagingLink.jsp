<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertApplicationLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			document.title = '插入<%="true".equals(request.getParameter("nextRecord")) ? "下" : "上"%>一篇链接';
			var linkFormat = "";
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj) {
				setLinkOpenMode(obj.target); //解析打开方式
				if(obj.id=="nextRecordLink" || obj.id=="previousRecordLink") {
					linkFormat = obj.getAttribute("urn");
				}
				else {
					linkFormat = obj.innerHTML;
				}
			}
			if(linkFormat=="") {
				linkFormat = dialogArguments.range.htmlText;
				if(!linkFormat || linkFormat=="") {
					linkFormat = "<%="true".equals(request.getParameter("nextRecord")) ? "下" : "上"%>一篇";
				}
			}
			var doc = frames["linkFormat"].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; overflow:hidden; margin:3px; font-family:宋体; font-size:12px">');
			doc.write(linkFormat);
			doc.write('</body>');
			doc.close();
		}
		function doOk() {
			var linkFormat = getLinkFormat();
			if(linkFormat=="") {
				alert("显示格式未配置");
				return false;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return;
				}
			}
			//打开方式
			obj.target = getLinkOpenMode();
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = "<%="true".equals(request.getParameter("nextRecord")) ? "下" : "上"%>一篇";
			}
			obj.setAttribute("urn", linkFormat);
			obj.id = '<%="true".equals(request.getParameter("nextRecord")) ? "nextRecordLink" : "previousRecordLink"%>';
			DialogUtils.closeDialog();
		}
		//打开插入字段对话框
		function openInsertFieldDialog() {
			var args = DomUtils.getWindowBookmark(window.frames["linkFormat"], '插入位置不正确，请重新选择');
			if(!args) {
				return;
			}
			args.editor = dialogArguments.editor;
			var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertField.shtml" +
							"?applicationName=<%=request.getParameter("applicationName")%>" +
							"&pageName=<%=request.getParameter("pageName")%>" +
							"&linkable=false";
			DialogUtils.openDialog(dialogUrl, 550, 360, '', args);
		}
		function selectImage() {
			window.range = DomSelection.getRange(window.frames["linkFormat"]);
			if(!window.range) {
				return;
			}
			window.selectedElement = DomSelection.getSelectedElement(window.range);
			DialogUtils.openDialog(dialogArguments.editor.getAttachmentSelectorURL('images'), 640, 400);
		}
		function setUrl(imageURL) {
			var img = window.selectedElement;
			if(!img || img.tagName!='IMG') {
				img = DomUtils.createElement(window.frames["linkFormat"], window.range, "img");
			}
			if(!img) {
				alert("图片插入失败");
			}
			else {
				img.src = imageURL;
			}
		}
		function viewSource(button) {
			var sourceMode = false;
			var divSource = document.getElementById("divLinkFormatSource");
			if(divSource.style.display=='none') {
				document.getElementsByName("linkFormatSource")[0].value = frames["linkFormat"].document.body.innerHTML;
				sourceMode = true;
			}
			else {
				frames["linkFormat"].document.body.innerHTML = document.getElementsByName("linkFormatSource")[0].value;
			}
			button.value = sourceMode ? "回到编辑模式" : "查看源代码";
			divSource.style.display = sourceMode ? "" : "none";
			document.getElementById('linkFormat').style.display = !sourceMode ? "" : "none";
			document.getElementById('insertFieldButton').disabled = sourceMode;
			document.getElementById('insertImageButton').disabled = sourceMode;
		}
		function getLinkFormat() {
			var divSource = document.getElementById("divLinkFormatSource");
			if(divSource.style.display=='none') {
				return frames["linkFormat"].document.body.innerHTML;
			}
			else {
				return document.getElementsByName("linkFormatSource")[0].value;
			}
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top:6px;">显示格式：</td>
			<td>
				<input type="button" class="Button" value="插入字段" id="insertFieldButton" style="width:58px" onmousedown="openInsertFieldDialog()">
				<input type="button" class="Button" value="插入图片" id="insertImageButton" style="width:58px" onmousedown="selectImage('recordFormat')">
				<input type="button" class="button" value="查看源代码" onmousedown="viewSource(this)">
				<div id="divLinkFormatSource" style="display:none;  margin-top:3px">
					<ext:field property="linkFormatSource" style="width:100%; height:100px !important; word-break:break-all;"/>
				</div>
				<iframe id="linkFormat" name="linkFormat" class="frame" style="width:100%; height:100px; margin-top: 3px" frameborder="0"></iframe>
			</td>
		</tr>
	</table>
</ext:form>