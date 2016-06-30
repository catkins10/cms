package com.yuanluesoft.jeaf.workflow.pojo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.StringUtils;


/**
 * 
 * @author yuanlue
 *
 */
public class WorkflowData extends Record {
	private String workflowInstanceId; //工作流实例ID
	private Set workItems;
	private Set opinions;
	private Set visitors;
	
	/**
	 * 获取流程状态
	 * @return
	 */
	public String getWorkflowStatus() {
		try {
			if(workItems==null || workItems.isEmpty()) {
				return "办理完毕";
			}
			List status = new ArrayList();
			String ret = null;
			for(Iterator iterator = workItems.iterator(); iterator.hasNext();) {
			    WorkItem workItem = (WorkItem)iterator.next();
			    if(status.indexOf(workItem.getActivityName())==-1) {
			    	status.add(workItem.getActivityName());
			    	ret = (ret==null ? "" : ret + ",") + workItem.getActivityName() + (workItem.getIsReverse()=='1' ? "[回退]" : "") + (workItem.getIsUndo()=='1' ? "[取回]" : "");
			    }
			}
			return ret;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取办理剩余时间,以毫秒为单位
	 * @return
	 */
	public long getTimeLeft() {
		try {
			if(workItems==null || workItems.isEmpty()) {
				return Long.MAX_VALUE;
			}
			long now = DateTimeUtils.now().getTime();
			long timeLeft = Long.MAX_VALUE;
			for(Iterator iterator=workItems.iterator(); iterator.hasNext();) {
				WorkItem workItem = (WorkItem)iterator.next();
				if(workItem.getDeadline()<=0) { //没有指定办理期限
					continue;
				}
				long left = workItem.getCreated().getTime() + (long)((24 * 3600 * 1000) * workItem.getDeadline()) - now;
				if(left<timeLeft) {
					timeLeft = left;
				}
			}
			return timeLeft;
		}
		catch(Exception e) {
			return Long.MAX_VALUE;
		}
	}
	
	/**
	 * 获取办理剩余时间百分比
	 * @return
	 */
	public double getTimeLeftPercent() {
		try {
			if(workItems==null || workItems.isEmpty()) {
				return 100;
			}
			long now = DateTimeUtils.now().getTime();
			double timeLeft = 100;
			for(Iterator iterator=workItems.iterator(); iterator.hasNext();) {
				WorkItem workItem = (WorkItem)iterator.next();
				if(workItem.getDeadline()<=0) { //没有指定办理期限
					continue;
				}
				double deadline = (24 * 3600 * 1000) * workItem.getDeadline();
				double left = Math.round((workItem.getCreated().getTime() + deadline - now) / deadline * 10000) / 100.0;
				if(left<timeLeft) {
					timeLeft = left;
				}
			}
			return timeLeft;
		}
		catch(Exception e) {
			return 100;
		}
	}
	
	/**
	 * 获取办理期限,以天为单位
	 * @return
	 */
	public double getDeadline() {
		try {
			if(workItems==null || workItems.isEmpty()) {
				return 0;
			}
			double deadline = Long.MAX_VALUE;
			for(Iterator iterator=workItems.iterator(); iterator.hasNext();) {
				WorkItem workItem = (WorkItem)iterator.next();
				if(workItem.getDeadline()>0 && workItem.getDeadline()<deadline) {
					deadline = workItem.getDeadline();
				}
			}
			return deadline==Long.MAX_VALUE ? 0 : deadline;
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * 获取办理剩余时间
	 * @return
	 */
	public String getTimeLeftTitle() {
		long timeLeft = getTimeLeft();
		if(timeLeft==Long.MAX_VALUE) {
			return null;
		}
		return (timeLeft<0 ? "超时" : "") + StringUtils.getTime(Math.abs(timeLeft));
	}
	
	/**
	 * 获取发送时间
	 * @return
	 */
	public Timestamp getWorkflowSendTime() {
		try {
			return ((WorkItem)workItems.iterator().next()).getCreated();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取流程名称
	 * @return
	 */
	public String getWorkflowName() {
		try {
			return ((WorkItem)workItems.iterator().next()).getWorkflowName();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取发送人
	 * @return
	 */
	public String getWorkflowSender() {
		try {
			return ((WorkItem)workItems.iterator().next()).getPreviousParticipantName();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取办理人,最多3个
	 * @return
	 */
	public String getWorkflowParticipants() {
		try {
			List participants = new ArrayList();
			String ret = null;
			int count = 0;
			for(Iterator iterator = workItems.iterator(); iterator.hasNext();) {
			    WorkItem workItem = (WorkItem)iterator.next();
			    if(participants.indexOf(workItem.getParticipantName())!=-1) {
			    	continue;
			    }
		    	participants.add(workItem.getParticipantName());
		    	ret = (ret==null ? "" : ret + ",") + workItem.getParticipantName();
		    	count++;
		    	if(count==3) {
		    		if(iterator.hasNext()) {
		    			ret += "等";
		    		}
		    		break;
		    	}
			}
			return ret;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * @return the opinions
	 */
	public Set getOpinions() {
		return opinions;
	}
	/**
	 * @param opinions the opinions to set
	 */
	public void setOpinions(Set opinions) {
		this.opinions = opinions;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
	/**
	 * @return the workItems
	 */
	public Set getWorkItems() {
		return workItems;
	}
	/**
	 * @param workItems the workItems to set
	 */
	public void setWorkItems(Set workItems) {
		this.workItems = workItems;
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
}