<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertSiteRecordList">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
		//获取配置元素标题,由继承者实现
		function getElementTitle(isSearchResults, isRssChannel) {
			
		}
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties=="") {
				return;
			}
			var siteNames =  StringUtils.getPropertyValue(recordListProperties, "siteNames");
			if(siteNames!="") {
				document.getElementsByName("siteNames")[0].value = siteNames;
				document.getElementsByName("siteIds")[0].value = StringUtils.getPropertyValue(recordListProperties, "siteIds");
				document.getElementsByName("siteSelect")[1].checked = true;
				onSiteSelectChanged('other');
				document.getElementsByName("containChildren")[0].checked = "true"==StringUtils.getPropertyValue(recordListProperties, "containChildren");
			}
		}
		
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			if(!document.getElementsByName("siteSelect")[1].checked) {
				return "";
			}
			if(document.getElementsByName("siteNames")[0].value=="") {
				alert('站点不能为空');
				return "ERROR";
			}
			return "siteIds=" + document.getElementsByName("siteIds")[0].value +
				   "&siteNames=" + document.getElementsByName("siteNames")[0].value +
				   (document.getElementsByName("containChildren")[0].checked ? "&containChildren=true" : "");
		}
		function onSiteSelectChanged(mode) {
			document.getElementById("tdSelectedSites").style.display = document.getElementById("tdContainChildren").style.display = (mode=="current" ? "none" : "");
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td nowrap="nowrap">站点选择：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td nowrap="nowrap"><ext:field property="siteSelect" onclick="onSiteSelectChanged(value)"/></td>
						<td id="tdSelectedSites" style="display:none; width:360px"><ext:field property="siteNames"/></td>
						<td id="tdContainChildren" style="display:none;" nowrap="nowrap"><ext:field property="containChildren"/></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</ext:form>