<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	window.onload = function() {
		if(document.getElementsByName('signupStart')[0].value=='') {
			DateTimeField.setValue('signupStart', 'hour', '0');
			DateTimeField.setValue('signupStart', 'minute', '0');
			DateTimeField.setValue('signupStart', 'second', '0');
		}
		if(document.getElementsByName('trainStart')[0].value=='') {
			DateTimeField.setValue('trainStart', 'hour', '0');
			DateTimeField.setValue('trainStart', 'minute', '0');
			DateTimeField.setValue('trainStart', 'second', '0');
		}
		if(document.getElementsByName('signupStop')[0].value=='') {
			DateTimeField.setValue('signupStop', 'hour', '23');
			DateTimeField.setValue('signupStop', 'minute', '59');
			DateTimeField.setValue('signupStop', 'second', '59');
		}
		if(document.getElementsByName('trainStop')[0].value=='') {
			DateTimeField.setValue('trainStop', 'hour', '23');
			DateTimeField.setValue('trainStop', 'minute', '59');
			DateTimeField.setValue('trainStop', 'second', '59');
		}
	}
</script>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%" class="tdcontent">
	<col valign="middle">
	<col valign="middle" width="50%" class="tdcontent">
	<tr>
		<td class="tdtitle" nowrap="nowrap">课程名称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">培训人员</td>
		<td class="tdcontent"><ext:field property="personType"/></td>
	</tr>
	<tr>
		<td valign="top" style="padding-top:6px">简介</td>
		<td colspan="3" class="tdcontent"><ext:field property="simpleIntro"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">报名开始时间</td>
		<td class="tdcontent"><ext:field property="signupStart"/></td>
		<td class="tdtitle" nowrap="nowrap">报名截止时间</td>
		<td class="tdcontent"><ext:field property="signupStop"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">培训开始时间</td>
		<td class="tdcontent"><ext:field property="trainStart"/></td>
		<td class="tdtitle" nowrap="nowrap">培训截止时间</td>
		<td class="tdcontent"><ext:field property="trainStop"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">鉴定职业</td>
		<td class="tdcontent"><ext:field property="profession"/></td>
		<td class="tdtitle" nowrap="nowrap">鉴定类型</td>
		<td class="tdcontent"><ext:field property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">鉴定级别</td>
		<td class="tdcontent" colspan="3"><ext:field property="level"/></td>
	</tr>