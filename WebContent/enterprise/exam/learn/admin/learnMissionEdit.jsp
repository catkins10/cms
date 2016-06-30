<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">资料</td>
		<td class="tdcontent"><ext:field property="fileName" onchange="if(document.getElementsByName('name')[0].value=='')document.getElementsByName('name')[0].value=value;"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">任务名称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">学习时间</td>
		<td class="tdcontent">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><ext:field property="beginTime"/></td>
					<td>&nbsp;至&nbsp;</td>
					<td><ext:field property="endTime"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">时长(分钟)</td>
		<td class="tdcontent"><ext:field property="learnTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">参加人员</td>
		<td class="tdcontent"><ext:field property="learners.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>