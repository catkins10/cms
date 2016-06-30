<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="33%">
			<col>
			<col width="33%">
			<col>
			<col width="33%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">标题<span style="color:#ff0000">*</span></td>
				<td class="tdcontent" colspan="5"><ext:field writeonly="true" property="subject"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">原号<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field writeonly="true" property="sn"/></td>
				<td class="tdtitle" nowrap="nowrap">部委号</td>
				<td class="tdcontent"><ext:field writeonly="true" property="unitCode"/></td>
				<td class="tdtitle" nowrap="nowrap">榕机密收<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field writeonly="true" property="sequence"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">密级<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field writeonly="true" property="securityLevel"/></td>
				<td class="tdtitle" nowrap="nowrap">等级<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field writeonly="true" property="telegramLevel"/></td>
				<td class="tdtitle" nowrap="nowrap">电报类型<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">发电单位<span style="color:#ff0000">*</span></td>
				<td class="tdcontent" colspan="3" title="发电单位"><ext:field writeonly="true" property="fromUnitNames"/></td>
				<td class="tdtitle" nowrap="nowrap">来电台家</td>
				<td class="tdcontent"><ext:field writeonly="true" property="station"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">收电单位<span style="color:#ff0000">*</span></td>
				<td class="tdcontent" colspan="5" title="收电单位"><ext:field writeonly="true" property="toUnitNames"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">页数<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field writeonly="true" property="pages" onchange="computeTotalPages()"/></td>
				<td class="tdtitle" nowrap="nowrap">份数<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field writeonly="true" property="telegramNumber" readonly="true" onchange="computeTotalPages()"/></td>
				<td class="tdtitle" nowrap="nowrap">总页数<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field writeonly="true" property="totalPages" readonly="true"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap" valign="top">内容摘要</td>
				<td class="tdcontent" colspan="5"><ext:field writeonly="true" property="summary"/></td>
			</tr>
			<tr>
			    <td class="tdtitle" nowrap="nowrap">正文</td>
			    <td class="tdcontent" colspan="5"><ext:field writeonly="true" property="attachments"/></td>
		    </tr>
		    <tr class="readonlyrow">
			    <td class="tdtitle" nowrap="nowrap">是否退报</td>
			    <td class="tdcontent" colspan="5"><ext:field writeonly="true" property="needReturn"/></td>
		    </tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">接收人<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field writeonly="true" property="receiverName"/></td>
				<td class="tdtitle" nowrap="nowrap">接收时间<span style="color:#ff0000">*</span></td>
				<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="receiveTime"/></td>
			</tr>
			<tr class="readonlyrow">
				<td class="tdtitle" nowrap="nowrap">登记人</td>
				<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
				<td class="tdtitle" nowrap="nowrap">登记时间</td>
				<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="created"/></td>
			</tr>
			<ext:notEmpty property="archiveTime">
				<tr class="readonlyrow">
					<td class="tdtitle" nowrap="nowrap">归档人</td>
					<td class="tdcontent"><ext:field writeonly="true" property="archivePersonName"/></td>
					<td class="tdtitle" nowrap="nowrap">归档时间</td>
					<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="archiveTime"/></td>
				</tr>
			</ext:notEmpty>
			<tr>
				<td class="tdtitle" nowrap="nowrap">备注</td>
				<td class="tdcontent" colspan="5"><ext:field writeonly="true" property="remark"/></td>
			</tr>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="sign">
		<div style="padding-bottom:3px;">简要信息：</div>
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col valign="middle" width="60px" class="tdtitle">
			<col valign="middle" width="30%" class="tdcontent">
			<col valign="middle" width="60px" class="tdtitle">
			<col valign="middle" width="60%" class="tdcontent">
			<tr class="readonlyrow">
				<td class="tdtitle" nowrap="nowrap">标题</td>
				<td class="tdcontent" colspan="3"><ext:write property="subject"/></td>
			</tr>
			<tr class="readonlyrow">
				<td class="tdtitle" nowrap="nowrap">密级</td>
				<td class="tdcontent"><ext:write property="securityLevel"/></td>
				<td class="tdtitle" nowrap="nowrap">等级</td>
				<td class="tdcontent"><ext:write property="telegramLevel"/></td>
			</tr>
			<tr class="readonlyrow">
				<td class="tdtitle" nowrap="nowrap">来电单位</td>
				<td class="tdcontent"><ext:write property="fromUnitNames"/></td>
				<td class="tdtitle" nowrap="nowrap">收电单位</td>
				<td class="tdcontent"><ext:write property="toUnitNames"/></td>
			</tr>
		</table>
		<br/>
		<jsp:include page="signsRead.jsp" />
	</ext:tabBody>
	
	<ext:tabBody tabId="signOpinion">
		<jsp:include page="../opinionsRead.jsp" />
	</ext:tabBody>
</ext:tab>