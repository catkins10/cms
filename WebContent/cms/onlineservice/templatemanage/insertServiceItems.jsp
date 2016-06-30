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
			if(directoryId!="-1" && directoryId!="") {
				document.getElementsByName("directorySelect")[1].click();
				document.getElementsByName("directoryName")[0].value = directoryName;
				document.getElementsByName("directoryId")[0].value = directoryId;
			}
		}
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			if(document.getElementsByName("directorySelect")[0].checked) {
				return "directoryName=当前目录";
			}
		 	var directoryName = document.getElementsByName("directoryName")[0].value;
			if(directoryName=='') {
				alert("上级目录不能为空");
				return "ERROR";
			}
			return "directoryId=" + document.getElementsByName("directoryId")[0].value + "&directoryName=" + directoryName;
		}
		//获取默认的左侧记录格式
		function getDeafultRecordLeftFormat() {
		 	if("<%=request.getParameter("recordListName")%>"=="onlineServiceItems") { //办理事项列表
				return '<A id=field urn="name=name&link=true">&lt;事项名称&gt;</A><br><A id=recordLink target=_blank urn=办事指南>办事指南</A>&nbsp;<A id=recordLink target=_blank urn=表格下载>表格下载</A>&nbsp;<A id=recordLink target=_blank urn=常见问题解答>常见问题解答</A>&nbsp;<A id=recordLink target=_blank urn=办理机构>办理机构</A>&nbsp;<A id=recordLink target=_blank urn=在线咨询>在线咨询</A>&nbsp;<A id=recordLink target=_blank urn=在线投诉>在线投诉</A>&nbsp;<A id=recordLink target=_blank urn=在线受理>在线受理</A>';
			}
			else { //表格列表
				return '<A id=field urn="name=tableName&link=true">&lt;表格名称&gt;</A>&nbsp;<A id=recordLink target=_blank urn=表格下载>表格下载</A>&nbsp;<A id=recordLink target=_blank urn=样表下载>样表下载</A>';			
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
	</table>
</ext:form>