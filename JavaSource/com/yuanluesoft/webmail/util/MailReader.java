package com.yuanluesoft.webmail.util;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.webmail.model.Mail;
import com.yuanluesoft.webmail.model.MailAttachment;
import com.yuanluesoft.webmail.model.MailBody;
import com.yuanluesoft.webmail.model.MailContentHeader;
import com.yuanluesoft.webmail.model.MailHeader;

/**
 * 读取邮件
 * @author linchuan
 *
 */
public class MailReader {
    private final int BUFFER_SIZE = 2048; //最小256
    //邮件读取级别
    public static final char READ_LEVEL_SUBJECT_ONLY = '1'; //只读取主题
	public static final char READ_LEVEL_SUBJECT_AND_BODY = '2'; //读取主题和正文
	public static final char READ_LEVEL_FULL = '3'; //读取全部
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.webmail.parser.MailParser#parseMail(java.io.Reader, boolean)
     */
    public Mail readMail(Reader reader, char readLevel) throws ServiceException {
        Mail mail = new Mail();
        MailDataReader mailDataReader = new MailDataReader(reader, BUFFER_SIZE);
        try {
            MailHeader mailHeader = new MailHeader();
            parseHeader(mailDataReader, mailHeader); //解析邮件头,并设置下一个对象的开始位置
            mail.setMailHeader(mailHeader);
            if(readLevel==READ_LEVEL_SUBJECT_ONLY) { //只读取主题
                return mail;
            }
            mail.setMailBodies(new ArrayList());
            if(mailHeader.getBoundary()==null) { //没有分隔标志,则邮件头以后全都是正文
                MailBody mailBody = new MailBody();
                mailBody.setCharset(mailHeader.getCharset());
                mailBody.setContentTransferEncoding(mailHeader.getContentTransferEncoding());
                mailBody.setContentType(mailHeader.getContentType());
                parseMailBody(mailBody, mailDataReader, mailBody.getContentTransferEncoding(), null);
                mail.getMailBodies().add(mailBody);
                return mail;
            }
            //有分隔标志,则需要先定位到分隔标志后,再解析正文
            Stack  boundaryStack = new Stack();
            String boundary = "--" + mailHeader.getBoundary();
            locateBoundary(mailDataReader, boundary);
            for(;;) {
                MailContentHeader mailContentHeader = new MailContentHeader();
                parseHeader(mailDataReader, mailContentHeader); //解析邮件内容头信息
                if(mailContentHeader.getName()!=null) { //附件
                	MailAttachment mailAttachment = new MailAttachment();
                    mailAttachment.setContentId(mailContentHeader.getContentId());
                    mailAttachment.setContentTransferEncoding(mailContentHeader.getContentTransferEncoding());
                    mailAttachment.setName(mailContentHeader.getName());
                    mailAttachment.setContentType(mailContentHeader.getContentType());
                    if(mail.getMailAttachments()==null) {
                        mail.setMailAttachments(new ArrayList());
                    }
                    mail.getMailAttachments().add(mailAttachment);
                    if(parseMailAttachment(mailAttachment, mailDataReader, "base64".equals(mailAttachment.getContentTransferEncoding()), boundary)) {
                    	//区段的最后一个附件
                        if(boundaryStack.isEmpty()) {
                        	break;
                        }
                        boundary = (String)boundaryStack.pop();
	                    locateBoundary(mailDataReader, boundary);
                    }
                }
				else if("multipart/alternative".equals(mailContentHeader.getContentType()) ||
						"multipart/alternative".equals(mailContentHeader.getType())) { //开始MIME区段
					boundaryStack.push(boundary);
				    boundary = "--" + mailContentHeader.getBoundary();
					locateBoundary(mailDataReader, boundary);
				}
				else { //正文
					MailBody mailBody = new MailBody();
	                mailBody.setCharset(mailContentHeader.getCharset());
	                mailBody.setContentTransferEncoding(mailContentHeader.getContentTransferEncoding());
	                mailBody.setContentType(mailContentHeader.getContentType());
	                mail.getMailBodies().add(mailBody);
	                if(parseMailBody(mailBody, mailDataReader, mailBody.getContentTransferEncoding(), boundary)) {
	                	if(readLevel==READ_LEVEL_SUBJECT_AND_BODY) { //读取主题和正文
	                		return mail;
	                	}
	                	//最后一个正文
	                	boundary = (String)boundaryStack.pop();
		                locateBoundary(mailDataReader, boundary);
	                }
				}
            }
        }
        catch (Exception e) {
        	
        }
        finally {
            mailDataReader.close();
        }
        return mail;
    }
    
