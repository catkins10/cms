<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目全称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">项目地址</td>
		<td class="tdcontent"><ext:field property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属行业</td>
		<td class="tdcontent"><ext:field property="industry"/></td>
		<td class="tdtitle" nowrap="nowrap">所属子行业</td>
		<td class="tdcontent"><ext:field property="childIndustry"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在区域/开发区</td>
		<td class="tdcontent"><ext:field property="area"/></td>
		<td class="tdtitle" nowrap="nowrap">建设周期</td>
		<td class="tdcontent"><ext:field property="cycle"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">承办单位</td>
		<td class="tdcontent"><ext:field property="managingUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">项目负责人</td>
		<td class="tdcontent"><ext:field property="leader"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人及电话</td>
		<td class="tdcontent" colspan="3"><ext:field property="linkman"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">总投资(万元)</td>
		<td class="tdcontent"><ext:field property="investment"/></td>
		<td class="tdtitle" nowrap="nowrap">币种</td>
		<td class="tdcontent"><ext:field property="currency"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">利用外资(万元)</td>
		<td class="tdcontent"><ext:field property="foreignInvestment"/></td>
		<td class="tdtitle" nowrap="nowrap">利用外资方式</td>
		<td class="tdcontent"><ext:field property="investMode"/></td>
	</tr>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">投资情况详情</td>
		<td class="tdcontent" colspan="3"><ext:field property="investmentDetail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目建设理由及条件</td>
		<td class="tdcontent" colspan="3"><ext:field property="reason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目建设规模和内容</td>
		<td class="tdcontent" colspan="3"><ext:field property="scale"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目经济效益分析</td>
		<td class="tdcontent" colspan="3"><ext:field property="benefit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">配套的优惠措施</td>
		<td class="tdcontent" colspan="3"><ext:field property="preferential"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">前期工作进展情况</td>
		<td class="tdcontent" colspan="3"><ext:field property="progress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
	</tr>
</table>