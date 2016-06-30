<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="email"/></td>
	</tr>
	<ext:iterate length="1" property="schoolings" id="schooling">
		<tr>
			<td class="tdtitle" nowrap="nowrap">学校</td>
			<td class="tdcontent" width="50%"><ext:field writeonly="true" name="schooling" property="school"/></td>
			<td class="tdtitle" nowrap="nowrap">专业</td>
			<td class="tdcontent" width="50%"><ext:field writeonly="true" name="schooling" property="specialty"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">班级</td>
			<td class="tdcontent" width="50%"><ext:field writeonly="true" name="schooling" property="schoolClass"/></td>
			<td class="tdtitle" nowrap="nowrap">学历</td>
			<td class="tdcontent" width="50%"><ext:field writeonly="true" name="schooling" property="qualification"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">考生号</td>
			<td class="tdcontent" width="50%"><ext:field writeonly="true" name="schooling" property="candidateNumber"/></td>
			<td class="tdtitle" nowrap="nowrap">学号</td>
			<td class="tdcontent" width="50%"><ext:field writeonly="true" name="schooling" property="studentNumber"/></td>
		</tr>
	</ext:iterate>
	<tr>
		<td class="tdtitle" nowrap="nowrap">密码</td>
		<td class="tdcontent"><ext:field property="password"/></td>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="sex"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">民族</td>
		<td class="tdcontent"><ext:field writeonly="true" property="nation"/></td>
		<td class="tdtitle" nowrap="nowrap">出生日期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="birthday"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">证件类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="identificationType"/></td>
		<td class="tdtitle" nowrap="nowrap">证件号码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="identificationNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">婚姻状况</td>
		<td class="tdcontent"><ext:field writeonly="true" property="maritalStatus"/></td>
		<td class="tdtitle" nowrap="nowrap">身高</td>
		<td class="tdcontent"><ext:field writeonly="true" property="stature"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">政治面貌</td>
		<td class="tdcontent"><ext:field writeonly="true" property="politicalStatus"/></td>
		<td class="tdtitle" nowrap="nowrap">工作年限</td>
		<td class="tdcontent"><ext:field writeonly="true" property="workYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">户口所在地</td>
		<td class="tdcontent"><ext:field writeonly="true" property="residence"/></td>
		<td class="tdtitle" nowrap="nowrap">生源所在地</td>
		<td class="tdcontent"><ext:field writeonly="true" property="studentSource"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否困难生</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isPoor"/></td>
		<td class="tdtitle" nowrap="nowrap">居住地</td>
		<td class="tdcontent"><ext:field writeonly="true" property="area"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:field writeonly="true" property="cell"/></td>
		<td class="tdtitle" nowrap="nowrap">IM</td>
		<td class="tdcontent"><ext:field writeonly="true" property="im"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">英语等级</td>
		<td class="tdcontent"><ext:field writeonly="true" property="englishLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">日语等级</td>
		<td class="tdcontent"><ext:field writeonly="true" property="japaneseLevel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">计算机水平</td>
		<td class="tdcontent"><ext:field writeonly="true" property="computerLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">驾照</td>
		<td class="tdcontent"><ext:field writeonly="true" property="drivingLicense"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">自我评价</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="selfAppraisal"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">注册时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">状态</td>
		<td class="tdcontent"><ext:field writeonly="true" property="status"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">审核时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="approvalTime"/></td>
		<td class="tdtitle" nowrap="nowrap">审核人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="approver"/></td>
	</tr>
	<ext:notEmpty property="failedReason">
		<tr>
			<td class="tdtitle" nowrap="nowrap">未通过原因</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="failedReason"/></td>
		</tr>
	</ext:notEmpty>
</table>