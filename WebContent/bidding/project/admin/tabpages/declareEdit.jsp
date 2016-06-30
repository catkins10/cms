<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	
	<tr>
		<td class="tdtitle" colspan="4"><b>建设单位</b></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent">
			<ext:field property="declare.owner"/>
		</td>
		<td class="tdtitle" nowrap="nowrap">单位性质</td>
		<td class="tdcontent"><ext:field property="declare.ownerType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field property="declare.ownerRepresentative"/></td>
		<td class="tdtitle" nowrap="nowrap">经办人</td>
		<td class="tdcontent"><ext:field property="declare.ownerLinkman"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="declare.ownerTel"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="4"><b>工程项目</b></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="declare.declaringProjectName"/></td>
		<td class="tdtitle" nowrap="nowrap">地点</td>
		<td class="tdcontent"><ext:field property="declare.projectAddress"/></td>	
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目性质</td>
		<td class="tdcontent"><ext:field property="declare.projectProperty"/></td>
		<td class="tdtitle" nowrap="nowrap">报建编号</td>
		<td class="tdcontent"><ext:field property="declare.declareNumber"/></td>	
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">立项文号</td>
		<td class="tdcontent"><ext:field property="declare.proposalNumber" /></td>
		<td class="tdtitle" nowrap="nowrap">批准时间</td>
		<td class="tdcontent"><ext:field property="declare.proposalDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">投资规模</td>
		<td class="tdcontent"><ext:field property="declare.invest"/></td>
		<td class="tdtitle" nowrap="nowrap">工程规模</td>
		<td class="tdcontent"><ext:field property="declare.scale"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请发布方式</td>
		<td class="tdcontent"><ext:field property="declare.award"/></td>
		<td class="tdtitle" nowrap="nowrap">拟定开竣工时间</td>
		<td class="tdcontent">
			<table border="0" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td><ext:field property="declare.beginDate"/></td>
					<td width="30px" align="center"> 至 </td>
					<td><ext:field property="declare.endDate"/></td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="4"><b>资金来源</b></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">拨款</td>
		<td class="tdcontent"><ext:field property="declare.govInvest"/></td>
		<td class="tdtitle" nowrap="nowrap">当年拨款</td>
		<td class="tdcontent"><ext:field property="declare.govInvestYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">自筹</td>
		<td class="tdcontent"><ext:field property="declare.personalInvest"/></td>
		<td class="tdtitle" nowrap="nowrap">当年自筹</td>
		<td class="tdcontent"><ext:field property="declare.personalInvestYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">外资</td>
		<td class="tdcontent"><ext:field property="declare.foriegnInvest"/></td>
		<td class="tdtitle" nowrap="nowrap">当年外资</td>
		<td class="tdcontent"><ext:field property="declare.foriegnInvestYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">其他投资</td>
		<td class="tdcontent"><ext:field property="declare.otherInvest"/></td>
		<td class="tdtitle" nowrap="nowrap">当年其他投资</td>
		<td class="tdcontent"><ext:field property="declare.otherInvestYear"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="4"><b>工程筹建情况以及基建管理机构的技术经济管理人员构成情况</b></td>
	</tr>
	<tr>
		<td class="tdcontent" colspan="4"><ext:field property="declare.prepare"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="4"><b>受理情况</b></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">收件时间</td>
		<td class="tdcontent"><ext:field property="declare.receiveTime"/></td>
		<td class="tdtitle" nowrap="nowrap">收件编号</td>
		<td class="tdcontent"><ext:field property="declare.receiveNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">核实情况及分类意见</td>
		<td class="tdcontent" colspan="3"><ext:field property="declare.verify"/></td>
	</tr>
</table>