package com.yuanluesoft.cms.situation.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.cms.publicservice.forms.PublicServiceAdminForm;
import com.yuanluesoft.cms.situation.pojo.SituationCoordinate;

/**
 * 
 * @author chuan
 *
 */
public class Situation extends PublicServiceAdminForm {
	private String category; //类型,预设“办事、咨询、建议、投诉、举报”
	private long unitId; //受理部门ID
	private String unitName; //受理部门
	private String area; //所属区域
	private String source; //受理渠道,网络平台、来信来访、服务热线、民情信息员、座谈会、便民服务受理点、民情回应平台、其他
	private String receiver; //受理人
	private String receiverTel; //受理人电话
	private Timestamp receiveTime; //受理时间
	private String transactOpinion; //民情办理意见,（1）由村、社区处理（2）提交镇党委、政府研究决定（3）由挂村领导协调职能部门处理（具体部门：          ）
	private long coordinateUnitId; //协调的部门ID
	private String coordinateUnitName; //协调的部门名称
	private long transactorId; //办理人ID
	private String transactor; //办理人
	private Timestamp transactTime; //办理时间
	private String feedback; //民事办理结果,民情回应的最终办理结果，建议由各级接收民情回应信件的单位填写，形成最终的民事办理结果
	private long feedbackUnitId; //回应单位ID
	private String feedbackUnit; //回应单位
	private Timestamp feedbackTime; //回应时间
	private String feedbackNumber; //回应函编号
	private String feedbackSender; //回应函送达人,后台补录
	private Timestamp feedbackSendTime; //回应函送达时间,后台补录
	private String appraise; //办理评价,办理评价由各村（社区）、乡镇或部门回应函送达人将村民的办理评价补录到系统中，内容如下：（1）满意（2）比较满意（3）不满意
	private String appraiser; //评价人,评价人由各村（社区）、乡镇或部门回应函送达人将填写在纸质上的评价村民姓名补录到系统中
	private Timestamp appraiseTime; //评价时间,评价时间由各村（社区）、乡镇或部门回应函送达人将村民填写在纸质上的评价时间补录到系统中
	private String appraiserTel; //联系方式,联系方式由各村（社区）、乡镇或部门回应函送达人将填写在纸质上的评价村民联系方式补录到系统中
	private Set coordinates; //上报或协调其它单位
	
	//扩展属性
	private String pageName; //切换的页面
	private boolean isVillage; //是否村或社区
	private String villageTransact; //村级办理选项
	private boolean coordinateOtherUnit; //是否协调其它部门
	private SituationCoordinate coordinate = new SituationCoordinate(); //上报或协调其它单位
	
