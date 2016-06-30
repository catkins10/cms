<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="js/site.js"></script>
<ext:iterate id="js" property="siteReferenceConfigureJs">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%><ext:write name="js"/>"></script>	
</ext:iterate>
<script>
	function selectReferenceView() {
		selectView(500, 320, "onReferenceViewSelected(\"{id}\".split(\"__\")[0], \"{id}\".split(\"__\")[1], \"{name}\")", "", "", false, false, false, false, true);
	}
	function onReferenceViewSelected(applicationName, viewName, viewTitle) {
		document.getElementsByName('applicationName')[0].value = applicationName;
		document.getElementsByName('viewName')[0].value = viewName;
		document.getElementsByName('viewTitle')[0].value = viewTitle;
		FormUtils.doAction("reference");
	}
	function siteReferenceConfigure() {
		<ext:write property="siteReferenceConfigure" filter="false"/>
	}
</script>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">简称(英文)</td>
		<td class="tdcontent"><ext:field property="shortName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">描述</td>
		<td class="tdcontent"><ext:field property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">引用目标</td>
		<td class="tdcontent"><ext:field property="viewTitle"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">参数配置</td>
		<td class="tdcontent"><ext:field property="referenceDescription"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">上级站点/栏目</td>
		<td class="tdcontent">
			<ext:equal value="true" property="changeParentDirectoryDisabled">
				<ext:field writeonly="true" property="parentDirectoryName"/>
			</ext:equal>
			<ext:equal value="false" property="changeParentDirectoryDisabled">
				<ext:field property="parentDirectoryName"/>
			</ext:equal>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否停用</td>
		<td class="tdcontent"><ext:field property="halt"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">重定向的URL</td>
		<td class="tdcontent"><ext:field property="redirectUrl"/></td>
	</tr>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
</table>