<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/columnBar">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		var parentElenentTypes = "div,td";
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, parentElenentTypes);
			if(!obj || obj.id!="columnBar") {
				return;
			}
			var urn = obj.getAttribute("urn");
			document.getElementsByName('barDisplayMode')[0].checked = StringUtils.getPropertyValue(urn, "displayMode")=='alwaysDisplay';
			onDisplayModeChanged();
			var displaySeconds = StringUtils.getPropertyValue(urn, "displaySeconds");
			if(displaySeconds!="") {
				document.getElementsByName('displaySeconds')[0].value = displaySeconds;
			}
		};
		function doOk() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, parentElenentTypes);
			if(!obj) {
				alert("位置不正确，请重新选择");
				return false;
			}
			dialogArguments.editor.saveUndoStep();
			obj.id = "columnBar";
			obj.title = "栏目栏";
			var displayModes = document.getElementsByName('barDisplayMode');
			obj.setAttribute("urn", "displayMode=" + (displayModes[1].checked ? 'autoHide' : 'alwaysDisplay') +
					 			    (displayModes[1].checked ? "&displaySeconds=" + document.getElementsByName('displaySeconds')[0].value : '') +
					  				"&" + generateStylesProperties());
			DialogUtils.closeDialog();
		}
		//获取样式名称列表,格式:样式1名称|标题,样式2名称|标题,由调用者实现
		function getStyleNames() {
			return "当前栏目|currentColumn\0栏目|column\0左侧有栏目提示|moreColumnsOnLeft\0右侧有栏目提示|moreColumnsOnRight";
		}
		//获取默认的样式,由调用者实现
		function getDefaultStyle(styleName) {
			
		}
		//获取页面元素属性(urn),由调用者实现
		function getPageElementProperties() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, parentElenentTypes);
			if(obj && obj.id && obj.id=="columnBar") {
				return obj.getAttribute("urn");
			}
		}
		function onDisplayModeChanged() {
			document.getElementById('spanDisplaySeconds').style.display = document.getElementsByName('barDisplayMode')[1].checked ? '' : 'none';
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<tr>
			<td nowrap="nowrap">显示方式：</td>
			<td width="160px" nowrap="nowrap"><ext:field property="barDisplayMode" onclick="onDisplayModeChanged()"/></td>
			<td id="spanDisplaySeconds" width="100%">
				<table border="0" width="100%" cellspacing="0" cellpadding="0px">
					<tr>
						<td nowrap="nowrap">显示时间(秒)：</td>
						<td width="100%"><ext:field property="displaySeconds"/></td>
					</tr>
				</table>
			</td>
			<tr>
				<td nowrap="nowrap" valign="top" style="padding-top: 5px">样式配置：</td>
				<td colspan="2" width="100%"><jsp:include page="/cms/templatemanage/dialog/template/customStyle.jsp"/></td>
			</tr>
		</tr>
	</table>
</ext:form>