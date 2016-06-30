<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
	   <td class="tdtitle" nowrap="nowrap">权利人名称</td>
	   <td class="tdcontent" colspan="3"><ext:field property="personName"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">土地坐落</td>
	   <td class="tdcontent" ><ext:field property="location"/></td>
	   <td class="tdtitle" nowrap="nowrap">登记类型</td>
	   <td class="tdcontent" ><ext:field property="registerType"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">土地证号</td>
	   <td class="tdcontent"><ext:field property="certificateNum"/></td>
	   <td class="tdtitle" nowrap="nowrap">变更前土地证号</td>
	   <td class="tdcontent"><ext:field property="oldCertificateNum"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">使用权面积</td>
	   <td class="tdcontent"><ext:field property="area"/></td>
	   <td class="tdtitle" nowrap="nowrap">使用权类型</td>
	   <td class="tdcontent"><ext:field property="userType"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">登记时间</td>
	   <td class="tdcontent" colspan="3"><ext:field property="registerDate"/></td>
	</tr>
</table>