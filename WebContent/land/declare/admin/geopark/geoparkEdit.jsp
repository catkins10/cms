<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">公园名称</td>
		<td class="tdcontent" ><ext:field property="parkName"/></td>
		<td class="tdtitle" nowrap="nowrap">公园位置</td>
		<td class="tdcontent" ><ext:field property="location"/></td>
	</tr>
	
	<tr>
	   <td class="tdtitle" nowrap="nowrap">级别</td>
	   <td class="tdcontent"><ext:field property="level"/></td>
	   <td class="tdtitle" nowrap="nowrap">面积</td>
	   <td class="tdcontent"><ext:field property="area"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">批准时间</td>
	   <td class="tdcontent"><ext:field property="approvalTime"/></td>
	   <td class="tdtitle" nowrap="nowrap">开园时间</td>
	   <td class="tdcontent"><ext:field property="enableTime"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">地质遗迹</td>
	   <td class="tdcontent" colspan="3"><ext:field property="geologicalHeritage"/></td>
	</tr>

</table>