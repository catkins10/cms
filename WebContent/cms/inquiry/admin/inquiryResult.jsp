<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab property="resultTabList">
	<ext:tabBody tabId="resultAsTable">
		<ext:iterate id="inquiry" indexId="inquiryIndex" property="inquiries">
			<ext:equal value="1" property="isQuestionnaire">
				&nbsp;<ext:writeNumber name="inquiryIndex" plus="1"/>、<ext:write name="inquiry" property="descriptionText" maxCharCount="100" ellipsis="..."/>
			</ext:equal>
			<ext:equal name="inquiry" property="isMultiSelect" value="2">
				<table width="100%" class="table" style="margin-top:3px; margin-bottom: 8px; border: 1px;"  cellpadding="0" cellspacing="0">
						<tr height="23px" valign="bottom" border="0">
							<td align="center" nowrap="nowrap" class="tdtitle" width="50px">序号</td>
							<ext:iterate id="option" indexId="resultIndex" name="inquiry"  property="options">
									<td align="center" nowrap="nowrap" class="tdtitle" >
										<ext:write name="option" property="inquiryOption"/>
									</td>
							</ext:iterate>
						</tr>
						<tr height="23px" valign="bottom" border="0">
							<td >
													<table   width="100%" cellspacing="0" cellpadding="0" class="table">
												<ext:iterate id="vote" indexId="voteIndex" name="option"  property="votes">
														<tr height="23px">
															<td class="tdcontent"  align="center" >
																<ext:writeNumber name="voteIndex" plus="1"/>
															</td>
														</tr>
												</ext:iterate>
													</table>
							</td>
							<ext:iterate id="option" indexId="optionIndex" name="inquiry"  property="options">
									<td border="0">
											<table   width="100%" cellspacing="0" cellpadding="0" class="table">
												<ext:iterate id="vote" indexId="voteIndex" name="option"  property="votes">
														<tr height="23px">
															<td class="tdcontent"  align="center" >
																<ext:write name="vote" property="supplement"/>
															</td>
														</tr>
												</ext:iterate>
											</table>
									</td>
							</ext:iterate>
						</tr>
				</table>
			</ext:equal>
			<ext:notEqual name="inquiry" property="isMultiSelect" value="2">
				<table width="100%" class="table" style="margin-top:3px; margin-bottom: 8px" border="1" cellpadding="0" cellspacing="0">
				<tr height="23px" valign="bottom">
					<td align="center" nowrap="nowrap" class="tdtitle" width="50px">序号</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="100%">选项</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="80px">得票数</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="300px">得票比例</td>
				</tr>
				<ext:iterate id="result" indexId="resultIndex" name="inquiry" property="results">
					<tr align="center">
						<td class="tdcontent" align="center"><ext:writeNumber name="resultIndex" plus="1"/></td>
						<td class="tdcontent" align="left">
							<ext:notEqual value="1" name="result" property="needSupplement">
								<ext:write name="result" property="option"/>
							</ext:notEqual>
							<ext:equal value="1" name="result" property="needSupplement">
								<a style="color:#0050ff" href="javascript:DialogUtils.openSelectDialog('cms/inquiry', 'admin/supplement', 720, 400, false, '', '', '', '', '', true, 'optionId=<ext:write name="result" property="optionId"/>');"><ext:write name="result" property="option"/></a>
							</ext:equal>
						</td>
						<td class="tdcontent" align="left" >
							<ext:equal value="true" property="manager">
								<input type="button" class="button" value="修改" onclick="DialogUtils.openInputDialog('修改投票数', [{name:'voteNumber', title:'投票数', defaultValue:'<ext:write name="result" property="voteNumber"/>', inputMode:'text'}], 360, 50, 'FormUtils.doAction(\'setVoteNumber\', \'optionId=<ext:write name="result" property="optionId"/>&voteNumber=\' + {value}.voteNumber)')"/>
							</ext:equal>
							<ext:write name="result" property="voteNumber"/>
						</td>
						<td class="tdcontent" align="left">
							<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
								<tr>
									<td style="width:43px"><ext:write name="result" property="votePercent" format="###.##%"/></td>
									<td>
										<div style="background:red;width:<ext:write name="result" property="votePercent" format="###.##%"/>"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</ext:iterate>
			</table>
			</ext:notEqual>
		</ext:iterate>
	</ext:tabBody>
	
	<ext:tabBody tabId="resultAsChart">
		<ext:iterate id="inquiry" indexId="inquiryIndex" property="inquiries">
			<ext:equal value="1" property="isQuestionnaire">
				&nbsp;<ext:writeNumber name="inquiryIndex" plus="1"/>、<ext:write name="inquiry" property="descriptionText" maxCharCount="100" ellipsis="..."/><br/>
			</ext:equal>
			<img src="<%=request.getContextPath()%>/cms/inquiry/inquiryResultChart.shtml?inquiryId=<ext:write name="inquiry" property="id"/>&chartWidth=480&chartHeight=360&chartMode=pieChart">
			<img src="<%=request.getContextPath()%>/cms/inquiry/inquiryResultChart.shtml?inquiryId=<ext:write name="inquiry" property="id"/>&chartWidth=480&chartHeight=360&chartMode=barChart">
			<br/>
		</ext:iterate>
	</ext:tabBody>
</ext:tab>