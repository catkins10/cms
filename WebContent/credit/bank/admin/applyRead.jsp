<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请人</td>
		<td class="tdcontent"><ext:write property="applyPerson"/></td>
		<td class="tdtitle" nowrap="nowrap">拟申请银行名称</td>
		<td class="tdcontent" ><ext:write property="bankName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">状态</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="status"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">企业性质</td>
		<td class="tdcontent"><ext:write property="nature"/></td>
		<td class="tdtitle" nowrap="nowrap">行业类别</td>
		<td class="tdcontent" ><ext:write property="industry"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">信用等级</td>
		<td class="tdcontent" ><ext:write property="level"/></td>
		<td class="tdtitle" nowrap="nowrap">公司地址</td>
		<td class="tdcontent" ><ext:write property="companyAddr"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">三证合一信用代码</td>
		<td class="tdcontent" ><ext:write property="code"/></td>
		<td class="tdtitle" nowrap="nowrap">营业执照号</td>
		<td class="tdcontent" ><ext:write property="licenseNo"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">组织机构代码</td>
		<td class="tdcontent" ><ext:write property="orgCode"/></td>
		<td class="tdtitle" nowrap="nowrap">法定代表人</td>
		<td class="tdcontent" ><ext:write property="legalPerson"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent" ><ext:write property="phone"/></td>
		<td class="tdtitle" nowrap="nowrap">身份证号码</td>
		<td class="tdcontent" ><ext:write property="idCard"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">总资产（万元）</td>
		<td class="tdcontent" ><ext:write property="totalMoney"/></td>
		<td class="tdtitle" nowrap="nowrap">净资产(万元)</td>
		<td class="tdcontent" ><ext:write property="cleanMoney"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">总负债(万元)</td>
		<td class="tdcontent" ><ext:write property="totalOwe"/></td>
		<td class="tdtitle" nowrap="nowrap">目前结欠贷款(万元)</td>
		<td class="tdcontent" ><ext:write property="nowOwe"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">销售收入（万元）</td>
		<td class="tdcontent" ><ext:write property="saleMoney"/></td>
		<td class="tdtitle" nowrap="nowrap">经营规模（万元）</td>
		<td class="tdcontent" ><ext:write property="scale"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请金额（万元）</td>
		<td class="tdcontent" ><ext:write property="applyMoney"/></td>
		<td class="tdtitle" nowrap="nowrap">借款性质</td>
		<td class="tdcontent" ><ext:write property="borrowNature"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">贷款方式</td>
		<td class="tdcontent" ><ext:write property="borrowType"/></td>
		<td class="tdtitle" nowrap="nowrap">借款用途</td>
		<td class="tdcontent" ><ext:write property="applyPurpose"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">借款期限自</td>
		<td class="tdcontent" ><ext:write property="applyStart"/></td>
		<td class="tdtitle" nowrap="nowrap">至</td>
		<td class="tdcontent" ><ext:write property="applyEnd"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">还款来源</td>
		<td class="tdcontent" ><ext:write property="payment"/></td>
		<td class="tdtitle" nowrap="nowrap">还款方式</td>
		<td class="tdcontent" ><ext:write property="payMethod"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证人名称1</td>
		<td class="tdcontent" ><ext:write property="guarantorf"/></td>
		<td class="tdtitle" nowrap="nowrap">保证人信用等级1</td>
		<td class="tdcontent" ><ext:write property="guarantorLevelf"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证人名称2</td>
		<td class="tdcontent" ><ext:write property="guarantors"/></td>
		<td class="tdtitle" nowrap="nowrap">保证人信用等级2</td>
		<td class="tdcontent" ><ext:write property="guarantorLevels"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证人名称3</td>
		<td class="tdcontent" ><ext:write property="guarantort"/></td>
		<td class="tdtitle" nowrap="nowrap">保证人信用等级3</td>
		<td class="tdcontent" ><ext:write property="guarantorLevelt"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证人名称4</td>
		<td class="tdcontent" ><ext:write property="guarantorfo"/></td>
		<td class="tdtitle" nowrap="nowrap">保证人信用等级4</td>
		<td class="tdcontent" ><ext:write property="guarantorLevelfo"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">抵押人（出质人）名称</td>
		<td class="tdcontent" ><ext:write property="mortgager"/></td>
		<td class="tdtitle" nowrap="nowrap">抵押物（质物）名称</td>
		<td class="tdcontent" ><ext:write property="collateral"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">抵押物（质物）具体地理位置</td>
		<td class="tdcontent" colspan="3"><ext:write property="collateralAddr"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">抵押物（质物）具体情况</td>
		<td class="tdcontent" colspan="3"><ext:write property="collateralDetail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请理由</td>
		<td class="tdcontent" colspan="3"><ext:write property="applyReason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办理意见</td>
		<td class="tdcontent" colspan="3"><ext:field property="opinions"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>