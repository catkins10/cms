<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">类型</td>
		<td class="tdcontent"><ext:field property="category"/></td>
		<td class="tdtitle" nowrap="nowrap">发送部门</td>
		<td class="tdcontent"><ext:field writeonly="true" property="unitName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td colspan="3" class="tdcontent"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">内容</td>
		<td colspan="3" class="tdcontent"><pre><ext:field property="content"/></pre></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td colspan="3" class="tdcontent"><ext:field property="attachment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">编号</td>
		<td class="tdcontent"><ext:field property="sn"/></td>
		<td class="tdtitle" nowrap="nowrap">受理时间</td>
		<td class="tdcontent"><ext:field property="receiveTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">隶属站点</td>
		<td class="tdcontent"><ext:field property="siteName" /></td>
		<td class="tdtitle" nowrap="nowrap">反应人姓名</td>
		<td class="tdcontent"><ext:field property="creator" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:field property="creatorMobile" /></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="creatorTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="creatorFax"/></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:field property="creatorMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在单位</td>
		<td class="tdcontent"><ext:field property="creatorUnit" /></td>
		<td class="tdtitle" nowrap="nowrap">职业</td>
		<td class="tdcontent"><ext:field property="creatorJob" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">证件名称</td>
		<td class="tdcontent"><ext:field property="creatorCertificateName"/></td>
		<td class="tdtitle" nowrap="nowrap">证件号码</td>
		<td class="tdcontent"><ext:field property="creatorIdentityCard"/></td>
	</tr><tr>
		<td class="tdtitle" nowrap="nowrap">所在区域</td>
		<td class="tdcontent"><ext:field property="area" /></td>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="creatorAddress" /></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">受理渠道</td>
		<td class="tdcontent"><ext:field property="source" /></td>
		<td class="tdtitle" nowrap="nowrap">受理人</td>
		<td class="tdcontent"><ext:field property="receiver" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">受理人电话</td>
		<td class="tdcontent"><ext:field property="receiverTel" /></td>
		<td class="tdtitle" nowrap="nowrap">受理时间</td>
		<td class="tdcontent"><ext:field property="receiveTime" /></td>
	</tr>
	<ext:notEqual value="create" property="act">
		<tr>
			<td class="tdtitle" nowrap="nowrap">民情办理意见</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="transactOpinion" /></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">办理人</td>
			<td class="tdcontent"><ext:field writeonly="true" property="transactor" /></td>
			<td class="tdtitle" nowrap="nowrap">办理时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="transactTime" /></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">民事办理结果</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="feedback" /></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">回应函编号</td>
			<td class="tdcontent"><ext:field writeonly="true" property="feedbackNumber" /></td>
			<td class="tdtitle" nowrap="nowrap">回应函送达人</td>
			<td class="tdcontent"><ext:field writeonly="true" property="feedbackSender" /></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">回应函送达时间</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="feedbackSendTime" /></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">办理评价</td>
			<td class="tdcontent"><ext:field writeonly="true" property="appraise" /></td>
			<td class="tdtitle" nowrap="nowrap">评价人</td>
			<td class="tdcontent"><ext:field writeonly="true" property="appraiser" /></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">评价时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="appraiseTime" /></td>
			<td class="tdtitle" nowrap="nowrap">联系方式</td>
			<td class="tdcontent"><ext:field writeonly="true" property="appraiserTel" /></td>
		</tr>
	</ext:notEqual>
</table>