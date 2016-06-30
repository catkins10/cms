<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
	request.setAttribute("editabled", "true");
%>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:write property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:write property="sex"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">毕业学校</td>
		<td class="tdcontent"><ext:write property="school"/></td>
		<td class="tdtitle" nowrap="nowrap" >身份证号</td>
		<td class="tdcontent"><ext:write property="idCard"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中考成绩（应届生填）</td>
		<td class="tdcontent"><ext:write property="score"/></td>
		<td class="tdtitle" nowrap="nowrap">准考证号（应届生填）</td>
		<td class="tdcontent"><ext:write property="candidateNo"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在地</td>
		<td class="tdcontent">
		<ext:write property="province"/>
		<ext:write property="city"/>
		<ext:write property="country"/></td>
		<td class="tdtitle" nowrap="nowrap">家庭住址</td>
		<td class="tdcontent"><ext:write property="addr"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">邮政编码</td>
		<td class="tdcontent"><ext:write property="postalCode"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:write property="phone"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">报读专业</td>
		<td class="tdcontent"><ext:write property="major"/></td>
		<td class="tdtitle" nowrap="nowrap">状态</td>
		<td class="tdcontent"><ext:field writeonly="true" property="status"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field  property="created"/></td>
	</tr>
</table>