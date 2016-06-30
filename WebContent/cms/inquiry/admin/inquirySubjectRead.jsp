<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" align="left">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle">主题</td>
		<td class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">说明</td>
		<td class="tdcontent"><ext:field writeonly="true" property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">调查截止日期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="endTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle">调查方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isAnonymous"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">结果公示</td>
		<td class="tdcontent"><ext:field writeonly="true" property="publishResult"/></td>
	</tr>
	<ext:equal value="0" property="isQuestionnaire">
		<tr>
			<td class="tdtitle" nowrap="nowrap">投票方式</td>
			<td class="tdcontent">
				<div style="float:left">
					<ext:field writeonly="true" property="inquiry.isMultiSelect" onclick="document.getElementById('divVote').style.display=(document.getElementsByName('inquiry.isMultiSelect')[1].checked ? '' : 'none');"/>
				</div>
				<div id="divVote" style="float:left;<ext:notEqual property="inquiry.isMultiSelect" value="1">display: none;</ext:notEqual>">
					&nbsp;&nbsp;最低投票数：<ext:field writeonly="true" property="inquiry.minVote"/>
					&nbsp;最高投票数：<ext:field writeonly="true" property="inquiry.maxVote"/>
				</div>
			</td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">IP限制</td>
		<td class="tdcontent">
			<ext:field writeonly="true" property="ipRestriction"/>
			<ext:equal property="ipRestriction" value="1">&nbsp;IP限制时间：<ext:field writeonly="true" property="ipRestrictionHours"/>小时</ext:equal>
		</td>
	</tr>
	<tr>
		<td class="tdtitle">创建人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
</table>