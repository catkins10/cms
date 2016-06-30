package com.yuanluesoft.municipal.facilities.pdaservice.soap;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.soap.BaseSoapService;
import com.yuanluesoft.jeaf.workflow.pojo.WorkItem;
import com.yuanluesoft.municipal.facilities.pdaservice.model.EventEntity;
import com.yuanluesoft.municipal.facilities.pdaservice.model.SzcgEventReportParameter;
import com.yuanluesoft.municipal.facilities.pdaservice.model.SzcgEventReportResult;
import com.yuanluesoft.municipal.facilities.pdaservice.model.ValiateProDepartHandleResultResult;
import com.yuanluesoft.municipal.facilities.pdaservice.model.ValidateEvent;
import com.yuanluesoft.municipal.facilities.pojo.FacilitiesEvent;
import com.yuanluesoft.municipal.facilities.service.FacilitiesService;

/**
 * 给PDA用的WEB服务
 * @author linchuan
 *
 */
public class PDASoapService extends BaseSoapService {
	
	/**
	 * 上报事件
	 * @param entity
	 * @return
	 */
	public SzcgEventReportResult ReportEvent(SzcgEventReportParameter entity) throws SoapException {
		if(Logger.isDebugEnabled()) {
			Logger.debug("PDASoapService: report a event");
		}
		FacilitiesService facilitiesService = (FacilitiesService)getSpringService("facilitiesService");
		SzcgEventReportResult result = new SzcgEventReportResult();
		try {
			facilitiesService.reportEvent(entity);
			result.ReturnValue = true;
		}
		catch(Exception e) {
			Logger.exception(e);
			result.ReturnValue = false;
		}
	    return result;
	}
	
	/**
	 * 获取事件
	 * @param eventId
	 * @return
	 */
	public EventEntity GetEvent(String eventId) throws SoapException {
		try {
			FacilitiesService facilitiesService = (FacilitiesService)getSpringService("facilitiesService");
			FacilitiesEvent facilitiesEvent = facilitiesService.getEvent(Long.parseLong(eventId));
			return javaEvent2DotNetEntity(facilitiesEvent);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new SoapException();
		}
	}
	
	/**
	 * java实体转换为.net实体
	 * @param facilitiesEvent
	 * @return
	 */
	private EventEntity javaEvent2DotNetEntity(FacilitiesEvent facilitiesEvent) {
		EventEntity entity = new EventEntity();
		entity.BigclassID = null;
	    entity.BigclassName = facilitiesEvent.getCategory();
	    entity.Code = facilitiesEvent.getId() + "";
	    entity.CommunityID = null;
	    entity.CommunityName = null;
	    entity.ContactTel = facilitiesEvent.getContect();
	    entity.ContactWay = facilitiesEvent.getReceiptMode();
	    entity.Createddate = Calendar.getInstance();
	    entity.Createddate.setTimeInMillis(facilitiesEvent.getCreated().getTime());
	    entity.DistrictID = null;
	    entity.DistrictName = null;
	    entity.DuplicateCase = facilitiesEvent.getDuplicate().startsWith("非") ? 0 : Integer.parseInt(facilitiesEvent.getDuplicate().substring(0, 1));
	    entity.EndCaseTime = null;
	    entity.EventTypeID = null;
	    entity.EventTypeName = null;
	    entity.GridCode = null;
	    entity.GridID = null;
	    entity.GridName = facilitiesEvent.getZone();
	    entity.ID = facilitiesEvent.getId() + "";
	    entity.ImportantLevel = facilitiesEvent.getLevel();
	    entity.ImportantName = facilitiesEvent.getLevel();
	    entity.IsReceipt = facilitiesEvent.getIsReceipt()=='1';
	    entity.IsReceipted = 0;
	    entity.Lastupdateddate = null;
	    entity.LastupdatedID = null;
	    entity.ObserverCheckDesc = null;
	    entity.ObserverCheckResult = null;
	    entity.ObserverID = facilitiesEvent.getObserver();
	    entity.ObserverName = facilitiesEvent.getObserver();
	    entity.ObserverPdaNum = facilitiesEvent.getObserverNumber();
	    entity.PartCode = null;
	    entity.PartID = null;
	    entity.PhoneNum = null;
	    entity.PositionDesc = facilitiesEvent.getPosition();
	    entity.ProbDesc = facilitiesEvent.getDescription();
	    entity.ProbSource = null;
	    entity.ProbSourceName = null;
	    entity.ProDtpId = null;
	    entity.Projcode = null;
	    entity.Projtitle = null;
	    entity.RelationID = null;
	    entity.Remark = facilitiesEvent.getRemark();
	    entity.Reporter = facilitiesEvent.getReporter();
	    entity.RevertObject = null;
	    entity.RevertTypeName = null;
	    entity.RevertWayID = null;
	    entity.ShowColor = null;
	    entity.SignTime = null;
	    entity.SignUserID = null;
	    entity.SmallclassID = facilitiesEvent.getChildCategory();
	    entity.SmallclassName = facilitiesEvent.getChildCategory();
	    entity.StartCaseTime = null;
	    entity.Status = 0;
	    entity.StreetID = null;
	    entity.StreetName = null;
	    entity.SuperviseTimes = 0;
	    entity.UrgencyTimes = 0;
	    entity.UserID = null;
	    entity.WishFinishDate = null;
	    entity.WorkflowInstanceIdentifier = null;
	    entity.WorkGridID = null;
	    entity.WorkGridName = null;
	    entity.XPos = facilitiesEvent.getXPos();
	    entity.YPos = facilitiesEvent.getYPos();
	    return entity;
	}
	
