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
			var directoryId = StringUtils.getPropertyValue(recordListProperties, "directoryId");
			var directoryName = StringUtils.getPropertyValue(recordListProperties, "directoryName");
			if(directoryId!="-1") {
				document.getElementsByName("directorySelect")[1].click();
				document.getElementsByName("directoryName")[0].value = directoryName;
				document.getElementsByName("directoryId")[0].value = directoryId;
			}
			var type = StringUtils.getPropertyValue(recordListProperties, "type");
			if(type!="") {
				var directoryTypes = document.getElementsByName("directoryType");
				for(var i=0; i<directoryTypes.length; i++) {
					if(directoryTypes[i].value==type) {
						directoryTypes[i].checked = true;
						break;
					}
				}
			}
		}
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			var directoryId, directoryName;
			if(document.getElementsByName("directorySelect")[0].checked) {
				directoryName = "当前目录";
				directoryId = "-1";
			}
			else {
			 	directoryName = document.getElementsByName("directoryName")[0].value;
				if(directoryName=='') {
					alert("上级目录不能为空");
					return "ERROR";
				}
				directoryId = document.getElementsByName("directoryId")[0].value;
			}
			return "directoryId=" + directoryId + "&directoryName=" + directoryName + "&type=" + getDirectoryType();
		}
		//获取默认的左侧记录格式
		function getDeafultRecordLeftFormat() {
			return '<a id="field" urn="name=directoryName&link=true">&lt;目录名称&gt;</a>';
		}
		//获取配置元素标题,由继承者实现
		function getElementTitle(isSearchResults, isRssChannel) {
			var title = "<%=("onlineServiceDirectories".equals(request.getParameter("recordListName")) ? "网上办事" : "表格下载")%>";
			var directoryType = getDirectoryType();
			if(directoryType=="child") {
				title += "子目录列表";
			}
			else if(directoryType=="parent") {
				title += "父目录列表";
			}
			else {
				title += "兄弟目录列表";
			}
			if(document.getElementsByName("directorySelect")[1].checked) {
				title += ":" + document.getElementsByName("directoryName")[0].value;
			}
			return title;
		}
		function getDirectoryType() { //获取目录类型
			var directoryTypes = document.getElementsByName("directoryType");
			for(var i=0; i<directoryTypes.length; i++) {
				if(directoryTypes[i].checked) {
					return directoryTypes[i].value;
				}
			}
		}
		function directorySelectChanged(mode) {
			document.getElementById("selectedDirectory").style.display = (mode=="current" ? "none" : "");
		}
	</script>
	<table border="0" cellspacing="5" cellpadding="0px">
		<tr>
			<td nowrap="nowrap" align="right">上级目录：</td>
			<td nowrap="nowrap"><ext:field property="directorySelect" onclick="directorySelectChanged(this.value)"/></td>
			<td id="selectedDirectory" style="display:none; width:185px"><ext:field property="directoryName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">目录类别：</td>
			<td colspan="2"><ext:field property="directoryType"/></td>
		</tr>
	</table>
</ext:form>