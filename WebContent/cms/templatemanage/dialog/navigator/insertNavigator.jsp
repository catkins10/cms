<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertNavigator">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/navigator/js/navigator.js"></script>
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			document.getElementById('typeImages').style.width = document.getElementById('typeImages').offsetWidth + "px";
			document.getElementById('tableNavigatorType').style.display = '';
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "div,td,span,ul");
			if(!obj) {
				return;
			}
			var navigator = getNavigator(obj);
			if(navigator!=null) {
				var urn = navigator.getAttribute("urn");
				document.getElementsByName("navigatorName")[0].value = StringUtils.getPropertyValue(urn, "name");
				selectNavigatorType(StringUtils.getPropertyValue(urn, "type"), true); //设置导航栏风格
			}
		}
		function doOk() {
			var navigatorName = document.getElementsByName("navigatorName")[0].value;
			if(navigatorName=='') {
				alert("导航栏名称不能为空");
				return;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "div,td,span,ul");
			var navigator = getNavigator(obj);
			if(navigator==null) {
				navigator = obj;
				navigator.id = "navigator";
			}
			var urn = "name=" + navigatorName +
					  "&type=" + getSelectedNavigatorType() +
					  "&" + generateStylesProperties();
			navigator.setAttribute("urn", urn);
			navigator.title = "导航栏:" + navigatorName;
			DialogUtils.closeDialog();
		}
		//选择导航栏风格
		function selectNavigatorType(navigatorType, scrollTo) {
			var cells = document.getElementById("tableNavigatorType").rows[0].cells;
			for(var i=0; i<cells.length; i++) {
				if(navigatorType!=cells[i].id) {
					cells[i].style.borderStyle = "none";
					continue;
				}
				cells[i].style.borderStyle = "solid";
				if(scrollTo) {
					var left = cells[i].offsetLeft;
					var right = left + cells[i].offsetWidth;
					var scrollLeft = document.getElementById('typeImages').scrollLeft;
					var scrollRight = scrollLeft + document.getElementById('typeImages').clientWidth;
					if(left<scrollLeft || left>scrollRight || right<scrollLeft || right>scrollRight) {
						document.getElementById('typeImages').scrollLeft = right - document.getElementById('typeImages').clientWidth + 20;
					}
				}
			}
		}
		//获取导航栏风格
		function getSelectedNavigatorType() {
			var cells = document.getElementById("tableNavigatorType").rows[0].cells;
			for(var i=0; i<cells.length; i++) {
				if(cells[i].style.borderStyle.toLowerCase()!="none") {
					return cells[i].id;
				}
			}
		}
		//获取样式名称列表,格式:样式1名称|标题,样式2名称|标题,由调用者实现
		function getStyleNames() {
			return 	"导航栏链接项目：未选中|linkItem\0" +
				    "导航栏链接项目：选中|linkItemSelected\0" +
					"导航栏菜单项目：未选中|menuItem\0" +
					"导航栏菜单项目：选中|menuItemSelected\0" +
					"导航栏菜单项目：未选中时的下拉按钮|menuItemDropdown\0" +
					"导航栏菜单项目：选中时的下拉按钮|menuItemSelectedDropdown\0" +
					"菜单|popupMenu\0" +
					"菜单条目：未选中|popupMenuItem\0" +
					"菜单条目：选中|popupMenuItemSelected";
		}
		//获取默认的样式,由调用者实现
		function getDefaultStyle(styleName) {
			if(",linkItem,linkItemSelected,menuItem,menuItemSelected,".indexOf("," + styleName + ",")!=-1) {
				return "height:";
			}
			else if("menuItemDropdown"==styleName) { //导航栏菜单项目：未选中时的下拉按钮
				return "margin-left:0px; margin-top:0px; width:9px; height:4px; background: url(<%=request.getContextPath()%>/cms/image/dropdown.gif) no-repeat 0 0px;";
			}
			else if("menuItemSelectedDropdown"==styleName) { //导航栏菜单项目：选中时的下拉按钮
				return "margin-left:0px; margin-top:0px; width:9px; height:4px; background: url(<%=request.getContextPath()%>/cms/image/dropdown.gif) no-repeat 0 -4px;";
			}
			else if("popupMenu"==styleName) { //菜单
				return "border: #808080 1px solid; background-color:#ffffff;";
			}
			else if("popupMenuItem"==styleName) { //菜单条目：未选中
				return "padding: 3px 3px 3px 3px; color:#000000";
			}
			else if("popupMenuItemSelected"==styleName) { //菜单条目：选中
				return "padding: 3px 3px 3px 3px; background-color: #f0f0f0; color:#000000";
			}
		}
		//获取页面元素属性(urn),由调用者实现
		function getPageElementProperties() {
			var obj = getRangeParentElement("div,td,span,ul");
			if(obj) {
				var navigator = getNavigator(obj);
				if(navigator!=null) {
					return navigator.getAttribute("urn");
				}
			}
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="2px">
		<tr>
			<td nowrap="nowrap" align="right">导航栏名称：</td>
			<td colspan="3" width="100%"><ext:field property="navigatorName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="bottom" align="right">导航栏风格：</td>
			<td nowrap="nowrap" valign="bottom"><img src="navigator/icons/left.gif" onclick="document.getElementById('typeImages').scrollLeft -= 100;"></td>
			<td width="100%">
				<div id="typeImages" style="width:100%; overflow:hidden;">
					<table id="tableNavigatorType" border="0" cellspacing="0" cellpadding="3px" style="display:none">
						<tr>
							<td id="horizontalMenuTop" style="border: #2020e0 1 solid" onclick="selectNavigatorType(id)"><img src="navigator/types/horizontal_menu_top.png"></td>
							<td id="horizontalMenuTopRight" style="border: #2020e0 1 none" onclick="selectNavigatorType(id)"><img src="navigator/types/horizontal_menu_top_right.png"></td>
							<td id="horizontalMenuBottom" style="border: #2020e0 1 none" onclick="selectNavigatorType(id)"><img src="navigator/types/horizontal_menu_bottom.png"></td>
							<td id="horizontalMenuBottomRight" style="border: #2020e0 1 none" onclick="selectNavigatorType(id)"><img src="navigator/types/horizontal_menu_bottom_right.png"></td>
							<td id="horizontalLineTop" style="border: #2020e0 1 none" onclick="selectNavigatorType(id)"><img src="navigator/types/horizontal_line_top.png"></td>
							<td id="horizontalLineBottom" style="border: #2020e0 1 none" onclick="selectNavigatorType(id)"><img src="navigator/types/horizontal_line_bottom.png"></td>
							<td id="verticalPopupLeft" style="border: #2020e0 1 none" onclick="selectNavigatorType(id)"><img src="navigator/types/vertical_popup_left.png"></td>
							<td id="verticalPopupRight" style="border: #2020e0 1 none" onclick="selectNavigatorType(id)"><img src="navigator/types/vertical_popup_right.png"></td>
							<td id="verticalLineLeft" style="border: #2020e0 1 none" onclick="selectNavigatorType(id)"><img src="navigator/types/vertical_line_left.png"></td>
							<td id="verticalLineRight" style="border: #2020e0 1 none" onclick="selectNavigatorType(id)"><img src="navigator/types/vertical_line_right.png"></td>
							<td id="verticalDropdown" style="border: #2020e0 1 none" onclick="selectNavigatorType(id)"><img src="navigator/types/vertical_dropdown.png"></td>
						</tr>
					</table>
				</div>
			</td>
			<td nowrap="nowrap" valign="bottom"><img src="navigator/icons/right.gif" onclick="document.getElementById('typeImages').scrollLeft += 100;"></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top: 10px" align="right">导航样式配置：</td>
			<td nowrap="nowrap" style="padding-top: 5px" colspan="3"><jsp:include page="/cms/templatemanage/dialog/template/customStyle.jsp"/></td>
		</tr>
	</table>
</ext:form>