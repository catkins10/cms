<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertWeathers">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
		//获取配置元素标题,由继承者实现
		function getElementTitle(isSearchResults, isRssChannel) {
			
		}
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties!="") {
				//地区
				document.getElementsByName("areas")[0].value = StringUtils.getPropertyValue(recordListProperties, "areas");
				//预报天数
				document.getElementsByName("forecastDays")[0].value = StringUtils.getPropertyValue(recordListProperties, "forecastDays");
			}
		}
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			return "areas=" + document.getElementsByName("areas")[0].value +
				   "&forecastDays=" + document.getElementsByName("forecastDays")[0].value;
		}
		//获取默认的左侧记录格式
		function getDeafultRecordLeftFormat() {
			return '';
		}
		//获取默认的右侧记录格式
		function getDeafultRecordRightFormat() {
			return '';
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td nowrap="nowrap">地区：</td>
			<td><ext:field property="areas"/></td>
			<td nowrap="nowrap">预报天数：</td>
			<td width="50px" nowrap="nowrap"><ext:field property="forecastDays"/></td>
		</tr>
	</table>
</ext:form>