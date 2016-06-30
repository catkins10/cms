<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col align="left">
	<col width="50%">
	<col align="left">
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">线路名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">票价</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ticketPrice"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">夏季下行首、末班车时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<ext:field writeonly="true" property="summerDownlinkFirst"/>
					</td>
					<td>&nbsp;至&nbsp;</td>
					<td>
						<ext:field writeonly="true" property="summerDownlinkLast"/>
					</td>
				</tr>
			</table>
		</td>
		<td class="tdtitle" nowrap="nowrap">冬季下行首、末班车时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<ext:field writeonly="true" property="winterDownlinkFirst"/>
					</td>
					<td>&nbsp;至&nbsp;</td>
					<td>
						<ext:field writeonly="true" property="winterDownlinkLast"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">夏季上行首、末班车时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<ext:field writeonly="true" property="summerUplinkFirst"/>
					</td>
					<td>&nbsp;至&nbsp;</td>
					<td>
						<ext:field writeonly="true" property="summerUplinkLast"/>
					</td>
				</tr>
			</table>
		</td>
		<td class="tdtitle" nowrap="nowrap">冬季上行首、末班车时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<ext:field writeonly="true" property="winterUplinkFirst"/>
					</td>
					<td>&nbsp;至&nbsp;</td>
					<td>
						<ext:field writeonly="true" property="winterUplinkLast"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">下行线路长度(公里)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="downlinkDistance"/></td>
		<td class="tdtitle" nowrap="nowrap">上行线路长度(公里)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="uplinkDistance"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">配车总数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="busTotal"/></td>
		<td class="tdtitle" nowrap="nowrap">班次</td>
		<td class="tdcontent"><ext:field writeonly="true" property="dutyNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">高峰发车间隔(分钟)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="peakFrequency"/></td>
		<td class="tdtitle" nowrap="nowrap">平峰发车间隔(分钟)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="commonFrequency"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">下行主要线路</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="downlinkLine"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">上行主要线路</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="uplinkLine"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap"valign="top">下行途经站点</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="downlinkStationNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap"valign="top">上行途经站点</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="uplinkStationNames"/></td>
	</tr>
</table>