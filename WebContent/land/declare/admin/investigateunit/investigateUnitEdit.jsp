<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位名称</td>
		<td class="tdcontent" colspan="3"><ext:field property="unitName"/></td>
	</tr>
	<tr>
		 <td class="tdtitle" nowrap="nowrap">资质类别和等级</td>
	   <td class="tdcontent" colspan="3"><ext:field property="qualificationTypeLeve"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">法定代表人</td>
	   <td class="tdcontent"><ext:field property="leader"/></td>
	   <td class="tdtitle" nowrap="nowrap">证书编号</td>
	   <td class="tdcontent"><ext:field property="certificateNum"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">住所</td>
	   <td class="tdcontent"><ext:field property="residence"/></td>
	   <td class="tdtitle" nowrap="nowrap">邮编</td>
	   <td class="tdcontent"><ext:field property="postal"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">联系电话</td>
	   <td class="tdcontent"><ext:field property="tel"/></td>
	   <td class="tdtitle" nowrap="nowrap">所属省份</td>
	   <td class="tdcontent"><ext:field property="provinces"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">申请类型</td>
	   <td class="tdcontent"><ext:field property="applyType"/></td>
	   <td class="tdtitle" nowrap="nowrap">发证机关</td>
	   <td class="tdcontent"><ext:field property="issuingUnit"/></td>
	</tr>
	<tr>
	   <td class="tdtitle" nowrap="nowrap">有效期起</td>
	   <td class="tdcontent"><ext:field property="validFrom"/></td>
	   <td class="tdtitle" nowrap="nowrap">有效期止</td>
	   <td class="tdcontent"><ext:field property="validEnd"/></td>
	</tr>
</table>