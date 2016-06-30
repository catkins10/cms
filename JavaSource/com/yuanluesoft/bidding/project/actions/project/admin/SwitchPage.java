package com.yuanluesoft.bidding.project.actions.project.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.forms.admin.Project;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 切换页面
 * @author yuanlue
 *
 */
public class SwitchPage extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#save(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean)
	 */
	public Record save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean reload, String tabSelected, String actionResult) throws Exception {
		Project projectForm = (Project)form;
		Record record = super.save(mapping, form, request, response, reload, tabSelected, actionResult);
		setTransactDialog(projectForm, request);
		return record;
	}
	
	/**
	 * 输出办理对话框
	 * @param projectForm
	 * @param request
	 */
	private void setTransactDialog(Project projectForm, HttpServletRequest request) {
		projectForm.setInnerDialog("transact/" + projectForm.getPageName() + ".jsp");
		if("approvalNotice".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("审核中标通知书");
			projectForm.getFormActions().addFormAction(-1, "发放", "FormUtils.doAction('approvalNotice')", true);
		}
		else if("archive".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("书面报告备案");
			projectForm.getFormActions().addFormAction(-1, "完成", "archive()", true);
		}
		else if("arrangeBidopeningRoom".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("审核招标时间和分配开标室");
			projectForm.getFormActions().addFormAction(-1, "完成", "arrangeRoom()", true);
		}
		else if("arrangeBidopeningRoomAndPublicTender".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("审核招标时间和分配开标室并发布招标公告");
			projectForm.getFormActions().addFormAction(-1, "完成", "arrangeRoom()", true);
		}
		else if("arrangeEvaluatingRoom".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("分配评标室");
			projectForm.getFormActions().addFormAction(-1, "完成", "arrangeRoom()", true);
		}
		else if("biddingFailed".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("招标失败");
			projectForm.getFormActions().addFormAction(-1, "确定", "FormUtils.doAction('biddingFailed')", true);
		}
		else if("changeBidopeningRoom".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("调整开标室");
			projectForm.getFormActions().addFormAction(-1, "完成", "FormUtils.doAction('changeBidopeningRoom');", true);
		}
		else if("complatePay".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("缴费");
			projectForm.getFormActions().addFormAction(-1, "完成缴费", "completePay()", true);
		}
		else if("completeCreate".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("完成登记");
			projectForm.getFormActions().addFormAction(-1, "完成登记", "FormUtils.doAction('completeCreate')", true);
		}
		else if("completeDeclare".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("完成报建");
			projectForm.getFormActions().addFormAction(-1, "完成", "FormUtils.doAction('completeDeclare')", true);
		}
		else if("completeProphase".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("完成招标前期资料备案");
			if(projectForm.getAgentEnable().equals("是") && projectForm.getAgentMode().equals("随机抽签")) {
				projectForm.getFormActions().addFormAction(-1, "发布代理抽签公示", "completeProphase()", true);
			}
			else {
				projectForm.getFormActions().addFormAction(-1, "完成备案", "completeProphase()", true);
			}
		}
		else if("completeUseFee".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("缴纳场地费");
			projectForm.getFormActions().addFormAction(-1, "完成缴费", "completeUseFee()", true);
		}
		else if("declareReceive".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("报建受理");
			projectForm.getFormActions().addFormAction(-1, "完成", "FormUtils.doAction('declareReceive')", true);
		}
		else if("licence".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("发放施工许可证");
			projectForm.getFormActions().addFormAction(-1, "发放", "FormUtils.doAction('licence')", true);
		}
		else if("publicAgent".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("发布中选代理公示");
			projectForm.getFormActions().addFormAction(-1, "发布中选代理公示", "publicAgent()", true);
		}
		else if("publicAnswer".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("发布答疑会议纪要");
			projectForm.getFormActions().addFormAction(-1, "发布", "FormUtils.doAction('publicAnswer')", true);
		}
		else if("publicBidopening".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("发布开标公示");
			projectForm.getFormActions().addFormAction(-1, "发布开标公示", "publicBidopening()", true);
		}
		else if("publicNotice".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("发放中标通知书");
			projectForm.getFormActions().addFormAction(-1, "发放", "FormUtils.doAction('publicNotice')", true);
		}
		else if("publicPitchon".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("发布中标结果公示");
			projectForm.getFormActions().addFormAction(-1, "发布", "publicPitchon()", true);
		}
		else if("publicPreapproval".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("发布预审公示");
			projectForm.getFormActions().addFormAction(-1, "发布预审公示", "publicPreapproval()", true);
		}
		else if("publicSupplement".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("发布补充通知");
			projectForm.getFormActions().addFormAction(-1, "发布", "FormUtils.doAction('publicSupplement')", true);
		}
		else if("publicTender".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("发布招标文件");
			projectForm.getFormActions().addFormAction(-1, "发布", "FormUtils.doAction('publicTender')", true);
		}
		else if("completePledgeConfirm".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("保证金确认");
			projectForm.getFormActions().addFormAction(-1, "确定", "FormUtils.doAction('runProject', 'workflowAction=send&prompted=true')", true);
		}
		else if("completePledgeReturnChoose".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("保证金返还名单");
			projectForm.getFormActions().addFormAction(-1, "打印", "window.open('" + request.getContextPath() + "/bidding/project/report/admin/projectPledgeReport.shtml?projectId=" + projectForm.getId() + "&status=1')", true);
			projectForm.getFormActions().addFormAction(-1, "确定", "FormUtils.doAction('runProject', 'workflowAction=send&prompted=true')", false);
		}
		else if("completePledgeReturn".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("保证金返还");
			projectForm.getFormActions().addFormAction(-1, "确定", "FormUtils.doAction('runProject', 'workflowAction=send&prompted=true')", true);
		}
		else if("publicCompletion".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("竣工");
			projectForm.getFormActions().addFormAction(-1, "竣工", "publicCompletion()", true);
		}
		addReloadAction(projectForm, "取消", request, -1, false);
	}
}