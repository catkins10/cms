<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" align="left">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">已投票用户数</td>
		<td class="tdcontent"><ext:write property="voterTotal.votePersonNumber"/>/<ext:write property="voterTotal.personNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">参与率</td>
		<td class="tdcontent"><ext:write property="voterTotal.votePercent" format="###.##%"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">已投票用户</td>
		<td class="tdcontent"><ext:write property="voterTotal.votePersonNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">未投票用户</td>
		<td class="tdcontent"><ext:write property="voterTotal.notVotePersonNames"/></td>
	</tr>
</table>