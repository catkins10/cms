<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertColumns">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties=="") {
				return;
			}
			var siteId = StringUtils.getPropertyValue(recordListProperties, "siteId");
			var siteName = StringUtils.getPropertyValue(recordListProperties, "siteName");
			if(siteId!='-1') {
				document.getElementsByName("siteSelect")[1].click();
				document.getElementsByName("siteName")[0].value = siteName;
				document.getElementsByName("siteId")[0].value = siteId;
			}
			var type = StringUtils.getPropertyValue(recordListProperties, "type");
			if(type!="") {
				var columnTypes = document.getElementsByName("columnType");
				for(var i=0; i<columnTypes.length; i++) {
					if(columnTypes[i].value==type) {
						columnTypes[i].checked = true;
						break;
					}
				}
			}
		}
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			var siteId, siteName;
			if(document.getElementsByName("siteSelect")[0].checked) {
				siteName = "本站点/栏目";
				siteId = "-1";
			}
			else {
			 	siteName = document.getElementsByName("siteName")[0].value;
				if(siteName=='') {
					alert("站点/栏目不能为空");
					return "ERROR";
				}
				siteId = document.getElementsByName("siteId")[0].value;
			}
			return "siteId=" + siteId + "&siteName=" + siteName + "&type=" + getColumnType();
		}
		
		//获取默认的左侧记录格式
		function getDeafultRecordLeftFormat() {
			return '<a id="field" urn="name=directoryName&link=true">&lt;栏目名称&gt;</a>';
		}
		//获取配置元素标题,由继承者实现
		function getElementTitle(isSearchResults, isRssChannel) {
			var columnType = getColumnType();
			if(columnType=="child") {
				columnType = "子栏目列表";
			}
			else if(columnType=="parentOnly") {
				columnType = "父栏目列表";
			}
			else if(columnType=="parent") {
				columnType = "父栏目及其兄弟栏目列表";
			}
			else {
				columnType = "兄弟栏目列表";
			}
			var elementTitle = columnType;
			if(document.getElementsByName("siteSelect")[1].checked) {
				elementTitle += ":" + document.getElementsByName("siteName")[0].value;
			}
			return elementTitle;
		}
		function getColumnType() { //获取栏目类型
			var columnTypes = document.getElementsByName("columnType");
			for(var i=0; i<columnTypes.length; i++) {
				if(columnTypes[i].checked) {
					return columnTypes[i].value;
				}
			}
		}
		function siteSelectChanged(mode) {
			document.getElementById("selectedSite").style.display = (mode=="current" ? "none" : "");
		}
	</script>
	<table border="0" cellspacing="5" cellpadding="0px">
		<tr valign="middle">
			<td nowrap="nowrap" align="right">站点/栏目：</td>
			<td nowrap="nowrap"><ext:field property="siteSelect" onclick="siteSelectChanged(value)"/></td>
			<td id="selectedSite" style="display:none; width:185px;"><ext:field property="siteName"/></td>
		</tr>
		<tr valign="middle">
			<td nowrap="nowrap" align="right">栏目类别：</td>
			<td colspan="2"><ext:field property="columnType"/></td>
		</tr>
	</table>
</ext:form>