<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<ext:sizeEqual value="1" property="processInstanceViews">
	<div style="padding-bottom:5px;">
		流程名称：<ext:write name="processInstanceView" property="processInstance.name"/>
	</div>
</ext:sizeEqual>
<div id="processInstanceImage_<ext:write name="processInstanceViewIndex"/>" onmouseout="mouseOutView(<ext:write name="processInstanceViewIndex"/>)" onmousemove="mouseMoveOnView(this, <ext:write name="processInstanceViewIndex"/>, event.offsetX, event.offsetY, event.clientX, event.clientY)" style="margin-left:5px; width:<ext:write name="processInstanceView" property="width"/>px; height:<ext:write name="processInstanceView" property="height"/>px; background-repeat:no-repeat;">&nbsp;</div>
<script language="javascript">
	var imageUrl = '<%=request.getContextPath()%>/workflow/writeProcessInstanceImage.shtml?passport=<ext:write property="passport"/>&processInstanceIndex=<ext:write name="processInstanceViewIndex"/>';
	var processInstanceImage = document.getElementById('processInstanceImage_<ext:write name="processInstanceViewIndex"/>');
	if(document.all) {
		processInstanceImage.style.filter = 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src=' + imageUrl + ')';
	}
	else {
		processInstanceImage.style.backgroundImage = 'url(' + imageUrl + ')';
	}
	dimensions[<ext:write name="processInstanceViewIndex"/>] = [<ext:write name="processInstanceView" property="width"/>, <ext:write name="processInstanceView" property="height"/>];
	coordinates[<ext:write name="processInstanceViewIndex"/>] = [
		<ext:iterate id="element" indexId="elementIndex" name="processInstanceView" property="elements">
			<ext:notEqual value="0" name="elementIndex">,</ext:notEqual>
			['<ext:write name="element" property="workflowElement.id"/>', <ext:write name="element" property="graphElement.startX"/>, <ext:write name="element" property="graphElement.startY"/>, <ext:write name="element" property="graphElement.endX"/>, <ext:write name="element" property="graphElement.endY"/>]
		</ext:iterate>
	];
