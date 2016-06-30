<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/openInsertSubPage">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/template/js/insertPredefinedPage.js"></script>
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.subPageName = '<ext:write property="subPageName"/>';
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, 'a');
<%			if(request.getMethod().equalsIgnoreCase("get")) { %>
				if(obj && obj.id=="subPage") {
					DropdownField.setValue("subPageName", StringUtils.getPropertyValue(obj.getAttribute("urn"), "pageName"));
					if(window.subPageName!=document.getElementsByName("subPageName")[0].value) {
						FormUtils.doAction('insertSubPage');
						return;
					}
				}
<%			} %>
			if(window.frames['pagePreview']) { //有预置页面
				previewPage();
				return;
			}
			if(!obj || obj.id!="subPage") {
				return;
			}
			var properties = obj.getAttribute("urn");
			//设置宽度
			DropdownField.setValue("widthMode", StringUtils.getPropertyValue(properties, "widthMode"));
			document.getElementsByName("width")[0].value = StringUtils.getPropertyValue(properties, "width");
			document.getElementsByName("horizontalScroll")[0].checked = "true"==StringUtils.getPropertyValue(properties, "horizontalScroll");
			widthModeChanged();
			//设置高度
			DropdownField.setValue("heightMode", StringUtils.getPropertyValue(properties, "heightMode"));
			document.getElementsByName("height")[0].value = StringUtils.getPropertyValue(properties, "height");
			document.getElementsByName("verticalScroll")[0].checked = "true"==StringUtils.getPropertyValue(properties, "verticalScroll");
			heightModeChanged();
			//设置样式
			var cssUrl = StringUtils.getPropertyValue(properties, "cssUrl");
			if(cssUrl!="") {
				DropdownField.setValue("cssUrl", cssUrl);
			}
		};
		function doOk() {
			if(frames['pagePreview']) {
				dialogArguments.editor.saveUndoStep();
				insertPredefinedPage('subPage', document.getElementsByName("cssUrl")[0].value);
				DialogUtils.closeDialog();
				return;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					alert('请重新选择插入的位置');
					return;
				}
			}
			var widthMode = document.getElementsByName("widthMode")[0].value;
			var heightMode = document.getElementsByName("heightMode")[0].value;
			var cssUrl = DropdownField.getSelectedIndex("cssUrl")==0 ? "" : document.getElementsByName("cssUrl")[0].value;
			obj.innerHTML = "&lt;系统预置页面:<ext:field property="subPageName" writeonly="true"/>&gt;";
			obj.id = "subPage";
			obj.setAttribute("urn", "pageName=" + document.getElementsByName("subPageName")[0].value + 
				   	   		 "&widthMode=" + widthMode + 
				   	   		 "&width=" + document.getElementsByName("width")[0].value.replace("%", "") + 
				       		 "&heightMode=" + heightMode + 
					   		 "&height=" + document.getElementsByName("height")[0].value.replace("%", "") + 
				       		 "&cssUrl=" + StringUtils.encodePropertyValue(cssUrl) +
				   	   		 (widthMode!="auto" && document.getElementsByName("horizontalScroll")[0].checked ? "&horizontalScroll=true" : "") +
				   	   		 (heightMode!="auto" && document.getElementsByName("verticalScroll")[0].checked ? "&verticalScroll=true" : ""));
			DialogUtils.closeDialog();
		}
		function subPageChange() {
			if(window.subPageName!=document.getElementsByName("subPageName")[0].value) {
				FormUtils.doAction('insertSubPage');
			}
		}
		function widthModeChanged() {
			var mode = document.getElementsByName("widthMode")[0].value;
			document.getElementById("widthSpan").style.visibility = (mode=="auto" ? "hidden" : "visible");
		}
		function heightModeChanged() {
			var mode = document.getElementsByName("heightMode")[0].value;
			document.getElementById("heightSpan").style.visibility = (mode=="auto" ? "hidden" : "visible");
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col width="50px" align="right">
		<col>
		<col width="80px">
		<tr>
			<td nowrap="nowrap" align="right">名称：</td>
			<td colspan="2" width="100%"><ext:field property="subPageName" onchange="subPageChange()"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">CSS：</td>
			<td colspan="2">
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td width="100%"><ext:field onchange="previewPage(true)" property="cssUrl"/></td>
						<td nowrap="nowrap" style="padding-left: 5px;"><input type="button" class="button" value="自定义CSS" style="width: 70px" onclick="customCssFile(document.getElementsByName('cssUrl')[0].value)"></td>
					</tr>
				</table>
			</td>
		</tr>
		<ext:notEmpty property="predefinedPage">
			<tr>
				<td valign="top" nowrap="nowrap" align="right">预览：</td>
				<td colspan="2"><iframe name="pagePreview" frameBorder="0" class="frame" style="width:100%; height:220px;"></iframe></td>
			</tr>
		</ext:notEmpty>
		<ext:empty property="predefinedPage">
			<tr>
				<td nowrap="nowrap">宽度：</td>
				<td width="160px" nowrap="nowrap"><ext:field property="widthMode" onchange="widthModeChanged()"/></td>
				<td id="widthSpan" style="visibility:hidden" width="100%">
					<table border="0" width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<td width="100%"><ext:field property="width"/>
							<td nowrap="nowrap"><ext:field property="horizontalScroll"/></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap">高度：</td>
				<td width="160px" nowrap="nowrap"><ext:field property="heightMode" onchange="heightModeChanged()"/></td>
				<td id="heightSpan" style="visibility:hidden" width="100%">
					<table border="0" width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<td width="100%"><ext:field property="height"/>
							<td nowrap="nowrap"><ext:field property="verticalScroll"/></td>
						</tr>
					</table>
				</td>
			</tr>
		</ext:empty>
	</table>
</ext:form>