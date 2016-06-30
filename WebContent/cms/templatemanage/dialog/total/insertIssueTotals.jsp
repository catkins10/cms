<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertIssueTotals">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
	//获取配置元素标题,由继承者实现
	function getElementTitle(isSearchResults, isRssChannel) {
		return "发布排行:" + document.getElementsByName("parentOrgNames")[0].value;
	}
	//显示扩展属性,由继承者实现
	function showExtendProperties(recordListProperties, recordListTextContent) {
		document.getElementsByName("parentOrgIds")[0].value = StringUtils.getPropertyValue(recordListProperties, "parentOrgIds");
		document.getElementsByName("parentOrgNames")[0].value = StringUtils.getPropertyValue(recordListProperties, "parentOrgNames");
	}
	
	//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
	function getExtendPropertiesAsText() {
		var parentOrgNames = document.getElementsByName("parentOrgNames")[0].value;
		if(parentOrgNames=="") {
			alert("上级机构未设置");
			return "ERROR";
		}
		var ret = "parentOrgIds=" + document.getElementsByName("parentOrgIds")[0].value +
				  "&parentOrgNames=" + StringUtils.encodePropertyValue(parentOrgNames);
		return ret;
	}
	
	//获取默认的左侧记录格式
	function getDeafultRecordLeftFormat() {
		return '<a id="field" urn="name=unitName">&lt;单位名称&gt;</a>';
	}
	//获取默认的右侧记录格式
	function getDeafultRecordRightFormat() {
		return '<a id="field" urn="name=issueCount">&lt;发布统计&gt;</a>';
	}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px" style="table-layout:fixed">
		<tr valign="middle" style="<%=("true".equals(request.getParameter("searchResults")) ? "display:none" : "")%>">
			<td nowrap="nowrap" align="right">上级机构：</td>
			<td width="100%"><ext:field property="parentOrgNames"/></td>
		</tr>
	</table>
</ext:form>