</script>
<ext:iterate id="element" indexId="elementIndex" name="processInstanceView" property="elements">
	<ext:instanceof className="Activity" name="element" property="workflowElement">
		<ext:empty name="element" property="instances">
			<div style="display:none;" id="process_<ext:write name="processInstanceViewIndex"/>_<ext:write name="element" property="workflowElement.id"/>">
				<table class="table" class="tip" border="1" cellpadding="0" cellspacing="0">
					<col align="right">
					<col>
					<tr>
						<td class="tdtitle" nowrap="nowrap">环节名称</td>
						<td class="tdcontent" nowrap="nowrap">
				  			<ext:write name="element" property="workflowElement.name"/>
						</td>
					</tr>
					<tr>
						<td class="tdtitle" nowrap="nowrap">办理期限</td>
						<td align="left" class="tdcontent" nowrap="nowrap">
							<ext:equal value="0.0" name="element" property="workflowElement.deadlineCondition">不限</ext:equal>
							<ext:notEqual value="0.0" name="element" property="workflowElement.deadlineCondition"><ext:write name="element" property="workflowElement.deadlineCondition" format="#.#"/>天</ext:notEqual>
						</td>
					</tr>
					<tr valign="top">
						<td class="tdtitle" nowrap="nowrap">办 理 人</td>
						<td class="tdcontent" nowrap="nowrap">
							<table width="160px" border="0" cellspacing="0" cellpadding="0">
								<ext:iterate id="participant" name="element" property="workflowElement.participantList">
									<tr valign="top">
										<td nowrap="nowrap" style="padding-bottom:5px">
											<logic:notPresent name="participant" property="title"><ext:write name="participant" property="name"/></logic:notPresent>
											<logic:present name="participant" property="title"><ext:write name="participant" property="title"/></logic:present>
										</td>
									</tr>
								</ext:iterate>
							</table>
						</td>
					</tr>
				</table>
			</div>			
		</ext:empty>
		<ext:notEmpty name="element" property="instances">
			<div style="display:none;" id="process_<ext:write name="processInstanceViewIndex"/>_<ext:write name="element" property="workflowElement.id"/>">
				<table class="table tip" border="1" cellpadding="0" cellspacing="0">
					<tr align="center">
						<td class="tdtitle" nowrap="nowrap">上一办理人</td>
						<td class="tdtitle" nowrap="nowrap">送达时间</td>
						<td class="tdtitle" nowrap="nowrap">期限</td>
						<td class="tdtitle" nowrap="nowrap">办理方式</td>
						<td class="tdtitle" nowrap="nowrap">办理人/完成时间/执行的操作</td>
					</tr>
					<ext:iterate id="activityInstance" name="element" property="instances"><ext:instanceof className="ActivityInstance" name="activityInstance">
					  	<tr valign="top" align="center" style="<ext:equal value="CLOSED_ABORTED" name="activityInstance" property="stateTitle">color:#008000</ext:equal>">
					  		<td align="left" class="tdcontent" nowrap="nowrap">
					  			<ext:empty name="activityInstance" property="previousParticipant"><center>-</center></ext:empty>
								<ext:notEmpty name="activityInstance" property="previousParticipant"><ext:write name="activityInstance" property="previousParticipant.name" maxCharCount="20" ellipsis="."/></ext:notEmpty>
							</td>
							<td class="tdcontent" nowrap="nowrap">
								<ext:write name="activityInstance" property="created" format="yyyy-MM-dd HH:mm"/>
							</td>
							<td class="tdcontent" nowrap="nowrap">
								<ext:equal value="0.0" name="activityInstance" property="deadline">不限</ext:equal>
								<ext:notEqual value="0.0" name="activityInstance" property="deadline"><ext:write name="activityInstance" property="deadline" format="#.#"/>天</ext:notEqual>
							</td>
							<td class="tdcontent" nowrap="nowrap">
								<ext:write name="activityInstance" property="transactModeTitle"/>
							</td>
							<td align="left" class="tdcontent" nowrap="nowrap">
								<table border="0" cellpadding="0" cellspacing="0" style="<ext:equal value="CLOSED_ABORTED" name="activityInstance" property="stateTitle">color:#008000</ext:equal>">
									<ext:iterate id="workItem" name="activityInstance" property="workItemList">
										<tr valign="top" style="<ext:equal name="workItem" property="id" propertyCompare="currentWorkItemId">color:red</ext:equal>">
											<td width="100px" nowrap="nowrap" title="<ext:write name="workItem" property="participant.name"/><logic:present name="workItem" property="participant.personNames"> <ext:write name="workItem" property="participant.personNames"/></logic:present>">
												<logic:notPresent name="workItem" property="participant.personNames">
													<ext:write name="workItem" property="participant.name" maxCharCount="16" ellipsis="."/>
												</logic:notPresent>
												<logic:present name="workItem" property="participant.personNames">
													<ext:empty name="workItem" property="participant.personNames">
														<ext:write name="workItem" property="participant.name" maxCharCount="16" ellipsis="."/>
													</ext:empty>
													<ext:notEmpty name="workItem" property="participant.personNames">
														<ext:write name="workItem" property="participant.name" maxCharCount="8" ellipsis="."/>(<ext:write name="workItem" property="participant.personNames" maxCharCount="6" ellipsis="."/>)
													</ext:notEmpty>
												</logic:present>
											</td>
											<td nowrap="nowrap">
												<table border="0" cellspacing="0" cellpadding="0" style="<ext:equal value="CLOSED_ABORTED" name="activityInstance" property="stateTitle">color:#008000</ext:equal>">
													<ext:iterate id="transactLog" name="workItem" property="transactLogList">
														<tr valign="top">
															<td nowrap="nowrap" style="padding-bottom:5px">
																<ext:write name="transactLog" property="time" format="yyyy-MM-dd HH:mm"/>
																<ext:notEmpty name="workItem" property="subFlowInstanceId"><ext:write name="transactLog" property="forwards"/></ext:notEmpty>
																<ext:empty name="workItem" property="subFlowInstanceId"><ext:write name="transactLog" property="forwards"/></ext:empty>
																<ext:notEmpty name="transactLog" property="participantName">[<ext:write name="transactLog" property="participantName"/>]</ext:notEmpty>
															</td>
														</tr>
													</ext:iterate>
												</table>
											</td>
										</tr>
									</ext:iterate>
								</table>
							</td>
						</tr>
					</ext:instanceof></ext:iterate>
				</table>
			</div>
		</ext:notEmpty>
	</ext:instanceof>
</ext:iterate>