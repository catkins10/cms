<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">货物名称</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="goodsName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">数量</td>
		<td class="tdcontent"><ext:field writeonly="true" property="quantity"/></td>
		<td class="tdtitle" nowrap="nowrap">单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="unit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">运费单价</td>
		<td class="tdcontent"><ext:field writeonly="true" property="freight"/></td>
		<td class="tdtitle" nowrap="nowrap">注意事项</td>
		<td class="tdcontent"><ext:field writeonly="true" property="notice"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>需要的车长</td>
		<td class="tdcontent"><ext:field writeonly="true" property="vehicleLong"/></td>
		<td class="tdtitle" nowrap="nowrap">需要的车宽</td>
		<td class="tdcontent"><ext:field writeonly="true" property="vehicleWidth"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>需要的车型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="vehicleType"/></td>
		<td class="tdtitle" nowrap>付款方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="paymentMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">有效期开始</td>
		<td class="tdcontent"><ext:field writeonly="true" property="validityBegin"/></td>
		<td class="tdtitle" nowrap="nowrap">有效期结束</td>
		<td class="tdcontent"><ext:field writeonly="true" property="validityEnd"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkman"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkmanTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">出发地</td>
		<td class="tdcontent"><ext:field writeonly="true" property="departureAreas"/></td>
		<td class="tdtitle" nowrap="nowrap">目的地</td>
		<td class="tdcontent"><ext:field writeonly="true" property="destinationAreas"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>登记人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap>登记人IP</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorIP"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		<td class="tdtitle" nowrap>最后修改时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="lastModified"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>