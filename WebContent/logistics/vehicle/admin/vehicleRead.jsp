<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">车牌号码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="plateNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">车主姓名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="owner"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">车主电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerTel"/></td>
		<td class="tdtitle" nowrap="nowrap">籍贯</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerBirthplace"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">家庭住址</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="ownerAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">身份证号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerIdNumber"/></td>
		<td class="tdtitle" nowrap>行车证号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="licenceNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>随车联系人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkman"/></td>
		<td class="tdtitle" nowrap>随车联系人电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkmanTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>汽车品牌</td>
		<td class="tdcontent"><ext:field writeonly="true" property="brand"/></td>
		<td class="tdtitle" nowrap>车型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>购买时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="buyDate"/></td>
		<td class="tdtitle" nowrap>车体状况</td>
		<td class="tdcontent"><ext:field writeonly="true" property="vehicleBody"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>载重(吨)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="vehicleLoad"/></td>
		<td class="tdtitle" nowrap>车辆归属地</td>
		<td class="tdcontent"><ext:field writeonly="true" property="area"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>车长(米)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="vehicleLong"/></td>
		<td class="tdtitle" nowrap>车宽(米)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="vehicleWidth"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>主要运输路线</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="transportLines"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>空车/重车</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isEmpty"/></td>
		<td class="tdtitle" nowrap>当前位置</td>
		<td class="tdcontent">
			<a id="location.placeName_<ext:write property="id"/>">正在定位...</a>
			<div style="display:none">
				<a id="location.coordinate.latitude_<ext:write property="id"/>"></a>
				<a id="location.coordinate.longitude_<ext:write property="id"/>"></a>
			</div>
			<input type="button" class="button" value="在地图中显示" onclick="openMap('<ext:write property="id"/>', '80%', '80%', true)"/>
		</td>
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