<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">职位名称</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">招聘人数</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">职能类别</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">月薪</td>
		<td class="tdtitle" nowrap="nowrap" width="70px">招聘对象</td>
		<td class="tdtitle" nowrap="nowrap" width="70px">学历要求</td>
		<td class="tdtitle" nowrap="nowrap" width="70px">工作年限</td>
		<td class="tdtitle" nowrap="nowrap" width="70px">年龄</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">性别</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">工作地点</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">工作性质</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">刷新时间</td>
		<td class="tdtitle" nowrap="nowrap" width="36px">公开</td>
	</tr>
	<ext:iterate id="job" indexId="jobIndex" property="jobs">
		<tr align="center" onclick="PageUtils.editrecord('job/company', 'admin/job', '<ext:field writeonly="true" name="job" property="id"/>', 'mode=dialog,width=720,height=400')">
			<td class="tdcontent"><ext:writeNumber name="jobIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="job" property="name"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="job" property="recruitNumber"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="job" property="post"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="job" property="monthlyPayRange"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="job" property="target"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="job" property="qualification"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="job" property="workYear"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="job" property="ageRange"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="job" property="sex"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="job" property="areaNames"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="job" property="types.type"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="job" property="refreshTime"/></td>
			<td class="tdcontent"><ext:equal value="1" name="job" property="isPublic">√</ext:equal></td>
		</tr>
	</ext:iterate>
</table>