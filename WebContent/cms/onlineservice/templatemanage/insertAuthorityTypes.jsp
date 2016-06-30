<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertAuthorityTypes">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties=="") {
				return;
			}
			document.getElementsByName("showAll")[0].checked = (StringUtils.getPropertyValue(recordListProperties, "showAll")=="true");
		}
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			return "showAll=" + document.getElementsByName("showAll")[0].checked;
		}
	</script>
	<table border="0" cellspacing="5" cellpadding="0px">
		<tr valign="middle">
			<td nowrap="nowrap" align="right">显示“全部”：</td>
			<td><ext:field property="showAll"/></td>
		</tr>
	</table>
</ext:form>