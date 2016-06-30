<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">提问内容</td>
		<td class="tdcontent"><ext:field writeonly="true" property="question"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">答复</td>
		<td class="tdcontent"><ext:field writeonly="true" property="reply"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提问时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="askTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提问发起点</td>
		<td class="tdcontent"><ext:field writeonly="true" property="askFrom"/></td>
	</tr>
</table>
<br>
<center>
	<ext:iterate id="formAction" property="formActions">
		<ext:button nameName="formAction" propertyName="title" nameOnclick="formAction" propertyOnclick="execute"/>
	</ext:iterate>
</center>