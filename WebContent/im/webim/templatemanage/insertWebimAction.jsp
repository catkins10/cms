<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertWebimAction">
	<script>
		var parentElenentTypes = "div,td,span,li";
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, parentElenentTypes);
			if(!obj || obj.id!="webimAction") {
				return;
			}
			var urn = obj.getAttribute("urn");
			document.getElementsByName("dialogWidth")[0].value = StringUtils.getPropertyValue(urn, "dialogWidth");
			document.getElementsByName("dialogHeight")[0].value = StringUtils.getPropertyValue(urn, "dialogHeight");
			document.getElementsByName("dialogAlign")[1].checked = StringUtils.getPropertyValue(urn, "dialogAlign")=="right";
		};
		function doOk() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, parentElenentTypes);
			if(!obj) {
				alert("位置不正确，请重新选择");
				return;
			}
			dialogArguments.editor.saveUndoStep();
			obj.id = obj.name = "webimAction";
			obj.title = "<ext:field property="action"/>";
			obj.setAttribute("urn", "action=<ext:field property="action"/>" +
					  				"&dialogWidth=" + document.getElementsByName("dialogWidth")[0].value +
					  				"&dialogHeight=" + document.getElementsByName("dialogHeight")[0].value +
					  				"&dialogAlign=" + (document.getElementsByName("dialogAlign")[0].checked ? "left" : "right") +
					  				"&" + generateStylesProperties());
			DialogUtils.closeDialog();
		}
		//获取样式名称列表,格式:样式1名称|标题,样式2名称|标题,由调用者实现
		function getStyleNames() {
			return 	"选中|selected\0鼠标经过|mouseover";
		}
		//获取默认的样式,由调用者实现
		function getDefaultStyle(styleName) {
			
		}
		//获取页面元素属性(urn),由调用者实现
		function getPageElementProperties() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, parentElenentTypes);
			if(obj && obj.id=="webimAction") {
				return obj.getAttribute("urn");
			}
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col>
		<tr>
			<td nowrap="nowrap" align="right"><ext:field property="action"/>窗口宽度：</td>
			<td width="60px" nowrap="nowrap"><ext:field property="dialogWidth"></ext:field>
			<td nowrap="nowrap" align="right">高度：</td>
			<td width="60px" nowrap="nowrap"><ext:field property="dialogHeight"/></td>
			<td nowrap="nowrap" align="right">对齐方式：</td>
			<td width="100%"><ext:field property="dialogAlign"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top: 5px" align="right">样式配置：</td>
			<td colspan="7" width="100%"><jsp:include page="/cms/templatemanage/dialog/template/customStyle.jsp"/></td>
		</tr>
	</table>
</ext:form>