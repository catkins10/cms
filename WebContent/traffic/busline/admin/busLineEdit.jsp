<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("editabled", "true");%>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col align="left">
	<col width="50%">
	<col align="left">
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">线路名称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">票价</td>
		<td class="tdcontent"><ext:field property="ticketPrice"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">夏季下行首、末班车时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<ext:field property="summerDownlinkFirst"/>
					</td>
					<td>&nbsp;至&nbsp;</td>
					<td>
						<ext:field property="summerDownlinkLast"/>
					</td>
				</tr>
			</table>
		</td>
		<td class="tdtitle" nowrap="nowrap">冬季下行首、末班车时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<ext:field property="winterDownlinkFirst"/>
					</td>
					<td>&nbsp;至&nbsp;</td>
					<td>
						<ext:field property="winterDownlinkLast"/>
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
						<ext:field property="summerUplinkFirst"/>
					</td>
					<td>&nbsp;至&nbsp;</td>
					<td>
						<ext:field property="summerUplinkLast"/>
					</td>
				</tr>
			</table>
		</td>
		<td class="tdtitle" nowrap="nowrap">冬季上行首、末班车时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<ext:field property="winterUplinkFirst"/>
					</td>
					<td>&nbsp;至&nbsp;</td>
					<td>
						<ext:field property="winterUplinkLast"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">下行线路长度(公里)</td>
		<td class="tdcontent"><ext:field property="downlinkDistance"/></td>
		<td class="tdtitle" nowrap="nowrap">上行线路长度(公里)</td>
		<td class="tdcontent"><ext:field property="uplinkDistance"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">配车总数</td>
		<td class="tdcontent"><ext:field property="busTotal"/></td>
		<td class="tdtitle" nowrap="nowrap">班次</td>
		<td class="tdcontent"><ext:field property="dutyNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">高峰发车间隔(分钟)</td>
		<td class="tdcontent"><ext:field property="peakFrequency"/></td>
		<td class="tdtitle" nowrap="nowrap">平峰发车间隔(分钟)</td>
		<td class="tdcontent"><ext:field property="commonFrequency"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">下行主要线路</td>
		<td colspan="3" class="tdcontent"><ext:field property="downlinkLine"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">上行主要线路</td>
		<td colspan="3" class="tdcontent"><ext:field property="uplinkLine"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap"valign="top">下行途经站点</td>
		<td colspan="3" class="tdcontent"><ext:field property="downlinkStationNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap"valign="top">上行途经站点</td>
		<td colspan="3" class="tdcontent"><ext:field property="uplinkStationNames"/></td>
	</tr>
</table>