<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

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
<%	if(request.getRequestURI().indexOf("plain")==-1) {%>
		<tr>
			<td class="tdtitle" nowrap="nowrap">部委号</td>
			<td class="tdcontent"><ext:field writeonly="true" property="unitCode"/></td>
			<td class="tdtitle" nowrap="nowrap">榕机密发<span style="color:#ff0000">*</span></td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="sequence"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">密级<span style="color:#ff0000">*</span></td>
			<td class="tdcontent"><ext:field writeonly="true" property="securityLevel"/></td>
			<td class="tdtitle" nowrap="nowrap">等级<span style="color:#ff0000">*</span></td>
			<td class="tdcontent"><ext:field writeonly="true" property="telegramLevel"/></td>
			<td class="tdtitle" nowrap="nowrap">电报类型<span style="color:#ff0000">*</span></td>
			<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
		</tr>
<%	}
	else {%>
		<tr>
			<td class="tdtitle" nowrap="nowrap">部委号</td>
			<td class="tdcontent"><ext:field writeonly="true" property="unitCode"/></td>
			<td class="tdtitle" nowrap="nowrap">榕机明发<span style="color:#ff0000">*</span></td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="sequence"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">等级<span style="color:#ff0000">*</span></td>
			<td class="tdcontent"><ext:field writeonly="true" property="telegramLevel"/></td>
			<td class="tdtitle" nowrap="nowrap">电报类型<span style="color:#ff0000">*</span></td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="category"/></td>
		</tr>
<%	}%>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发电单位<span style="color:#ff0000">*</span></td>
		<td class="tdcontent" colspan="5" title="发电单位"><ext:field writeonly="true" property="fromUnitNames"/></td>
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
    <tr>
    	<td class="tdtitle" nowrap="nowrap">发电时间<span style="color:#ff0000">*</span></td>
		<td class="tdcontent" colspan="5"><ext:field writeonly="true" property="sendTime"/></td>
    </tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="5"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>