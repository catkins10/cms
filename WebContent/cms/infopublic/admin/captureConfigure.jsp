<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" src="<%=request.getContextPath()%>/cms/infopublic/js/infopublic.js"></script>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">目录选择</td>
		<td class="tdcontent">
			<input type="hidden" name="directoryId" value="" onchange="updateExtendParameters()">
			<script>new SelectField('<input type="text" name="directoryName" value="" onchange="updateExtendParameters()" readonly="readonly" class="field required" title="目录选择">', 'selectDirectory(640, 480, false, \'directoryId{id},directoryName{fullName}\', \'\', \'\', \'\', 0, \'info\')', 'field required', 'null', 'null', '');</script>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">直接发布</td>
		<td class="tdcontent">
			<input type="radio" class="radio" name="issue" value="1" checked="true" onclick="updateExtendParameters()">是&nbsp;
			<input type="radio" class="radio" name="issue" value="0" onclick="updateExtendParameters()">不是
		</td>
	</tr>
</table>
<script>
	var extendedParameters = document.getElementsByName("extendedParameters")[0].value;
	document.getElementsByName("directoryId")[0].value = StringUtils.getPropertyValue(extendedParameters, "directoryId");
	document.getElementsByName("directoryName")[0].value = StringUtils.getPropertyValue(extendedParameters, "directoryName");
	document.getElementsByName("issue")[1].checked = StringUtils.getPropertyValue(extendedParameters, "issue")!="true";
	function updateExtendParameters() {
		document.getElementsByName("extendedParameters")[0].value = "directoryId=" + document.getElementsByName("directoryId")[0].value +
																	"&directoryName=" + document.getElementsByName("directoryName")[0].value +
																	"&issue=" + document.getElementsByName("issue")[0].checked;
	}
</script>