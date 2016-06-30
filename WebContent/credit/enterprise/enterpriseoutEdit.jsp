<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">登录用户名</td>
		<td class="tdcontent"><ext:field property="loginName"/></td>
		<td class="tdtitle" nowrap="nowrap">密码</td>
		<td class="tdcontent"><ext:field property="password"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">注册号</td>
		<td class="tdcontent" ><ext:field property="registCode"/></td>
		<td class="tdtitle" nowrap="nowrap">统一社会信用代码</td>
		<td class="tdcontent" ><ext:field property="creditCode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">企业名称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">登记类型</td>
		<td class="tdcontent"><ext:field property="registType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">企业类型</td>
		<td class="tdcontent"><ext:field property="type"/></td>
		<td class="tdtitle" nowrap="nowrap">外企国别</td>
		<td class="tdcontent"><ext:field property="country"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法定代表人\负责人</td>
		<td class="tdcontent"><ext:field property="person"/></td>
		<td class="tdtitle" nowrap="nowrap">投资总额(万美元)</td>
		<td class="tdcontent"><ext:field property="invest"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">注册资本(万美元)</td>
		<td class="tdcontent"><ext:field property="worth"/></td>
		<td class="tdtitle" nowrap="nowrap">实收资本(万美元)</td>
		<td class="tdcontent"><ext:field property="realWorth"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">外方认缴资本(万美元)</td>
		<td class="tdcontent" colspan="3"><ext:field property="outWorth"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
		<td class="tdtitle" nowrap="nowrap">公示联络员</td>
		<td class="tdcontent"><ext:field property="linkMan"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联络员电话</td>
		<td class="tdcontent"><ext:field property="linkTel"/></td>
		<td class="tdtitle" nowrap="nowrap">行业门类</td>
		<td class="tdcontent"><ext:field property="doorType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">行业类别</td>
		<td class="tdcontent"><ext:field property="industry"/></td>
		<td class="tdtitle" nowrap="nowrap">行业代码</td>
		<td class="tdcontent" ><ext:field property="code"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">经营范围</td>
		<td class="tdcontent"><ext:field property="businessScope"/></td>
		<td class="tdtitle" nowrap="nowrap">成立日期</td>
		<td class="tdcontent" ><ext:field property="startDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">营业期限</td>
		<td class="tdcontent"><ext:field property="limitDate"/></td>
		<td class="tdtitle" nowrap="nowrap">核准日期</td>
		<td class="tdcontent" ><ext:field property="approvalDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">住所</td>
		<td class="tdcontent" ><ext:field property="addr"/></td>
		<td class="tdtitle" nowrap="nowrap">管片工商所</td>
		<td class="tdcontent" ><ext:field property="ascription"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">片区</td>
		<td class="tdcontent" colspan="3"><ext:field property="area"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>