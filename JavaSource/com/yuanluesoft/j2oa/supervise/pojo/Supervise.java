package com.yuanluesoft.j2oa.supervise.pojo;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;

import com.yuanluesoft.j2oa.supervise.service.SuperviseService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 督办(supervise_supervise)
 * @author linchuan
 *
 */
public class Supervise extends WorkflowData {
	private String sn; //督办号
	private String content; //工作内容
	private String departmentIds; //责任部门ID
	private String departmentNames; //责任部门
	private String departmentSupervisors; //分管领导
	private String transactorIds; //经办人ID
	private String transactorNames; //经办人
	private String result; //落实情况
	private Timestamp completeTime; //落实时间
	private int status = 0; //状态,0/新建,1/正在落实,2/销号
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //创建时间
	private String remark; //备注
	private String workflowInstanceId; //工作流实例ID
	private Set timeLimits; //时限
	
	/**
	 * 获取时限
	 * @return
	 */
	public String getTimeLimit() {
		String timeLimit = null;
		SuperviseTimeLimit previousTimeLimit = null;
		try {
			for(Iterator iterator = timeLimits.iterator(); iterator.hasNext();) {
				SuperviseTimeLimit superviseTimeLimit = (SuperviseTimeLimit)iterator.next();
				timeLimit = (timeLimit==null ? "" : timeLimit + (superviseTimeLimit.getTimeLimit().after(previousTimeLimit.getTimeLimit()) ? " 延期至" : " 提前至")) + DateTimeUtils.formatTimestamp(superviseTimeLimit.getTimeLimit(), "MM月dd日");
				previousTimeLimit = superviseTimeLimit;
			}
		}
		catch(Exception e) {
			
		}
		return timeLimit;
	}
	
	/**
	 * 获取最新的时限
	 * @return
	 */
	public Timestamp getLastTimeLimit() {
		try {
			for(Iterator iterator = timeLimits.iterator();;) {
				SuperviseTimeLimit superviseTimeLimit = (SuperviseTimeLimit)iterator.next();
				if(!iterator.hasNext()) {
					return superviseTimeLimit.getTimeLimit();
				}
			}
		}
		catch(Exception e) {
			
		}
		return null;
	}
	
	/**
	 * 获取状态说明
	 * @return
	 */
	public String getStatusTitle() {
		return SuperviseService.SUPERVISE_STATUS_TITLES[status];
	}
	
	/**
	 * @return the completeTime
	 */
	public Timestamp getCompleteTime() {
		return completeTime;
	}
	/**
	 * @param completeTime the completeTime to set
	 */
	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the departmentIds
	 */
	public String getDepartmentIds() {
		return departmentIds;
	}
	/**
	 * @param departmentIds the departmentIds to set
	 */
	public void setDepartmentIds(String departmentIds) {
		this.departmentIds = departmentIds;
	}
	/**
	 * @return the departmentNames
	 */
	public String getDepartmentNames() {
		return departmentNames;
	}
	/**
	 * @param departmentNames the departmentNames to set
	 */
	public void setDepartmentNames(String departmentNames) {
		this.departmentNames = departmentNames;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the timeLimits
	 */
	public Set getTimeLimits() {
		return timeLimits;
	}
	/**
	 * @param timeLimits the timeLimits to set
	 */
	public void setTimeLimits(Set timeLimits) {
		this.timeLimits = timeLimits;
	}
	/**
	 * @return the transactorIds
	 */
	public String getTransactorIds() {
		return transactorIds;
	}
	/**
	 * @param transactorIds the transactorIds to set
	 */
	public void setTransactorIds(String transactorIds) {
		this.transactorIds = transactorIds;
	}
	/**
	 * @return the transactorNames
	 */
	public String getTransactorNames() {
		return transactorNames;
	}
	/**
	 * @param transactorNames the transactorNames to set
	 */
	public void setTransactorNames(String transactorNames) {
		this.transactorNames = transactorNames;
	}
	/**
	 * @return the workflowInstanceId
	 */
	public String getWorkflowInstanceId() {
		return workflowInstanceId;
	}
	/**
	 * @param workflowInstanceId the workflowInstanceId to set
	 */
	public void setWorkflowInstanceId(String workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}

	/**
	 * @return the departmentSupervisors
	 */
	public String getDepartmentSupervisors() {
		return departmentSupervisors;
	}

	/**
	 * @param departmentSupervisors the departmentSupervisors to set
	 */
	public void setDepartmentSupervisors(String departmentSupervisors) {
		this.departmentSupervisors = departmentSupervisors;
	}
}