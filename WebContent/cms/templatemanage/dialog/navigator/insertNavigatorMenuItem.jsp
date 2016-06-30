<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertNavigatorMenuItem">
	<style>
		.linkTd {
			border-bottom: #f0f0f0 1 solid;
		}
	</style>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/navigator/js/navigator.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>
	<script>
	var dialogArguments = DialogUtils.getDialogArguments();
	window.onload = function() {
		var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "div,td,span,li");
		if(!obj) {
			return false;
		}
		if(obj && obj.id && obj.id.indexOf("navigatorMenuItem_")==0) {
			//隶属导航栏
			document.getElementsByName("navigatorName")[0].value = obj.id.substring("navigatorMenuItem_".length);
			//设置链接列表
			for(var i=0; i<100; i++) {
				var urn = obj.getAttribute("urn");
				var linkTitle = StringUtils.getPropertyValue(urn, "link" + i + "Title");
				if(linkTitle=="") {
					break;
				}
				addLink(linkTitle, StringUtils.getPropertyValue(urn, "link" + i));
			}
		}
		else {
			var navigator = getNavigator(obj);
			document.getElementsByName("navigatorName")[0].value = StringUtils.getPropertyValue(navigator.getAttribute("urn"), "name");
		}
		var doc = frames["linkFormat"].document;
		doc.open();
		doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border:1 solid #808080; overflow:hidden; margin:3px; font-family:宋体; font-size:12px">');
		doc.write('</body>');
		doc.close();
	};
	function doOk() {
		var linkElement = DomUtils.getParentNode(dialogArguments.selectedElement, "div,td,span,li");
		if(!linkElement) {
			alert("请重新选择菜单位置");
			return false;
		}
		if(document.getElementsByName("navigatorName")[0].value=='') {
			alert("隶属导航栏不能为空");
			return false;
		}
		dialogArguments.editor.saveUndoStep();
		linkElement.id = linkElement.name = "navigatorMenuItem_" + document.getElementsByName("navigatorName")[0].value;
		linkElement.title = "菜单:" + document.getElementsByName("navigatorName")[0].value;
		var urn = generateStylesProperties();
		//设置菜单条目列表
		var links = document.getElementById("linkTable").rows;
		for(var i=0; i<links.length; i++) {
			var linkTitle = links[i].cells[1].getElementsByTagName("input")[0].value;
			if(linkTitle=="") {
				alert("菜单项名称不能为空");
				return false;
			}
			urn += "&link" + i + "Title=" + StringUtils.encodePropertyValue(links[i].cells[1].getElementsByTagName("input")[0].value) +
				   "&link" + i + "=" + StringUtils.encodePropertyValue(links[i].cells[2].innerHTML)
		}
		linkElement.setAttribute("urn", urn);
		DialogUtils.closeDialog();
	}
	function appendLink() { //添加链接
		var doc = frames["linkFormat"].document;
		doc.body.innerHTML = "";
		doc.body.focus();
		window.args = DomUtils.getWindowBookmark(window.frames["linkFormat"], '插入位置不正确，请重新选择');
		if(!window.args) {
			return;
		}
		window.args.editor = dialogArguments.editor;
		selectSiteLink(550, 360, "doInsertLink('{id}', '{name}', '{dialogURL}', '{url}', '{applicationTitle}')");
	}
	function doInsertLink(id, title, dialogURL, url, applicationTitle) {
		DialogUtils.openDialog(RequestUtils.getContextPath() + dialogURL, 455, 180, "", window.args);
		var doc = frames["linkFormat"].document;
		doc.body.onfocus = function() { //取到焦点,说明链接已经插入
			doc.body.onfocus = function() {
				return;
			};
			window.setTimeout(function() {
				if(doc.body.innerHTML!="") {
					var a = doc.getElementsByTagName("a")[0];
					addLink(a.innerHTML, doc.body.innerHTML);
				}
			}, 300);
		};
	}
	function addLink(title, linkHTML) { //添加链接
		var tr = document.getElementById("linkTable").insertRow(-1);
		var td = tr.insertCell(-1);
		td.className = "linkTd";
		td.noWrap  = true; 
		td.innerHTML = '&nbsp;<input type="checkbox" class="checkbox" name="selectLink">&nbsp;';
		
		td = tr.insertCell(-1);
		td.className = "linkTd";
		td.width = "100%";
		td.innerHTML = '<input type="text" style="border-style:none; width:100%; height:100%" value="' + StringUtils.encodePropertyValue(StringUtils.filterEscapeCharacter(title)) + '">';
		
		td = tr.insertCell(-1);
		td.style.display = 'none';
		DomUtils.setElementHTML(td, linkHTML, true);
	}
	function deleteLink() { //删除链接
		var table = document.getElementById("linkTable");
		var selects = document.getElementsByName("selectLink");
		if(selects.length==0) {
			return;
		}
		for(var i=selects.length-1; i>=0; i--) {
			if(selects[i].checked) {
				table.deleteRow(i);
			}
		}
	}
	function moveUp() { //上移
		var table = document.getElementById("linkTable");
		var selects = document.getElementsByName("selectLink");
		if(selects.length==0) {
			return;
		}
		for(var i=0; i<selects.length; i++) {
			if(selects[i].checked) {
				if(i==0) {
					return;
				}
				DomUtils.moveTableRow(table, i, i-1);
				selects[i-1].checked = true;
			}
		}
	}
	function moveDown() { //下移
		var table = document.getElementById("linkTable");
		var selects = document.getElementsByName("selectLink");
		if(selects.length==0) {
			return;
		}
		for(var i=selects.length-1; i>=0; i--) {
			if(selects[i].checked) {
				flag = false;
				if(i==selects.length-1) {
					return;
				}
				DomUtils.moveTableRow(table, i, i+1);
				selects[i+1].checked = true;
			}
		}
	}
	//获取样式名称列表,格式:样式1名称|标题,样式2名称|标题,由调用者实现
	function getStyleNames() {
		return 	"导航栏菜单项目：未选中|menuItem\0" +
				"导航栏菜单项目：选中|menuItemSelected\0" +
				"导航栏菜单项目：未选中时的下拉按钮|menuItemDropdown\0" +
				"导航栏菜单项目：选中时的下拉按钮|menuItemSelectedDropdown";
	}
	//获取默认的样式,由调用者实现
	function getDefaultStyle(styleName) {
		
	}
	//获取页面元素属性(urn),由调用者实现
	function getPageElementProperties() {
		var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "div,td,span,li");
		if(obj && obj.id && obj.id.indexOf("navigatorMenuItem_")==0) {
			return obj.getAttribute("urn");
		}
	}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">导航栏名称：</td>
			<td><ext:field property="navigatorName" readonly="true"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top: 10px">样式配置：</td>
			<td nowrap="nowrap" style="padding-top: 5px">
				<jsp:include page="/cms/templatemanage/dialog/template/customStyle.jsp"/>
			</td>
		</tr>
		<tr>
			<td valign="top" style="padding-top:5px">菜单项列表：</td>
			<td>
				<iframe id="linkFormat" name="linkFormat" src="about:blank" style="width:0; height:0;"></iframe>
				<input type="button" class="Button" value="添加菜单项" style="width:68px" onclick="appendLink()">
				<input type="button" class="Button" value="删除菜单项" style="width:68px" onclick="deleteLink()">
				<input type="button" class="Button" value="上移" style="width:40px" onclick="moveUp()">
				<input type="button" class="Button" value="下移" style="width:40px" onclick="moveDown()">
				<div class="frame" style="width:100%; height: 120px; background-color:white; margin-top:5px; overflow:auto">
					<table id="linkTable" border="0" width="100%" cellspacing="0" cellpadding="2px"></table>
				</div>
			</td>
		</tr>
	</table>
</ext:form>