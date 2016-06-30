<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">站点选择</td>
		<td class="tdcontent">
			<input type="hidden" name="relationSiteId" value="" onchange="updateExtendParameters()">
			<script>new SelectField('<input type="text" name="relationSiteName" value="" onchange="updateExtendParameters()" readonly="readonly" class="field required" title="站点选择">', 'selectSite(600, 400, false, \'relationSiteId{id},relationSiteName{name}\', \'\', \'\', \'\', \'site\')', 'field required', 'null', 'null', '');</script>
		</td>
	</tr>
</table>
<script>
	var extendedParameters = document.getElementsByName("extendedParameters")[0].value;
	document.getElementsByName("relationSiteId")[0].value = StringUtils.getPropertyValue(extendedParameters, "relationSiteId");
	document.getElementsByName("relationSiteName")[0].value = StringUtils.getPropertyValue(extendedParameters, "relationSiteName");
	function updateExtendParameters() {
		document.getElementsByName("extendedParameters")[0].value = "relationSiteId=" + document.getElementsByName("relationSiteId")[0].value + 
																	"&relationSiteName=" + document.getElementsByName("relationSiteName")[0].value;
	}
</script>