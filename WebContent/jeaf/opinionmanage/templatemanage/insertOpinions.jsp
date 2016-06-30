<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertOpinions">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
		//获取配置元素标题,由继承者实现
		function getElementTitle(isSearchResults, isRssChannel) {
			return document.getElementsByName("opinionType")[0].value + "意见";
		}
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties!="") {
				DropdownField.setValue("opinionType", StringUtils.getPropertyValue(recordListProperties, "opinionType"));
			}
		}
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			return "opinionType=" + document.getElementsByName("opinionType")[0].value;
		}
		//获取默认的左侧记录格式
		function getDeafultRecordLeftFormat() {
			return '<DIV><A id=field urn=name=opinion&amp;link=true>&lt;意见内容&gt;</A></DIV><DIV style="TEXT-ALIGN: right; PADDING-RIGHT: 50px; PADDING-TOP: 10px"><A id=field urn=name=created&amp;link=true&amp;fieldFormat=yyyy-MM-dd>&lt;填写时间&gt;</A></DIV>';
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
			<td nowrap="nowrap">意见类型：</td>
			<td><ext:field property="opinionType"/></td>
		</tr>
	</table>
</ext:form>