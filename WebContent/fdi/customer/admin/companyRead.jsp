<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">中文名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">英文名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="englishName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="address" title="中英文，邮编"/></td>
		<td class="tdtitle" nowrap="nowrap">网站地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="webSite"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">母公司所在国家或地区</td>
		<td class="tdcontent"><ext:field writeonly="true" property="country"/></td>
		<td class="tdtitle" nowrap="nowrap">行业分类</td>
		<td class="tdcontent"><ext:field writeonly="true" property="industryNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">单位简介</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="introduction" title="包括战略布局、核心行业与产品优势、竞争力分析等"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中国区总部名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="chinaHeadOffice"/></td>
		<td class="tdtitle" nowrap="nowrap">联络方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="contact"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否世界500强企业</td>
		<td class="tdcontent"><ext:field writeonly="true" property="worldTop500"/></td>
		<td class="tdtitle" nowrap="nowrap">入选年份</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="60px"><ext:field writeonly="true" property="chosenYear"/></td>
					<td>&nbsp;排名：</td>
					<td width="60px"><ext:field writeonly="true" property="ranking"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
</table>