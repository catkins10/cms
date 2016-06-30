<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="../../enterprise/js/enterprise.js"></script>
<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col align="right">
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">中选代理</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentRepresentative"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentLinkman"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">公示期限(天)</td>
		<td class="tdcontent"><ext:field property="biddingAgent.publicLimit"/></td>
	</tr>
</table>