/*
 * Created on 2006-5-17
 *
 */
package com.yuanluesoft.jeaf.security.service;

import com.yuanluesoft.jeaf.security.service.exception.SecurityException;

/**
 * 
 * @author linchuan
 *
 */
public interface CryptService {

    /**
     * 加密
     * @param plain
     * @param key
     * @param useBraces 是否使用花括号,如果使用了,则表示文本已经加密
     * @return
     * @throws SecurityException
     */
    public String encrypt(String plain, String key, boolean useBraces) throws SecurityException;
   
    /**
     * 解密
     * @param encrypted
     * @param key
     * @param useBraces 是否使用花括号,如果使用了,则表示文本已经加密
     * @return
     * @throws SecurityException
     */
    public String decrypt(String encrypted, String key, boolean useBraces) throws SecurityException;
}
