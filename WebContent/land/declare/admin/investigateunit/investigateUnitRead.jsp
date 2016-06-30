<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位名称</td>
		<td class="tdcontent" colspan="3"><ext:field property="unitName" writeonly="true"/></td>
	</tr>
	<tr>
		 <td class="tdtitle" nowrap="nowrap">资质类别和等级</td>
	   <td class="tdcontent" colspan="3"><ext:field property="qualificationTypeLeve" writeonly="true"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">法定代表人</td>
	   <td class="tdcontent"><ext:field property="leader" writeonly="true"/></td>
	   <td class="tdtitle" nowrap="nowrap">证书编号</td>
	   <td class="tdcontent"><ext:field property="certificateNum" writeonly="true"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">住所</td>
	   <td class="tdcontent"><ext:field property="residence" writeonly="true"/></td>
	   <td class="tdtitle" nowrap="nowrap">邮编</td>
	   <td class="tdcontent"><ext:field property="postal" writeonly="true"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">联系电话</td>
	   <td class="tdcontent"><ext:field property="tel" writeonly="true"/></td>
	   <td class="tdtitle" nowrap="nowrap">所属省份</td>
	   <td class="tdcontent"><ext:field property="provinces" writeonly="true"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">申请类型</td>
	   <td class="tdcontent"><ext:field property="applyType" writeonly="true"/></td>
	   <td class="tdtitle" nowrap="nowrap">发证机关</td>
	   <td class="tdcontent"><ext:field property="issuingUnit" writeonly="true"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">有效期起</td>
	   <td class="tdcontent"><ext:field property="validFrom" writeonly="true"/></td>
	   <td class="tdtitle" nowrap="nowrap">有效期止</td>
	   <td class="tdcontent"><ext:field property="validEnd" writeonly="true"/></td>
	</tr>
</table>