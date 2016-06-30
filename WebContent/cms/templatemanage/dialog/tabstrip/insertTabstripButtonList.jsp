<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertTabstripButtonList">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			//设置选项卡列表
			var tabstripNames = "";
			var elements = dialogArguments.document.getElementsByTagName("A");
			for(var i=0; i<elements.length; i++) {
				if(elements[i].id=="tabstripBody") {
					tabstripNames += (tabstripNames=="" ? "" : "\0 ") + StringUtils.getPropertyValue(elements[i].getAttribute("urn"), "name");
				}
			}
			DropdownField.setListValues("tabstripName", tabstripNames);
			
			var buttonFormat = "", bodyFormat = "", moreLinkFormat = "";
			var recordListExtendProperties = "";
			var recordListTextContent = "";
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "span");
			if(obj && obj.id && obj.id.indexOf("tabstripButtonList_")==0) {
				var urn = obj.getAttribute("urn");
				recordListExtendProperties = StringUtils.getPropertyValue(urn, "extendProperties");
				recordListTextContent = obj.innerHTML;
			
				//隶属选项卡
				DropdownField.setValue("tabstripName",  obj.id.substring("tabstripButtonList_".length));
				//选项数量
				DropdownField.setValue("buttonCount", StringUtils.getPropertyValue(urn, "buttonCount"));
				//选项间隔
				document.getElementsByName("buttonSpacing")[0].value = StringUtils.getPropertyValue(urn, "buttonSpacing");
	
				//格式配置
				buttonFormat = StringUtils.getPropertyValue(urn, "tabstripButton"); //内容格式
				bodyFormat = StringUtils.getPropertyValue(urn, "tabstripBody"); //内容格式
				moreLinkFormat = StringUtils.getPropertyValue(urn, "moreLink"); //more链接格式
			}
			//显示扩展属性
			try {
				window.frames['dialogExtendFrame'].showExtendProperties(recordListExtendProperties, recordListTextContent)
			}
			catch(e) {
				
			}
			
			var doc = frames["buttonFormat"].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; overflow:hidden; margin:3px; font-family:宋体; font-size:12px">');
			doc.write(buttonFormat);
			doc.write('</body>');
			doc.close();
			
			doc = frames["moreLinkFormat"].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; overflow:hidden; margin:3px; font-family:宋体; font-size:12px">');
			doc.write(moreLinkFormat);
			doc.write('</body>');
			doc.close();
			
			doc = frames["bodyFormat"].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; overflow:hidden; margin:3px; font-family:宋体; font-size:12px">');
			doc.write(bodyFormat);
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
			if(document.getElementsByName("tabstripName")[0].value=='') {
				alert("隶属选项卡不能为空");
				return false;
			}
			var buttonSpacing = Number(document.getElementsByName('buttonSpacing')[0].value);
			if(isNaN(buttonSpacing) || buttonSpacing==0) {
				alert("选项间隔不正确");
				return false;
			}
			var buttonFormat = frames['buttonFormat'].document.body.innerHTML.replace(/<p>/gi, "").replace(/<\/p>/gi, "");
			if(buttonFormat=="") {
				alert("选项格式不能为空");
				return false;
			}
			var bodyFormat = frames['bodyFormat'].document.body.innerHTML;
			if(bodyFormat=="") {
				alert("标签页显示内容不能为空");
				return false;
			}
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "span");
			if(!obj) {
				try {
					obj = DomUtils.createElement(dialogArguments.window, dialogArguments.range, "SPAN");
				}
				catch(e) {
				
				}
				if(!obj) {
					alert('请重新选择插入位置');
					return false;
				}
			}
			obj.innerHTML = "&lt;TAB选项列表:" + document.getElementsByName("tabstripName")[0].value + "&gt;";
			obj.id = "tabstripButtonList_" + document.getElementsByName("tabstripName")[0].value;
			obj.name = "tabstripButton_" + document.getElementsByName("tabstripName")[0].value;
			var urn = (extendProperties!="" ? "extendProperties=" + StringUtils.encodePropertyValue(extendProperties) + "&" : "") + 
					  "applicationName=" +  document.getElementsByName("applicationName")[0].value +
					  "&recordListName=" +  document.getElementsByName("recordListName")[0].value +
					  "&buttonCount=" + document.getElementsByName('buttonCount')[0].value +
					  "&buttonSpacing=" + buttonSpacing +
					  "&tabstripButton=" +  StringUtils.encodePropertyValue(buttonFormat) +
					  "&tabstripBody=" +  StringUtils.encodePropertyValue(bodyFormat) +
					  "&moreLink=" +  StringUtils.encodePropertyValue(frames['moreLinkFormat'].document.body.innerHTML.replace(/<p>/gi, "").replace(/<\/p>/gi, "")) +
					  "&" + generateStylesProperties();
			obj.setAttribute("urn", urn);
			DialogUtils.closeDialog();
		}
		function selectEmbedView(src) { //选择内嵌记录列表
			var args = generateDialogArguments("bodyFormat");
			if(!args) {
				return;
			}
			PopupMenu.popupMenu(DropdownField.getListValues('embedViews'), function(menuItemId, menuItemTitle) {
				var embedView;
				eval('embedView=' + menuItemId);
				var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertRecordList.shtml" +
								"?embedRecordList=true" +
								"&applicationName=" + embedView.applicationName +
								"&recordListName=" + embedView.recordListName +
								(embedView.privateRecordList ? "&privateRecordList=true&recordClassName=" + embedView.recordClassName : "") +
								"&themeType=" + dialogArguments.editor.document.getElementsByName("themeType")[0].value;
				DialogUtils.openDialog(dialogUrl, 780, 400, "", args);
			}, src, '100px');
		}
		function openInsertLinkDialog(frameName) {
			var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertRecordLink.shtml" +
							"?applicationName=" + document.getElementsByName("applicationName")[0].value +
							"&recordListName=" + document.getElementsByName("recordListName")[0].value;
			var args = generateDialogArguments(frameName);
			if(args) {
				DialogUtils.openDialog(dialogUrl, 430, 180, "", args);
			}
		}
		function openInsertFieldDialog() {
			var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertField.shtml" +
							"?applicationName=" + document.getElementsByName("applicationName")[0].value +
							"&recordListName=" + document.getElementsByName("recordListName")[0].value;
			var args = generateDialogArguments("buttonFormat");
			if(args) {
				DialogUtils.openDialog(dialogUrl, 550, 360, "", args);
			}
		}
		function openInsertImageDialog() {
			window.args = generateDialogArguments("buttonFormat");
			if(window.args) {
				DialogUtils.openDialog(dialogArguments.editor.getAttachmentSelectorURL('images', 'insertButtonImage("{URL}")'), 640, 400);
			}
		}
		function insertButtonImage(imageURL) {
			var img = window.args.selectedElement;
			if(!img || img.tagName!='IMG') {
				img = DomUtils.createElement(window.frames["buttonFormat"], window.args.range, "img");
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
			return "鼠标经过时|mouseOver\0选中|selected\0未选中|unselected";
		}
		//获取默认的样式,由调用者实现
		function getDefaultStyle(styleName) {
			
		}
		//获取页面元素属性(urn),由调用者实现
		function getPageElementProperties() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "span");
			if(obj && obj.id && obj.id.indexOf("tabstripButtonList_")==0) {
				return obj.getAttribute("urn");
			}
		}
	</script>
	<ext:notEmpty property="templateExtendURL">
		<iframe name="dialogExtendFrame" id="dialogExtendFrame" src="<%=request.getContextPath()%><ext:write property="templateExtendURL"/>" frameborder="0" style="width:100%;height: 0px"></iframe>
	</ext:notEmpty>
	<table id="mainTable" border="0" width="100%" cellspacing="5" cellpadding="0px">
		<tr>
			<td nowrap="nowrap" align="right">隶属选项卡：</td>
			<td width="33%"><ext:field property="tabstripName"/></td>
			<td nowrap="nowrap" align="right">选项数量：</td>
			<td width="33%"><ext:field property="buttonCount"/></td>
			<td nowrap="nowrap" align="right">选项间隔(像素)：</td>
			<td width="33%"><ext:field property="buttonSpacing"/></td>
		</tr>
		<tr>
			<td valign="top" style="padding-top:28px" align="right">选项格式：</td>
			<td colspan="5">
				<input type="button" class="Button" value="插入字段" id="insertFieldButton" style="width:58px" onmousedown="openInsertFieldDialog()">
				<input type="button" class="Button" value="插入图片" id="insertImageButton" style="width:58px" onmousedown="openInsertImageDialog()">
				<iframe name="buttonFormat" id="recordFormat" frameBorder="0" src="about:blank" class="frame" style="width:100%; height:20px; margin-top:5px"></iframe>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top:28px" align="right">“更多”链接地址：</td>
			<td colspan="5">
				<input type="button" class="Button" value="插入链接" style="width:60px" onmousedown="openInsertLinkDialog('moreLinkFormat')">
				<iframe id="moreLinkFormat" name="moreLinkFormat" src="about:blank" class="frame" style="width:100%; height:20px; margin-top:5px;" frameborder="0"></iframe>
			</td>
		</tr>
		<tr>
			<td valign="top" style="padding-top:28px" align="right">标签页显示内容：</td>
			<td colspan="5">
				<ext:field style="display:none" property="embedViews"/>
				<input type="button" class="button" value="插入列表▼" id="insertEmbedViewButton" style="width:68px" onmousedown="selectEmbedView(this);return false;">
				<input type="button" class="Button" value="插入链接" style="width:60px" onmousedown="openInsertLinkDialog('bodyFormat')">
				<iframe id="bodyFormat" name="bodyFormat" src="about:blank" class="frame" style="width:100%; height:60px; margin-top:5px;" frameborder="0"></iframe>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top: 10px" align="right">样式配置：</td>
			<td nowrap="nowrap" style="padding-top: 5px" colspan="5"><jsp:include page="/cms/templatemanage/dialog/template/customStyle.jsp"/></td>
		</tr>
	</table>
</ext:form>