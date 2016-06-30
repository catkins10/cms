<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table valign="middle" width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#2D5C7A">
			<col>
			<col width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">专业</td>
				<td class="tdcontent"><ext:field writeonly="true" property="speciality"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">类别</td>
				<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">期数</td>
				<td class="tdcontent"><ext:field writeonly="true" property="period"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">考试地点</td>
				<td class="tdcontent"><ext:field writeonly="true" property="examAddress"/></td>
			</tr>
		</table>
	</ext:tabBody>
	<ext:tabBody tabId="examinees">
		<script>
		function newExaminee() {
			DialogUtils.openDialog('<%=request.getContextPath()%>/msa/seafarer/admin/certificationExaminee.shtml?id=<ext:write property="id"/>', 500, 300);
		}
		function openExaminee(examineeId) {
			DialogUtils.openDialog('<%=request.getContextPath()%>/msa/seafarer/admin/certificationExaminee.shtml?id=<ext:write property="id"/>&examinee.id=' + examineeId, 500, 300);
		}
		</script>
		<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
			<tr align="center">
				<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
				<td class="tdtitle" nowrap="nowrap" width="50%">准考证号码</td>
				<td class="tdtitle" nowrap="nowrap" width="80px">姓名</td>
				<td class="tdtitle" nowrap="nowrap" width="80px">申考等级</td>
				<td class="tdtitle" nowrap="nowrap" width="80px">申考职务</td>
				<td class="tdtitle" nowrap="nowrap" width="80px">考试结果</td>
				<td class="tdtitle" nowrap="nowrap" width="80px">评估结果</td>
				<td class="tdtitle" nowrap="nowrap" width="50%">合格证明序列号</td>
				<td class="tdtitle" nowrap="nowrap" width="80px">发放日期</td>
				<td class="tdtitle" nowrap="nowrap" width="80px">领取日期</td>
				<td class="tdtitle" nowrap="nowrap" width="150px">身份证号码</td>
			</tr>
			<ext:iterate id="examinee" indexId="examineeIndex" property="examinees">
				<tr valign="top" align="center" onclick="openExaminee('<ext:write name="examinee" property="id"/>')">
					<td class="tdcontent"><ext:writeNumber name="examineeIndex" plus="1"/></td>
					<td class="tdcontent" align="left"><ext:write name="examinee" property="permit"/></td>
					<td class="tdcontent" align="left"><ext:write name="examinee" property="name"/></td>
					<td class="tdcontent"><ext:write name="examinee" property="level"/></td>
					<td class="tdcontent"><ext:write name="examinee" property="job"/></td>
					<td class="tdcontent"><ext:write name="examinee" property="examResult"/></td>
					<td class="tdcontent"><ext:write name="examinee" property="result"/></td>
					<td class="tdcontent"><ext:write name="examinee" property="sn"/></td>
					<td class="tdcontent"><ext:write name="examinee" property="grantDate" format="yyyy-MM-dd"/></td>
					<td class="tdcontent"><ext:write name="examinee" property="receiveDate" format="yyyy-MM-dd"/></td>
					<td class="tdcontent"><ext:write name="examinee" property="identityCard"/></td>
				</tr>
			</ext:iterate>
		</table>
	</ext:tabBody>
</ext:tab>