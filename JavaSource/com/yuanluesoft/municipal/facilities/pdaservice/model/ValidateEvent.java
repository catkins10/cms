package com.yuanluesoft.municipal.facilities.pdaservice.model;

/**
 * 监督员验证事件真实性
 * @author linchuan
 *
 */
public class ValidateEvent {
	public EventEntity eventBody;
	public boolean IsTrue; //是否属实
	public String Comment; //处理意见
    public String MessageId; //任务ID
    public String PDAUserCode; //PDA用户工号
}
