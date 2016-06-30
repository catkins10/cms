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
		<td class="tdcontent"><ext:field writeonly="true" property="declare.owner" /></td>
		<td class="tdtitle" nowrap="nowrap">单位性质</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.ownerType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.ownerRepresentative"/></td>
		<td class="tdtitle" nowrap="nowrap">经办人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.ownerLinkman"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.ownerTel"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="4"><b>工程项目</b></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.declaringProjectName"/></td>
		<td class="tdtitle" nowrap="nowrap">地点</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.projectAddress"/></td>	
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目性质</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.projectProperty"/></td>
		<td class="tdtitle" nowrap="nowrap">报建编号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.declareNumber"/></td>	
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">立项文号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.proposalNumber" /></td>
		<td class="tdtitle" nowrap="nowrap">批准时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.proposalDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">投资规模</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.invest"/></td>
		<td class="tdtitle" nowrap="nowrap">工程规模</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.scale"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请发布方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.award"/></td>
		<td class="tdtitle" nowrap="nowrap">拟定开竣工时间</td>
		<td class="tdcontent">
			<ext:field writeonly="true" property="declare.beginDate"/>
			至
			<ext:field writeonly="true" property="declare.endDate"/>
		</td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="4"><b>资金来源</b></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">拨款</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.govInvest"/></td>
		<td class="tdtitle" nowrap="nowrap">当年拨款</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.govInvestYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">自筹</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.personalInvest"/></td>
		<td class="tdtitle" nowrap="nowrap">当年自筹</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.personalInvestYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">外资</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.foriegnInvest"/></td>
		<td class="tdtitle" nowrap="nowrap">当年外资</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.foriegnInvestYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">其他投资</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.otherInvest"/></td>
		<td class="tdtitle" nowrap="nowrap">当年其他投资</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.otherInvestYear"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="4"><b>工程筹建情况以及基建管理机构的技术经济管理人员构成情况</b></td>
	</tr>
	<tr>
		<td class="tdcontent" colspan="4"><ext:field writeonly="true" property="declare.prepare"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="4"><b>受理情况</b></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">收件时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.receiveTime"/></td>
		<td class="tdtitle" nowrap="nowrap">收件编号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="declare.receiveNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">核实情况及分类意见</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="declare.verify"/></td>
	</tr>
</table>