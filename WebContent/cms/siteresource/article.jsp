<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveArticle" onsubmit="return formOnSubmit();">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>
	<script>
		function preview() {
			window.open('../article.shtml?id=' + document.getElementsByName("id")[0].value + "&siteId=" + document.getElementsByName("columnId")[0].value);
		}
		function setAsHeadline() {
			setHeadline('<ext:write name="article" property="siteIds"/>', '<ext:write name="article" property="subjectTrim"/>', '<%=request.getContextPath()%>/cms/siteresource/article.shtml?id=<ext:write name="article" property="id"/>', '<ext:write name="article" property="summarize"/>');
		}
		function setTop() {
			DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/admin/setTop.shtml?resourceId=<ext:write name="article" property="id"/>&columnIds=<ext:write property="columnId"/>,<ext:write property="otherColumnIds"/>', 430, 200);
		}
		function deleteArticle() {
			var columnNames = "";
			var callback = function(buttonName) {
				if(buttonName=="删除") {
					FormUtils.doAction('deleteArticle');
				}
				else if(buttonName=="从指定栏目删除") {
					var columnNames = '<ext:write property="columnFullName"/>,<ext:write property="otherColumnNames"/>'.split(",");
					var columnIds = '<ext:write property="columnId"/>,<ext:write property="otherColumnIds"/>'.split(",");
					var list = "";
					for(var i=0; i<columnNames.length; i++) {
						if(columnNames[i]!="") {
							list += (list=="" ? "" : ",") + columnNames[i] + "|" + columnIds[i];
						}
					}
					DialogUtils.openListDialog('栏目选择', list, 500, 300, false, '', 'FormUtils.doAction("deleteArticle", "deleteFromColumn={value}")');
				}
			};
			DialogUtils.openMessageDialog('删除', '删除后不可恢复，是否确定要删除？', '删除' + (document.getElementsByName('otherColumnIds')[0].value=='' ? '' : ',从指定栏目删除') + ',取消', 'warn', 360, 200, '', callback);
		}
	</script>
   	<ext:tab/>
</ext:form>