<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertComments">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			var citationFormat = StringUtils.getPropertyValue(recordListProperties, "citation");
			var doc = frames["citationFormat"].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style: none; background-color:#ffffff; overflow:hidden; margin:3px; font-family:宋体; font-size:12px">');
			doc.write(citationFormat);
			doc.write('</body>');
			doc.close();
		}
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			var format;
			var teatarea = document.getElementsByName("source")[0];
			if(teatarea.style.display=='none') {
				var doc = frames['citationFormat'].document;
				format = doc.body.innerHTML;
			}
			else {
				format = teatarea.value;
			}
			if(format=="") {
				return "";
			}
			return "citation=" + StringUtils.encodePropertyValue(format);
		}
		function openInsertFieldDialog() {
			var args = DomUtils.getWindowBookmark(window.frames["citationFormat"], '插入位置不正确，请重新选择');
			if(!args) {
				return;
			}
			args.editor = editor;
			var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertField.shtml" +
							"?applicationName=cms/comment" +
							"&recordListName=comments";
			DialogUtils.openDialog(dialogUrl, 550, 360, '', args);
		}
		function insertImage(selectFor) {
			window.args = DomUtils.getWindowBookmark(window.frames["citationFormat"], '插入位置不正确，请重新选择');
			if(!window.args) {
				return;
			}
			DialogUtils.openDialog(editor.getAttachmentSelectorURL('images'), 640, 400);
		}
		function setUrl(imageURL) {
			var img = window.args.selectedElement;
			if(!img || img.tagName!='IMG') {
				img = DomUtils.createElement(window.frames["citationFormat"], window.args.range, "img");
			}
			if(!img) {
				alert("图片插入失败");
			}
			else {
				img.src = imageURL;
			}
		}
		function showtSource(button) {
			var teatarea = document.getElementsByName("source")[0];
			var sourceMode = false;
			var doc = frames["citationFormat"].document;
			if(teatarea.style.display=='none') {
				teatarea.value = doc.body.innerHTML;
				sourceMode = true;
			}
			else {
				doc.body.innerHTML = teatarea.value;
			}
			teatarea.style.display = sourceMode ? "" : "none";
			document.getElementById("citationFormat").style.display = !sourceMode ? "" : "none";
			button.value = sourceMode ? "回到编辑模式" : "查看源代码";
			document.getElementById("insertFieldButton").disabled = sourceMode;
			document.getElementById("insertImageButton").disabled = sourceMode;
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<tr>
			<td align="right" valign="top" style="line-height:1.3em" nowrap="nowrap">被引用时&nbsp;&nbsp;<br>记录格式：</td>
			<td width="100%">
				<input type="button" class="Button" value="插入字段" id="insertFieldButton" style="width:58px" onmousedown="openInsertFieldDialog()">
				<input type="button" class="Button" value="插入图片" id="insertImageButton" style="width:58px" onmousedown="insertImage()">
				<input type="button" class="Button" value="查看源代码" style="width:80px" onmousedown="showtSource(this)">
				<iframe name="citationFormat" id="citationFormat" frameBorder="0" src="about:blank" style="border:1 solid #808080; margin-top:5px; width:100%; height:80px;"></iframe>
				<textarea name="source" style="margin-top:5px; width:100%; height:80px !important; display:none"></textarea>
			</td>
		</tr>
	</table>
</ext:form>