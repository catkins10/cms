<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertSites">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties=="") {
				return;
			}
			document.getElementsByName("siteName")[0].value = StringUtils.getPropertyValue(recordListProperties, "siteName");
			document.getElementsByName("siteId")[0].value = StringUtils.getPropertyValue(recordListProperties, "siteId");
		}
		
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			var siteName = document.getElementsByName("siteName")[0].value;
			if(siteName=='') {
				alert("父站点不能为空");
				return "ERROR";
			}
			var siteId = document.getElementsByName("siteId")[0].value;
			return "siteId=" + siteId + "&siteName=" + siteName;
		}
		
		//获取默认的左侧记录格式
		function getDeafultRecordLeftFormat() {
			return '<a id="field" urn="name=directoryName&link=true">&lt;站点名称&gt;</a>';
		}
		//获取配置元素标题,由继承者实现
		function getElementTitle(isSearchResults, isRssChannel) {
			return "站点列表:" + document.getElementsByName("siteName")[0].value;
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td nowrap="nowrap">父站点：</td>
			<td><ext:field property="siteName"/></td>
		</tr>
	</table>
</ext:form>