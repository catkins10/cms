<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
		
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">编号</td>
		<td class="tdcontent"><ext:field property="serialNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:field property="sex"/></td>
		<td class="tdtitle" nowrap="nowrap">出生日期</td>
		<td class="tdcontent"><ext:field property="birthday"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">籍贯</td>
		<td class="tdcontent"><ext:field property="nativePlace"/></td>
		<td class="tdtitle" nowrap="nowrap">民族</td>
		<td class="tdcontent"><ext:field property="nation"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">血型</td>
		<td class="tdcontent"><ext:field property="bloodType"/></td>
		<td class="tdtitle" nowrap="nowrap">健康状况</td>
		<td class="tdcontent"><ext:field property="health"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">婚姻状况</td>
		<td class="tdcontent"><ext:field property="maritalStatus"/></td>
		<td class="tdtitle" nowrap="nowrap">身份证号码</td>
		<td class="tdcontent"><ext:field property="identityCard"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">政治面貌</td>
		<td class="tdcontent"><ext:field property="politicalStatus"/></td>
		<td class="tdtitle" nowrap="nowrap">入司时间</td>
		<td class="tdcontent"><ext:field property="joinedDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">参加工作时间</td>
		<td class="tdcontent"><ext:field property="employedDate"/></td>
		<td class="tdtitle" nowrap="nowrap">家庭住址</td>
		<td class="tdcontent"><ext:field property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
		<td class="tdtitle" nowrap="nowrap">EMAIL</td>
		<td class="tdcontent"><ext:field property="email"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">技术职称或等级</td>
		<td class="tdcontent"><ext:field property="level"/></td>
		<td class="tdtitle" nowrap="nowrap">所在部门</td>
		<td class="tdcontent"><ext:field property="department"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">职务</td>
		<td class="tdcontent"><ext:field property="duty"/></td>
		<td class="tdtitle" nowrap="nowrap">学历</td>
		<td class="tdcontent"><ext:field property="education"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">毕业院校</td>
		<td class="tdcontent"><ext:field property="school"/></td>
		<td class="tdtitle" nowrap="nowrap">毕业时间</td>
		<td class="tdcontent"><ext:field property="graduationDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">学习专业</td>
		<td class="tdcontent"><ext:field property="specialty"/></td>
		<td class="tdtitle" nowrap="nowrap">工作状态</td>
		<td class="tdcontent"><ext:field property="dutyStatus"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>