<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">类别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
		<td class="tdtitle" nowrap="nowrap">重要性</td>
		<td class="tdcontent"><ext:field writeonly="true" property="important"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开始时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="beginTime"/></td>
		<td class="tdtitle" nowrap="nowrap">结束时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="endTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办结时间</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="completeTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办理人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="handPersonNames"/></td>
		<td class="tdtitle" nowrap="nowrap">交办人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="personName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">描述</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top" style="padding-top:5px">办理反馈</td>
		<td class="tdcontent" colspan="3" style="line-height:16px">
			<ext:iterate id="personFeedback" name="hand" property="handPersons">
				<logic:notEmpty name="personFeedback" property="feedback">
					<ext:write name="personFeedback" property="feedback"/>(<ext:write name="personFeedback" property="personName"/>, <ext:write name="personFeedback" property="feedbackTime" format="yyyy-MM-dd HH:mm"/>)<br>
				</logic:notEmpty>
			</ext:iterate>
		</td>
	</tr>
</table>