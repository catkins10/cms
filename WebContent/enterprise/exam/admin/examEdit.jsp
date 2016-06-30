<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">自助方式</td>
		<td class="tdcontent"><ext:field property="selfTest" onclick="onSelfTestChanged()"/></td>
	</tr>
	<tr id="trQuestionNumber" style="display:none">
		<td class="tdtitle" nowrap="nowrap">每月考试题数</td>
		<td class="tdcontent"><ext:field property="monthQuestionNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">年度考试题数</td>
		<td class="tdcontent"><ext:field property="yearQuestionNumber"/></td>
	</tr>
	<tr id="trDayLimit" style="display:none">
		<td class="tdtitle" nowrap="nowrap">日考试次数上限</td>
		<td class="tdcontent"><ext:field property="examDayLimit"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">总分</td>
		<td class="tdcontent"><ext:field property="score"/></td>
		<td class="tdtitle" nowrap="nowrap">考试时长(分钟)</td>
		<td class="tdcontent"><ext:field property="examTime"/></td>
	</tr>
	<tr valign="top">
		<td class="tdtitle" nowrap="nowrap" valign="top">考试题型</td>
		<td class="tdcontent">
			<table border="0" cellpadding="2" cellspacing="0">
				<ext:iterate id="questionType" property="questionTypes">
					<tr>
						<td><input name="examQuestionType" type="checkbox" class="checkbox" <ext:select id="examQuestionType" select="questionType" property="examQuestionTypes" nameValue="questionType">checked</ext:select> value="<ext:write name="questionType"/>"/><ext:write name="questionType"/>&nbsp;&nbsp;</td>
						<td>题目数：<input type="text" name="examQuestionNumber_<ext:write name="questionType"/>" class="field" style="width: 50px; height:18px;" value="<ext:select id="examQuestionType" select="questionType" property="examQuestionTypes" nameValue="questionType"><ext:write name="examQuestionType" property="questionNumber"/></ext:select>"/>&nbsp;&nbsp;</td>
						<td>分值：<input type="text" name="examQuestionScore_<ext:write name="questionType"/>" class="field" style="width: 50px; height:18px;" value="<ext:select id="examQuestionType" select="questionType" property="examQuestionTypes" nameValue="questionType"><ext:write name="examQuestionType" property="questionScore"/></ext:select>"/></td>
					</tr>
				</ext:iterate>
			</table>
		</td>
		<td class="tdtitle" nowrap="nowrap">难易程度</td>
		<td class="tdcontent">
			<table border="0" cellpadding="2" cellspacing="0">
				<ext:iterate id="difficultyLevel" property="difficultyLevels">
					<tr>
						<td><input name="examDifficultyLevel" type="checkbox" class="checkbox" <ext:select id="examDifficultyLevel" select="difficulty" property="examDifficultyLevels" nameValue="difficultyLevel">checked</ext:select> value="<ext:write name="difficultyLevel"/>"/><ext:write name="difficultyLevel"/>&nbsp;&nbsp;</td>
						<td>比例：<input type="text" name="examDifficultyRatio_<ext:write name="difficultyLevel"/>" class="field" style="width: 50px; height:18px;" value="<ext:select id="examDifficultyLevel" select="difficulty" property="examDifficultyLevels" nameValue="difficultyLevel"><ext:write name="examDifficultyLevel" property="ratio"/></ext:select>"/>&nbsp;%</td>
					</tr>
				</ext:iterate>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">知识类别</td>
		<td class="tdcontent"><ext:field property="knowledges"/></td>
		<td class="tdtitle" nowrap="nowrap">项目分类</td>
		<td class="tdcontent"><ext:field property="items"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">适用的岗位</td>
		<td class="tdcontent"><ext:field property="examPostNames"/></td>
		<td class="tdtitle" nowrap="nowrap">批改人</td>
		<td class="tdcontent"><ext:field property="examCorrectorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
</table>

<script>
	function formOnSubmit() {
		var examQuestionTypes = document.getElementsByName('examQuestionType');
		var checked = false;
		for(var i=0; i<examQuestionTypes.length; i++) {
			if(examQuestionTypes[i].checked) {
				checked = true;
				//题目数校验
				var field = document.getElementsByName('examQuestionNumber_' + examQuestionTypes[i].value)[0];
				var examQuestionNumber = new Number(field.value);
  				if(isNaN(examQuestionNumber) || examQuestionNumber==0) {
  					alert(examQuestionTypes[i].value + "题目数不正确");
  					field.focus();
  					return false;
  				}
				//分数校验
				field = document.getElementsByName('examQuestionScore_' + examQuestionTypes[i].value)[0];
				var examQuestionScore = new Number(field.value);
  				if(isNaN(examQuestionScore) || examQuestionScore==0) {
  					alert(examQuestionTypes[i].value + "分值不正确");
  					field.focus();
  					return false;
  				}
			}
		}
		if(!checked) {
			alert("题型未选择");
			return false;
		}
		var examDifficultyLevels = document.getElementsByName('examDifficultyLevel');
		var ratioTotal = 0;
		checked = false;
		for(var i=0; i<examDifficultyLevels.length; i++) {
			if(examDifficultyLevels[i].checked) {
				checked = true;
				//比例校验
				var field = document.getElementsByName('examDifficultyRatio_' + examDifficultyLevels[i].value)[0];
				var examDifficultyRatio = new Number(field.value);
  				if(isNaN(examDifficultyRatio) || examDifficultyRatio==0) {
  					alert(examDifficultyLevels[i].value + "比例不正确");
  					field.focus();
  					return false;
  				}
  				ratioTotal += examDifficultyRatio;
			}
		}
		if(checked && ratioTotal!=100) {
			alert("难度比例相加应该等于100");
			return false;
		}
		return true;
	}
	function onSelfTestChanged() {
		var fields = document.getElementsByName("selfTest");
		document.getElementById("trQuestionNumber").style.display = document.getElementById("trDayLimit").style.display = (fields[0].checked ? "" : "none");
	}
	onSelfTestChanged();
</script>