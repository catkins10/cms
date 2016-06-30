package com.yuanluesoft.credit.enterprisecredit.batchoperate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.credit.enterprisecredit.agricultural.pojo.Agricultural;
import com.yuanluesoft.credit.enterprisecredit.commoncredit.pojo.CommonCredit;
import com.yuanluesoft.credit.enterprisecredit.court.pojo.Court;
import com.yuanluesoft.credit.enterprisecredit.forestry.pojo.Forestry;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.BigStandard;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.Handle;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.LinkPerson;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.Permit;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.Punish;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.SmallStandard;
import com.yuanluesoft.credit.enterprisecredit.landresources.pojo.LandResources;
import com.yuanluesoft.credit.enterprisecredit.license.pojo.License;
import com.yuanluesoft.credit.enterprisecredit.localtax.pojo.LocalTax;
import com.yuanluesoft.credit.enterprisecredit.market.pojo.MarketEnterprise;
import com.yuanluesoft.credit.enterprisecredit.market.pojo.MarketPerson;
import com.yuanluesoft.credit.enterprisecredit.municipal.pojo.Municipal;
import com.yuanluesoft.credit.enterprisecredit.owetax.pojo.OweTax;
import com.yuanluesoft.credit.enterprisecredit.quartertax.pojo.QuarterTax;
import com.yuanluesoft.credit.enterprisecredit.trafficcredit.pojo.CarCredit;
import com.yuanluesoft.credit.enterprisecredit.trafficcredit.pojo.TrafficCredit;
import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

public class BatchOperate extends ApplicationViewAction{
	
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		String pojoName = request.getParameter("pojoName");
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		delete(pojoName, ids);
		return mapping.getInputForward();
	}
	
	public void delete(String pojoName,String[] ids) throws SystemUnregistException{
		BusinessService businessService = (BusinessService)getService("businessService");
		if(pojoName.equals("Agricultural")){//龙海市农业局关于企业、个人诚信信息表
			try{
				for(int i = 0; i<ids.length; i++){
					Agricultural agricultural  = (Agricultural)businessService.load(Agricultural.class, Long.parseLong(ids[i]));
					businessService.delete(agricultural);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}else if(pojoName.equals("CommonCredit")){//通用信用记录
			try{
				for(int i = 0; i<ids.length; i++){
					CommonCredit commonCredit  = (CommonCredit)businessService.load(CommonCredit.class, Long.parseLong(ids[i]));
					businessService.delete(commonCredit);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}else if(pojoName.equals("Forestry")){//龙海市林业局林政案件数据
			try{
				for(int i = 0; i<ids.length; i++){
					Forestry forestry  = (Forestry)businessService.load(Forestry.class, Long.parseLong(ids[i]));
					businessService.delete(forestry);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}else if(pojoName.equals("BigStandard")){//龙海市工贸行业标准化网上申报通过名单
			try{
				for(int i = 0; i<ids.length; i++){
					BigStandard bigStandard  = (BigStandard)businessService.load(BigStandard.class, Long.parseLong(ids[i]));
					businessService.delete(bigStandard);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("Handle")){//龙海市安监局事故调查处理情况统计
			try{
				for(int i = 0; i<ids.length; i++){
					Handle handle  = (Handle)businessService.load(Handle.class, Long.parseLong(ids[i]));
					businessService.delete(handle);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("LinkPerson")){//公开事项联系人
			try{
				for(int i = 0; i<ids.length; i++){
					LinkPerson linkPerson  = (LinkPerson)businessService.load(LinkPerson.class, Long.parseLong(ids[i]));
					businessService.delete(linkPerson);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("Permit")){//龙海市安监局行政许可情况统计
			try{
				for(int i = 0; i<ids.length; i++){
					Permit permit  = (Permit)businessService.load(Permit.class, Long.parseLong(ids[i]));
					businessService.delete(permit);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("Punish")){//龙海市安监局行政处罚情况统计
			try{
				for(int i = 0; i<ids.length; i++){
					Punish punish  = (Punish)businessService.load(Punish.class, Long.parseLong(ids[i]));
					businessService.delete(punish);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("SmallStandard")){//龙海市小、微型企业标准化网上申报通过名单
			try{
				for(int i = 0; i<ids.length; i++){
					SmallStandard smallStandard  = (SmallStandard)businessService.load(SmallStandard.class, Long.parseLong(ids[i]));
					businessService.delete(smallStandard);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("LandResources")){//龙海市国土资源局违法案件结果公开
			try{
				for(int i = 0; i<ids.length; i++){
					LandResources landResources  = (LandResources)businessService.load(LandResources.class, Long.parseLong(ids[i]));
					businessService.delete(landResources);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("License")){//排污许可证核发情况
			try{
				for(int i = 0; i<ids.length; i++){
					License license  = (License)businessService.load(License.class, Long.parseLong(ids[i]));
					businessService.delete(license);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("LocalTax")){//年度纳税信用评价Ａ级企业(地税)
			try{
				for(int i = 0; i<ids.length; i++){
					LocalTax localTax  = (LocalTax)businessService.load(LocalTax.class, Long.parseLong(ids[i]));
					businessService.delete(localTax);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("Municipal")){//龙海市行政处罚案件统计报表
			try{
				for(int i = 0; i<ids.length; i++){
					Municipal municipal  = (Municipal)businessService.load(Municipal.class, Long.parseLong(ids[i]));
					businessService.delete(municipal);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("OweTax")){//龙海市国家税务局辖区企业欠税情况
			try{
				for(int i = 0; i<ids.length; i++){
					OweTax oweTax  = (OweTax)businessService.load(OweTax.class, Long.parseLong(ids[i]));
					businessService.delete(oweTax);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("QuarterTax")){//龙海市国家税务局辖区企业欠税情况
			try{
				for(int i = 0; i<ids.length; i++){
					QuarterTax quarterTax  = (QuarterTax)businessService.load(QuarterTax.class, Long.parseLong(ids[i]));
					businessService.delete(quarterTax);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("TrafficCredit")){//龙海市国家税务局辖区企业欠税情况
			try{
				for(int i = 0; i<ids.length; i++){
					TrafficCredit trafficCredit  = (TrafficCredit)businessService.load(TrafficCredit.class, Long.parseLong(ids[i]));
					businessService.delete(trafficCredit);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("MarketEnterprise")){//市场监督管理局企业信用
			try{
				for(int i = 0; i<ids.length; i++){
					MarketEnterprise marketEnterprise  = (MarketEnterprise)businessService.load(MarketEnterprise.class, Long.parseLong(ids[i]));
					businessService.delete(marketEnterprise);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("MarketPerson")){//市场监督管理局个体信用
			try{
				for(int i = 0; i<ids.length; i++){
					MarketPerson marketPerson  = (MarketPerson)businessService.load(MarketPerson.class, Long.parseLong(ids[i]));
					businessService.delete(marketPerson);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("Court")){//法院失信被执行人名单
			try{
				for(int i = 0; i<ids.length; i++){
					Court court  = (Court)businessService.load(Court.class, Long.parseLong(ids[i]));
					businessService.delete(court);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
		else if(pojoName.equals("CarCredit")){//法院失信被执行人名单
			try{
				for(int i = 0; i<ids.length; i++){
					CarCredit carCredit  = (CarCredit)businessService.load(CarCredit.class, Long.parseLong(ids[i]));
					businessService.delete(carCredit);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}
			
	}
	
}
