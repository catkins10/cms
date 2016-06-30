<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertSearchDirectoryField">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/form/js/insertFieldExtend.js"></script>
	<script>
		//显示扩展属性,由继承者实现
		function showExtendProperties(fieldProperties) {
			document.getElementsByName('parentDirectoryIds')[0].value = StringUtils.getPropertyValue(fieldProperties, "parentDirectoryIds");
			document.getElementsByName('parentDirectoryNames')[0].value = StringUtils.getPropertyValue(fieldProperties, "parentDirectoryNames");
		}
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			return "parentDirectoryIds=" + document.getElementsByName('parentDirectoryIds')[0].value +
				   "&parentDirectoryNames=" + StringUtils.encodePropertyValue(document.getElementsByName('parentDirectoryNames')[0].value);
		}		
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<tr valign="middle">
			<td nowrap="nowrap">上级目录：</td>
			<td width="100%"><ext:field property="parentDirectoryNames"/></td>
		</tr>
	</table>
</ext:form>