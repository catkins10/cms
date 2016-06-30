<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertEnterprises">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties=="") {
				return;
			}
			var category = StringUtils.getPropertyValue(recordListProperties, "enterpriseCategory");
			document.getElementsByName("enterpriseCategory")[0].value = category;
		}
		
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			return "enterpriseCategory=" + document.getElementsByName("enterpriseCategory")[0].value;
		}
		
		//获取默认的左侧记录格式
		function getDeafultRecordLeftFormat() {
			return '<a id="field" urn="name=name&link=true">&lt;企业名称&gt;</a>';
		}
		//获取配置元素标题,由继承者实现
		function getElementTitle(isSearchResults, isRssChannel) {
			if(isRssChannel) {
				return "企业列表";
			}
			var category = document.getElementsByName("enterpriseCategory")[0].value;
			if(!category || category == "") {
				return "企业列表:全部";
			}
			else {
				return "企业列表:" + category;
			}
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td nowrap="nowrap">资质类型：</td>
			<td><ext:field property="enterpriseCategory"/></td>
		</tr>
	</table>
</ext:form>