	/**
	 * @return the appraise
	 */
	public String getAppraise() {
		return appraise;
	}
	/**
	 * @param appraise the appraise to set
	 */
	public void setAppraise(String appraise) {
		this.appraise = appraise;
	}
	/**
	 * @return the appraiser
	 */
	public String getAppraiser() {
		return appraiser;
	}
	/**
	 * @param appraiser the appraiser to set
	 */
	public void setAppraiser(String appraiser) {
		this.appraiser = appraiser;
	}
	/**
	 * @return the appraiserTel
	 */
	public String getAppraiserTel() {
		return appraiserTel;
	}
	/**
	 * @param appraiserTel the appraiserTel to set
	 */
	public void setAppraiserTel(String appraiserTel) {
		this.appraiserTel = appraiserTel;
	}
	/**
	 * @return the appraiseTime
	 */
	public Timestamp getAppraiseTime() {
		return appraiseTime;
	}
	/**
	 * @param appraiseTime the appraiseTime to set
	 */
	public void setAppraiseTime(Timestamp appraiseTime) {
		this.appraiseTime = appraiseTime;
	}
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the feedbackNumber
	 */
	public String getFeedbackNumber() {
		return feedbackNumber;
	}
	/**
	 * @param feedbackNumber the feedbackNumber to set
	 */
	public void setFeedbackNumber(String feedbackNumber) {
		this.feedbackNumber = feedbackNumber;
	}
	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}
	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	/**
	 * @return the receiverTel
	 */
	public String getReceiverTel() {
		return receiverTel;
	}
	/**
	 * @param receiverTel the receiverTel to set
	 */
	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}
	/**
	 * @return the receiveTime
	 */
	public Timestamp getReceiveTime() {
		return receiveTime;
	}
	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * @return the coordinateUnitId
	 */
	public long getCoordinateUnitId() {
		return coordinateUnitId;
	}
	/**
	 * @param coordinateUnitId the coordinateUnitId to set
	 */
	public void setCoordinateUnitId(long coordinateUnitId) {
		this.coordinateUnitId = coordinateUnitId;
	}
	/**
	 * @return the coordinateUnitName
	 */
	public String getCoordinateUnitName() {
		return coordinateUnitName;
	}
	/**
	 * @param coordinateUnitName the coordinateUnitName to set
	 */
	public void setCoordinateUnitName(String coordinateUnitName) {
		this.coordinateUnitName = coordinateUnitName;
	}
	/**
	 * @return the transactOpinion
	 */
	public String getTransactOpinion() {
		return transactOpinion;
	}
	/**
	 * @param transactOpinion the transactOpinion to set
	 */
	public void setTransactOpinion(String transactOpinion) {
		this.transactOpinion = transactOpinion;
	}
	/**
	 * @return the transactor
	 */
	public String getTransactor() {
		return transactor;
	}
	/**
	 * @param transactor the transactor to set
	 */
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
	/**
	 * @return the transactorId
	 */
	public long getTransactorId() {
		return transactorId;
	}
	/**
	 * @param transactorId the transactorId to set
	 */
	public void setTransactorId(long transactorId) {
		this.transactorId = transactorId;
	}
	/**
	 * @return the transactTime
	 */
	public Timestamp getTransactTime() {
		return transactTime;
	}
	/**
	 * @param transactTime the transactTime to set
	 */
	public void setTransactTime(Timestamp transactTime) {
		this.transactTime = transactTime;
	}
	/**
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}
	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	/**
	 * @return the feedbackSender
	 */
	public String getFeedbackSender() {
		return feedbackSender;
	}
	/**
	 * @param feedbackSender the feedbackSender to set
	 */
	public void setFeedbackSender(String feedbackSender) {
		this.feedbackSender = feedbackSender;
	}
	/**
	 * @return the feedbackSendTime
	 */
	public Timestamp getFeedbackSendTime() {
		return feedbackSendTime;
	}
	/**
	 * @param feedbackSendTime the feedbackSendTime to set
	 */
	public void setFeedbackSendTime(Timestamp feedbackSendTime) {
		this.feedbackSendTime = feedbackSendTime;
	}
	/**
	 * @return the feedbackUnit
	 */
	public String getFeedbackUnit() {
		return feedbackUnit;
	}
	/**
	 * @param feedbackUnit the feedbackUnit to set
	 */
	public void setFeedbackUnit(String feedbackUnit) {
		this.feedbackUnit = feedbackUnit;
	}
	/**
	 * @return the feedbackUnitId
	 */
	public long getFeedbackUnitId() {
		return feedbackUnitId;
	}
	/**
	 * @param feedbackUnitId the feedbackUnitId to set
	 */
	public void setFeedbackUnitId(long feedbackUnitId) {
		this.feedbackUnitId = feedbackUnitId;
	}
	/**
	 * @return the feedbackTime
	 */
	public Timestamp getFeedbackTime() {
		return feedbackTime;
	}
	/**
	 * @param feedbackTime the feedbackTime to set
	 */
	public void setFeedbackTime(Timestamp feedbackTime) {
		this.feedbackTime = feedbackTime;
	}
	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}
	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	/**
	 * @return the coordinateOtherUnit
	 */
	public boolean isCoordinateOtherUnit() {
		return coordinateOtherUnit;
	}
	/**
	 * @param coordinateOtherUnit the coordinateOtherUnit to set
	 */
	public void setCoordinateOtherUnit(boolean coordinateOtherUnit) {
		this.coordinateOtherUnit = coordinateOtherUnit;
	}
	/**
	 * @return the isVillage
	 */
	public boolean isVillage() {
		return isVillage;
	}
	/**
	 * @param isVillage the isVillage to set
	 */
	public void setVillage(boolean isVillage) {
		this.isVillage = isVillage;
	}
	/**
	 * @return the villageTransact
	 */
	public String getVillageTransact() {
		return villageTransact;
	}
	/**
	 * @param villageTransact the villageTransact to set
	 */
	public void setVillageTransact(String villageTransact) {
		this.villageTransact = villageTransact;
	}
	/**
	 * @return the coordinate
	 */
	public SituationCoordinate getCoordinate() {
		return coordinate;
	}
	/**
	 * @param coordinate the coordinate to set
	 */
	public void setCoordinate(SituationCoordinate coordinate) {
		this.coordinate = coordinate;
	}
	/**
	 * @return the coordinates
	 */
	public Set getCoordinates() {
		return coordinates;
	}
	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(Set coordinates) {
		this.coordinates = coordinates;
	}
}