	/**
	 * 获取任务
	 * @param pdaUserCode PDA登录名（工号）
	 * @param pageIndex 页面索引（用于查询结果分页显示）
	 * @param pageSize 页面大小（每页显示的记录数）
	 * @return
	 */
	public String GetMyTask(String pdaUserCode, int pageIndex, int pageSize) throws SoapException {
		try {
			FacilitiesService facilitiesService = (FacilitiesService)getSpringService("facilitiesService");
			ImageService imageService = (ImageService)getSpringService("imageService");
			//获取任务数量
			int count = facilitiesService.countPdaUserTasks(pdaUserCode);
			//获取任务列表
			List tasks = facilitiesService.listPdaUserTasks(pdaUserCode, pageIndex, pageSize);
			//生xml
			Document document = DocumentHelper.createDocument();
			Element rootElement =  document.addElement("GetMyTaskResult");
			rootElement.addAttribute("RecordCount", "" + count);
			if(tasks!=null) {
				for(int i=0; i<tasks.size(); i++) {
					FacilitiesEvent facilitiesEvent = (FacilitiesEvent)tasks.get(i);
					WorkItem workItem = (WorkItem)facilitiesEvent.getWorkItems().iterator().next();
					Element messageElement = rootElement.addElement("ObserverMessage");
					messageElement.addElement("id").setText("" + facilitiesEvent.getId()); //案件id
					//获取图片列表
					List images = imageService.list("municipal/facilities", "images", facilitiesEvent.getId(), false, 0, null);
					List processImages = imageService.list("municipal/facilities", "processImages", facilitiesEvent.getId(), false, 0, null);
					if(images==null ) {
						images = processImages;
					}
					else if(processImages!=null ) {
						images.addAll(processImages);
					}
					String imageFileNames = "";
					if(images!=null ) {
						for(Iterator iterator = images.iterator(); iterator.hasNext();) {
							Image image = (Image)iterator.next();
							imageFileNames += (imageFileNames.equals("") ? "" : ",") + image.getFilePath();
						}
					}
					messageElement.addElement("ImageUrl").setText(imageFileNames); //图片地址列表（字符串数组）
					messageElement.addElement("YPos").setText(facilitiesEvent.getYPos() + ""); //案发地x坐标
					messageElement.addElement("XPos").setText(facilitiesEvent.getXPos() + ""); //案发地y坐标
					messageElement.addElement("MsgRelationID").setText(workItem.getWorkItemId()); //任务id
					messageElement.addElement("MsgReceiver").setText(workItem.getParticipantId() + ""); //接收人id
					messageElement.addElement("MsgReceiverType").setText("");
					messageElement.addElement("MsgTitle").setText("" + facilitiesEvent.getChildCategory()); //任务标题
					messageElement.addElement("MsgContent").setText("" + facilitiesEvent.getDescription()); //任务内容
					messageElement.addElement("MsgSenderID").setText("");
					messageElement.addElement("MsgSenderName").setText("");
					messageElement.addElement("MsgStatus").setText("Unread"); //Unread or Readed or Finished 任务状态（未读、已读、完成）
					//messageElement.addElement("MsgType").setText(workItem.getActivityName().indexOf("真实性")==-1 && workItem.getActivityName().indexOf("核实")==-1 ? "CheckProDptResult" : "CheckPublicReport"); //CheckPublicReport or CheckProDptResult or EventHandle or EvtEndLDChenk 任务类型：核实（0）、核查（1）、任务处理（2）、案件结束领导审核（3）
					messageElement.addElement("MsgType").setText(workItem.getActivityName()); //CheckPublicReport or CheckProDptResult or EventHandle or EvtEndLDChenk 任务类型：核实（0）、核查（1）、任务处理（2）、案件结束领导审核（3）
					messageElement.addElement("SortID").setText("" + i); //任务序号
					messageElement.addElement("EvtFinishDate").setText(""); //截止办理时间
					messageElement.addElement("DbCreateDate").setText("");
					messageElement.addElement("DbCreateUser").setText("");
					messageElement.addElement("DbLastUpdateDate").setText("");
					messageElement.addElement("DbLastUpdateUser").setText("");
					messageElement.addElement("ActivityInstanceId").setText("");
					messageElement.addElement("StreetName").setText("" + facilitiesEvent.getZone());
					
					
					/*ObserverMessage message = new ObserverMessage();
					message.id = "" + facilitiesEvent.getId(); //案件id
					message.ImageUrl = null; //图片地址列表（字符串数组）
					message.YPos = facilitiesEvent.getYPos(); //案发地x坐标
					message.XPos = facilitiesEvent.getXPos(); //案发地y坐标
					message.MsgRelationID = workItem.getWorkItemId(); //任务id
				    message.MsgReceiver = workItem.getParticipantId() + ""; //接收人id
				    message.MsgReceiverType = null;
				    message.MsgTitle = facilitiesEvent.getChildCategory(); //任务标题
				    message.MsgContent = facilitiesEvent.getDescription(); //任务内容
				    message.MsgSenderID = null;
				    message.MsgSenderName = null;
				    message.MsgStatus = "Unread"; //Unread or Readed or Finished 任务状态（未读、已读、完成）
				    message.MsgType = workItem.getActivityName().indexOf("真实性")==-1 && workItem.getActivityName().indexOf("核实")==-1 ? "CheckProDptResult" : "CheckPublicReport"; //CheckPublicReport or CheckProDptResult or EventHandle or EvtEndLDChenk 任务类型：核实（0）、核查（1）、任务处理（2）、案件结束领导审核（3）
				    message.SortID = i; //任务序号
				    message.EvtFinishDate = null; //截止办理时间
				    message.DbCreateDate = null;
				    message.DbCreateUser = null;
				    message.DbLastUpdateDate = null;
				    message.DbLastUpdateUser = null;
				    message.ActivityInstanceId = null;
				    message.StreetName = facilitiesEvent.getZone();
				    messages[i] = message;*/
				}
			}
			return document.getRootElement().asXML();
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new SoapException();
		}
	}
	
	/**
	 * 监督员验证专业部门处理结果
	 * @param validateResult
	 * @return 上报是否成功
	 */
	public boolean ValiateProDepartHandleResult(ValiateProDepartHandleResultResult validateResult) throws SoapException {
		FacilitiesService facilitiesService = (FacilitiesService)getSpringService("facilitiesService");
		try {
			facilitiesService.completePdaValidateResult(validateResult);
			return true;
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		return false;
	}
	
	/**
	 * 监督员验证事件真实性
	 * @param validateResult
	 * @return 上报是否成功
	 */
	public boolean ValidateEvent(ValidateEvent validateEvent) throws SoapException {
		FacilitiesService facilitiesService = (FacilitiesService)getSpringService("facilitiesService");
		try {
			facilitiesService.completePdaValidateTruth(validateEvent);
			return true;
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		return false;
	}
}