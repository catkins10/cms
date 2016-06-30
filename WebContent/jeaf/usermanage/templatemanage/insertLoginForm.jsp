<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertLoginForm">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			//从form.target解析站点ID和名称列表
			var form = DomUtils.getParentNode(dialogArguments.selectedElement, 'form');
			var redirectLinkFormat = '';
			if(form && form.id=="loginForm") {
				//从form.action解析搜索站点
				var redirectMode = StringUtils.getPropertyValue(form.action, "redirectMode");
				if(redirectMode=="custom") {
					redirectLinkFormat = StringUtils.getPropertyValue(form.action, "redirect");
					document.getElementsByName("redirectMode")[1].checked = true;
		 			document.getElementById("setRedirectLink").style.display = "";
		 		}
			}
			var doc = frames["linkFormat"].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="overflow:hidden; margin:3px; font-family:宋体; font-size:12px">');
			doc.write(redirectLinkFormat);
			doc.write('</body>');
			doc.close();
		}
		function doOk() {
			var redirectModes = document.getElementsByName("redirectMode");
			var extend = "redirectMode=" + (redirectModes[0].checked ? "default" : "custom");
			if(redirectModes[1].checked) {
				extend += "&redirect=" + StringUtils.encodePropertyValue(frames['linkFormat'].document.body.innerHTML);
			}
			dialogArguments.editor.window.setTimeout("FormCommand.doInsertForm(\"jeaf/usermanage\", \"loginForm\", \"登录\", \"" + extend.replace(new RegExp("\"", "g"), "\\\"") + "\")", 10);
			DialogUtils.closeDialog();
		}
		function redirectModeChanged(mode) {
			document.getElementById("setRedirectLink").style.display = (mode=="default" ? "none" : "");
			if(mode!="default") {
				var frame = window.frames["linkFormat"];
				DomSelection.selectRange(frame, DomSelection.createRange(frame.document, frame.document.body));
			}
			DialogUtils.adjustDialogSize();
		}
		function insertLink() { //插入链接
			window.args = DomUtils.getWindowBookmark(window.frames["linkFormat"], '插入位置不正确，请重新选择');
			if(!window.args) {
				return;
			}
			window.args.editor = dialogArguments.editor;
			selectSiteLink(550, 360, "doInsertLink('{id}', '{name}', '{dialogURL}', '{url}', '{applicationTitle}')");
		}
		function doInsertLink(id, title, dialogURL, url, applicationTitle) {
			DialogUtils.openDialog(RequestUtils.getContextPath() + dialogURL, 455, 180, '', window.args);
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td nowrap="nowrap">登录后显示的页面：</td>
			<td><ext:field property="redirectMode" onclick="redirectModeChanged(value)"/></td>
		</tr>
		<tr id="setRedirectLink" style="display:none;">
			<td></td>
			<td>
				<iframe id="linkFormat" name="linkFormat" src="about:blank" class="frame" style="width:180px; height:20px;" frameborder="0" align="top"></iframe>&nbsp;
				<input type="button" class="button" value="插入链接" style="width:68px" onmousedown="insertLink()">&nbsp;
			</td>
		</tr>
	</table>
</ext:form>