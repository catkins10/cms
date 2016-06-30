package com.yuanluesoft.webmail.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.jeaf.util.Mime;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.webmail.pojo.Mail;
import com.yuanluesoft.webmail.pojo.MailBody;

/**
 * 邮件输出
 * @author linchuan
 *
 */
public class MailWriter {

	/**
	 * 输出邮件
	 * @param writer
	 * @param mail
	 * @param images 图片列表
	 * @param flashs FLASH列表
	 * @param attachments 附件列表
	 * @throws ServiceException
	 */
    public void writeMail(Writer writer, Mail mail, List images, List flashs, List attachments) throws ServiceException {
        try {
        	//获取正文
        	String body = "";
            if(mail.getMailBodies()!=null && !mail.getMailBodies().isEmpty()) {
            	body = ((MailBody) mail.getMailBodies().iterator().next()).getBody();
            }

            //检查图片是否被正文引用到,并修改正文中的图片URL
            if(images!=null && !images.isEmpty()) {
            	for(Iterator iterator=images.iterator(); iterator.hasNext();) {
            		Image image = (Image)iterator.next();
	            	if(body.indexOf(image.getUrl())==-1) {
	            		iterator.remove();
	                }
	            	else { //修改正文中的图片URL
	            		body = StringUtils.replace(body, image.getUrl(), "cid:" + image.hashCode() + "@mail");
	            	}
            	}
            }
            //检查FLASH是否被正文引用到,并修改正文中的图片URL
            if(flashs!=null && !flashs.isEmpty()) {
            	for(Iterator iterator=flashs.iterator(); iterator.hasNext();) {
            		Attachment flash = (Attachment)iterator.next();
	            	if(body.indexOf(flash.getUrlInline())==-1) {
	            		iterator.remove();
	                }
	            	else { //修改正文中的图片URL
	            		body = StringUtils.replace(body, flash.getUrlInline(), "cid:" + flash.hashCode() + "@mail");
	            	}
            	}
            }
            
            boolean hasAttachment = (attachments!=null && !attachments.isEmpty()); //是否有附件
            boolean hasImage = (images!=null && !images.isEmpty()); //是否有图片
            boolean hasFlash = (flashs!=null && !flashs.isEmpty()); //是否有flash
            
            //输出邮件头
            writeMailProperty(writer, "Return-Path", mail.getMailFrom().substring(mail.getMailFrom().indexOf(" <") + 1), false); //退回地址
    		writeMailProperty(writer, "To", mail.getMailTo(), true); //收件人
    		writeMailProperty(writer, "Cc", mail.getMailCc(), true); //抄送
    		writeMailProperty(writer, "Bcc", mail.getMailBcc(), true); //密送
    		writeMailProperty(writer, "Subject", mail.getSubject(), true); //标题
    		writeMailProperty(writer, "From", mail.getMailFrom(), true); //发件人
    		writeMailProperty(writer, "X-Priority", mail.getPriority(), false); //优先级
    		writeMailProperty(writer, "X-Mailer", "yuanlue webmail system", false); //发送客户端
    		writeMailProperty(writer, "MIME-Version", "1.0", false); //MIME版本

    		//邮件头:邮件类型
    		String contentType;
    		if(hasAttachment) { //有附件
    			contentType = "multipart/mixed;";
    		}
    		else if(hasImage || hasFlash) { //没有附件,有图片或者flash 
    			contentType = "multipart/related;\r\n\ttype=\"multipart/alternative\";";
    		}
    		else { //只有正文,没有附件,没有图片和flash
    			contentType = "multipart/alternative;";
    		}
    		String boundary = "====="  + UUIDLongGenerator.generateId() + "=====";
    		contentType += "\r\n\tboundary=\"" + boundary + "\"\r\n";
    		writeMailProperty(writer, "Content-Type", contentType, false);
    		
    		try {
    		    writer.write("This is a multi-part message in MIME format.\r\n\r\n");
            } 
    		catch (IOException e) {
                
            }
            
            //输出邮件主体头部
    		Stack boundaryStack = new Stack();
    		if(hasAttachment && (hasImage || hasFlash)) { //有附件,且有图片或者FLASH
        	 	writer.write("--" + boundary + "\r\n");
                boundaryStack.push(boundary);
                boundary = "====="  + UUIDLongGenerator.generateId() + "=====";
                writeMailProperty(writer, "Content-Type", "multipart/related;\r\n\ttype=\"multipart/alternative\";\r\n\tboundary=\"" + boundary + "\"\r\n", false);
            }
            if(hasAttachment || hasImage || hasFlash) { //有图片、flash或者附件
            	writer.write("--" + boundary + "\r\n");
            	boundaryStack.push(boundary);
                boundary = "====="  + UUIDLongGenerator.generateId() + "=====";
            	writeMailProperty(writer, "Content-Type", "multipart/alternative;\r\n\tboundary=\"" + boundary + "\"\r\n\r\n", false);
            }
            
    		//输出文本正文
            writer.write("--" + boundary + "\r\n");
            writeMailBody(writer, StringUtils.filterHtmlElement(body, false), "text/plain");
            //输出HTML正文
            writer.write("--" + boundary + "\r\n"); //HTML正文开始
            writeMailBody(writer, body, "text/html");
            writer.write("--" + boundary + "--\r\n"); //正文输出完成
            
            if(hasAttachment || hasImage || hasFlash) { //有图片、flash或者附件
            	writer.write("\r\n"); //添加一个空行
            }
            
            //输出图片和FLASH
            if(hasImage || hasFlash) {
                boundary = (String)boundaryStack.pop();
                if(hasImage) { //输出图片
	            	for(Iterator iterator = images.iterator(); iterator.hasNext();) {
	            		Image image = (Image)iterator.next();
	            		writer.write("--" + boundary + "\r\n");
	            		writeAttachment(writer, image, image.hashCode() + "@mail");
	            	}
                }
                if(hasFlash) { //输出FLASH
	            	for(Iterator iterator = flashs.iterator(); iterator.hasNext();) {
	            		Attachment flash = (Attachment)iterator.next();
	            		writer.write("--" + boundary + "\r\n");
	            		writeAttachment(writer, flash, flash.hashCode() + "@mail");
	            	}
                }
            	writer.write("--" + boundary + "--\r\n"); //图片和FLASH输出完成
            }
            
            //输出附件
            if(hasAttachment) { //有附件
	            boundary = (String)boundaryStack.pop();
	            for(Iterator iterator=attachments.iterator(); iterator.hasNext();) {
                    Attachment attachment = (Attachment)iterator.next();
                    writer.write("--" + boundary + "\r\n");
                    writeAttachment(writer, attachment, null);
                }
	            writer.write("--" + boundary + "--\r\n"); //附件结束
            }
        }
        catch(Exception e) {
        	Logger.exception(e);
            throw new ServiceException(e.getMessage());
        }
        finally {
            try {
                writer.close();
            }
            catch(Exception e) {
                
            }
        }
    }
    
