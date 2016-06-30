<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertTabstripButton">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>
	<script>
		var buttonTagNames = "div,td,span,li,a,img";
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, buttonTagNames);
			if(!obj) {
				return;
			}
			//设置选项卡列表
			var tabstripNames = "";
			var elements = dialogArguments.document.getElementsByTagName("A");
			for(var i=0; i<elements.length; i++) {
				if(elements[i].id=="tabstripBody") {
					tabstripNames += (tabstripNames=="" ? "" : "\0 ") + StringUtils.getPropertyValue(elements[i].getAttribute("urn"), "name");
				}
			}
			DropdownField.setListValues("tabstripName", tabstripNames);
			
			var bodyFormat = "";
			var moreLinkFormat = "";
			if(obj && obj.id && obj.id.indexOf("tabstripButton_")==0) {
				var urn = obj.getAttribute("urn");
				if(urn) {
					bodyFormat = StringUtils.getPropertyValue(urn, "tabstripBody"); //内容格式
					moreLinkFormat = StringUtils.getPropertyValue(urn, "moreLink"); //more链接格式
				}
				DropdownField.setValue("tabstripName", obj.id.substring("tabstripButton_".length)); //隶属选项卡
			}
			//设置选项HTML
			var doc = frames["buttonHTML"].document;
			doc.open();
			doc.write('<body style="border-style:none; overflow:hidden; margin:3px; font-family:宋体; font-size:12px">');
			doc.write(obj.innerHTML);
			doc.write('</body>');
			doc.close();
	
			//设置“更多”的格式
			doc = frames["moreLinkFormat"].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; overflow:hidden; margin:3px; font-family:宋体; font-size:12px">');
			doc.write(moreLinkFormat);
			doc.write('</body>');
			doc.close();
			
			//设置内容的格式
			doc = frames["bodyFormat"].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; overflow:hidden; margin:3px; font-family:宋体; font-size:12px">');
			doc.write(bodyFormat);
			doc.write('</body>');
			doc.close();
		}
		function doOk() {
			var butttonElement = DomUtils.getParentNode(dialogArguments.selectedElement, buttonTagNames);
			if(!butttonElement) {
				alert("请重新选择TAB选项位置");
				return false;
			}
			if(document.getElementsByName("tabstripName")[0].value=='') {
				alert("隶属选项卡不能为空");
				return false;
			}
			var bodyFormat = "";
			var textarea = document.getElementsByName("source")[0];
			if(textarea.style.display=='none') {
				bodyFormat = frames['bodyFormat'].document.body.innerHTML;
			}
			else {
				bodyFormat = textarea.value;
			}
			if(bodyFormat=="") {
				alert("标签页显示内容不能为空");
				return false;
			}
			dialogArguments.editor.saveUndoStep();
			butttonElement.id = butttonElement.name = "tabstripButton_" + document.getElementsByName("tabstripName")[0].value;
			butttonElement.title = "TAB选项:" + document.getElementsByName("tabstripName")[0].value;
			var urn = "&tabstripBody=" + StringUtils.encodePropertyValue(bodyFormat) +
					  "&moreLink=" +  StringUtils.encodePropertyValue(frames['moreLinkFormat'].document.body.innerHTML.replace(/<p>/gi, "").replace(/<\/p>/gi, "")) +
					  "&" + generateStylesProperties();
			butttonElement.setAttribute("urn", urn);
			DialogUtils.closeDialog();
		}
		function insertRecordList() { //插入记录列表
			window.args = DomUtils.getWindowBookmark(window.frames["bodyFormat"], '插入位置不正确，请重新选择');
			if(!window.args) {
				return;
			}
			window.args.editor = dialogArguments.editor;
			selectView(550, 360, "doInsertRecordList('{id}'.split('__')[0], '{id}'.split('__')[1], '{name}', '{private}', '{recordClassName}')");
		}
		function doInsertRecordList(applicationName, recordListName, title, privateRecordList, recordClassName) {
			var themeType = dialogArguments.editor.document.getElementsByName("themeType")[0];
			var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertRecordList.shtml" +
							"?recordListName=" + recordListName +
							(applicationName!="" ? "&applicationName=" + applicationName : "") +
							"&themeType=" + (themeType ?  themeType.value : "") +
							(privateRecordList=="true" ? "&privateRecordList=true&recordClassName=" + recordClassName : "");
			DialogUtils.openDialog(dialogUrl, 720, 400, "", window.args);
		}
		function editRecordList() { //编辑记录列表
			window.args = DomUtils.getWindowBookmark(window.frames["bodyFormat"], '插入位置不正确，请重新选择');
			window.args.editor = dialogArguments.editor;
			var obj = window.args.selectedElement;
			if(!obj) {
				alert("未选定记录列表");
				return;
			}
			if(obj.id!='recordList') {
				alert("未选定记录列表");
				return;
			}
			var applicationName = '';
			var recordListName = '';
			var privateRecordList = '';
			var recordClassName = '';
			var urn = obj.getAttribute("urn");
			var index = urn.indexOf('applicationName=');
			if(index!=-1) {
				index += 'applicationName='.length;
				var indexEnd = urn.indexOf("&", index);
				applicationName = indexEnd==-1 ?  urn.substring(index) : urn.substring(index, indexEnd);
			}
			index = urn.indexOf('recordListName=');
			if(index!=-1) {
				index += 'recordListName='.length;
				var indexEnd = urn.indexOf("&", index);
				recordListName = indexEnd==-1 ?  urn.substring(index) : urn.substring(index, indexEnd);
			}
			index = urn.indexOf('privateRecordList=');
			if(index!=-1) {
				index += 'privateRecordList='.length;
				var indexEnd = urn.indexOf("&", index);
				privateRecordList = indexEnd==-1 ?  urn.substring(index) : urn.substring(index, indexEnd);
			}
			index = urn.indexOf('recordClassName=');
			if(index!=-1) {
				index += 'recordClassName='.length;
				var indexEnd = urn.indexOf("&", index);
				recordClassName = indexEnd==-1 ?  urn.substring(index) : urn.substring(index, indexEnd);
			}
			if(recordListName=='') {
				alert("记录列表格式不正确");
				return;
			}
			doInsertRecordList(applicationName, recordListName, obj.innerHTML.replace(/&lt;/gi, "").replace(/&gt;/gi, ""), privateRecordList, recordClassName);
		}
		function insertLink(isBodyFormat) { //插入链接
			window.args = DomUtils.getWindowBookmark(window.frames[isBodyFormat ? "bodyFormat" : "moreLinkFormat"], '插入位置不正确，请重新选择');
			if(!window.args) {
				return;
			}
			window.args.editor = dialogArguments.editor;
			selectSiteLink(550, 360, "doInsertLink('{id}', '{name}', '{dialogURL}', '{url}', '{applicationTitle}')");
		}
		function doInsertLink(id, title, dialogURL, url, applicationTitle) {
			DialogUtils.openDialog(RequestUtils.getContextPath() + dialogURL, 455, 180, "", window.args);
		}
		//获取样式名称列表,格式:样式1名称|标题,样式2名称|标题,由调用者实现
		function getStyleNames() {
			return "选中|selected\0未选中|unselected\0鼠标经过时|mouseOver";
		}
		//获取默认的样式,由调用者实现
		function getDefaultStyle(styleName) {
			//获取同一个选项卡的其他选项
			var tanstripButtonElement = dialogArguments.document.getElementById("tabstripButton_" + document.getElementsByName('tabstripName')[0].value);
			if(tanstripButtonElement) {
				return StringUtils.getPropertyValue(tanstripButtonElement.getAttribute("urn"), styleName + "Style");
			}
		}
		//获取页面元素属性(urn),由调用者实现
		function getPageElementProperties() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, buttonTagNames);
			if(obj && obj.id && obj.id.indexOf("tabstripButton_")==0) {
				return obj.getAttribute("urn");
			}
		}
		//查看源代码
		function showtSource(button) {
			var textarea = document.getElementsByName("source")[0];
			var sourceMode = false;
			var doc = frames["bodyFormat"].document;
			if(textarea.style.display=='none') {
				textarea.value = doc.body.innerHTML;
				sourceMode = true;
			}
			else {
				doc.body.innerHTML = textarea.value;
			}
			textarea.style.width = document.getElementById("bodyFormat").offsetWidth + "px";
			textarea.style.display = sourceMode ? "" : "none";
			document.getElementById("bodyFormat").style.display = !sourceMode ? "" : "none";
			button.value = sourceMode ? "回到编辑模式" : "查看源代码";
			document.getElementById("insertRecordListButton").disabled = sourceMode;
			document.getElementById("editRecordListButton").disabled = sourceMode;
			document.getElementById("insertLinkButton").disabled = sourceMode;
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top: 10px">选项：</td>
			<td nowrap="nowrap" style="padding-top: 5px">
				<iframe id="buttonHTML" name="buttonHTML" src="about:blank" class="frame" style="width:100%; height:20px;" frameborder="0"></iframe>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">隶属选项卡：</td>
			<td><ext:field property="tabstripName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top: 10px">样式配置：</td>
			<td nowrap="nowrap" style="padding-top: 5px"><jsp:include page="/cms/templatemanage/dialog/template/customStyle.jsp"/></td>
		</tr>
		<tr>
			<td valign="top" style="padding-top:30px" nowrap="nowrap">“更多”链接地址：</td>
			<td>
				<input type="button" class="Button" value="插入链接" style="width:60px" onmousedown="insertLink(false)">
				<iframe id="moreLinkFormat" name="moreLinkFormat" src="about:blank" class="frame" style="width:100%; height:20px;; margin-top:5px" frameborder="0"></iframe>
			</td>
		</tr>
		<tr>
			<td valign="top" style="padding-top:36px">标签页显示内容：</td>
			<td>
				<input id="insertRecordListButton" type="button" class="Button" value="插入记录列表" style="width:80px" onmousedown="insertRecordList()">
				<input id="editRecordListButton" type="button" class="Button" value="编辑记录列表" style="width:80px" onmousedown="editRecordList()">
				<input id="insertLinkButton" type="button" class="Button" value="插入链接" style="width:60px" onmousedown="insertLink(true)">
				<input type="button" class="Button" value="查看源代码" style="width:80px" onmousedown="showtSource(this)">
				<iframe id="bodyFormat" name="bodyFormat" src="about:blank" class="frame" style="width:100%; height:80px; margin-top:5px" frameborder="0"></iframe>
				<textarea name="source" style="width:100%; height:80px !important; display:none; margin-top:5px"></textarea>
			</td>
		</tr>
	</table>
</ext:form>