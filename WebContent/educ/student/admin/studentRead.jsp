<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<input name="physical" value="true" type="hidden" />
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
<style>
img{height:160px;width:120px}
</style>
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="sex"/></td>
		<td class="tdcontent" nowrap="nowrap" rowspan="6"  width="120" align="center">
		  <ext:empty property="imageName">请上传照片</ext:empty>
		  <ext:field writeonly="true" property="imageName"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">身份证号码（账号）</td>
		<td class="tdcontent"><ext:field writeonly="true" property="idcardNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">民族</td>
		<td class="tdcontent"><ext:field writeonly="true" property="nation"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">学号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="studentId"/></td>
		<td class="tdtitle" nowrap="nowrap">系部</td>
		<td class="tdcontent"><ext:field writeonly="true" property="department"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">专业</td>
		<td class="tdcontent"><ext:field writeonly="true" property="speciality"/></td>
		<td class="tdtitle" nowrap="nowrap">班级</td>
		<td class="tdcontent"><ext:field writeonly="true" property="stuclass"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">年级</td>
		<td class="tdcontent"><ext:field writeonly="true" property="grade"/></td>
		<td class="tdtitle" nowrap="nowrap">身份证地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="idcardAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">家庭地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="houseAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="phone"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否本学院学生</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isOurStudent"/></td>
		<td class="tdtitle" nowrap="nowrap">照片</td>
		<td class="tdcontent" colspan="2"><ext:field writeonly="true" property="images"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
		<td class="tdtitle" nowrap="nowrap">创建日期</td>
		<td class="tdcontent" colspan="2"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<ext:notEmpty property="alterDescription">
		<tr>
			<td class="tdtitle" valign="top">变更情况</td>
			<td class="tdcontent" colspan="4" style="color:red"><pre><ext:field writeonly="true" property="alterDescription"/></pre></td>
		</tr>
	</ext:notEmpty>
</table>