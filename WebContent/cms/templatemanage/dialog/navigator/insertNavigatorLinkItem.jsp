<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertNavigatorLinkItem">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/navigator/js/navigator.js"></script>
	<script>
	var dialogArguments = DialogUtils.getDialogArguments();
	window.onload = function() {
		var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "div,td,span,li");
		if(!obj) {
			return;
		}
		if(obj && obj.id && obj.id.indexOf("navigatorLinkItem_")==0) {
			//隶属导航栏
			document.getElementsByName("navigatorName")[0].value = obj.id.substring("navigatorLinkItem_".length);
		}
		else {
			var navigator = getNavigator(obj);
			document.getElementsByName("navigatorName")[0].value = StringUtils.getPropertyValue(navigator.getAttribute("urn"), "name");
		}
	}
	function doOk() {
		var linkElement = DomUtils.getParentNode(dialogArguments.selectedElement, "div,td,span,li");
		if(!linkElement) {
			alert("请重新选择链接位置");
			return false;
		}
		if(document.getElementsByName("navigatorName")[0].value=='') {
			alert("隶属导航栏不能为空");
			return false;
		}
		dialogArguments.editor.saveUndoStep();
		linkElement.id = linkElement.name = "navigatorLinkItem_" + document.getElementsByName("navigatorName")[0].value;
		linkElement.title = "链接:" + document.getElementsByName("navigatorName")[0].value;
		linkElement.setAttribute("urn", generateStylesProperties());
		DialogUtils.closeDialog();
	}
	//获取样式名称列表,格式:样式1名称|标题,样式2名称|标题,由调用者实现
	function getStyleNames() {
		return 	"导航栏链接项目：未选中|linkItem\0" +
				"导航栏链接项目：选中|linkItemSelected";
	}
	//获取默认的样式,由调用者实现
	function getDefaultStyle(styleName) {
		
	}
	//获取页面元素属性(urn),由调用者实现
	function getPageElementProperties() {
		var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "div,td,span,li");
		if(obj && obj.id && obj.id.indexOf("navigatorLinkItem_")==0) {
			return obj.getAttribute("urn");
		}
	}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
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
	</table>
</ext:form>