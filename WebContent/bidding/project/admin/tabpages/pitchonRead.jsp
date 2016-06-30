<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标候选人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="rankingEnterpriseNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="pitchon.pitchonEnterprise"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标金额</td>
		<td class="tdcontent"><ext:field writeonly="true" property="pitchon.pitchonMoney"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">正文</td>
		<td class="tdcontent"><ext:field writeonly="true" property="pitchon.body"/></td>
	</tr>
</table>