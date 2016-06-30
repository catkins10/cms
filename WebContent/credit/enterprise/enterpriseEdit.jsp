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
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent" ><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">组成形式</td>
		<td class="tdcontent"><ext:field property="composition"/></td>
		<td class="tdtitle" nowrap="nowrap">资金数额(万元)</td>
		<td class="tdcontent"><ext:field property="worth"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">经营场所</td>
		<td class="tdcontent"><ext:field property="addr"/></td>
		<td class="tdtitle" nowrap="nowrap">行业门类</td>
		<td class="tdcontent"><ext:field property="doorType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">行业类别</td>
		<td class="tdcontent"><ext:field property="industry"/></td>
		<td class="tdtitle" nowrap="nowrap">行业代码</td>
		<td class="tdcontent"><ext:field property="code"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">经营范围</td>
		<td class="tdcontent"><ext:field property="businessScope"/></td>
		<td class="tdtitle" nowrap="nowrap">经营者</td>
		<td class="tdcontent"><ext:field property="person"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
		<td class="tdtitle" nowrap="nowrap">开业日期</td>
		<td class="tdcontent"><ext:field property="openDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">核准日期</td>
		<td class="tdcontent"><ext:field property="approvalDate"/></td>
		<td class="tdtitle" nowrap="nowrap">属地工商所</td>
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