package com.yuanluesoft.jeaf.word.model;

/**
 * 
 * @author linchuan
 *
 */
public class Word {
	private String word; //词语
	private int type; //类型
	
	public Word(String word, int type) {
		super();
		this.word = word;
		this.type = type;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}
	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}
}