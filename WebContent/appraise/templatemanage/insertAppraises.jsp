<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertAppraises">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties=="") {
				return;
			}
			var appraiserType = StringUtils.getPropertyValue(recordListProperties, "appraiserType");
			if(appraiserType=="1") {
				document.getElementsByName("appraiserType")[1].checked = true;
			}
		}
		
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			return "appraiserType=" + (document.getElementsByName("appraiserType")[0].checked ? "0" : "1");
		}
		
		//获取默认的左侧记录格式
		function getDeafultRecordLeftFormat() {
			return "";
		}
		//获取配置元素标题,由继承者实现
		function getElementTitle(isSearchResults, isRssChannel) {
			return StringUtils.getPropertyValue(location.search, "viewTitle") + ":" + (document.getElementsByName("appraiserType")[0].checked ? "基础库" : "管理服务对象");
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td nowrap="nowrap" height="26px">评议对象：</td>
			<td><ext:field property="appraiserType"/></td>
		</tr>
	</table>
</ext:form>