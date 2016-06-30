package com.yuanluesoft.jeaf.membermanage.model;


/**
 * 
 * @author yuanlue
 *
 */
public class MemberLogin {
	private String memberLoginName; //登录用户名
	private long memberId; //用户ID
	private int memberType; //用户类型, PERSON_TYPE_EMPLOYEE/普通用户, PERSON_TYPE_TEACHER/教师, PERSON_TYPE_STUDENT/学生, PERSON_TYPE_GENEARCH/家长, PERSON_TYPE_OTHER/其他用户自定义类型
	private String password; //密码,用于密码安全等级校验,空不校验,且不需要定期修改
	private boolean passwordWrong; //是否密码错误
	
	/**
	 * 用户登录
	 * @param memberLoginName 登录用户名
	 * @param memberId 用户ID
	 * @param memberType 用户类型, PERSON_TYPE_EMPLOYEE/普通用户, PERSON_TYPE_TEACHER/教师, PERSON_TYPE_STUDENT/学生, PERSON_TYPE_GENEARCH/家长, PERSON_TYPE_OTHER/其他用户自定义类型
	 * @param password 密码,用于密码安全等级校验,null则不校验,且不需要定期修改
	 * @param passwordWrong 是否密码错误
	 */
	public MemberLogin(String memberLoginName, long memberId, int memberType, String password, boolean passwordWrong) {
		super();
		this.memberLoginName = memberLoginName;
		this.memberId = memberId;
		this.memberType = memberType;
		this.password = password;
		this.passwordWrong = passwordWrong;
	}
	/**
	 * @return the memberId
	 */
	public long getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the memberLoginName
	 */
	public String getMemberLoginName() {
		return memberLoginName;
	}
	/**
	 * @param memberLoginName the memberLoginName to set
	 */
	public void setMemberLoginName(String memberLoginName) {
		this.memberLoginName = memberLoginName;
	}
	/**
	 * @return the memberType
	 */
	public int getMemberType() {
		return memberType;
	}
	/**
	 * @param memberType the memberType to set
	 */
	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the passwordWrong
	 */
	public boolean isPasswordWrong() {
		return passwordWrong;
	}
	/**
	 * @param passwordWrong the passwordWrong to set
	 */
	public void setPasswordWrong(boolean passwordWrong) {
		this.passwordWrong = passwordWrong;
	}
}