    /**
     * 编码并输出邮件属性
     * @param out
     * @param propertyName
     * @param propertyValue
     * @param encode
     * @return
     * @throws ServiceException
     */
	private void writeMailProperty(Writer writer, String propertyName, String propertyValue, boolean encode) throws ServiceException {
		try {
		    if(propertyValue!=null && !propertyValue.equals("")) {
		        writer.write(propertyName + ": " + (encode ? Encoder.getInstance().encodePropertyValue(propertyValue, "UTF-8") : propertyValue) + "\r\n");
		    }
		} catch (Exception e) {
		    Logger.exception(e);
			throw new ServiceException();
		}
	}
	
	/**
	 * 输出正文
	 * @param out
	 * @param mailBody
	 * @throws ServiceException
	 */
	private void writeMailBody(Writer writer, String mailBody, String contentType) throws ServiceException {
	    try {
	        //输出正文头
	        writeMailProperty(writer, "Content-Type", contentType + ";\r\n\tcharset=UTF-8", false);
	        writeMailProperty(writer, "Content-Transfer-Encoding", "base64\r\n", false);
	        //输出正文内容
	        Base64Encoder base64Encoder = new Base64Encoder();
		    char[] base64Chars = new char[78];
		    base64Chars[76] = '\r';
		    base64Chars[77] = '\n';
		    byte[] body = mailBody.getBytes("UTF-8");
	        int len = body.length;
	        for(int i=0; i<len; i+=57) {
	            if(i+57<len) {
	            	base64Encoder.encode(body, i, i + 57, base64Chars);
	                writer.write(base64Chars);
	            }
	            else {
	                int base64Len = base64Encoder.encode(body, i, len, base64Chars);
	                base64Chars[base64Len] = '\r';
	                base64Chars[base64Len+1] = '\n';
	                writer.write(base64Chars, 0, base64Len + 2);
	            }
	        }
	        writer.write("\r\n");
	    }
		catch(Exception e) {
		    Logger.exception(e);
		    throw new ServiceException();
		}
	}
	
	/**
	 * 输出附件
	 * @param out
	 * @param attachment
	 * @throws ServiceException
	 */
	private void writeAttachment(Writer writer, Attachment attachment, String contentId) throws ServiceException {
		Encoder encoder = Encoder.getInstance();
		Base64Encoder base64Encoder = new Base64Encoder();
        //输出附件头
        String name = encoder.encodePropertyValue(attachment.getName(), "UTF-8");
        writeMailProperty(writer, "Content-Type", Mime.getMimeType(attachment.getName()) + ";\r\n\tname=\"" + name + "\"", false);
        writeMailProperty(writer, "Content-Transfer-Encoding", "base64", false);
        if(contentId!=null) {
        	writeMailProperty(writer, "Content-ID", "<" + contentId + ">\r\n", false);
        }
        else {
        	writeMailProperty(writer, "Content-Disposition", "attachment;\r\n\tfilename=\"" + name + "\"\r\n", false);
        }
        FileInputStream in = null;
        try { //输出附件内容
            in = new FileInputStream(attachment.getFilePath());
            char[] base64Chars = new char[78];
            base64Chars[76] = '\r';
            base64Chars[77] = '\n';
            byte[] buffer = new byte[57 * 80];
            int readLen;
            while((readLen = in.read(buffer))!=-1) {
                for(int i=0; i<readLen; i+=57) {
                    if(i+57<readLen) {
                    	base64Encoder.encode(buffer, i, i + 57, base64Chars);
                        writer.write(base64Chars);
                    }
                    else {
                        int base64Len = base64Encoder.encode(buffer, i, readLen, base64Chars);
                        base64Chars[base64Len] = '\r';
                        base64Chars[base64Len+1] = '\n';
                        writer.write(base64Chars, 0, base64Len + 2);
                    }
                }
            }
            writer.write("\r\n");
        }
        catch(Exception e) {
            Logger.exception(e);
            throw new ServiceException();
        }
        finally {
        	try {
        		in.close();
        	}
        	catch(Exception e) {
        		
        	}
        }
	}
}
