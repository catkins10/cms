<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">标题</td>
		<td colspan="3" class="tdcontent"><ext:field property="subject" onchange="parseKeyword(value, 'keyword')"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题词</td>
		<td class="tdcontent"><ext:field property="keyword"/></td>
		<td class="tdtitle" nowrap="nowrap">机关代字</td>
		<td class="tdcontent"><ext:field property="docMark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件种类</td>
		<td class="tdcontent"><ext:field property="docType"/></td>
		<td class="tdtitle" nowrap="nowrap">紧急程度</td>
		<td class="tdcontent"><ext:field property="priority"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">秘密等级</td>
		<td class="tdcontent"><ext:field property="secureLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">保密期限</td>
		<td class="tdcontent"><ext:field property="secureTerm"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">信息公开</td>
		<td class="tdcontent"><ext:field property="publicType" onchange="document.getElementsByName('publicReason')[0].disabled = (value!='不公开')"/></td>
		<td class="tdtitle" nowrap="nowrap">不公开原因</td>
		<td class="tdcontent">
			<ext:notEqual value="不公开" property="publicType"><ext:field property="publicReason" disabled="true"/></ext:notEqual>
			<ext:equal value="不公开" property="publicType"><ext:field property="publicReason"/></ext:equal>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发文字号</td>
		<td class="tdcontent"><ext:field property="docWord"/></td>
		<td class="tdtitle" nowrap="nowrap">打印份数</td>
		<td class="tdcontent"><ext:field property="printNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">拟稿人</td>
		<td class="tdcontent"><ext:field property="draftPerson"/></td>
		<td class="tdtitle" nowrap="nowrap">拟稿部门</td>
		<td class="tdcontent"><ext:field property="draftDepartment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">拟稿日期</td>
		<td class="tdcontent"><ext:field property="draftDate"/></td>
		<td class="tdtitle"></td>
		<td class="tdcontent"></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主送</td>
		<td class="tdcontent" colspan="3"><ext:field property="mainSend"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">抄送</td>
		<td class="tdcontent" colspan="3"><ext:field property="copySend"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">内部分发</td>
		<td class="tdcontent" colspan="3"><ext:field title="分发后，允许查看的本单位人员" property="interSendVisitors.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent" colspan="3"><ext:field property="attachment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">签发人</td>
		<td class="tdcontent"><ext:field property="signPerson"/></td>
		<td class="tdtitle" nowrap="nowrap">签发日期</td>
		<td class="tdcontent"><ext:field property="signDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">成文日期</td>
		<td class="tdcontent"><ext:field property="generateDate"/></td>
		<td class="tdtitle" nowrap="nowrap">印发日期</td>
		<td class="tdcontent"><ext:field property="distributeDate"/></td>
	</tr>
	<tr style="display:none"> 
		<td class="tdtitle" nowrap="nowrap">可访问者</td>
		<td class="tdcontent" colspan="3"><ext:field property="readerNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附注</td>
		<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
	</tr>
</table>