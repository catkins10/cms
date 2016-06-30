<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="50%">
			<col>
			<col width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">客商姓名</td>
				<td class="tdcontent"><ext:field property="name"/></td>
				<td class="tdtitle" nowrap="nowrap">公司名称</td>
				<td class="tdcontent"><ext:field property="company"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">公司职务</td>
				<td class="tdcontent"><ext:field property="duty"/></td>
				<td class="tdtitle" nowrap="nowrap">其他职衔</td>
				<td class="tdcontent"><ext:field property="otherDuties"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所在国别或地区</td>
				<td class="tdcontent"><ext:field property="country"/></td>
				<td class="tdtitle" nowrap="nowrap">行业类别</td>
				<td class="tdcontent"><ext:field property="industry"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">经营范围</td>
				<td class="tdcontent"><ext:field property="businessScope"/></td>
				<td class="tdtitle" nowrap="nowrap">公司地址</td>
				<td class="tdcontent"><ext:field property="address"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">电话</td>
				<td class="tdcontent"><ext:field property="tel"/></td>
				<td class="tdtitle" nowrap="nowrap">传真</td>
				<td class="tdcontent"><ext:field property="fax"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">手机</td>
				<td class="tdcontent"><ext:field property="mobile"/></td>
				<td class="tdtitle" nowrap="nowrap">电子邮件</td>
				<td class="tdcontent"><ext:field property="mail"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">网址</td>
				<td class="tdcontent" colspan="3"><ext:field property="webSite"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">已投资区域</td>
				<td class="tdcontent"><ext:field property="investmentAreas"/></td>
				<td class="tdtitle" nowrap="nowrap">已投资项目</td>
				<td class="tdcontent"><ext:field property="investmentPorjects"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">投资意向</td>
				<td class="tdcontent" colspan="3"><ext:field property="investmentOrder"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">公司背景资料</td>
				<td class="tdcontent" colspan="3"><ext:field property="background"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">规模</td>
				<td class="tdcontent"><ext:field property="enterpriseScale"/></td>
				<td class="tdtitle" nowrap="nowrap">公司成立时间</td>
				<td class="tdcontent"><ext:field property="companyCreated"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">备注</td>
				<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
			</tr>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="linkman">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">联络人</td>
				<td class="tdcontent"><ext:field property="linkman"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">公司职务</td>
				<td class="tdcontent"><ext:field property="linkmanDuty"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">电话</td>
				<td class="tdcontent"><ext:field property="linkmanTel"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">传真</td>
				<td class="tdcontent"><ext:field property="linkmanFax"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">手机</td>
				<td class="tdcontent"><ext:field property="linkmanMobile"/></td>
			</tr>
		</table>
	</ext:tabBody>
</ext:tab>