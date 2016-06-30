<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertLeaderMails">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/onlineservice/js/onlineservice.js"></script>
	<script type="text/javascript">
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties=="") {
				return;
			}
			document.getElementsByName("directoryName")[0].value = StringUtils.getPropertyValue(recordListProperties, "directoryName");
			document.getElementsByName("directoryId")[0].value = StringUtils.getPropertyValue(recordListProperties, "directoryId");
			document.getElementsByName("showAll")[0].checked = (StringUtils.getPropertyValue(recordListProperties, "showAll")=="true");
		}
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
		 	var directoryName = document.getElementsByName("directoryName")[0].value;
			if(directoryName=='') {
				alert("上级目录不能为空");
				return "ERROR";
			}
			var directoryId = document.getElementsByName("directoryId")[0].value;
			return "directoryId=" + directoryId + "&directoryName=" + directoryName + "&showAll=" + document.getElementsByName("showAll")[0].checked;
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td nowrap="nowrap">上级目录：</td>
			<td><ext:field property="directoryName"/></td>
		</tr>
		<tr valign="middle">
			<td nowrap="nowrap">显示“全部”：</td>
			<td><ext:field property="showAll"/></td>
		</tr>
	</table>
</ext:form>