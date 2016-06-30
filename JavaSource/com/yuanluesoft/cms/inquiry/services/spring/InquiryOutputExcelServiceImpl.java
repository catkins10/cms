package com.yuanluesoft.cms.inquiry.services.spring;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import com.yuanluesoft.cms.inquiry.model.InquiryResult;
import com.yuanluesoft.cms.inquiry.pojo.Inquiry;
import com.yuanluesoft.cms.inquiry.pojo.InquiryOption;
import com.yuanluesoft.cms.inquiry.pojo.InquirySubject;
import com.yuanluesoft.cms.inquiry.pojo.InquiryVote;
import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;

public class InquiryOutputExcelServiceImpl extends BusinessServiceImpl{
	private InquiryService inquiryService;

	public void createExcel(long recordId, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成方法存根
		
        HSSFWorkbook projectExcel=new HSSFWorkbook();//创建工作薄对象
        HSSFSheet inquirySubjectSheet=projectExcel.createSheet("投票结果");
		
		HSSFCellStyle style=projectExcel.createCellStyle();//设置单元格格式对象
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setVerticalAlignment((short)1);    //单元格垂直对齐方式   0   居上   1   居中   2   居下   3   正当   
        style.setWrapText(true); //设置自动换行。
        
        InquirySubject inquirySubject = null;
        
        try {
        	inquirySubject=(InquirySubject) load(InquirySubject.class, recordId);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
        
		inquirySubjectSheet=createExcel(inquirySubject, inquirySubjectSheet, style);
        
        
//      设置相关请求头
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition","attachment;filename=InquiryResult.xls");
		
		
		try {
			OutputStream output=response.getOutputStream();
			projectExcel.write(output);
			output.close();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		

	}

	private HSSFSheet createExcel(InquirySubject inquirySubject,HSSFSheet hssfSheet,HSSFCellStyle style)
	{
		
		if(inquirySubject==null)
			return hssfSheet;
		
		String subject = inquirySubject.getSubject();//问卷主题
		//调查描述
		String[] descriptions = new String[inquirySubject.getInquiries().size()];
		int[] optionNumForEachInquiry = new int[inquirySubject.getInquiries().size()];//每个调查有多少个选项，用于合并单元格。
		Set inquiries = inquirySubject.getInquiries();
		//选项
		String hql = "from InquiryOption InquiryOption,Inquiry Inquiry where InquiryOption.inquiryId = Inquiry.id and Inquiry.subjectId = "+inquirySubject.getId();
		List inquiryOptions= getDatabaseService().findRecordsByHql(hql);
		String[] options = new String[inquiryOptions.size()];
		int i = 0;//调查数量
		int j = 0;//选项数量
		for (Iterator ite = inquiries.iterator(); ite.hasNext();) {
			Inquiry inquiry = (Inquiry) ite.next();
			descriptions[i] = inquiry.getDescriptionText();
			optionNumForEachInquiry[i] = inquiry.getOptions().size();
			i++;
			for(Iterator ite2 = inquiry.getOptions().iterator(); ite2.hasNext();){
				InquiryOption inquiryOption= (InquiryOption) ite2.next();
				 options[j] = inquiryOption.getId()+","+inquiryOption.getInquiryOption();
				 j++;
			}
		}
		hssfSheet.setColumnWidth(0,1700);//设置列宽
		hssfSheet.setDefaultRowHeight((short)600);//设置默认行高
		//创建主题行，需合并单元格：合并单元格数量等于选项数量
		HSSFRow subjectRow=hssfSheet.createRow((short)0);//创建行
		HSSFCell subjectTitle=subjectRow.createCell(0);//创建单元格
		subjectTitle.setCellValue(subject);
		hssfSheet.addMergedRegion(new CellRangeAddress(0,0,0,options.length));//合并单元格
		subjectTitle.setCellStyle(style);
		
		//创建调查描述行，需合并单元格：合并单元格数量等于对应选项的数量
		HSSFRow descriptionRow=hssfSheet.createRow((short)1);//创建调查描述行
		HSSFCell numCell = descriptionRow.createCell(0);
		numCell.setCellValue("序号");
		for(int cellNum=0; cellNum<options.length; cellNum++){
			HSSFCell cell = descriptionRow.createCell(cellNum+1);
			cell.setCellStyle(style);
		}
		for(int k=0; k<descriptions.length; k++){
			if(k==0){
				descriptionRow.getCell(k+1).setCellValue(descriptions[k]);
			}else{
				int startCell =0;
				for(int c =0 ; c<k; c++){
					startCell = startCell+optionNumForEachInquiry[c];
				}
				descriptionRow.getCell(startCell+1).setCellValue(descriptions[k]);
			}
		}
		for(int k=0; k<descriptions.length; k++){
			if(k==0){
				hssfSheet.addMergedRegion(new CellRangeAddress(1,1,1,optionNumForEachInquiry[k]));//合并单元格
			}else{
				int startCell =0;
				int endCell = 0;
				for(int c =0 ; c<k; c++){
					startCell = startCell+optionNumForEachInquiry[c];
				}
				endCell = startCell+optionNumForEachInquiry[k];
				hssfSheet.addMergedRegion(new CellRangeAddress(1,1,startCell+1,endCell));//合并单元格
			}
			descriptionRow.getCell(k+1).setCellStyle(style);
		}
		
		//创建选项行
		HSSFRow optionRow=hssfSheet.createRow((short)2);//创建选项行
		optionRow.setHeight((short)1500);
		HSSFCell optionCell = optionRow.createCell(0);
		optionCell.setCellStyle(style);
		hssfSheet.addMergedRegion(new CellRangeAddress(1,2,0,0));
		numCell.setCellStyle(style);
		for(int h=0; h<options.length; h++){
			HSSFCell optionTitle=optionRow.createCell(h+1);//创建单元格
			optionTitle.setCellValue((options[h].split(","))[1]);
			optionTitle.setCellStyle(style);
		}
		
		hql = "select distinct InquiryVote.voterId" +
		 " from InquiryVote InquiryVote, InquiryOption InquiryOption, Inquiry Inquiry" + 
		 " where InquiryVote.optionId=InquiryOption.id" + 
		 " and InquiryOption.inquiryId=Inquiry.id" +
		 " and Inquiry.subjectId=" + inquirySubject.getId();
		List voterIds = getDatabaseService().findRecordsByHql(hql);//票数
		List votes = new ArrayList();
		if(voterIds!=null){
			for(int size = 0; size<voterIds.size(); size++){
				String[] vote = new String[options.length];
				for(int optionSize = 0; optionSize < options.length; optionSize++){
					hql = "select InquiryVote" +
					" from InquiryVote InquiryVote, InquiryOption InquiryOption, Inquiry Inquiry" + 
					" where InquiryVote.optionId=InquiryOption.id" + 
					" and InquiryOption.inquiryId=Inquiry.id" +
					" and InquiryOption.id=" +(options[optionSize].split(","))[0]+
					" and InquiryVote.voterId=" +voterIds.get(size)+
					" and Inquiry.subjectId=" + inquirySubject.getId();//每个用户的投票记录
					InquiryVote inquiryVote = (InquiryVote)getDatabaseService().findRecordByHql(hql);
					if(inquiryVote==null){
						vote[optionSize]="";
					}else if(inquiryVote.getSupplement()!=null && !inquiryVote.getSupplement().equals("")){
						//有补充说明则显示
						vote[optionSize]=inquiryVote.getSupplement();
					}else{
						vote[optionSize]="√";
					}
				}
				votes.add(vote);
			}
			try {
				inquiryService.retrieveInquiryResults(inquirySubject);
			} catch (ServiceException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			//创建投票记录行
			for(int voteSize = 0 ; voteSize < votes.size(); voteSize++){
				HSSFRow row=hssfSheet.createRow((short)3+voteSize);//创建行
				String[] vote = (String[])votes.get(voteSize);
				HSSFCell votecell = row.createCell(0);
				votecell.setCellValue(voteSize+1);
				votecell.setCellStyle(style);
				for(int cellNum = 0; cellNum < vote.length ; cellNum++){
					HSSFCell cellContent=row.createCell(cellNum+1);//创建内容单元格
					cellContent.setCellValue(vote[cellNum]);
					cellContent.setCellStyle(style);
				}
			}
		}
		HSSFRow row=hssfSheet.createRow((short)3+votes.size());//创建票数合计行
		HSSFRow row2=hssfSheet.createRow((short)4+votes.size());//创建百分比行
		HSSFCell rowCell = row.createCell(0);
		rowCell.setCellValue("合计");
		rowCell.setCellStyle(style);
		HSSFCell row2Cell = row2.createCell(0);
		row2Cell.setCellValue("占比重");
		row2Cell.setCellStyle(style);
		int cellNo = 1;
		for (Iterator ite = inquiries.iterator(); ite.hasNext();) {//票数合计
			Inquiry inquiry = (Inquiry) ite.next();
			Set optiones = inquiry.getOptions();
			for(Iterator option = optiones.iterator(); option.hasNext();){
				InquiryOption inquiryOption = (InquiryOption)option.next();
				for(Iterator ite2 = inquiry.getResults().iterator(); ite2.hasNext();){
					InquiryResult inquiryResult= (InquiryResult) ite2.next();
					if(inquiryResult.getOptionId()==inquiryOption.getId()){
						HSSFCell cell = row.createCell(cellNo);
						cell.setCellValue(inquiryResult.getVoteNumber());
						cell.setCellStyle(style);
						HSSFCell cell2 = row2.createCell(cellNo);
						;
						cell2.setCellValue(new BigDecimal(inquiryResult.getVotePercent()*1000).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue()/10+"%");
						cell2.setCellStyle(style);
					}
				}
				cellNo++;
			}
		}
		return hssfSheet;
	}

	public InquiryService getInquiryService() {
		return inquiryService;
	}

	public void setInquiryService(InquiryService inquiryService) {
		this.inquiryService = inquiryService;
	}
	
	
	
}

