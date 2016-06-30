<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标候选人</td>
		<td class="tdcontent"><ext:field property="rankingEnterpriseNames" onchange="setPitchonEnterprise()"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标人</td>
		<td class="tdcontent"><ext:field property="pitchon.pitchonEnterprise"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标金额(元)</td>
		<td class="tdcontent"><ext:field property="pitchon.pitchonMoney"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">正文</td>
		<td class="tdcontent"><ext:field property="pitchon.body"/></td>
	</tr>
</table>

<script>
	function setPitchonEnterprise() {
		var rankingSignUpIds = document.getElementsByName("rankingSignUpIds")[0].value.split(",");
		var rankingEnterpriseNames = document.getElementsByName("rankingEnterpriseNames")[0].value.split(",");
		if(rankingSignUpIds[0]=="") {
			return;
		}
		document.getElementsByName("pitchon.pitchonEnterpriseId")[0].value = rankingSignUpIds[0];
		document.getElementsByName("pitchon.pitchonEnterprise")[0].value = rankingEnterpriseNames[0];
	}
</script>