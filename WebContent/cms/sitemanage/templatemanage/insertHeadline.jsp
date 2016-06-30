<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertHeadline">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var format = "";
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj && obj.id=="headline") {
				var siteNames = obj.innerText;
				var properties = obj.getAttribute("urn");
				//解析站点名称
				document.getElementsByName("siteName")[0].value = StringUtils.getPropertyValue(properties, "siteName");
				//解析站点ID
				document.getElementsByName("siteId")[0].value = StringUtils.getPropertyValue(properties, "siteId");
				if(document.getElementsByName("siteId")[0].value!="-1") { //指定栏目
					document.getElementsByName("siteSelect")[1].checked = true;
					siteSelectChanged('other');
				}
				//解析打开方式
				setLinkOpenMode(StringUtils.getPropertyValue(properties, "openMode"));
				//解析格式
				format = StringUtils.getPropertyValue(properties, "format");
			}
			setHeadlineFormat(format);
		}
		function doOk() {
			//站点名称
			var siteName = document.getElementsByName("siteSelect")[0].checked ? "当前站点" : document.getElementsByName("siteName")[0].value;
			if(siteName=='') {
				alert("站点/栏目不能为空");
				return;
			}
			var format = frames["headlineFormat"].document.body.innerHTML;
			if(format=="") {
				alert("格式未配置");
				return;
			}
			//站点ID
			var siteId = document.getElementsByName("siteSelect")[0].checked ? "-1" : document.getElementsByName("siteId")[0].value;
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return false;
				}
			}
			obj.innerHTML = "<头版头条:" + siteName + ">";
			obj.id = "headline";
			obj.setAttribute("urn", "siteName=" + siteName + "&siteId=" + siteId + "&openMode=" + getLinkOpenMode() + "&format=" + StringUtils.encodePropertyValue(format));
			DialogUtils.closeDialog();
		}
		function siteSelectChanged(mode) {
			document.getElementById("selectedSite").style.display = (mode=="current" ? "none" : "");
		}
		//初始化格式设置框
		function setHeadlineFormat(format) {
			var doc = frames["headlineFormat"].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="font-family:宋体; font-size:12px; border-style:none; overflow:auto; margin:3px">');
			doc.write(format);
			doc.write('</body>');
			doc.close();
		}
		//插入头版头条图片
		function insertHeadlineImage() {
			var args = DomUtils.getWindowBookmark(window.frames["headlineFormat"], '插入位置不正确，请重新选择');
			if(!args) {
				return;
			}
			DialogUtils.openDialog(RequestUtils.getContextPath() + '/cms/sitemanage/templatemanage/insertHeadlineImage.shtml', 520, 230, '', args);
		}
		//插入字段
		function openInsertFieldDialog(fieldName, fieldTitle, fieldType) {
			var args = DomUtils.getWindowBookmark(window.frames["headlineFormat"], '插入位置不正确，请重新选择');
			if(!args) {
				return;
			}
			args.editor = dialogArguments.editor;
			var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertField.shtml" +
							"?fieldName=" + fieldName + "&fieldTitle=" + StringUtils.utf8Encode(fieldTitle) + "&fieldType=" + fieldType;
			DialogUtils.openDialog(dialogUrl, 500, 320, '', args);
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">站点/栏目选择：</td>
			<td width="180px" nowrap="nowrap"><ext:field onclick="siteSelectChanged(value)" property="siteSelect"/></td>
			<td id="selectedSite" style="display:none"><ext:field property="siteName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">链接打开方式：</td>
			<td colspan="2"><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
		<tr>
			<td valign="top" style="padding-top:6px" nowrap="nowrap">头版头条格式：</td>
			<td colspan="2">
				<input type="button" class="button" value="插入标题图片" style="width:80px" onmousedown="insertHeadlineImage()">
				<input type="button" class="button" value="插入标题" style="width:58px" onmousedown="openInsertFieldDialog('headlineName', '标题', 'string')">
				<input type="button" class="button" value="插入概述" style="width:58px" onmousedown="openInsertFieldDialog('summarize', '概述', 'string')">
				<iframe name="headlineFormat" id="headlineFormat" class="frame" style="width:100%; height:60px; margin-top:5px"></iframe>
			</td>
		</tr>
	</table>
</ext:form>