<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/bidding/enterprise/js/enterprise.js"></script>
<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标人</td>
		<td class="tdcontent"><ext:field property="notice.pitchonEnterprise"/></td>
		<td class="tdtitle" nowrap="nowrap">标段</td>
		<td class="tdcontent"><ext:field property="notice.stage"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标价(元)</td>
		<td class="tdcontent"><ext:field property="notice.pitchonPrice"/></td>
		<td class="tdtitle" nowrap="nowrap">总工期</td>
		<td class="tdcontent"><ext:field property="notice.timeLimit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">关键节点工期要求</td>
		<td class="tdcontent"><ext:field property="notice.keysTimeLimit"/></td>
		<td class="tdtitle" nowrap="nowrap">签订合同时限(天)</td>
		<td class="tdcontent"><ext:field property="notice.contractDays"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">签订合同地点</td>
		<td class="tdcontent"><ext:field property="notice.contractAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">工程质量</td>
		<td class="tdcontent"><ext:field property="notice.quality"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">投标日期</td>
		<td class="tdcontent"><ext:field property="notice.biddingDate"/></td>
		<td class="tdtitle" nowrap="nowrap">项目经理</td>
		<td class="tdcontent"><ext:field property="notice.manager"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标人</td>
		<td class="tdcontent"><ext:field property="notice.tenderee"/></td>
		<td class="tdtitle" nowrap="nowrap">法定代表人</td>
		<td class="tdcontent"><ext:field property="notice.tendereeRepresentative"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">通知时间</td>
		<td class="tdcontent"><ext:field property="notice.noticeDate"/></td>
		<td class="tdtitle" nowrap="nowrap">中标通知书编号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="notice.noticeNumber"/></td>
	</tr>
</table>