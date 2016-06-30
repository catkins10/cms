<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/styleDefine">
	<script>
		var ICON_MAX_WIDTH = 36;
		var ICON_MAX_HEIGHT = 36;
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			//设置样式列表
			var styleDefine = DomUtils.getMeta(dialogArguments.document, 'styleDefineMeta', false);
			if(!styleDefine) {
				return;
			}
			styleDefine = styleDefine.content.split('$$');
			for(var i=0; i<styleDefine.length ;i++) {
				var values = styleDefine[i].split('##');
				var tr = addStyle();
				DomUtils.getElement(tr, 'input', 'styleName').value = values[0]; //名称
				//图标
				if(values[1]!="") {
					setImageUrl(values[1], Number(values[2]), Number(values[3]), i);
				}
				DomUtils.getElement(tr, 'input', 'cssFile').value = values[4];
				if(values.length>5) {
					DomUtils.getElement(tr, 'input', 'cssUrl').value = values[5];
				}
			}
		};
		function doOk() {
			var styleDefine = "";
			var tableStyleDefine = document.getElementById('tableStyleDefine');
			for(var i=2; i<tableStyleDefine.rows.length; i++) {
				var styleName = DomUtils.getElement(tableStyleDefine.rows[i], 'input', 'styleName').value; //名称
				var cssFile = DomUtils.getElement(tableStyleDefine.rows[i], 'input', 'cssFile').value;
				if(styleName=='' || cssFile=="") {
					continue;
				}
				var cssUrl = DomUtils.getElement(tableStyleDefine.rows[i], 'input', 'cssUrl').value;
				var icon = DomUtils.getElement(tableStyleDefine.rows[i], 'img', 'icon'); //图标
				var iconWidth = "", iconHeight = "";
				if(icon.style.display=='none') {
					icon = "";
				}
				else {
					iconWidth = icon.name.split("/")[0];
					iconHeight = icon.name.split("/")[1];
					icon = icon.src;
				}
				if(cssUrl=="") {
					cssUrl = cssFile;
				}
				styleDefine += (styleDefine=="" ? "" : "$$") + styleName + "##" + removeHost(icon) + "##" + iconWidth + "##" + iconHeight + "##" + cssFile + "##" + cssUrl;
			}
			var styleDefineMeta = DomUtils.getMeta(dialogArguments.document, 'styleDefineMeta', true);;
			styleDefineMeta.content = styleDefine;
			DialogUtils.closeDialog();
		}
		function removeHost(url) { //从URL中删除主机信息
			var index = url.indexOf('://');
			if(index==-1) {
				return url;
			}
			return url.substring(url.indexOf('/', index + 3));
		}
		//添加样式
		function addStyle() {
			var tableStyleDefine = document.getElementById('tableStyleDefine');
			var trSample = tableStyleDefine.rows[1];
			var tr = tableStyleDefine.insertRow(-1);
			tr.align = trSample.align;
			tr.height = trSample.height;
			for(var i=0; i<trSample.cells.length; i++) {
				var td = tr.insertCell(-1);
				td.className = trSample.cells[i].className;
				td.innerHTML = trSample.cells[i].innerHTML;
			}
			return tr;
		}
		function removeStyle() {
			var table = document.getElementById("tableStyleDefine");
			var selects = document.getElementsByName("selectStyle");
			for(var i=selects.length-1; i>=0; i--) {
				if(selects[i].checked) {
					table.deleteRow(i+1);
				}
			}
		}
		function moveUp() {
			var table = document.getElementById("tableStyleDefine");
			var selects = document.getElementsByName("selectStyle");
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
			var table = document.getElementById("tableStyleDefine");
			var selects = document.getElementsByName("selectStyle");
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
		
		//选取图片,并将url插入到样式配置框中
		function selectImage(styleIndex) {
			var dialogUrl = dialogArguments.editor.getAttachmentSelectorURL('images', 'setImageUrl("{URL}", {WIDTH}, {HEIGHT}, ' + styleIndex + ')');
			DialogUtils.openDialog(dialogUrl, 600, 400);
		}
		function setImageUrl(imageUrl, width, height, styleIndex) { //设置图标
			var image = document.getElementById('tableStyleDefine').rows[styleIndex+2].getElementsByTagName("img")[0];
			image.style.display = 'none';
			image.src = imageUrl;
			image.name = width + "/" + height;
			image.removeAttribute("width");
			image.removeAttribute("height");
			if(height>ICON_MAX_HEIGHT) {
				var newWidth = width * ICON_MAX_HEIGHT/height;
				if(newWidth>ICON_MAX_WIDTH) {
					image.width = ICON_MAX_WIDTH;
					image.height = height * ICON_MAX_WIDTH/width;
				}
				else {
					image.height = ICON_MAX_HEIGHT;
				}
			}
			else if(width>ICON_MAX_WIDTH) {
				image.width = ICON_MAX_WIDTH;
			}
			image.style.display = '';
		}
		function selectCss(styleIndex) { //选择CSS
			var dialogUrl = dialogArguments.editor.getAttachmentSelectorURL('css', 'setCssName("{URL}", ' + styleIndex + ')');
			DialogUtils.openDialog(dialogUrl, 600, 400);
		}
		function setCssName(cssUrl, styleIndex) { //设置CSS
			DomUtils.getElement(document.getElementById('tableStyleDefine').rows[styleIndex+2], 'input', 'cssUrl').value = cssUrl;
			DomUtils.getElement(document.getElementById('tableStyleDefine').rows[styleIndex+2], 'input', 'cssFile').value = cssUrl.substring(cssUrl.lastIndexOf('/') + 1);
		}
	</script>
	<input type="button" class="Button" value="添加样式" style="width:60px" onclick="addStyle()">&nbsp;
	<input type="button" class="Button" value="删除样式" style="width:60px" onclick="removeStyle()">&nbsp;
	<input type="button" class="Button" value="上移" style="width:40px" onclick="moveUp()">&nbsp;
	<input type="button" class="Button" value="下移" style="width:40px" onclick="moveDown()">&nbsp;
	<div style="width:100%; height: 280px; border: #BFC5CE 1px solid; background-color:white; margin-top:5px; overflow:auto">
		<table id="tableStyleDefine" width="100%" class="table" style="margin-top: -1px; margin-left: -1px" border="1" cellpadding="2" cellspacing="0">
			<tr align="center">
				<td width="25px" nowrap="nowrap" class="tdtitle"></td>
				<td width="120px" nowrap="nowrap" class="tdtitle">样式名称</td>
				<td width="80px" nowrap="nowrap" class="tdtitle">图标</td>
				<td nowrap="nowrap" class="tdtitle">CSS文件</td>
			</tr>
			<tr valign="middle" height="22px" style="display: none">
				<td class="tdcontent">
					<input name="selectStyle" type="checkbox" class="checkbox" style="height:18px">
				</td>
				<td class="tdcontent">
					<input name="styleName" type="text" class="field" style="border:#fff 0px none">
				</td>
				<td class="tdcontent">
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="100%"><img id="icon" style="display:none; float:left" align="absmiddle"></td>
							<td nowrap="nowrap" class="selectButton" onclick="selectImage(offsetParent.offsetParent.parentNode.rowIndex-2)">&nbsp;</td>
						</tr>
					</table>
				</td>
				<td class="tdcontent">
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="100%">
								<input name="cssUrl" type="hidden" style="border:#fff 0px none">
								<input name="cssFile" type="text" style="border:#fff 0px none; width:100%">
							</td>
							<td nowrap="nowrap" class="selectButton" onclick="selectCss(offsetParent.offsetParent.parentNode.rowIndex-2)">&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</ext:form>