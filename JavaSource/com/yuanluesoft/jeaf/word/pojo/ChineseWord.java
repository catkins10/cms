package com.yuanluesoft.jeaf.word.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 中文词库(chinese_word)
 * @author linchuan
 *
 */
public class ChineseWord extends Record {
	private String word; //词语
	private int wordLength; //长度
	private int isNoun; //是否名词
	private int isVerb; //是否动词
	private int isAdjective; //是否形容词
	private int isAdverb; //是否副词
	private int isMeasure; //是否量词
	private int isOnomatopoeia; //是否拟声词
	private int isStructural; //是否结构助词
	private int isAuxiliary; //是否助词
	private int isCoordinating; //是否并列连词
	private int isConjunction; //是否连词
	private int isPreposition; //是否介词
	private int isPronoun; //是否代词
	private int isInterrogative; //是否疑问词
	private int isNumeral; //是否数词
	private int isProverbs; //是否成语
	
	/**
	 * @return the isAdjective
	 */
	public int getIsAdjective() {
		return isAdjective;
	}
	/**
	 * @param isAdjective the isAdjective to set
	 */
	public void setIsAdjective(int isAdjective) {
		this.isAdjective = isAdjective;
	}
	/**
	 * @return the isAdverb
	 */
	public int getIsAdverb() {
		return isAdverb;
	}
	/**
	 * @param isAdverb the isAdverb to set
	 */
	public void setIsAdverb(int isAdverb) {
		this.isAdverb = isAdverb;
	}
	/**
	 * @return the isAuxiliary
	 */
	public int getIsAuxiliary() {
		return isAuxiliary;
	}
	/**
	 * @param isAuxiliary the isAuxiliary to set
	 */
	public void setIsAuxiliary(int isAuxiliary) {
		this.isAuxiliary = isAuxiliary;
	}
	/**
	 * @return the isConjunction
	 */
	public int getIsConjunction() {
		return isConjunction;
	}
	/**
	 * @param isConjunction the isConjunction to set
	 */
	public void setIsConjunction(int isConjunction) {
		this.isConjunction = isConjunction;
	}
	/**
	 * @return the isCoordinating
	 */
	public int getIsCoordinating() {
		return isCoordinating;
	}
	/**
	 * @param isCoordinating the isCoordinating to set
	 */
	public void setIsCoordinating(int isCoordinating) {
		this.isCoordinating = isCoordinating;
	}
	/**
	 * @return the isInterrogative
	 */
	public int getIsInterrogative() {
		return isInterrogative;
	}
	/**
	 * @param isInterrogative the isInterrogative to set
	 */
	public void setIsInterrogative(int isInterrogative) {
		this.isInterrogative = isInterrogative;
	}
	/**
	 * @return the isMeasure
	 */
	public int getIsMeasure() {
		return isMeasure;
	}
	/**
	 * @param isMeasure the isMeasure to set
	 */
	public void setIsMeasure(int isMeasure) {
		this.isMeasure = isMeasure;
	}
	/**
	 * @return the isNoun
	 */
	public int getIsNoun() {
		return isNoun;
	}
	/**
	 * @param isNoun the isNoun to set
	 */
	public void setIsNoun(int isNoun) {
		this.isNoun = isNoun;
	}
	/**
	 * @return the isNumeral
	 */
	public int getIsNumeral() {
		return isNumeral;
	}
	/**
	 * @param isNumeral the isNumeral to set
	 */
	public void setIsNumeral(int isNumeral) {
		this.isNumeral = isNumeral;
	}
	/**
	 * @return the isOnomatopoeia
	 */
	public int getIsOnomatopoeia() {
		return isOnomatopoeia;
	}
	/**
	 * @param isOnomatopoeia the isOnomatopoeia to set
	 */
	public void setIsOnomatopoeia(int isOnomatopoeia) {
		this.isOnomatopoeia = isOnomatopoeia;
	}
	/**
	 * @return the isPreposition
	 */
	public int getIsPreposition() {
		return isPreposition;
	}
	/**
	 * @param isPreposition the isPreposition to set
	 */
	public void setIsPreposition(int isPreposition) {
		this.isPreposition = isPreposition;
	}
	/**
	 * @return the isPronoun
	 */
	public int getIsPronoun() {
		return isPronoun;
	}
	/**
	 * @param isPronoun the isPronoun to set
	 */
	public void setIsPronoun(int isPronoun) {
		this.isPronoun = isPronoun;
	}
	/**
	 * @return the isProverbs
	 */
	public int getIsProverbs() {
		return isProverbs;
	}
	/**
	 * @param isProverbs the isProverbs to set
	 */
	public void setIsProverbs(int isProverbs) {
		this.isProverbs = isProverbs;
	}
	/**
	 * @return the isStructural
	 */
	public int getIsStructural() {
		return isStructural;
	}
	/**
	 * @param isStructural the isStructural to set
	 */
	public void setIsStructural(int isStructural) {
		this.isStructural = isStructural;
	}
	/**
	 * @return the isVerb
	 */
	public int getIsVerb() {
		return isVerb;
	}
	/**
	 * @param isVerb the isVerb to set
	 */
	public void setIsVerb(int isVerb) {
		this.isVerb = isVerb;
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
	/**
	 * @return the wordLength
	 */
	public int getWordLength() {
		return wordLength;
	}
	/**
	 * @param wordLength the wordLength to set
	 */
	public void setWordLength(int wordLength) {
		this.wordLength = wordLength;
	}
}