    /**
     * 解析邮件头
     * @param mailHeaderReader
     * @return
     * @throws ServiceException
     */
    private void parseHeader(MailDataReader mailDataReader, Object header) throws ServiceException, ServiceException {
        StringBuffer sb = new StringBuffer();

        char data;
        for(;;) {
            data = mailDataReader.read();
            if(data=='\r') {
                continue;
            }
            if(data!='\n') {
                sb.append(data);
                continue;
            }
            mailDataReader.readMore(2);
            data = mailDataReader.preview(0);
            if(data==' ' || data=='\t') {
                mailDataReader.skip(1);
                continue;
            }
            //解析属性
            parseProperty(header, sb.toString());
            //开始下一个属性的解析
            sb = new StringBuffer();
            data = mailDataReader.preview(0);
            if(data=='\n' ||
              (data=='\r' && mailDataReader.preview(1)=='\n')) { //头结束
                mailDataReader.skip(data=='\n' ? 1 : 2);
                return;
            }
        }
	}
    
	/**
	 * 解析邮件头属性,返回下一个对象开始位置
	 * @param header
	 * @param propertyText
	 * @throws ServiceException
	 */
	private void parseProperty(Object header, String propertyText) throws ServiceException {
		int index = propertyText.indexOf(':');
		if(index==-1 || index==propertyText.length()-1) {
		    return;
		}
		String propertyName = propertyText.substring(0, index);
		String propertyValue = propertyText.substring((propertyText.charAt(index+1)==' ' ? 2 : 1) + index);
		Decoder decoder = Decoder.getInstance();
		try {
		    if(propertyName.equals("From")) { //发送人
		        PropertyUtils.setProperty(header, "mailFrom", decoder.decodePropertyValue(propertyValue));
		    }
		    else if(propertyName.equals("To")) { //收件人
		        PropertyUtils.setProperty(header, "mailTo", decoder.decodePropertyValue(propertyValue));
		    }
		    else if(propertyName.equals("Cc")) { //抄送
		        PropertyUtils.setProperty(header, "mailCc", decoder.decodePropertyValue(propertyValue));
		    }
		    else if(propertyName.equals("Bcc")) { //密送
		        PropertyUtils.setProperty(header, "mailBcc", decoder.decodePropertyValue(propertyValue));
		    }
		    else if(propertyName.equals("Subject")) { //主题
		        PropertyUtils.setProperty(header, "subject", decoder.decodePropertyValue(propertyValue));
		    }
		    else if(propertyName.equals("Content-Type")) { //邮件类型
		        String[] values = propertyValue.split(";");
		        PropertyUtils.setProperty(header, "contentType", values[0]);
		        for(int i=values.length - 1; i>0; i--) {
		            values[i] = values[i].trim();
		            if(values[i].startsWith("boundary=")) { //分隔符
		                PropertyUtils.setProperty(header, "boundary", (values[i].charAt(9)=='"' ? values[i].substring(10, values[i].length() - 1) : values[i].substring(9)));
		            }
		            else if(values[i].startsWith("charset=")) { //字符集
		                PropertyUtils.setProperty(header, "charset", (values[i].charAt(8)=='"' ? values[i].substring(9, values[i].length() - 1) : values[i].substring(8)));
		            }
		            else if(values[i].startsWith("name=")) {
		                PropertyUtils.setProperty(header, "name", decoder.decodePropertyValue(values[i].charAt(5)=='"' ? values[i].substring(6, values[i].length() - 1) : values[i].substring(5)));
					}
		            else if(values[i].startsWith("type=")) {
		                PropertyUtils.setProperty(header, "type", decoder.decodePropertyValue(values[i].charAt(5)=='"' ? values[i].substring(6, values[i].length() - 1) : values[i].substring(5)));
					}
		        }
		    }
		    else if(propertyName.equals("Mime-Version")) { //MIME版本
		        PropertyUtils.setProperty(header, "mimeVersion", propertyValue);
		    }
		    else if(propertyName.equals("X-Priority")) { //重要性
		        PropertyUtils.setProperty(header, "priority", propertyValue);
		    }
		    else if(propertyName.equals("Date")) { //接收日期
		        try {
		            PropertyUtils.setProperty(header, "receiveDate", DateTimeUtils.parseCookieTimestamp(propertyValue));
		        }
		        catch (ParseException e) {
		            Logger.exception(e);
		            throw new ServiceException();
		        }
		    }
		    else if(propertyName.equals("Content-Transfer-Encoding")) {
		        PropertyUtils.setProperty(header, "contentTransferEncoding", propertyValue);
		    }
		    if(propertyName.equals("Content-ID")) { //ID
		        PropertyUtils.setProperty(header, "contentId", propertyValue.startsWith("<") && propertyValue.endsWith(">") ? propertyValue.substring(1, propertyValue.length() - 1)  : propertyValue);
			}
			else if(propertyName.equals("Content-Disposition") && PropertyUtils.isReadable(header, "contentDisposition")) {
				int split = propertyValue.indexOf(';');
				if(split==-1) {
				    PropertyUtils.setProperty(header, "contentDisposition", propertyValue);
				}
				else {
				    PropertyUtils.setProperty(header, "contentDisposition", propertyValue.substring(0, split));
					int indexFileName = propertyValue.indexOf("filename=", split + 1);
					if(indexFileName!=-1) {
						String name = propertyValue.substring(indexFileName + 9);
						if(name.charAt(0)=='"') {
							name = name.substring(1, name.length() - 1);
						}
						PropertyUtils.setProperty(header, "fileName", decoder.decodePropertyValue(name));
					}
				}
			}
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * 查找分隔行
	 * @param mailDataReader
	 * @param seek
	 * @param boundary
	 * @return
	 * @throws ServiceException
	 * @throws IOException
	 */
	private void locateBoundary(MailDataReader mailDataReader, String boundary) throws ServiceException, ServiceException {
	    char data;
	    for(;;) {
	        data = mailDataReader.read();
	        if(data!='-') {
	            continue;
	        }
	        mailDataReader.readMore(boundary.length() + 1);
	        ///检查分隔行
	        int j=0;
	        for(; j<boundary.length()-1 && boundary.charAt(j+1)==mailDataReader.preview(j); j++);
	        if(j==boundary.length()-1 &&
	           (mailDataReader.preview(j)=='\n' || (mailDataReader.preview(j)=='\r' && mailDataReader.preview(1+j)=='\n'))) {
	            mailDataReader.skip(j + (mailDataReader.preview(j)=='\r' ? 2 : 1));
	            return;
	        }
	    }
	}
	
	/**
	 * 解析正文,如果是最后一个MIME正文,返回true
	 * @param mail
	 * @param in
	 * @param buffer
	 * @param beginIndex
	 * @param dataLength
	 * @throws ServiceException
	 * @throws IOException
	 * @return
	 */
	private boolean parseMailBody(MailBody mailBody, MailDataReader mailDataReader, String encoding, String boundary) throws ServiceException, ServiceException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    boolean lastBody = false;
	    char data;
	    boolean base64Encoded = "base64".equals(encoding);
        String charset = mailBody.getCharset();
        if(charset==null) { //未指定字符集时,默认为GBK
            charset = "GBK";
        }
	    try {
	        for(;;) {
	            data = mailDataReader.read();
	            if(data=='\r') {
	                if(!base64Encoded) {
	                    buffer.write((int)data);
	                }
	                continue;
	            }
	            if(data!='\n') {
	                if(data<255) {
	                    buffer.write((int)data);
	                }
	                else {
	                    try {
                            buffer.write(("" + data).getBytes(charset));
                        }
	                    catch (IOException e) {
                            
                        }
	                }
	                continue;
	            }
	            if(!base64Encoded) {
	                buffer.write(10);
	            }
	            if(boundary!=null) { //检查分隔行
	                mailDataReader.readMore(boundary.length() + 2);
	                int j=0;
	                for(; j<boundary.length() && boundary.charAt(j)==mailDataReader.preview(j); j++);
	                if(j==boundary.length() && 
	                   (mailDataReader.preview(j)=='\n' || 
	                   (mailDataReader.preview(j)=='\r' && mailDataReader.preview(1+j)=='\n') ||
	                   (mailDataReader.preview(j)=='-' && mailDataReader.preview(1+j)=='-'))) {
	                    if(mailDataReader.preview(j)=='-') {
	                        lastBody = true;
	                    }
	                    mailDataReader.skip(j + (mailDataReader.preview(j)=='\n' ? 1 : 2));
	                    break;
	                }
	            }
	        }
	    }
        catch(ServiceException e) {
            
        }
        if(base64Encoded) {
            try {
                byte[] bytes = buffer.toByteArray();
                int parseLen = new Base64Decoder().decode(bytes);
                mailBody.setBody(new String(new String(bytes, 0, parseLen, charset).getBytes()));
            }
            catch (Exception e) {
                Logger.exception(e);
                throw new ServiceException();
            }
        }
        else { //明文或QP编码
            try {
                String body = buffer.toString(charset);
                int len = body.length();
                int back = 0;
                for(int i=len-1; i>=0 && i>len-6; i--) {
                    if(body.charAt(i)=='\n' || body.charAt(i)=='\r') {
                        back ++;
                    }
                    else {
                        break;
                    }
                }
                if(back>0) {
                    body = body.substring(0, len - back);
                }
                if("quoted-printable".equals(encoding)) {
                    String qpBody = Decoder.getInstance().qpDecode(body, charset);
                    if(mailBody.getContentType().equals("text/html")) {
                        int index = qpBody.indexOf("charset=");
                        if(index!=-1) {
                            int indexEnd = qpBody.indexOf('"', index + 8);
                            charset = qpBody.substring(index + 8, indexEnd);
                            try {
                                qpBody = Decoder.getInstance().qpDecode(body, charset);
                            }
                            catch(Exception e) {
                                
                            }
                        }
                    }
                    mailBody.setBody(new String(qpBody.getBytes()));
                }
                else {
                    mailBody.setBody(new String(body.getBytes()));
                }
            } catch (Exception e) {
                Logger.exception(e);
                throw new ServiceException();
            }
        }
        return lastBody;
	}
	
	/**
	 * 解析附件
	 * @param mail
	 * @param mailContentHeader
	 * @param in
	 * @param buffer
	 * @param beginIndex
	 * @param dataLength
	 * @throws ServiceException
	 * @throws IOException
	 * @return 是否最后一个附件
	 */
	private boolean parseMailAttachment(MailAttachment attachment, MailDataReader mailDataReader, boolean base64Encoded, String boundary) throws ServiceException, ServiceException {
		attachment.setBeginIndex(mailDataReader.getSeek()); //附件的起始位置
	    int adjust = 0;
	    char data;
	    int lineSize = 0;
	    boolean end = false;
	    boolean crlf = true;
	    for(;;) {
	        data = mailDataReader.read();
	        if(data=='=') {
	            adjust++;
	        }
	        else if(data=='\r') {
	            crlf = true;
	        }
	        if(data!='\n') {
	            continue;
	        }
            if(lineSize==0) { //计算每行字节数
                lineSize = (int)(mailDataReader.getSeek() - attachment.getBeginIndex());
            }
	        //检查分隔行
	        mailDataReader.readMore(boundary.length() + 3);
	        int j=0;
	        try {
	            for(; j<boundary.length() && boundary.charAt(j)==mailDataReader.preview(j); j++);
	            if(j==boundary.length() && 
		           (mailDataReader.preview(j)=='\n' || 
		           (mailDataReader.preview(j)=='\r' && mailDataReader.preview(1+j)=='\n') ||
		           (mailDataReader.preview(j)=='-' && mailDataReader.preview(1+j)=='-'))) { //附件结束
		            end = true;
		        }
	        }
	        catch(ServiceException e) {
	            end = true;
	        }
	        if(end) {
	            //设置附件的结束位置
	            attachment.setEndIndex(mailDataReader.getSeek() - (crlf ? 4 : 2));
	            //计算附件大小
	            long size = attachment.getEndIndex() - attachment.getBeginIndex();
	            if(base64Encoded) {
	                size = size / lineSize * (lineSize/4*3) + (size % lineSize) / 4 * 3 - adjust;
	            }
	            attachment.setSize(size);
	            try {
		            if(mailDataReader.preview(1+j)=='-') { //邮件结束
		                return true;
		            }
		            else {
		                mailDataReader.skip(j + (mailDataReader.preview(j)=='\n' ? 1 : 2));
		                return false;
		            }
	            }
		        catch(ServiceException e) {
		            return true;
		        }
	        }
	    }
	}
	
    /**
     * 读取附件,并输出
     * @param reader
     * @param out
     * @param beginIndex
     * @param endIndex
     * @param contentTransferEncoding
     * @throws ServiceException
     */
    public void readAttachment(Reader reader, OutputStream out, long beginIndex, long endIndex, String contentTransferEncoding) throws ServiceException {
        try {
            reader.skip(beginIndex);
            int left = (int)(endIndex - beginIndex);
            if("base64".equals(contentTransferEncoding)) { //base64
				char[] buffer = new char[200];
				int lineSize = 0;
				int wholeLineSize = 0;
				int readLen = reader.read(buffer);
				left -= readLen;
				for(int i=0; i<readLen; i++) { //判断每行字节数
				    if(buffer[i]=='\r' || buffer[i]=='\n') {
				        lineSize = i;
				        wholeLineSize = (buffer[i]=='\r' ? 2 : 1) + lineSize;
				        break;
				    }
				}
				Base64Decoder decoder = new Base64Decoder();
				int bufferSize = wholeLineSize * 20;
				if(bufferSize<1024) {
				    bufferSize = 1024 / wholeLineSize * wholeLineSize;
				}
				char[] mailBuffer = new char[bufferSize];
				int offset = readLen;
				System.arraycopy(buffer, 0, mailBuffer, 0, readLen); //拷贝原有内容到新缓存
				do {
				    if(left>0) {
				        readLen = reader.read(mailBuffer, offset, (bufferSize-offset>left ? left : bufferSize-offset));
						left -= readLen;
						readLen += offset;
						offset = 0;
				    }
					for(int i=0; i<readLen; i+=wholeLineSize) {
						out.write(decoder.decode(mailBuffer, i, (readLen - i < lineSize ? readLen : i + lineSize)));
					}
				}
				while(left>0);
			}
			else {
				char[] buffer = new char[BUFFER_SIZE];
				while(left>0) {
					int readLen = reader.read(buffer, 0, BUFFER_SIZE>left ? left : BUFFER_SIZE);
					left -= readLen;
					for(int i=0; i<readLen; i++) {
					    out.write(buffer[i]);
					}
				}
			}
        }
        catch (IOException e) {
            Logger.exception(e);
            throw new ServiceException();
        }
        finally {
            try {
                out.close();
            }
            catch(Exception e) {
                
            }
            try {
                reader.close();
            }
            catch(Exception e) {
                
            }
        }
    }
    
    /**
     * 邮件数据读取
     * @author linchuan
     * 
     */
    public class MailDataReader {
        private char[] buffer; //缓存最小256
        private int cursor;
        private long seek;
        private Reader reader;
        private int dataSize;
            
        public MailDataReader(Reader reader, int bufferSize) {
            seek = 0;
            cursor = 0;
            buffer = new char[bufferSize<256 ? 256 : bufferSize];
            this.reader = reader;
        }
        
        /**
         * 获取数据并将游标下移
         * @return
         */
        public char read() throws ServiceException {
            if(cursor==dataSize) {
                try {
                    dataSize = reader.read(buffer);
                    seek += dataSize;
                }
                catch (Exception e) {
                    throw new ServiceException(e.getMessage());
                }
                if(dataSize==-1) {
                    throw new ServiceException();
                }
                cursor = 0;
            }
            return buffer[cursor++];
        }
        
        /**
         * 预览数据(不移动游标)
         * @param skip
         * @return
         * @throws ServiceException
         */
        public char preview(int skip) throws ServiceException {
            if(cursor+skip<0 || cursor+skip>dataSize-1) {
                throw new ServiceException();
            }
            return buffer[cursor + skip];
        }
        
        /**
         * 当数据量小于[lessThan]时读取数据
         * @param number
         */
        public void readMore(int lessThan) throws ServiceException {
            int left = dataSize - cursor;
            if(left < lessThan) {
            	System.arraycopy(buffer, cursor, buffer, 0, left);
                try {
                    dataSize = reader.read(buffer, left, buffer.length - left);
                }
                catch (Exception e) {
                    throw new ServiceException(e.getMessage());
                }
                seek += dataSize;
                dataSize += left;
                cursor = 0;
            }
        }
        
        /**
         * 获取当前读取位置
         * @return
         */
        public long getSeek() {
            return seek - dataSize + cursor;
        }
        
        /**
         * 移动游标
         * @param n
         */
        public void skip(int n) {
            cursor += n;
        }
        
        /**
         * 关闭
         *
         */
        public void close() {
            try {
                reader.close();
            }
            catch (IOException e) {

            }
        }
    }
    
    public static void main(String[] args) {
    	try {
	    	Mail mail = new MailReader().readMail(new FileReader("c:\\a.eml"), '3');
	    	for(Iterator iterator = mail.getMailBodies().iterator(); iterator.hasNext();) {
	    		MailBody mailBody = (MailBody)iterator.next();
	    		System.out.println("***************" + mailBody.getBody());
	    	}
    	}
    	catch(Exception e) {
    		
    	}
    }
}