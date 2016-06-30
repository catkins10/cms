package com.yuanluesoft.municipal.facilities.pdaservice.model;

import java.util.Calendar;

/**
 * 
 * @author linchuan
 *
 */
public class ObserverMessage {
    public String id; //案件id
	public String[] ImageUrl; //图片地址列表（字符串数组）
    public double YPos; //案发地x坐标
    public double XPos; //案发地y坐标
    public String MsgRelationID; //任务id
    public String MsgReceiver; //接收人id
    public String MsgReceiverType;
    public String MsgTitle; //任务标题
    public String MsgContent; //任务内容
    public String MsgSenderID;
    public String MsgSenderName;
    public String MsgStatus; //Unread or Readed or Finished 任务状态（未读、已读、完成）
    public String MsgType; //CheckPublicReport or CheckProDptResult or EventHandle or EvtEndLDChenk 任务类型：核实（0）、核查（1）、任务处理（2）、案件结束领导审核（3）
    public long SortID; //任务序号
    public Calendar EvtFinishDate; //截止办理时间
    public Calendar DbCreateDate;
    public String DbCreateUser;
    public Calendar DbLastUpdateDate;
    public String DbLastUpdateUser;
    public String ActivityInstanceId;
    public String StreetName;
}