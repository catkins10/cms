<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="50%">
			<col>
			<col width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">题型</td>
				<td class="tdcontent"><ext:field property="questionType" onchange="onQuestionTypeChanged()"/></td>
				<td class="tdtitle" nowrap="nowrap">难度系数</td>
				<td class="tdcontent"><ext:field property="difficulty"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap" valign="top">试题内容</td>
				<td class="tdcontent" colspan="3"><ext:field property="content"/></td>
			</tr>
			<tr id="trBlankNumber" style="display:none">
				<td class="tdtitle" nowrap="nowrap">答案个数</td>
				<td class="tdcontent" colspan="3"><ext:field property="blankNumber" onchange="onQuestionTypeChanged()"/></td>
			</tr>
			<tr id="trOptionNumber" style="display:none">
				<td class="tdtitle" nowrap="nowrap">选项个数</td>
				<td class="tdcontent" colspan="3"><ext:field property="optionNumber" onchange="onQuestionTypeChanged()"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap" valign="top">答案</td>
				<td class="tdcontent" colspan="3">
					<div id="divAnswers">
						
					</div>
					<div id="divEssayAnswers" style="display:none">
						<ext:field property="essayAnswers"/>
					</div>
				</td>
			</tr>
			<tr id="trAnswerCaseSensitive" style="display:none">
				<td class="tdtitle" nowrap="nowrap">区分大小写</td>
				<td class="tdcontent" colspan="3"><ext:field property="answerCaseSensitive"/></td>
			</tr>
			<tr id="trAnswerOnComputer" style="display:none">
				<td class="tdtitle" nowrap="nowrap">计算机上可作答</td>
				<td class="tdcontent"><ext:field property="answerOnComputer"/></td>
				<td class="tdtitle" nowrap="nowrap">计算机可判卷</td>
				<td class="tdcontent"><ext:field property="computerMarking"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">题目分值</td>
				<td class="tdcontent"><ext:field property="score"/></td>
				<td class="tdtitle" nowrap="nowrap">作答时间(秒)</td>
				<td class="tdcontent"><ext:field property="responseTime"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">编号</td>
				<td class="tdcontent"><ext:field property="no"/></td>
				<td class="tdtitle" nowrap="nowrap">试题来源</td>
				<td class="tdcontent"><ext:field property="source"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">适用的岗位</td>
				<td class="tdcontent" colspan="3"><ext:field property="posts"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">知识类别</td>
				<td class="tdcontent"><ext:field property="knowledges"/></td>
				<td class="tdtitle" nowrap="nowrap">项目分类</td>
				<td class="tdcontent"><ext:field property="items"/></td>
			</tr>
			<tr style="display:none">
				<td class="tdtitle" nowrap="nowrap">使用次数</td>
				<td class="tdcontent"><ext:field property="useTimes"/></td>
				<td class="tdtitle" nowrap="nowrap">答错次数</td>
				<td class="tdcontent"><ext:field property="failedTimes"/></td>
			</tr>
			<tr style="display:none">
				<td class="tdtitle" nowrap="nowrap">答错率</td>
				<td class="tdcontent"><ext:field property="failedRate"/>%</td>
				<td class="tdtitle" nowrap="nowrap">题目有错误</td>
				<td class="tdcontent"><ext:field property="wrong"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">创建时间</td>
				<td class="tdcontent"><ext:field property="created"/></td>
				<td class="tdtitle" nowrap="nowrap">创建人</td>
				<td class="tdcontent"><ext:field property="creator"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">备注</td>
				<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
			</tr>
		</table>
	</ext:tabBody>
	
	<ext:tabBody tabId="tabHint">
		<div><ext:field property="hint"/></div>
	</ext:tabBody>
	
	<ext:tabBody tabId="tabAnalysis">
		<div><ext:field property="analysis"/></div>
	</ext:tabBody>
	
	<ext:tabBody tabId="tabDetail">
		<div><ext:field property="detail"/></div>
	</ext:tabBody>
</ext:tab>

<script>
	function onQuestionTypeChanged() {
		var questionType = document.getElementsByName("questionType")[0].value;
		if(questionType=="") {
			return;
		}
		document.getElementById("trBlankNumber").style.display = questionType=="填空题" ? "" : "none"; //答案个数
		document.getElementById("trOptionNumber").style.display = questionType=="单选题" || questionType=="多选题" ? "" : "none"; //选项个数
		document.getElementById("trAnswerOnComputer").style.display = "单选题,多选题,判断题".indexOf(questionType)==-1 ? "" : "none"; //计算机上可以作答
		document.getElementById("trBlankNumber").style.display = questionType=="填空题" ? "" : "none"; //答案个数
		document.getElementById("trAnswerCaseSensitive").style.display = "单选题,多选题,判断题".indexOf(questionType)==-1 ? "" : "none"; //答案是否区分大小写
		
		var isEssay = "单选题,多选题,判断题,填空题".indexOf(questionType)==-1;
		document.getElementById("divEssayAnswers").style.display = isEssay ? "" : "none"; //问答题答案
		var divAnswers = document.getElementById("divAnswers");
		divAnswers.style.display = !isEssay ? "" : "none"; //答案
		//设置答案列表
		var answers = document.getElementsByName("answers")[0].value;
		if(questionType=="单选题" || questionType=="多选题") {
			var html = "";
			for(var i=0; i<Number(document.getElementsByName("optionNumber")[0].value); i++) {
				var option = String.fromCharCode(0x41 + i);
				var type = questionType=="多选题" ? "checkbox" : "radio";
				html += '<input onclick="onAnswerChanged()" name="answer" type="' + type + '" class="' + type + '" value="' + option + '"' + (answers.indexOf(option)==-1 ? '' : ' checked="true"') + '/>' + option + '&nbsp;';
			}
			divAnswers.innerHTML = html;
		}
		else if(questionType=="判断题") {
			var html = "";
			for(var i=0; i<2; i++) {
				var option = (i==0 ? 'T' : 'F');
				html += '<input onclick="onAnswerChanged()" name="answer" type="radio" class="radio" value="' + option + '"' + (answers.indexOf(option)==-1 ? '' : ' checked="true"') + '/>' + (i==0 ? '正确' : '错误') + '&nbsp;';
			}
			divAnswers.innerHTML = html;
		}
		else if(questionType=="填空题") {
			var html = "";
			for(var i=0; i<Number(document.getElementsByName("blankNumber")[0].value); i++) {
				html += (i+1) + '.<input onchange="onAnswerChanged()" name="answer" type="text" class="field" style="width:80px"/>&nbsp;';
			}
			divAnswers.innerHTML = html;
			if(answers!="") {
				answers = answers.split("\r\n");
				var fields = document.getElementsByName("answer");
				for(var i=0; i<Math.min(fields.length,answers.length); i++) {
					fields[i].value = answers[i];
				}
			}
		}
	}
	function onAnswerChanged() {
		var fields = document.getElementsByName("answer");
		var answers = "";
		for(var i=0; i<fields.length; i++) {
			if(fields[i].type.toLowerCase()=="text" || fields[i].checked) {
				answers = (answers=="" ? "" : answers + "\r\n") + fields[i].value;
			}
		}
		document.getElementsByName("answers")[0].value = answers;
	}
	onQuestionTypeChanged();
</script>