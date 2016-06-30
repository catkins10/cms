package com.yuanluesoft.jeaf.sensitive.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 敏感词(sensitive_word)
 * @author linchuan
 *
 */
public class SensitiveWord extends Record {
	private String words; //敏感词,每个一行,允许使用正则表达式

	/**
	 * @return the words
	 */
	public String getWords() {
		return words;
	}

	/**
	 * @param words the words to set
	 */
	public void setWords(String words) {
		this.words = words;
	}
}