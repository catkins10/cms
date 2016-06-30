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
				<td class="tdcontent" colspan="5"><ext:field property="subject"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">原号<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field property="sn"/></td>
				<td class="tdtitle" nowrap="nowrap">部委号</td>
				<td class="tdcontent"><ext:field property="unitCode"/></td>
				<td class="tdtitle" nowrap="nowrap">榕机密收<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field property="sequence"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">密级<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field property="securityLevel"/></td>
				<td class="tdtitle" nowrap="nowrap">等级<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field property="telegramLevel"/></td>
				<td class="tdtitle" nowrap="nowrap">电报类型<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field property="category"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">发电单位<span style="color:#ff0000">*</span></td>
				<td class="tdcontent" colspan="3" title="发电单位"><ext:field property="fromUnitNames"/></td>
				<td class="tdtitle" nowrap="nowrap">来电台家</td>
				<td class="tdcontent"><ext:field property="station"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">收电单位<span style="color:#ff0000">*</span></td>
				<td class="tdcontent" colspan="5" title="收电单位"><ext:field property="toUnitNames"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">页数<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field property="pages" onchange="computeTotalPages()"/></td>
				<td class="tdtitle" nowrap="nowrap">份数<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field property="telegramNumber" readonly="true" onchange="computeTotalPages()"/></td>
				<td class="tdtitle" nowrap="nowrap">总页数<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field property="totalPages" readonly="true"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap" valign="top">内容摘要</td>
				<td class="tdcontent" colspan="5"><ext:field property="summary"/></td>
			</tr>
			<tr>
			    <td class="tdtitle" nowrap="nowrap">正文</td>
			    <td class="tdcontent" colspan="5"><ext:field property="attachments"/></td>
		    </tr>
		    <tr class="readonlyrow">
			    <td class="tdtitle" nowrap="nowrap">是否退报</td>
			    <td class="tdcontent" colspan="5"><ext:field property="needReturn"/></td>
		    </tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">接收人<span style="color:#ff0000">*</span></td>
				<td class="tdcontent"><ext:field property="receiverName"/></td>
				<td class="tdtitle" nowrap="nowrap">接收时间<span style="color:#ff0000">*</span></td>
				<td class="tdcontent" colspan="3"><ext:field property="receiveTime"/></td>
			</tr>
			<tr class="readonlyrow">
				<td class="tdtitle" nowrap="nowrap">登记人</td>
				<td class="tdcontent"><ext:field property="creator"/></td>
				<td class="tdtitle" nowrap="nowrap">登记时间</td>
				<td class="tdcontent" colspan="3"><ext:field property="created"/></td>
			</tr>
			<ext:notEmpty property="archiveTime">
				<tr class="readonlyrow">
					<td class="tdtitle" nowrap="nowrap">归档人</td>
					<td class="tdcontent"><ext:field property="archivePersonName"/></td>
					<td class="tdtitle" nowrap="nowrap">归档时间</td>
					<td class="tdcontent" colspan="3"><ext:field property="archiveTime"/></td>
				</tr>
			</ext:notEmpty>
			<tr>
				<td class="tdtitle" nowrap="nowrap">备注</td>
				<td class="tdcontent" colspan="5"><ext:field property="remark"/></td>
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
		<ext:equal value="true" property="signEnabled">
			<jsp:include page="signsEdit.jsp" />
		</ext:equal>
		<ext:equal value="false" property="signEnabled">
			<jsp:include page="signsRead.jsp" />
		</ext:equal>
	</ext:tabBody>
	
	<ext:tabBody tabId="signOpinion">
		<ext:equal value="true" property="signEnabled">
			<jsp:include page="../opinionsEdit.jsp" />
		</ext:equal>
		<ext:equal value="false" property="signEnabled">
			<jsp:include page="../opinionsRead.jsp" />
		</ext:equal>
	</ext:tabBody>
</ext:tab>