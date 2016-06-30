<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="../js/infopublic.js"></script>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>

<table valign="middle" border="0" cellpadding="0" cellspacing="1">
	<col align="right">
	<col width="620px">
	<tr>
		<td class="tdtitle" valign="top">名称：</td>
		<td class="tdcontent"><ext:field property="subject"/></td>
	</tr>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" nowrap="nowrap">文号：</td>
			<td class="tdcontent"><ext:field property="mark"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">发布机构：</td>
			<td class="tdcontent"><ext:field property="infoFrom" onchange="setIssueSite()"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">主题词：</td>
			<td class="tdcontent"><ext:field property="keywords"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">信息分类：</td>
		<td class="tdcontent"><ext:field property="directoryFullName" onchange="setCategory()"/></td>
	</tr>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" nowrap="nowrap">生成日期：</td>
			<td class="tdcontent"><ext:field property="generateDate" readonly="true"/></td>
		</tr>
	</ext:equal>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">所属其它目录：</td>
		<td class="tdcontent"><ext:field property="otherDirectoryFullNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">同步更新到网站栏目：</td>
		<td class="tdcontent"><ext:field property="issueSite" onclick="showSelectIssueSite()"/></td>
	</tr>
	<tr id="selectIssueSite" style="<ext:notEqual property="issueSite" value="1">display:none</ext:notEqual>">
		<td class="tdtitle" nowrap="nowrap">被同步的网站栏目：</td>
		<td class="tdcontent"><ext:field property="issueSiteNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间：</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布时间：</td>
		<td class="tdcontent"><ext:field property="issueTime"/></td>
	</tr>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" nowrap="nowrap">索引号：</td>
			<td class="tdcontent"><ext:field property="infoIndex"/></td>
		</tr>
		<tr>
			<td class="tdtitle" valign="top">内容概述：</td>
			<td class="tdcontent"><ext:field property="summarize"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">省办统计分类：</td>
		<td class="tdcontent"><ext:field property="category"/></tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent">
			<ext:iterate id="action" property="formActions">
				<input type="button" class="button" onclick="<ext:write name="action" property="execute"/>" value="<ext:write name="action" property="title"/>"/>
			</ext:iterate>
		</td>
	</tr>
	<tr style="height:360px">
		<td class="tdtitle" valign="top">内容：</td>
		<td class="tdcontent"><ext:field property="body"/></td>
	</tr>
</table>
<script>
function showSelectIssueSite() {
	document.getElementById("selectIssueSite").style.display = (document.getElementsByName("issueSite")[0].checked ? "" : "none");
}
function setIssueSite() {
	var infoFrom = document.getElementsByName('infoFrom')[0].value;
	var issueSiteNames = document.getElementsByName('issueSiteNames')[0];
	if(infoFrom=="漳州市人民政府") {
		if(issueSiteNames.value=="" || (issueSiteNames.value.indexOf("市府办文件")!=-1 && issueSiteNames.value.indexOf(",")==-1)) {
			issueSiteNames.value = "市政府文件";
			document.getElementsByName('issueSiteIds')[0].value = "20219194899900000";
		}
	}
	else if(infoFrom=="漳州市人民政府办公室") {
		if(issueSiteNames.value=="" || (issueSiteNames.value.indexOf("市政府文件")!=-1 && issueSiteNames.value.indexOf(",")==-1)) {
			issueSiteNames.value = "市府办文件";
			document.getElementsByName('issueSiteIds')[0].value = "20219194902150000";
		}
	}
}
function setCategory() {
	var mapping = [
		['机构职能类', '组织机构'],
		['政策、规范性文件类', '行政法规、规章和规范性文件'],
		['规划计划类', '规划计划'],
		['行政许可类', '行政许可'],
		['重大建设项目、为民办实事类', '重大项目'],
		['民政扶贫救灾社会保障就业类', '扶贫', '社会保障', '促进就业', '抢险救灾、优抚、救济、社会捐助'],
		['国土资源城乡建设环保能源类', '节能环保', '城乡建设和管理', '国土资源'],
		['科教文体卫生类', '文体教育', '医疗', '公共卫生'],
		['安全生产、应急管理类', '安全生产', '应急管理']
	];
	document.getElementsByName("category")[0].value = "";
	for(var i=0; i<mapping.length; i++) {
		for(var j=1; j<mapping[i].length; j++) {
			var directoryFullName = document.getElementsByName("directoryFullName")[0].value;
			if(directoryFullName.indexOf("/" + mapping[i][j])!=-1 || directoryFullName.indexOf(" " + mapping[i][j])!=-1) {
				document.getElementsByName("category")[0].value = mapping[i][0];
				break;
			}
		}
	}
}
setIssueSite();
setCategory();
</script>