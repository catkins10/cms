<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="50%">
			<col>
			<col width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">项目名称</td>
				<td class="tdcontent"><ext:field property="name"/></td>
				<td class="tdtitle" nowrap="nowrap">项目进展情况</td>
				<td class="tdcontent"><ext:field property="status"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">活动名称</td>
				<td class="tdcontent"><ext:field property="fairName"/></td>
				<td class="tdtitle" nowrap="nowrap">届别</td>
				<td class="tdcontent"><ext:field property="fairNumber"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">县别</td>
				<td class="tdcontent"><ext:field property="county"/></td>
				<td class="tdtitle" nowrap="nowrap">填报日期</td>
				<td class="tdcontent"><ext:field property="created"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">项目主管单位</td>
				<td class="tdcontent"><ext:field property="manageUnit"/></td>
				<td class="tdtitle" nowrap="nowrap">项目审批单位</td>
				<td class="tdcontent"><ext:field property="approvalUnit"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">项目地址</td>
				<td class="tdcontent"><ext:field property="address"/></td>
				<td class="tdtitle" nowrap="nowrap">联系人</td>
				<td class="tdcontent"><ext:field property="linkman"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">联系电话</td>
				<td class="tdcontent"><ext:field property="tel"/></td>
				<td class="tdtitle" nowrap="nowrap">传真</td>
				<td class="tdcontent"><ext:field property="fax"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">外方单位</td>
				<td class="tdcontent"><ext:field property="foreignCompany"/></td>
				<td class="tdtitle" nowrap="nowrap">中方单位</td>
				<td class="tdcontent"><ext:field property="chineseCompany"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">经营范围</td>
				<td class="tdcontent" colspan="3"><ext:field property="businessScope"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">行业</td>
				<td class="tdcontent"><ext:field property="industry"/></td>
				<td class="tdtitle" nowrap="nowrap">建设内容和规模</td>
				<td class="tdcontent"><ext:field property="enterpriseScale"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">备注</td>
				<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
			</tr>
		</table>
	</ext:tabBody>	
		
	<ext:tabBody tabId="signInvestment">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="50%">
			<col>
			<col width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">签约情况</td>
				<td class="tdcontent"><ext:field property="sign"/></td>
				<td class="tdtitle" nowrap="nowrap">总投资</td>
				<td class="tdcontent"><ext:field property="totalInvestment"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">合同外资</td>
				<td class="tdcontent"><ext:field property="bargainInvestment"/></td>
				<td class="tdtitle" nowrap="nowrap">注册资金</td>
				<td class="tdcontent"><ext:field property="registInvestment"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">外方国别地区</td>
				<td class="tdcontent"><ext:field property="country"/></td>
				<td class="tdtitle" nowrap="nowrap">投资方式</td>
				<td class="tdcontent"><ext:field property="investmentType"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">签约种类</td>
				<td class="tdcontent"><ext:field property="signCategory"/></td>
				<td class="tdtitle" nowrap="nowrap">升格时间</td>
				<td class="tdcontent"><ext:field property="upgradeDate"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">对接洽谈情况</td>
				<td class="tdcontent" colspan="3"><ext:field property="consult"/></td>
			</tr>
		</table>
	</ext:tabBody>
		
	<ext:tabBody tabId="submit">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="50%">
			<col>
			<col width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">拟报批时间</td>
				<td class="tdcontent"><ext:field property="toSubmitTime"/></td>
				<td class="tdtitle" nowrap="nowrap">拟报批类型</td>
				<td class="tdcontent"><ext:field property="toSubmitType"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">未报批项目进展说明</td>
				<td class="tdcontent"><ext:field property="toSubmitEvolve"/></td>
				<td class="tdtitle" nowrap="nowrap">批准时间</td>
				<td class="tdcontent"><ext:field property="approvalTime"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">批准总投资</td>
				<td class="tdcontent"><ext:field property="approvalTotalInvestment"/></td>
				<td class="tdtitle" nowrap="nowrap">批准合同外资</td>
				<td class="tdcontent"><ext:field property="approvalBargainInvestment"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">批准注册资金</td>
				<td class="tdcontent"><ext:field property="approvalRegistInvestment"/></td>
				<td class="tdtitle" nowrap="nowrap">企业代码</td>
				<td class="tdcontent"><ext:field property="organizationCode"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">企业名称</td>
				<td class="tdcontent"><ext:field property="companyName"/></td>
				<td class="tdtitle" nowrap="nowrap">领照时间</td>
				<td class="tdcontent"><ext:field property="getLicenseTime"/></td>
			</tr>
		</table>
	</ext:tabBody>
		
	<ext:tabBody tabId="investment">
		<html:hidden property="investmentId"/>
		<table cellpadding="0" cellspacing="3" border="0">
			<tr>
				<td>资金到达时间：</td>
				<td><ext:field property="investmentReceiveTime" style="width:130"/></td>
				<td>&nbsp;到达资金(万美元)：</td>
				<td><html:text property="investmentReceiveMoney" style="width:80"/></td>
				<td>&nbsp;已验资资金(万美元)：</td>
				<td><html:text property="investmentReceiveChecked" style="width:80"/></td>
				<td>&nbsp;&nbsp;<input type="button" class="button" value="添加" onclick="FormUtils.doAction('addInvestment')"></td>
				<td></td>
			</tr>
		</table>
		<table valign="middle" width="100%" class="table" border="1" cellpadding="0" cellspacing="1" bgcolor="BFC5CE">
			<tr>
				<td align="center" nowrap="nowrap" class="tdtitle" width="160px">时间</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="50%">到达金额(万美元)</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="50%">已验资资金(万美元)</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px"></td>
			</tr>
			<ext:iterate id="projectInvestment" property="investments">
				<tr>
					<td align="center" class="tdcontent" id="investmentReceiveTime_<ext:write name="projectInvestment" property="id"/>"><ext:write name="projectInvestment" property="receiveTime" format="yyyy-MM-dd"/></td>
					<td align="center" class="tdcontent" id="investmentReceiveMoney_<ext:write name="projectInvestment" property="id"/>"><ext:write name="projectInvestment" property="money"/></td>
					<td align="center" class="tdcontent" id="investmentReceiveChecked_<ext:write name="projectInvestment" property="id"/>"><ext:write name="projectInvestment" property="moneyChecked"/></td>
					<td class="tdcontent" align="center">
						<a style="cursor:pointer" onclick="editInvestment('<ext:write name="projectInvestment" property="id"/>');return false;">修改</a>
					 	<a style="cursor:pointer" onclick="deleteInvestment('<ext:write name="projectInvestment" property="id"/>');return false;">删除</a>
					 </td>
				</tr>
			</ext:iterate>
			<tr>
				<td align="center" class="tdcontent">合计</td>
				<td align="center" class="tdcontent"><ext:write property="receivedInvestment"/></td>
				<td class="tdcontent" nowrap="nowrap"><ext:write property="receivedChecked"/></td>
				<td class="tdcontent" nowrap="nowrap"></td>
			</tr>
		</table>
	</ext:tabBody>
		
	<ext:tabBody tabId="building">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="50%">
			<col>
			<col width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">拟开工时间</td>
				<td class="tdcontent"><ext:field property="toBuildingDate"/></td>
				<td class="tdtitle" nowrap="nowrap">开工时间</td>
				<td class="tdcontent"><ext:field property="buildingDate"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">竣工时间</td>
				<td class="tdcontent"><ext:field property="compeletTime"/></td>
				<td class="tdtitle" nowrap="nowrap"></td>
				<td class="tdcontent"></td>
			</tr>
		</table>
	</ext:tabBody>
		
	<ext:tabBody tabId="problemtab">
		<ext:notEmpty property="problems">
			<div style="float:left; width:50%; padding-right:5px;">
				<ext:iterate id="projectProblem" property="problems"><div>
					<span id="problemContent_<ext:write name="projectProblem" property="id"/>"><ext:write name="projectProblem" property="problem"/></span>
					<div style="float:right; width:100%; border-bottom:1 solid #c0c0f0; padding:5px; text-align:right">
						<span id="problemCreated_<ext:write name="projectProblem" property="id"/>"><ext:write name="projectProblem" property="created" format="yyyy-MM-dd"/></span>
						<a style="cursor:pointer" onclick="editProblem('<ext:write name="projectProblem" property="id"/>');return false;">修改</a>
					 	<a style="cursor:pointer" onclick="deleteProblem('<ext:write name="projectProblem" property="id"/>');return false;">删除</a>
						&nbsp;&nbsp;
					</div></div><br><br>
				</ext:iterate>
			</div>
		</ext:notEmpty>
		<div style="float:left; width:<ext:empty property="problems">100%</ext:empty><ext:notEmpty property="problems">50%</ext:notEmpty>">
			<html:hidden property="problemId"/>
			问题描述：<html:textarea property="problem" style="height:120px"/>
			<html:hidden property="problemCreated"/>
			<div style="float:right; padding-top:5px; padding-bottom:10px">
				<input type="button" class="button" value="提交" style="width:50px" onclick="FormUtils.doAction('addProblem')">&nbsp;&nbsp;
			</div>
		</div>
	</ext:tabBody>
</ext:tab>
<script>
function editInvestment(id) {
	document.getElementsByName("investmentId")[0].value = id;
	document.getElementsByName("investmentReceiveTime")[0].value = document.getElementById("investmentReceiveTime_" + id).innerHTML;
	document.getElementsByName("investmentReceiveMoney")[0].value = document.getElementById("investmentReceiveMoney_" + id).innerHTML;
	document.getElementsByName("investmentReceiveChecked")[0].value = document.getElementById("investmentReceiveChecked_" + id).innerHTML;
}
function deleteInvestment(id) {
	document.getElementsByName("investmentId")[0].value = id;
	FormUtils.doAction("deleteInvestment");
}
function editProblem(id) {
	document.getElementsByName("problemId")[0].value = id;
	document.getElementsByName("problem")[0].value = document.getElementById("problemContent_" + id).innerHTML;
	document.getElementsByName("problemCreated")[0].value = document.getElementById("problemCreated_" + id).innerHTML;
}
function deleteProblem(id) {
	document.getElementsByName("problemId")[0].value = id;
	FormUtils.doAction("deleteProblem");
}
</script>