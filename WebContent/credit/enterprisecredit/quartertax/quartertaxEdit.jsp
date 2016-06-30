<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">基层管征单位</td>
		<td class="tdcontent" ><ext:field property="department"/></td>
		<td class="tdtitle" nowrap="nowrap">企业或单位名称</td>
		<td class="tdcontent" ><ext:field property="enterprise"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">纳税人识别号</td>
		<td class="tdcontent"><ext:field property="number"/></td>
		<td class="tdtitle" nowrap="nowrap">法定代表人或负责人姓名</td>
		<td class="tdcontent"><ext:field property="person"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">居民身份证或其他有效身份证件号码</td>
		<td class="tdcontent"><ext:field property="cardNum"/></td>
		<td class="tdtitle" nowrap="nowrap">经营地点</td>
		<td class="tdcontent"><ext:field property="addr"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">欠税税种</td>
		<td class="tdcontent"><ext:field property="taxType"/></td>
		<td class="tdtitle" nowrap="nowrap">累计欠税余额(元)</td>
		<td class="tdcontent"><ext:field property="total"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">本年欠税余额(元)</td>
		<td class="tdcontent"><ext:field property="thisYear"/></td>
		<td class="tdtitle" nowrap="nowrap">欠税时间</td>
		<td class="tdcontent"><ext:field property="oweTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位</td>
		<td class="tdcontent" colspan="3"><ext:field property="nuit"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>