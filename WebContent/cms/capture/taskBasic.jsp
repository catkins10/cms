<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">任务描述</td>
		<td class="tdcontent"><ext:field property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">分类</td>
		<td class="tdcontent"><ext:field property="category"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">对应的内容</td>
		<td class="tdcontent">
			<ext:empty property="businessTitle">
				<ext:field property="businessTitle" onchange="if(value!='')FormUtils.doAction('task', '', false, '', '_self')"/>
			</ext:empty>
			<ext:notEmpty property="businessTitle">
				<ext:field property="businessTitle" writeonly="true"/>
				<html:hidden property="businessTitle"/>
			</ext:notEmpty>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">被抓取页面URL</td>
		<td class="tdcontent"><ext:field property="captureURL"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">字符集</td>
		<td class="tdcontent"><ext:field property="websiteCharset"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否启用</td>
		<td class="tdcontent"><ext:field property="enabled"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">抓取安排</td>
		<td class="tdcontent">
			<div style="float:left;"><ext:field property="schedule" onclick="document.getElementById('divCaptureTime').style.display=(document.getElementsByName('schedule')[1].checked ? '' : 'none');document.getElementById('divCaptureInterval').style.display=(document.getElementsByName('schedule')[2].checked ? '' : 'none');"/></div>
			<div id="divCaptureTime" style="float:left; padding-left:5px; <ext:notEqual value="1" property="schedule">display:none</ext:notEqual>"><ext:field property="captureTime"/></div>
			<div id="divCaptureInterval" style="float:left; padding-left:5px; <ext:notEqual value="2" property="schedule">display:none</ext:notEqual>"><ext:field property="captureInterval" style="width:60px; float:left"/><span style="display:block; padding-top:1px">分钟</span></div>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
</table>