<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%" class="tdcontent">
	<col valign="middle">
	<col valign="middle" width="50%" class="tdcontent">
	<tr>
		<td class="tdtitle" nowrap="nowrap">课程名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">培训人员</td>
		<td class="tdcontent"><ext:field writeonly="true" property="personType"/></td>
	</tr>
	<tr>
		<td valign="top" style="padding-top:6px">简介</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="simpleIntro"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">报名开始时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="signupStart"/></td>
		<td class="tdtitle" nowrap="nowrap">报名截止时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="signupStop"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">培训开始时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="trainStart"/></td>
		<td class="tdtitle" nowrap="nowrap">培训截止时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="trainStop"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">鉴定职业</td>
		<td class="tdcontent"><ext:field writeonly="true" property="profession"/></td>
		<td class="tdtitle" nowrap="nowrap">鉴定类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">鉴定级别</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="level"/></td>
	</tr>