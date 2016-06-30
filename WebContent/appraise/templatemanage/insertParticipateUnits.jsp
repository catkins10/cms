<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertParticipateUnits">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties=="") {
				return;
			}
			var category = StringUtils.getPropertyValue(recordListProperties, "category");
			document.getElementsByName("category")[0].value = category;
		}
		
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			return "category=" + document.getElementsByName("category")[0].value;
		}
		
		//获取默认的左侧记录格式
		function getDeafultRecordLeftFormat() {
			return "";
		}
		//获取配置元素标题,由继承者实现
		function getElementTitle(isSearchResults, isRssChannel) {
			var category = document.getElementsByName("category")[0].value;
			return "参评单位" + (category=="" ? "" : ":" + category);
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td nowrap="nowrap" height="26px">单位分类：</td>
			<td><ext:field property="category"/></td>
		</tr>
	</table>
</ext:form>