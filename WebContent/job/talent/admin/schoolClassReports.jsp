<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>

<ext:form action="/admin/saveSchoolClassReports">
	<ext:empty property="schoolClass">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<tr>
				<td class="tdtitle" nowrap="nowrap">班级</td>
				<td class="tdcontent" width="100%"><ext:field property="schoolClass"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">毕业时间</td>
				<td class="tdcontent" width="100%"><ext:field property="graduateDate"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">学历层次</td>
				<td class="tdcontent" width="100%"><ext:field property="qualification"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">专业</td>
				<td class="tdcontent" width="100%"><ext:field property="specialty"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">培养方式</td>
				<td class="tdcontent" width="100%"><ext:field property="trainingMode"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">报到开始时间</td>
				<td class="tdcontent" width="100%"><ext:field property="reportBegin"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">报到截止时间</td>
				<td class="tdcontent" width="100%"><ext:field property="reportEnd"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">通知书起始编号</td>
				<td class="tdcontent" width="100%"><ext:field property="noticeNumber"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">报到证起始编号</td>
				<td class="tdcontent" width="100%"><ext:field property="reportNumber"/></td>
			</tr>
		</table>
	</ext:empty>
	<ext:notEmpty property="schoolClass">
		<html:hidden property="schoolClassId"/>
		<html:hidden property="schoolClass"/>
		<table id="reportsTable" border="1" cellpadding="0" cellspacing="0" class="table">
			<tr>
				<td class="tdtitle" align="center" nowrap="nowrap" width="30px"><input type="checkbox" onclick="selectAllReports(checked)"/></td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="30px">学号</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="50px">姓名</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="220px">单位名称</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="120px">单位组织机构代码</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="110px">单位性质</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="80px">单位产业</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="200px">单位行业</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="220px">档案接收单位</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="200px">档案接收地址</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="36px">性别</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="80px">出生年月</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="80px">毕业时间</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="100px">入学前户口所在地</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="80px">民族</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="80px">政治面貌</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="50px">学制</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="50px">学历层次</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="120px">专业</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="80px">培养方式</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="100px">联系电话</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="180px">家庭地址</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="180px">电子邮箱</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="180px">工作职位类别</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="80px">报到开始时间</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="80px">报到截止时间</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="80px">报到证编号</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="80px">通知书编号</td>
				<td class="tdtitle" align="center" nowrap="nowrap" width="120px">备注</td>
			</tr>
			<ext:iterate id="report" property="reports">
				<tr>
					<td class="tdcontent" align="center"><input type="checkbox" name="selectReport" value="<ext:write name="report" property="talentId"/>"/></td>
					<td class="tdcontent"><html:hidden name="report" property="talentId"/><ext:field name="report" property="studentNumber"/></td>
					<td class="tdcontent"><ext:field name="report" property="name"/></td>
					<td class="tdcontent"><ext:field name="report" property="company"/></td>
					<td class="tdcontent"><ext:field name="report" property="companyCode"/></td>
					<td class="tdcontent"><ext:field name="report" property="companyType"/></td>
					<td class="tdcontent"><ext:field name="report" property="companySector"/></td>
					<td class="tdcontent"><ext:field name="report" property="companyIndustry"/></td>
					<td class="tdcontent"><ext:field name="report" property="personnelFileCompany"/></td>
					<td class="tdcontent"><ext:field name="report" property="personnelFileAddress"/></td>
					<td class="tdcontent"><ext:field name="report" property="sex"/></td>
					<td class="tdcontent"><ext:field name="report" property="birthday"/></td>
					<td class="tdcontent"><ext:field name="report" property="graduateDate"/></td>
					<td class="tdcontent"><ext:field name="report" property="residence"/></td>
					<td class="tdcontent"><ext:field name="report" property="nation"/></td>
					<td class="tdcontent"><ext:field name="report" property="politicalStatus"/></td>
					<td class="tdcontent"><ext:field name="report" property="schoolingLength"/></td>
					<td class="tdcontent"><ext:field name="report" property="qualification"/></td>
					<td class="tdcontent"><ext:field name="report" property="specialty"/></td>
					<td class="tdcontent"><ext:field name="report" property="trainingMode"/></td>
					<td class="tdcontent"><ext:field name="report" property="tel"/></td>
					<td class="tdcontent"><ext:field name="report" property="address"/></td>
					<td class="tdcontent"><ext:field name="report" property="email"/></td>
					<td class="tdcontent"><ext:field name="report" property="jobType"/></td>
					<td class="tdcontent"><ext:field name="report" property="reportBegin"/></td>
					<td class="tdcontent"><ext:field name="report" property="reportEnd"/></td>
					<td class="tdcontent"><ext:field name="report" property="reportNumber"/></td>
					<td class="tdcontent"><ext:field name="report" property="noticeNumber"/></td>
					<td class="tdcontent"><ext:field name="report" property="remark"/></td>
				</tr>
			</ext:iterate>
		</table>
		<script>
			function reportMonitor() {
				var reportsTable = document.getElementById('reportsTable');
				for(var i = 1; i < reportsTable.rows.length; i++) {
					var inputs = reportsTable.rows[i].getElementsByTagName('input');
					for(var j = 0; j < inputs.length; j++) {
						inputs[j].rowIndex = i;
						inputs[j].onchange = function() {
							reportsTable.rows[this.rowIndex].changed = true;
						};
						inputs[j].onfocus = function() {
							window.currentReportIndex = this.rowIndex;
						};
						inputs[j].onblur = function() {
							window.currentReportIndex = null;
							var rowIndex = this.rowIndex;
							if(!reportsTable.rows[rowIndex].changed) {
								return;
							}
							window.setTimeout(function() {
								if(window.currentReportIndex != rowIndex) {
									submitReport(reportsTable.rows[rowIndex]);
								}
							}, 300);
						};
					}
				}
			}
			reportMonitor();
			function submitReport(reportRow) {
				reportRow.changed = false;
				var data = '';
				var inputs = reportRow.getElementsByTagName('input');
				for(var i = 0; i < inputs.length; i++) {
					data += (data=="" ? "" : "&") + inputs[i].name + "=" + StringUtils.utf8Encode(inputs[i].value);
				}
				data += '&schoolClassId=' + StringUtils.utf8Encode(document.getElementsByName("schoolClassId")[0].value);
				data += '&schoolClass=' + StringUtils.utf8Encode(document.getElementsByName("schoolClass")[0].value);
				new Ajax().openRequest('POST', RequestUtils.getContextPath() + "/job/talent/admin/saveSchoolClassReports.shtml", data, true, null, null, "application/x-www-form-urlencoded;charset=utf-8");
			}
			function selectAllReports(checked) {
				var selectReports = document.getElementsByName("selectReport");
				for(var i = 0; i < selectReports.length; i++) {
					selectReports[i].checked = checked;
				}
			}
			function printReports(printNotify) {
				var talentIds = "";
				var selectReports = document.getElementsByName("selectReport");
				for(var i = 0; i < selectReports.length; i++) {
					if(selectReports[i].checked) {
						talentIds += (talentIds=="" ? "" : ",") + selectReports[i].value;
					}
				}
				if(talentIds=="") {
					alert("请选择需要打印的记录");
					return;
				}
				PageUtils.openurl(RequestUtils.getContextPath() + "/job/talent/admin/printReports.shtml?talentIds=" + talentIds + (printNotify ? "&printNotify=true" : ""));
			}
		</script>
	</ext:notEmpty>
</ext:form>