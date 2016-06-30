package com.yuanluesoft.jeaf.util;

/**
 * 
 * @author linchuan
 *
 */
public class PasswordUtils {
	public static final int PASSWORD_STRENGTH_NONE = 0; //密码强度:未能评级
	public static final int PASSWORD_STRENGTH_LOW = 1; //密码强度:弱
	public static final int PASSWORD_STRENGTH_MIDDLE = 2; //密码强度:中
	public static final int PASSWORD_STRENGTH_HIGH = 3; //密码强度:强

	/**
	 * 获取密码强度
	 * @param password
	 * @return
	 */
	public static int getPasswordStrength(String password) {
		if(password==null || password.length()<6) {
			return 0; //未能评级
		}
		int number = 0; //是否有数字
		int lowerCaseLetter = 0; //小写字母
		int upperCaseLetter = 0; //大写字母
		int otherLetter = 0; //其它字符
		for(int i=0; i<password.length(); i++) {
			char ch = password.charAt(i);
			if(ch>='0' && ch<='9') {
				number = 1;
			}
			else if(ch>='a' && ch<='z') {
				lowerCaseLetter = 1;
			}
			else if(ch>='A' && ch<='Z') {
				upperCaseLetter = 1;
			}
			else {
				otherLetter = 1;
			}
		}
		int strength = number + lowerCaseLetter + upperCaseLetter + otherLetter;
		return strength>3 ? 3 : strength;
	}
}