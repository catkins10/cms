package com.yuanluesoft.jeaf.word.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 词库服务
 * @author linchuan
 *
 */
public interface WordService extends BusinessService {
	public static final int WORD_TYPE_UNKNOWN = 0; //是否名词
	public static final int WORD_TYPE_NOUN = 1; //是否名词
	public static final int WORD_TYPE_VERB = 2; //是否动词
	public static final int WORD_TYPE_ADJECTIVE = 3; //是否形容词
	public static final int WORD_TYPE_ADVERB = 4; //是否副词
	public static final int WORD_TYPE_MEASURE = 5; //是否量词
	public static final int WORD_TYPE_ONOMATOPOEIA = 6; //是否拟声词
	public static final int WORD_TYPE_STRUCTURAL = 7; //是否结构助词
	public static final int WORD_TYPE_AUXILIARY = 8; //是否助词
	public static final int WORD_TYPE_COORDINATING = 9; //是否并列连词
	public static final int WORD_TYPE_CONJUNCTION = 10; //是否连词
	public static final int WORD_TYPE_PREPOSITION = 11; //是否介词
	public static final int WORD_TYPE_PRONOUN = 12; //是否代词
	public static final int WORD_TYPE_INTERROGATIVE = 13; //是否疑问词
	public static final int WORD_TYPE_NUMERAL = 14; //是否数词
	public static final int WORD_TYPE_PROVERBS = 15; //是否成语
	
	public static final String[] WORD_TYPE_NAMES = {"", "noun", "verb", "adjective", "adverb", "measure", "onomatopoeia", "structural",
		"auxiliary", "coordinating", "conjunction", "preposition", "pronoun", "interrogative", "numeral", "proverbs"};

	public static final String[] WORD_TYPE_TITLES = {"", "名词", "动词", "形容词", "副词", "量词", "拟声词", "结构助词",
		"助词", "并列连词", "连词", "介词", "代词", "疑问词", "数词", "成语"};
	
	/**
	 * 解析中文句子,返回中文词语(com.yuanluesoft.jeaf.word.model.Word)列表
	 * @param sentence
	 * @param wordTypes 默认:[WORD_TYPE_NOUN, WORD_TYPE_VERB, WORD_TYPE_ADJECTIVE] 即:名词,动词,形容词
	 * @return
	 * @throws ServiceException
	 */
	public List parseChineseSentence(String sentence, int[] wordTypes) throws ServiceException;
}