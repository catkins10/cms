<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">学号</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="studentNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">班级</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="schoolClass"/></td>
		<td class="tdtitle" nowrap="nowrap">单位名称</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="company"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位组织机构代码</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="companyCode"/></td>
		<td class="tdtitle" nowrap="nowrap">单位性质</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="companyType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位产业</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="companySector"/></td>
		<td class="tdtitle" nowrap="nowrap">单位行业</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="companyIndustry"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">档案接收单位</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="personnelFileCompany"/></td>
		<td class="tdtitle" nowrap="nowrap">档案接收地址</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="personnelFileAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="sex"/></td>
		<td class="tdtitle" nowrap="nowrap">出生年月</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="birthday"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">毕业时间</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="graduateDate"/></td>
		<td class="tdtitle" nowrap="nowrap">入学前户口所在地</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="residence"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">民族</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="nation"/></td>
		<td class="tdtitle" nowrap="nowrap">政治面貌</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="politicalStatus"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">学制</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="schoolingLength"/></td>
		<td class="tdtitle" nowrap="nowrap">学历层次</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="qualification"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">专业</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="specialty"/></td>
		<td class="tdtitle" nowrap="nowrap">培养方式</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="trainingMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="tel"/></td>
		<td class="tdtitle" nowrap="nowrap">家庭地址</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="email"/></td>
		<td class="tdtitle" nowrap="nowrap">工作职位类别</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="jobType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">报到开始时间</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="reportBegin"/></td>
		<td class="tdtitle" nowrap="nowrap">报到截止时间</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="reportEnd"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">报到证编号</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="reportNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">通知书编号</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="noticeNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" width="100%" colspan="3"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>