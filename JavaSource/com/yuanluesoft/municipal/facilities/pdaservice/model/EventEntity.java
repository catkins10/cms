package com.yuanluesoft.municipal.facilities.pdaservice.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 
 * @author linchuan
 *
 */
public class EventEntity extends BaseEntity implements Serializable {
	public String BigclassID;
    public String BigclassName;
    public String Code;
    public String CommunityID;
    public String CommunityName;
    public String ContactTel;
    public String ContactWay;
    public Calendar Createddate;
    public String DistrictID;
    public String DistrictName;
    public int DuplicateCase;
    public Calendar EndCaseTime;
    public String EventTypeID;
    public String EventTypeName;
    public String GridCode;
    public String GridID;
    public String GridName;
    public String ID;
    public String ImportantLevel;
    public String ImportantName;
    public boolean IsReceipt;
    public int IsReceipted;
    public Calendar Lastupdateddate;
    public String LastupdatedID;
    public String ObserverCheckDesc;
    public String ObserverCheckResult;
    public String ObserverID;
    public String ObserverName;
    public String ObserverPdaNum;
    public String PartCode;
    public String PartID;
    public String PhoneNum;
    public String PositionDesc;
    public String ProbDesc;
    public String ProbSource;
    public String ProbSourceName;
    public String ProDtpId;
    public String Projcode;
    public String Projtitle;
    public String RelationID;
    public String Remark;
    public String Reporter;
    public String RevertObject;
    public String RevertTypeName;
    public String RevertWayID;
    public String ShowColor;
    public Calendar SignTime;
    public String SignUserID;
    public String SmallclassID;
    public String SmallclassName;
    public Calendar StartCaseTime;
    public int Status;
    public String StreetID;
    public String StreetName;
    public int SuperviseTimes;
    public int UrgencyTimes;
    public String UserID;
    public Calendar WishFinishDate;
    public String WorkflowInstanceIdentifier;
    public String WorkGridID;
    public String WorkGridName;
    public double XPos;
    public double YPos;
}