<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/channel">
    <script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		var parentElenentTypes = "span,div,td";
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, parentElenentTypes);
			if(!obj || obj.id!="channel") {
				return;
			}
			var urn = obj.getAttribute("urn");
			document.getElementsByName('name')[0].value = StringUtils.getPropertyValue(urn, "name"); //名称
			document.getElementsByName('columnCount')[0].value = StringUtils.getPropertyValue(urn, "columnCount"); //显示栏目数
			//设置图标
			var icon = StringUtils.getPropertyValue(urn, "icon");
			if(icon!='') {
				setImageUrl(icon, Number(StringUtils.getPropertyValue(urn, "iconWidth")), Number(StringUtils.getPropertyValue(urn, "iconHeight")), -1, 100, 24);
			}
			//设置栏目列表
			for(var i=0; ;i++) {
				var column = StringUtils.getPropertyValue(urn, "column" + i);
				if(column=='') {
					break;
				}
				var tr = addColumn();
				DomUtils.getElement(tr, 'input', 'columnName').value = StringUtils.getPropertyValue(column, 'name'); //名称
				DomUtils.getElement(tr, 'input', 'columnCategory').value = StringUtils.getPropertyValue(column, 'category'); //分类
				icon = StringUtils.getPropertyValue(column, "icon"); 
				if(icon!='') {
					setImageUrl(icon, Number(StringUtils.getPropertyValue(column, "iconWidth")), Number(StringUtils.getPropertyValue(column, "iconHeight")), i, 60, 22);
				}
				DomUtils.getElement(tr, 'iframe', 'frameLink').contentWindow.document.body.innerHTML = StringUtils.getPropertyValue(column, 'link'); //链接
			}
		}
		function doOk() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, parentElenentTypes);
			if(!obj) {
				alert("位置不正确，请重新选择");
				return false;
			}
			var name = document.getElementsByName('name')[0].value;
			if(name=='') {
				alert("频道名称不能为空");
				return false;
			}
			var tableColumns = document.getElementById('tableColumns');
			var columns = "";
			for(var i=2; i<tableColumns.rows.length; i++) {
				var columnName = DomUtils.getElement(tableColumns.rows[i], 'input', 'columnName').value; //名称
				var columnIcon = DomUtils.getElement(tableColumns.rows[i], 'img', 'columnIcon'); //图标
				var columnIconWidth, columnIconHeight;
				if(columnIcon.style.display=='none') {
					columnIcon = "";
				}
				else {
					columnIconWidth = columnIcon.name.split("/")[0];
					columnIconHeight = columnIcon.name.split("/")[1];
					columnIcon = columnIcon.src;
				}
				
				var columnCategory = DomUtils.getElement(tableColumns.rows[i], 'input', 'columnCategory').value; //分类
				var link = DomUtils.getElement(tableColumns.rows[i], 'iframe', 'frameLink').contentWindow.document.body.innerHTML.replace(/<p>/gi, "").replace(/<\/p>/gi, ""); //链接
				if(columnName=='' && link=='') {
					continue;
				}
				if(columnName=='') {
					alert("栏目名称不能为空");
					return;
				}
				if(link=='') {
					alert("链接不能为空");
					return;
				}
				var column = "name=" + StringUtils.encodePropertyValue(columnName) + 
							 (columnIcon=="" ? "" : "&icon=" + StringUtils.encodePropertyValue(removeHost(columnIcon)) + "&iconWidth=" + columnIconWidth + "&iconHeight=" + columnIconHeight) +
							 (columnCategory=="" ? "" : "&category=" + StringUtils.encodePropertyValue(columnCategory)) +
							 "&link=" + StringUtils.encodePropertyValue(link);
				columns += "&column" + (i-2) + "=" + StringUtils.encodePropertyValue(column);
			}
			
			var channelIcon = document.getElementById('channelIcon');
			dialogArguments.editor.saveUndoStep();
			obj.id = "channel";
			obj.title = "频道:" + name;
			obj.setAttribute("urn", "name=" + StringUtils.encodePropertyValue(name) +
					  (channelIcon.style.display=='none' ? "" : "&icon=" + StringUtils.encodePropertyValue(removeHost(channelIcon.src)) + "&iconWidth=" + channelIcon.name.split("/")[0] + "&iconHeight=" + channelIcon.name.split("/")[1]) +
					  "&columnCount=" + StringUtils.encodePropertyValue(document.getElementsByName('columnCount')[0].value) +
					  columns);
			DialogUtils.closeDialog();
		}
		function removeHost(url) { //从URL中删除主机信息
			var index = url.indexOf('://');
			if(index==-1) {
				return url;
			}
			return url.substring(url.indexOf('/', index + 3));
		}
		//选取图片,并将url插入到样式配置框中
		function selectImage(columnIndex, maxWidth, maxHeight) {
			var dialogUrl = dialogArguments.editor.getAttachmentSelectorURL('images', 'setImageUrl("{URL}", {WIDTH}, {HEIGHT}, ' + columnIndex + ', ' + maxWidth + ', ' + maxHeight + ')');
			DialogUtils.openDialog(dialogUrl, 600, 400);
		}
		function setImageUrl(imageUrl, width, height, imageColumnIndex, imageMaxWidth, imageMaxHeight) {
			var image;
			if(imageColumnIndex==-1) { //设置频道图标
				image = document.getElementById('channelIcon');
			}
			else { //设置栏目图标
				var tableColumns = document.getElementById('tableColumns');
				image = tableColumns.rows[imageColumnIndex+2].getElementsByTagName("img")[0];
			}
			image.style.display = 'none';
			image.src = imageUrl;
			image.name = width + "/" + height;
			image.removeAttribute("width");
			image.removeAttribute("height");
			if(height>imageMaxHeight) {
				var newWidth = width * imageMaxHeight/height;
				if(newWidth>imageMaxWidth) {
					image.width = imageMaxWidth;
					image.height = height * imageMaxWidth/width;
				}
				else {
					image.height = imageMaxHeight;
				}
			}
			else if(width>imageMaxWidth) {
				image.width = imageMaxWidth;
			}
			image.style.display = '';
		}
		
		//添加栏目
		function addColumn() {
			var tableColumns = document.getElementById('tableColumns');
			var trSample = tableColumns.rows[1];
			var tr = tableColumns.insertRow(-1);
			tr.align = trSample.align;
			tr.height = trSample.height;
			for(var i=0; i<trSample.cells.length; i++) {
				var td = tr.insertCell(-1);
				td.className = trSample.cells[i].className;
				td.innerHTML = trSample.cells[i].innerHTML;
			}
			var doc = tr.getElementsByTagName("iframe")[0].contentWindow.document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; overflow:hidden; padding:5px 2px 2px 2px; margin:0px; font-family:宋体; font-size:12px">');
			doc.write('</body>');
			doc.close();
			return tr;
		}
		
		//插入链接
		function insertLink(columnIndex) {
			var tableColumns = document.getElementById('tableColumns');
			var iframe = tableColumns.rows[columnIndex+2].getElementsByTagName("iframe")[0];
			iframe.contentWindow.document.body.focus();
			window.args = DomUtils.getWindowBookmark(iframe.contentWindow, '插入位置不正确，请重新选择');
			if(!window.args) {
				return;
			}
			window.args.editor = dialogArguments.editor;
			selectSiteLink(550, 360, "doInsertLink('{id}', '{name}', '{dialogURL}', '{url}', '{applicationTitle}')");
		}
		function doInsertLink(id, title, dialogURL, url, applicationTitle) {
			DialogUtils.openDialog(RequestUtils.getContextPath() + dialogURL, 455, 180, "", window.args);
		}
		function removeColumn() {
			var table = document.getElementById("tableColumns");
			var selects = document.getElementsByName("selectColumn");
			for(var i=selects.length-1; i>=0; i--) {
				if(selects[i].checked) {
					table.deleteRow(i+1);
				}
			}
		}
		function moveUp() {
			var table = document.getElementById("tableColumns");
			var selects = document.getElementsByName("selectColumn");
			for(var i=0; i<selects.length; i++) {
				if(selects[i].checked) {
					if(i==1) {
						return;
					}
					DomUtils.moveTableRow(table, i+1, i);
				}
			}
		}
		function moveDown() {
			var table = document.getElementById("tableColumns");
			var selects = document.getElementsByName("selectColumn");
			for(var i=selects.length-1; i>=0; i--) {
				if(selects[i].checked) {
					flag = false;
					if(i==selects.length-1) {
						return;
					}
					DomUtils.moveTableRow(table, i+1, i+2);
				}
			}
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">频道名称：</td>
			<td><ext:field property="name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">图标：</td>
			<td><img id="channelIcon" style="display:none" align="absmiddle">&nbsp;<input type="button" value="选择" class="Button" onclick="selectImage(-1, 100, 24)"></td>
		</tr>
		<tr>
			<td nowrap="nowrap">显示栏目数：</td>
			<td><ext:field property="columnCount"/></td>
		</tr>
		<tr>
			<td valign="top" style="padding-top:3px">栏目列表：</td>
			<td>
				<input type="button" class="Button" value="添加栏目" style="width:60px" onclick="addColumn()">
				<input type="button" class="Button" value="删除栏目" style="width:60px" onclick="removeColumn()">
				<input type="button" class="Button" value="上移" style="width:40px" onclick="moveUp()">
				<input type="button" class="Button" value="下移" style="width:40px" onclick="moveDown()">
				<div style="width:100%; height: 230px; border: #BFC5CE 1px solid; background-color:white; margin-top:5px; overflow:auto">
					<table id="tableColumns" width="100%" class="table" style="margin-top: -1px; margin-left: -1px" border="1" cellpadding="0" cellspacing="0">
						<tr align="center">
							<td width="20px" nowrap="nowrap" class="tdtitle"></td>
							<td width="80px" nowrap="nowrap" class="tdtitle">名称</td>
							<td width="80px" nowrap="nowrap" class="tdtitle">图标</td>
							<td width="80px" nowrap="nowrap" class="tdtitle">分类</td>
							<td nowrap="nowrap" class="tdtitle">链接地址</td>
						</tr>
						<tr style="display: none">
							<td class="tdcontent"><input name="selectColumn" type="checkbox" class="checkbox" style="height:18px"></td>
							<td class="tdcontent"><input name="columnName" type="text" class="field" style="border:#fff 0px none"></td>
							<td class="tdcontent">
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td width="100%"><img id="columnIcon" style="display:none; float:left" align="absmiddle"></td>
										<td nowrap="nowrap" class="selectButton" onclick="selectImage(this.offsetParent.offsetParent.parentNode.rowIndex-2, 60, 22)">&nbsp;</td>
									</tr>
								</table>
							</td>
							<td class="tdcontent"><input name="columnCategory" type="text" class="field" style="border:#fff 0px none"></td>
							<td class="tdcontent">
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td width="100%"><iframe id="frameLink" frameborder="0" width="100%" height="20px" scrolling="no"></iframe></td>
										<td nowrap="nowrap" class="selectButton" onmousedown="insertLink(this.offsetParent.offsetParent.parentNode.rowIndex-2)">&nbsp;</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</ext:form>