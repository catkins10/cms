<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/openInsertNavigatorItemList">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/navigator/js/navigator.js"></script>
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "span");
			var itemFormat = "";
			if(obj && obj.id && obj.id.indexOf("navigatorItemList_")==0) {
				var urn = obj.getAttribute("urn");
				//显示扩展属性
				try {
					window.frames['dialogExtendFrame'].showExtendProperties(StringUtils.getPropertyValue(urn, "extendProperties"), obj.innerHTML)
				}
				catch(e) {
					
				}	
				//隶属导航栏
				document.getElementsByName("navigatorName")[0].value = obj.id.substring("navigatorItemList_".length);
				//选项数量
				document.getElementsByName("itemCount")[0].value = StringUtils.getPropertyValue(urn, "itemCount");
				//选项间隔
				document.getElementsByName("itemSpacing")[0].value = StringUtils.getPropertyValue(urn, "itemSpacing");
				//条目格式
				itemFormat = StringUtils.getPropertyValue(urn, "itemFormat");
				//链接方式
				setLinkOpenMode(StringUtils.getPropertyValue(urn, "linkOpenMode"));
				//链接地址
				DropdownField.setValue("recordLink", StringUtils.getPropertyValue(urn, "linkTitle"));
			}
			else {
				var navigator = getNavigator(DomUtils.getParentNode(dialogArguments.selectedElement, "div,td,span,ul"));
				document.getElementsByName("navigatorName")[0].value = StringUtils.getPropertyValue(navigator.getAttribute("urn"), "name");
			}
			var doc = frames["itemFormat"].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; overflow:hidden; margin:3px; font-family:宋体; font-size:12px">');
			doc.write(itemFormat);
			doc.write('</body>');
			doc.close();
		};
		function doOk() {
			//保存扩展属性
			var extendProperties = "";
			try {
				extendProperties = window.frames['dialogExtendFrame'].getExtendPropertiesAsText();
			}
			catch(e) {
				
			}
			if(extendProperties=="ERROR") {
				return false;
			}
			if(document.getElementsByName("navigatorName")[0].value=='') {
				alert("隶属导航栏不能为空");
				return false;
			}
			var itemSpacing = Number(document.getElementsByName('itemSpacing')[0].value);
			if(isNaN(itemSpacing) || itemSpacing==0) {
				alert("项目间隔不正确");
				return false;
			}
			var itemFormat = frames['itemFormat'].document.body.innerHTML.replace(/<p>/gi, "").replace(/<\/p>/gi, "");
			if(itemFormat=="") {
				alert("显示格式不能为空");
				return false;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "span");
			if(!obj || !obj.id || obj.id.indexOf("navigatorItemList_")==-1) {
				try {
					obj = DomUtils.createElement(dialogArguments.window, dialogArguments.range, "span");
				}
				catch(e) {

				}
				if(!obj) {
					alert('请重新选择插入位置');
					return;
				}
			}
			obj.innerHTML = "&lt;导航栏项目列表:" + document.getElementsByName("navigatorName")[0].value + "&gt;";
			obj.name = obj.id = "navigatorItemList_" + document.getElementsByName("navigatorName")[0].value;
			var linkTitle = document.getElementsByName("recordLink")[0].value;
			var urn = (extendProperties!="" ? "extendProperties=" + StringUtils.encodePropertyValue(extendProperties) + "&" : "") + 
					  "applicationName=" +  document.getElementsByName("applicationName")[0].value +
					  "&recordListName=" +  document.getElementsByName("recordListName")[0].value +
					  "&linkOpenMode=" + getLinkOpenMode() +
					  (linkTitle!="" ? "&linkTitle=" + linkTitle : "") +
					  "&itemCount=" + document.getElementsByName('itemCount')[0].value +
					  "&itemSpacing=" + itemSpacing +
					  "&itemFormat=" + StringUtils.encodePropertyValue(itemFormat);
			urn += "&" +　generateStylesProperties();
			obj.setAttribute("urn", urn);
			DialogUtils.closeDialog();
		}
		function openInsertFieldDialog() {
			var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertField.shtml" +
							"?applicationName=" + document.getElementsByName("applicationName")[0].value +
							"&recordListName=" + document.getElementsByName("recordListName")[0].value +
							"&linkable=true";
			var args = generateDialogArguments("itemFormat");
			if(args) {
				DialogUtils.openDialog(dialogUrl, 550, 360, "", args);
			}
		}
		function openInsertImageDialog() {
			window.args = generateDialogArguments("itemFormat");
			if(window.args) {
				DialogUtils.openDialog(dialogArguments.editor.getAttachmentSelectorURL('images', 'insertButtonImage("{URL}")'), 640, 400);
			}
		}
		function insertButtonImage(imageURL) {
			var img = window.args.selectedElement;
			if(!img || img.tagName!='IMG') {
				img = DomUtils.createElement(window.frames["itemFormat"], window.args.range, "img");
			}
			if(!img) {
				alert("图片插入失败");
			}
			else {
				img.src = imageURL;
			}
		}
		function generateDialogArguments(frameName) {
			var args = DomUtils.getWindowBookmark(window.frames[frameName], '插入位置不正确，请重新选择');
			if(!args) {
				return;
			}
			args.editor = dialogArguments.editor;
			return args;
		}
		//获取样式名称列表,格式:样式1名称|标题,样式2名称|标题,由调用者实现
		function getStyleNames() {
			return 	"导航栏链接项目：未选中|linkItem\0" +
					"导航栏链接项目：选中|linkItemSelected\0" +
					"导航栏菜单项目：未选中|menuItem\0" +
					"导航栏菜单项目：选中|menuItemSelected\0" +
					"导航栏菜单项目：未选中时的下拉按钮|menuItemDropdown\0" +
					"导航栏菜单项目：选中时的下拉按钮|menuItemSelectedDropdown";
		}
		//获取默认的样式,由调用者实现
		function getDefaultStyle(styleName) {
			
		}
		//获取页面元素属性(urn),由调用者实现
		function getPageElementProperties() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "span");
			if(obj && obj.id && obj.id.indexOf("navigatorItemList_")==0) {
				return obj.getAttribute("urn");
			}
		}
	</script>
	<ext:notEmpty property="templateExtendURL">
		<iframe name="dialogExtendFrame" id="dialogExtendFrame" src="<%=request.getContextPath()%><ext:write property="templateExtendURL"/>" frameborder="0" style="width:100%;height: 0px"></iframe>
	</ext:notEmpty>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<tr valign="middle">
			<td nowrap="nowrap" align="right">隶属导航栏：</td>
			<td width="100%" colspan="3"><ext:field property="navigatorName" readonly="true"/></td>
		</tr>
		<tr valign="middle">
			<td nowrap="nowrap" align="right">显示项目数：</td>
			<td width="50%"><ext:field property="itemCount"/></td>
			<td nowrap="nowrap" align="right">项目间隔(像素)：</td>
			<td width="50%"><ext:field property="itemSpacing"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">链接方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
			<td nowrap="nowrap" align="right">链接地址：</td>
			<td><ext:field property="recordLink"/></td>
		</tr>
		<tr>
			<td valign="top" style="padding-top:28px" align="right">显示格式：</td>
			<td colspan="3">
				<input type="button" class="button" value="插入字段" id="insertFieldButton" style="width:58px" onmousedown="openInsertFieldDialog()">
				<input type="button" class="button" value="插入图片" id="insertImageButton" style="width:58px" onmousedown="openInsertImageDialog()">
				<iframe name="itemFormat" id="recordFormat" frameBorder="0" src="about:blank" class="frame" style="width:100%; height:20px; margin-top: 5px;"></iframe>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top: 3px" align="right">样式配置：</td>
			<td nowrap="nowrap" colspan="3"><jsp:include page="/cms/templatemanage/dialog/template/customStyle.jsp"/></td>
		</tr>
	</table>
</ext:form>