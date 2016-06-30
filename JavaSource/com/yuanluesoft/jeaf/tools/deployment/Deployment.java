package com.yuanluesoft.jeaf.tools.deployment;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.database.dialect.DatabaseDialect;
import com.yuanluesoft.jeaf.database.dialect.mysql.MysqlDatabaseDialect;
import com.yuanluesoft.jeaf.database.dialect.oracle.OracleDatabaseDialect;
import com.yuanluesoft.jeaf.database.dialect.postgres.PostgresDatabaseDialect;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.JsUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.ProcessUtils;
import com.yuanluesoft.jeaf.util.callback.FileSearchCallback;

/**
 * 
 * @author linchuan
 *
 */
public class Deployment {
	private List javaClassesInJsp = new ArrayList(); //jsp引用到的类型,不做类加密
	private String CLASS_PREFIX = "com.yuanluesoft";
	private char[] CLASS_POSTFIX = {'\"', ')', ' ', '*', ';'};
	private String CLASS_PATH = "com/yuanluesoft"; //类路径
	
	private String[] UNDEPLOY_FOLDER_NAMES = {"pages", "templates", "videos", "temp", "deployment", "html", "/jeaf/common/js/common", "/jeaf/htmleditor/js/internals", "/jeaf/htmleditor/js/commands"}; //不需要部署的目录
	private String[] JS_KEEP_FOLDER_NAMES = {"htmleditor", "capture"}; //不需要脚本转换的脚步目录
	private String APPLICATION_PATH = "c:/workspace/cms"; //源目录
	private String DEPLOY_PATH = "c:/Users/chuan/Desktop/"; //目标目录
	
	public static void main(String[] args) {
		try {
			if(false) { //数据库信息加密
				System.out.println("jdbc driver: " + Encoder.getInstance().desBase64Encode("org.postgresql.Driver", "yu050718", null));
				System.out.println("jdbc url: " + Encoder.getInstance().desBase64Encode("jdbc:postgresql://localhost:5432/fjmsacms", "yu050718", null));
				System.out.println("jdbc user name: " + Encoder.getInstance().desBase64Encode("postgres", "yu050718", null));
				System.out.println("jdbc password: " + Encoder.getInstance().desBase64Encode("lwcms@2012", "yu050718", null));
				System.out.println("PostgresDumpCommandPath: " + Encoder.getInstance().desBase64Encode("E:/PostgreSQL/8.4/bin/pg_dump.exe", "yu050718", null));
				return;
			}
			Deployment deployment = new Deployment();
			if(args.length>0 && args[0].equals("deplymentClasses")) { //部署类文件
				deployment.deploymentClasses();
			}
			else {
				//站点名称,oa/cms/fzjsj/fzztb/npztb/npztb_test/sxztb/zzfet/fzjt/smwjm/xcboa
				//fjsc/scjc/jyszfw/ypzf/zhenghe/guangze/jianou/npqi/fzqi/fzsz/zhangzhou/zhangzhou_new/npzdxm/fzlx/songxi
				//wuping/zhaoan/yunxiao/pinghe/changtai/longhai/huaan/xiangcheng
				//fjmsa_intranet/fjmsa_internet_web1/fjmsa_internet_web2
				//zzcc/fzdcd/npwllm/chdeval/chdeval_ct/fjfao_intranet/fjfao_internet(2010-1-2)
				//zztelecom/tanjinzhi/changshan/fjfdi/zzjc/fjfzb/fzccg
				//fjgtzy_intranet/fjgtzy_internet/fjgtzy_intranet/fjgtzy_internet/fjgtzy_touch
				//zzgwjh/wjmrcw/npkx/zhangping/zpxww/fjlqb/nanping/npjc
				deployment.deployment("wjmrcw", "2015-6-1 00:00");
			}
			System.out.println("Deployment success.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化站点参数
	 *
	 */
	private DeploymentConfigure getDeploymentConfigure(String siteName) {
		DeploymentConfigure deploymentConfigure = new DeploymentConfigure();
		String siteApplicationNames = "cms/photocollect,cms/monitor,cms/smssend,cms/smsreceive,cms/evaluation,cms/ad,cms/advice,cms/comment,cms/complaint,cms/correction,cms/infopublic,cms/inquiry,cms/interview,cms/leadermail,cms/messageboard,cms/messenger,cms/onlineservice,cms/preapproval,cms/publicservice,cms/rsssubscription,cms/scene,cms/sitemessage,cms/supervision";
		if("balance".equals(siteName)) {
			deploymentConfigure.setDistribution(true);
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setDistributeURL("http://{LOCALHOSTIP}");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("/balance");
			deploymentConfigure.setDeploymentPath("d:/cms_balance");
			deploymentConfigure.setUndeployFolders("".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/cms_balance");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("c:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("fullsystem".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/yuanlue");
			deploymentConfigure.setUndeployFolders("".split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://localhost/yuanluecms?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.0/data");
		}
		else if("fjsc".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("f:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/sccms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("sccms@2012");
			deploymentConfigure.setBackupTo("f:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("jyszfw".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/jycms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/jycms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("jycms@2014");
			deploymentConfigure.setBackupTo("d:/backup_jycms");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("jianou".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/cms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("jocms@2014");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/9.0/bin/pg_dump.exe");
		}
		else if("wuping".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/wpcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5433/wpcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("wpcms@2012");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/9.0/bin/pg_dump.exe");
		}
		else if("yunxiao".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/yxcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/yxcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("yx@2011");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("E:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("zhangping".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/zpcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("zpcms@2014");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("zpxww".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/zpxww");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/zpxww");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("zpxww@2014");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/9.0/bin/pg_dump.exe");
		}
		else if("fjlqb".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("c:/fjlqb");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://192.168.1.116:5432/fjlqb");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("lixiaoming");
			deploymentConfigure.setBackupTo("c:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("c:/PostgreSQL/9.0/bin/pg_dump.exe");
		}
		else if("nanping".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/npcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:oracle:thin:@172.31.140.3:1521:NPDB");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.Oracle9Dialect");
			deploymentConfigure.setDbUser("cstccms");
			deploymentConfigure.setDbPassword("ctusers");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setOracleBackupTnsName("npcms");
			deploymentConfigure.setOracleExportCommandPath("d:/oracle/product/11.2.0/dbhome_1/BIN/exp.exe");
		}
		else if("pinghe".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/sites/phcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/phcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("zzcms2011");
			deploymentConfigure.setDbMonitor("192.168.3.236:514");
			deploymentConfigure.setBackupTo("e:/backup_pinghe");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("changtai".equals(siteName)) {
			deploymentConfigure.setDistribution(true);
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/ctcms");
			deploymentConfigure.setAttachmentFilePath("e:/ctcms");
			deploymentConfigure.setAttachmentServerUrl("/site");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/ctcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("ct@2011");
			deploymentConfigure.setBackupTo("f:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("longhai".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/cms");
			deploymentConfigure.setAttachmentFilePath("//172.16.2.69/d$/cms");
			deploymentConfigure.setAttachmentServerUrl("http://img.longhai.gov.cn"); //server.xml 设置为 d:\cms\cms
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://172.16.2.68:5432/lhcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("lhcms@2012");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("changting".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/ctcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/ctcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("ct@2011");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/9.0/bin/pg_dump.exe");
		}
		else if("songxi".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/sxcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/sxcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("sxcms@2011");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("pucheng".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/pccms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/pccms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("pccms@2012");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/9.0/bin/pg_dump.exe");
		}
		else if("longwen".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/lwcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/lwcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("lwcms@2012");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/9.0/bin/pg_dump.exe");
		}
		else if("zhaoan".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/zacms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/zacms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("za@2011");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("fjfao_intranet".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://10.5.159.15/cms?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("wsb#918!");
			deploymentConfigure.setBackupTo("f:/backup");
			deploymentConfigure.setToBackupFolders("db|e:/MySQL Server 5.1/data");
		}
		else if("fjfao_internet".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://10.5.159.15/cms?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("wsb#918!");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.1/data");
		}
		else if("ypzf".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("/cms");
			deploymentConfigure.setDeploymentPath("d:/site/cms");
			deploymentConfigure.setUndeployFolders("job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/ypcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("ypcms@2012");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("fzjt_internet".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/fzjtcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,messenger,cms/scene,cms/interview,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:oracle:thin:@172.18.46.164:1521:fzjtcms");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.Oracle9Dialect");
			deploymentConfigure.setDbUser("cms");
			deploymentConfigure.setDbPassword("fzjt123456");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setOracleBackupTnsName("fzjtcms");
			deploymentConfigure.setOracleExportCommandPath("C:/oracle/product/10.2.0/db_1/BIN/exp.exe");
		}
		else if("fzjt_intranet".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("10000");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/fzjtcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,messenger,cms/scene,cms/interview,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:oracle:thin:@172.18.46.164:1521:fzjtcms");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.Oracle9Dialect");
			deploymentConfigure.setDbUser("cms");
			deploymentConfigure.setDbPassword("fzjt123456");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setOracleBackupTnsName("fzjtcms");
			deploymentConfigure.setOracleExportCommandPath("C:/oracle/product/10.2.0/db_1/BIN/exp.exe");
		}
		else if("fzztb".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/fzztb");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,traffic,cms/scene,cms/interview,messenger,cms/infopublic,cms/leadermail,cms/complaint,cms/supervision,cms/preapproval,cms/advice,bbs,cms/onlineservice".split(","));
			deploymentConfigure.setDbURL("jdbc:oracle:thin:@192.168.10.5:1521:fzztb");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.Oracle9Dialect");
			deploymentConfigure.setDbUser("fzztb");
			deploymentConfigure.setDbPassword("fzjsjztb");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setOracleBackupTnsName("fzjsjztb");
			deploymentConfigure.setOracleExportCommandPath("d:/oracle/ora92/bin/exp.exe");
		}
		else if("npztb".equals(siteName)) { //x2132npc
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8081");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/npztb");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,traffic,messenger,cms/preapproval,bbs,cms/onlineservice".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/bidding");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("npztb@2012");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("E:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("npztb_test".equals(siteName)) { //x2132npc
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8082");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/npztb2013");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,traffic,messenger,cms/preapproval,bbs,cms/onlineservice".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/npztbtest");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("npztb@2012");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("E:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("sxztb".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8081");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/sxztb");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,traffic,messenger,cms/preapproval,bbs,cms/onlineservice".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/sxztb");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("sxztb@2014");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("ztb".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/ztb");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,traffic,messenger,cms/preapproval,bbs,cms/onlineservice".split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://localhost/cms?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.0/data");
		}
		else if("fzjsj".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/fzjsjcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment,cms/onlineservice".split(","));
			deploymentConfigure.setDbURL("jdbc:oracle:thin:@192.168.4.4:1521:fjtgisdb");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.Oracle9Dialect");
			deploymentConfigure.setDbUser("cms");
			deploymentConfigure.setDbPassword("fzjsjcms");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setOracleBackupTnsName("fzjsjcms");
			deploymentConfigure.setOracleExportCommandPath("d:/oracle/ora92/bin/exp.exe");
		}
		else if("zhenghe".equals(siteName)) {
			deploymentConfigure.setURL("http://www.zhenghe.gov.cn:8088");
			deploymentConfigure.setSafeURL("http://www.zhenghe.gov.cn:8088");
			deploymentConfigure.setLocalPort("8088");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/zhcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,unicipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://localhost/cms?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.0/data");
		}
		else if("guangze".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/gzcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/gzcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("gzcms@2012");
			deploymentConfigure.setBackupTo("h:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("xcboa".equals(siteName)) {
			deploymentConfigure.setURL("http://10.0.8.7");
			deploymentConfigure.setSafeURL("http://10.0.8.7");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/xcboa");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,bbs,cms/onlineservice,cms/interview,fet,bidding,traffic,messenger,jeaf/usermanage/member,jeaf/payment,j2oa/businesstrip,j2oa/leave,j2oa/loan,j2oa/reimburse,j2oa/attendance".split(","));
			deploymentConfigure.setDbURL("jdbc:oracle:thin:@10.0.8.5:1521:xcboa");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.Oracle9Dialect");
			deploymentConfigure.setDbUser("xcboa");
			deploymentConfigure.setDbPassword("xcboa123456");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setOracleBackupTnsName("xcboa");
			deploymentConfigure.setOracleExportCommandPath("d:/oracle/ora92/bin/exp.exe");
		}
		else if("ptjsj".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/j2oa");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,bbs,cms/onlineservice,cms/interview,fet,bidding,traffic,messenger,jeaf/usermanage/member,jeaf/payment,j2oa/businesstrip,j2oa/leave,j2oa/loan,j2oa/reimburse,j2oa/attendance".split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://localhost/oa?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.0/data");
		}
		else if("faas".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/faascms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,cms/scene,cms/interview,jeaf/payment,cms/onlineservice".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/faascms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("fjktp@2014");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
			
			/*deploymentConfigure.setDbURL("jdbc:mysql://localhost/cms?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.0/data");*/
		}
		else if("fzqi".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/fzqicms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("fzqicms@2012");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("npqi".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/npqicms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://localhost/cms?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.0/data");
		}
		else if("ndqi".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/ndqicms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/ndqicms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("ndqicms@2012");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("fzsz".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/fzsz");
			deploymentConfigure.setUndeployFolders((siteApplicationNames + ",cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,fdi,customise,jeaf/application/builder,lss,railway,land,aic,chd,msa,im,logistics,enterprise,dpc,jeaf/weather,bbs,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment,jeaf/usermanage/member").split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://localhost/cms?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setToBackupFolders("db|e:/MySQL Server 5.0/data");
		}
		else if("zzqi".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://localhost/cms?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.0/data");
		}
		else if("zhangzhou".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("f:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,appraise,electric,telex,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,railway,chd,cms/evaluation,msa,im,logistics,enterprise,dpc/keyproject,dpc/fiveyearproject,municipal,cin,archives,webmail,j2oa/info,j2oa/infocontribute,j2oa/addresslist,j2oa/attendance,j2oa/book,j2oa/bulletin,j2oa/businesstrip,j2oa/calendar,j2oa/dispatch,j2oa/exchange,j2oa/leave,j2oa/loan,j2oa/meeting,j2oa/memorabilia,j2oa/personnel,j2oa/receival,j2oa/reimburse,j2oa/supervise,j2oa/todo,traffic,messenger,fet/project".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/zzcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("zzcms2011");
			deploymentConfigure.setDbMonitor("192.168.3.236:514");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("zhangzhou_new".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/zzcms");
			deploymentConfigure.setAttachmentFilePath("//192.168.3.62/d$/zzcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,appraise,electric,telex,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,railway,chd,cms/evaluation,msa,im,logistics,enterprise,dpc/keyproject,dpc/fiveyearproject,municipal,cin,archives,webmail,j2oa/info,j2oa/infocontribute,j2oa/addresslist,j2oa/attendance,j2oa/book,j2oa/bulletin,j2oa/businesstrip,j2oa/calendar,j2oa/dispatch,j2oa/exchange,j2oa/leave,j2oa/loan,j2oa/meeting,j2oa/memorabilia,j2oa/personnel,j2oa/receival,j2oa/reimburse,j2oa/supervise,j2oa/todo,traffic,messenger,fet/project".split(","));
			deploymentConfigure.setDbURL("jdbc:oracle:thin:@192.168.3.62:1521:zzcms");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.Oracle9Dialect");
			deploymentConfigure.setDbUser("zzcms");
			deploymentConfigure.setDbPassword("zzcms2014");
			deploymentConfigure.setDbMonitor("192.168.3.236:514");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setOracleBackupTnsName("zzcms");
			deploymentConfigure.setOracleExportCommandPath("c:/oracle/product/10.2.0/db_1/BIN/exp.exe");
		}
		else if("zzdw".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8082");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("f:/zzdw");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/zzdw");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("zzcms2011");
			deploymentConfigure.setDbMonitor("192.168.3.236:514");
			deploymentConfigure.setBackupTo("e:/backup_zzdw");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("zzjc".equals(siteName)) { //漳州监察
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/zzjc");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,cms/photocollect,cms/smssend,cms/smsreceive,cms/evaluation,cms/ad,cms/advice,cms/comment,cms/complaint,cms/correction,cms/infopublic,cms/inquiry,cms/interview,cms/leadermail,cms/messageboard,cms/messenger,cms/onlineservice,cms/preapproval,cms/publicservice,cms/rsssubscription,cms/scene,cms/sitemessage,cms/supervision,electric,telex,asc,fdi,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/zzjc");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("zzjc@2013");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("scjc".equals(siteName)) { //顺昌监察
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/scjc");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,cms/photocollect,cms/smssend,cms/smsreceive,cms/evaluation,cms/ad,cms/advice,cms/comment,cms/complaint,cms/correction,cms/infopublic,cms/inquiry,cms/interview,cms/leadermail,cms/messageboard,cms/messenger,cms/onlineservice,cms/preapproval,cms/publicservice,cms/rsssubscription,cms/scene,cms/sitemessage,cms/supervision,electric,telex,asc,fdi,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/scjc");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("sccms@2012");
			deploymentConfigure.setBackupTo("f:/backup_jc");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("npjc".equals(siteName)) { //南平监察
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/npjc");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,cms/photocollect,cms/smssend,cms/smsreceive,cms/evaluation,cms/ad,cms/advice,cms/comment,cms/complaint,cms/correction,cms/infopublic,cms/inquiry,cms/interview,cms/leadermail,cms/messageboard,cms/messenger,cms/onlineservice,cms/preapproval,cms/publicservice,cms/rsssubscription,cms/scene,cms/sitemessage,cms/supervision,electric,telex,asc,fdi,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/npjc");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("npjc@2015");
			deploymentConfigure.setBackupTo("d:/npjc_backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("npzdxm".equals(siteName)) { //ab847453
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/npzdxm");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,cms/interview,cms/complaint,cms/messageboard,cms/preapproval,cms/leadermail,cms/advice,cms/supervision,cms/onlineservice,cms/infopublic,bbs,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://localhost/cms?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.0/data");
		}
		else if("zzfetzdxm".equals(siteName)) { //ab847453
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8150");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/zzfetzdxm");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,cms/interview,cms/complaint,cms/messageboard,cms/preapproval,cms/leadermail,cms/advice,cms/supervision,cms/onlineservice,cms/infopublic,bbs,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://localhost/project?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("d:/zdxmbackup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.0/data");
		}
		else if("fzlx".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/fzlxoa");
			deploymentConfigure.setUndeployFolders((siteApplicationNames + ",cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,fdi,customise,jeaf/application/builder,lss,railway,land,aic,enterprise/exam,chd,msa,im,logistics,dpc,municipal,cin,bbs,fet,bidding,traffic,messenger,jeaf/weather,jeaf/usermanage/member,jeaf/payment,j2oa/loan,j2oa/reimburse").split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://localhost/oa?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.0/data");
		}
		else if("j2oa".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/j2oa");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,bbs,cms/onlineservice,cms/interview,fet,bidding,traffic,messenger,jeaf/usermanage/member,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:mysql://localhost/oa?useUnicode=true&characterEncoding=gbk");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.MySQLDialect");
			deploymentConfigure.setDbUser("root");
			deploymentConfigure.setDbPassword("root");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setToBackupFolders("db|d:/MySQL Server 5.0/data");
		}
		else if("npwllm".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/npwllm");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,dpc,enterprise,cms/interview,cms/complaint,cms/messageboard,cms/preapproval,cms/leadermail,cms/advice,cms/supervision,cms/onlineservice,cms/infopublic,bbs,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:oracle:thin:@192.168.2.2:1521:npwllm");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.Oracle9Dialect");
			deploymentConfigure.setDbUser("npwllm");
			deploymentConfigure.setDbPassword("npwllm114");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setOracleBackupTnsName("npwllm");
			deploymentConfigure.setOracleExportCommandPath("E:/oracle/product/10.2.0/db_1/BIN/exp.exe");
		}
		else if("fjmsa_intranet".equals(siteName)) { //海事局内网
			deploymentConfigure.setDistribution(true);
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setDistributeURL("http://{LOCALHOSTIP|198.20.8.}:8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/fjmsacms_intranet");
			deploymentConfigure.setAttachmentFilePath("//198.20.8.22/fjmsacms_intranet");
			deploymentConfigure.setAttachmentServerUrl("/data");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://198.20.8.22:5432/cms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("fjmsacms2010");
			deploymentConfigure.setBackupTo("f:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("C:/Program Files/pgAdmin III/1.10/pg_dump.exe");
		}
		else if("fjmsa_internet_web1".equals(siteName)) { //海事局外网WEB1
			deploymentConfigure.setDistribution(true);
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setDistributeURL("http://{LOCALHOSTIP|192.168.0.}:8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/fjmsacms_internet_web1");
			deploymentConfigure.setAttachmentFilePath("//192.168.0.22/fjmsacms_internet");
			deploymentConfigure.setAttachmentServerUrl("/data");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://192.168.0.22:5432/cms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("fjmsacms2010");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("C:/Program Files/pgAdmin III/1.8/pg_dump.exe");
		}
		else if("fjmsa_internet_web2".equals(siteName)) { //海事局外网WEB2
			deploymentConfigure.setDistribution(true);
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setDistributeURL("http://{LOCALHOSTIP|192.168.0.}:8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/fjmsacms_internet_web2");
			deploymentConfigure.setAttachmentFilePath("//192.168.0.22/fjmsacms_internet");
			deploymentConfigure.setAttachmentServerUrl("/data");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://192.168.0.22:5432/cms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("fjmsacms2010");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("C:/pgAdmin III/1.8/pg_dump.exe");
		}
		else if("chdeval".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/chdeval");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,logistics,cms/evaluation,msa,im,dpc,enterprise,cms/interview,cms/complaint,cms/messageboard,cms/preapproval,cms/leadermail,cms/advice,cms/supervision,cms/onlineservice,cms/infopublic,bbs,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:oracle:thin:@localhost:1521:orcl");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.Oracle9Dialect");
			deploymentConfigure.setDbUser("chdeval");
			deploymentConfigure.setDbPassword("chdeval123456");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setOracleBackupTnsName("orcl");
			deploymentConfigure.setOracleExportCommandPath("E:/oracle/product/10.2.0/db_1/BIN/exp.exe");
		}
		else if("fzdcd".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/fzdcdcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,aic,land,chd,cms/evaluation,cms/infopublic,msa,im,logistics,enterprise/assess,enterprise/bidding,enterprise/customer,enterprise/iso,enterprise/project,enterprise/quality,enterprise/workreport,dpc,municipal,cin,archives,j2oa/addresslist,j2oa/attendance,j2oa/book,j2oa/businesstrip,j2oa/calendar,j2oa/databank,j2oa/dispatch,j2oa/document,j2oa/exchange,j2oa/leave,j2oa/loan,j2oa/meeting,j2oa/memorabilia,j2oa/personnel,j2oa/receival,j2oa/reimburse,j2oa/todo,webmail,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/fzdccms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("fzdccms@2011");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
			deploymentConfigure.setSessionTimeoutSeconds(4*3600); //4小时
		}
		else if("zztelecom".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,lss,railway,aic,land,chd,cms/evaluation,msa,logistics,enterprise,dpc,municipal,cin,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/zztelecom");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("zztelecom2011");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("e:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("tanjinzhi".equals(siteName)) { //坦金枝
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/cms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("cms@2012");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/9.0/bin/pg_dump.exe");
		}
		else if("changshan".equals(siteName)) { //常山
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/cscms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("cscms@2012");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("e:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("fjfdi".equals(siteName)) { //投资服务中心
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/fjfdi");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,logistics,enterprise,dpc,municipal,cin,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/fjfdi");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("fjfdi@2012");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("pttelex".equals(siteName)) { //平潭机要局
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/telex");
			deploymentConfigure.setUndeployFolders((siteApplicationNames + ",cms/situation,job,wechat,microblog,sportsevents,appraise,electric,asc,fdi,customise,jeaf/application/builder,lss,railway,aic,land,chd,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment").split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/telex");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("telex@2013");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setSessionTimeoutSeconds(5*3600); //5小时
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("zzmpw".equals(siteName)) { //漳州民评网
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/zzmpw");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/zzmpw");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("zzmpw@2013");
			deploymentConfigure.setDbMonitor("192.168.3.236:514");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("zzzfjsw".equals(siteName)) { //漳州作风建设网
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/cms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/cms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("cms@2013");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("fjfzb".equals(siteName)) { //福建法制办
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/fjfzbcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:oracle:thin:@localhost:1521:fjfzb");
			deploymentConfigure.setDbDialect("net.sf.hibernate.dialect.Oracle9Dialect");
			deploymentConfigure.setDbUser("fjfzb");
			deploymentConfigure.setDbPassword("fjfzb@2013");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setOracleBackupTnsName("fjfzb");
			deploymentConfigure.setOracleExportCommandPath("E:/oracle/product/10.2.0/db_1/BIN/exp.exe");
		}
		else if("fzccg".equals(siteName)) { //交建
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("81");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/fzccgcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/fzccgcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("fzccgcms@2013");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("fjgtzy_intranet".equals(siteName)) { //国土资源厅内网触摸屏
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/fjgtzycms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/fjgtzycms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("fjgtzy@2013");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("fjgtzy_touch".equals(siteName)) { //国土资源厅外网触摸屏
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/fjgtzytouch");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/fjgtzytouch");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("fjgtzytouch@2014");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("fjgtzy_internet".equals(siteName)) { //国土资源厅网站
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8089");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("e:/fjgtzycms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land/landcertificate,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://172.16.1.15:5432/fjgtzycms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("fjgtzy@2013");
			deploymentConfigure.setBackupTo("e:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/9/1.18/pg_dump.exe");
		}
		else if("huaan".equals(siteName)) { //华安
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8081");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/hacms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/hacms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("zzmpw@2013");
			deploymentConfigure.setDbMonitor("192.168.3.236:514");
			deploymentConfigure.setBackupTo("d:/backup_huaan");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("xiangcheng".equals(siteName)) { //芗城
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("8080");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/web/xccms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/xccms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("xccms@2014");
			deploymentConfigure.setBackupTo("d:/web/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/web/PostgreSQL/9.0/bin/pg_dump.exe");
		}
		else if("zzgwjh".equals(siteName)) { //漳州公文交换
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/zzgwjh");
			deploymentConfigure.setUndeployFolders("cms/situation,job,wechat,microblog,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,cms/onlineservice,cms/interview,fet,bidding,traffic,messenger,jeaf/payment,j2oa/addresslist,j2oa/attendance,j2oa/book,j2oa/bulletin,j2oa/businesstrip,j2oa/calendar,j2oa/dispatch,j2oa/leave,j2oa/loan,j2oa/meeting,j2oa/memorabilia,j2oa/personnel,j2oa/receival,j2oa/reimburse,j2oa/supervise,j2oa/todo".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/zzgwjh");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("zzgwjh@2014");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("d:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("wjmrcw".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/wjmrcw");
			deploymentConfigure.setUndeployFolders("cms/situation,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/wjmrcw");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("wjmrcw@2014");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		else if("npkx".equals(siteName)) {
			deploymentConfigure.setURL("");
			deploymentConfigure.setSafeURL("");
			deploymentConfigure.setLocalPort("80");
			deploymentConfigure.setContextPath("");
			deploymentConfigure.setDeploymentPath("d:/npkxcms");
			deploymentConfigure.setUndeployFolders("cms/situation,job,sportsevents,appraise,electric,telex,asc,cms/photocollect,fdi,cms/monitor,cms/smssend,cms/smsreceive,customise,jeaf/application/builder,lss,railway,aic,land,chd,cms/evaluation,msa,im,logistics,enterprise,dpc,municipal,cin,archives,webmail,j2oa,fet,bidding,traffic,messenger,jeaf/payment".split(","));
			deploymentConfigure.setDbURL("jdbc:postgresql://localhost:5432/npkxcms");
			deploymentConfigure.setDbDialect("com.yuanluesoft.jeaf.database.hibernate.dialect.PostgreSQLDialect");
			deploymentConfigure.setDbUser("postgres");
			deploymentConfigure.setDbPassword("npkxcms@2014");
			deploymentConfigure.setBackupTo("d:/backup");
			deploymentConfigure.setPostgresDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		}
		return deploymentConfigure;
	}
	
	/**
	 * 部署
	 * @param siteName
	 * @param src
	 * @param dest
	 * @throws Exception
	 */
	public void deployment(String siteName, String lastUpdateDate) throws Exception {
		DeploymentConfigure deploymentConfigure = getDeploymentConfigure(siteName); //获取部署配置
		if(deploymentConfigure.getAttachmentFilePath()==null) { //没有配置附件路径
			deploymentConfigure.setAttachmentFilePath(deploymentConfigure.getDeploymentPath());
		}
		if(deploymentConfigure.getAttachmentServerUrl()==null) {
			deploymentConfigure.setAttachmentServerUrl(deploymentConfigure.getContextPath() + "/cms");
		}
		File sourceContentPath = new File(APPLICATION_PATH + "/WebContent");
		String destPath = DEPLOY_PATH + deploymentConfigure.getDeploymentPath().substring(deploymentConfigure.getDeploymentPath().lastIndexOf("/" ) + 1);
		File destContentPath = new File(destPath);
		if(destContentPath.exists()) { //目录不允许存在
			throw new Exception("目录" + destPath + "已存在");
		}
		
		//在jsp文件中查找java类型
		searchClassJspFolder(sourceContentPath);
		javaClassesInJsp.add(ActionForm.class.getName());
		javaClassesInJsp.add(Record.class.getName());
		
		final long lastModified = new SimpleDateFormat(lastUpdateDate.indexOf(" ")==-1 ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm").parse(lastUpdateDate).getTime();
		deploymentFolder(siteName, deploymentConfigure, sourceContentPath.getAbsolutePath(), sourceContentPath.getAbsolutePath(), destContentPath.getAbsolutePath(), lastModified);
	}
	
	/**
	 * 部署类文件
	 * @throws Exception
	 */
	public void deploymentClasses() throws Exception {
		File sourceContentPath = new File(APPLICATION_PATH + "/WebContent");
		javaClassesInJsp.add(ServiceException.class.getName());
		searchClassJspFolder(sourceContentPath);
		javaClassesInJsp.add(Record.class.getName());
		final String destPath = FileUtils.createDirectory(APPLICATION_PATH + "/WebContent/WEB-INF/classes_deployment");
		final String classesPath = new File(APPLICATION_PATH + "/WebContent/WEB-INF/classes").getPath();
		FileSearchCallback fileSearchCallback = new FileSearchCallback() {
			public void onFileFound(File file) {
				System.out.println(file.getPath());
				if(file.isDirectory()) { //目录
					FileUtils.createDirectory(destPath + file.getPath().substring(classesPath.length()));
					return;
				}
				try {
					String srcFileParh = file.getPath().replace('\\', '/');
					String destFilePath = destPath + file.getPath().substring(classesPath.length());
					if(file.getName().endsWith(".class")) { //class文件
						deploymentClassFile(srcFileParh, destFilePath);;
					}
					else {
						FileUtils.copyFile(srcFileParh, destFilePath, false, false);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		FileUtils.fileSearch(APPLICATION_PATH + "/WebContent/WEB-INF/classes", null, fileSearchCallback);
	}
	
	/**
	 * 递归函数:目录部署
	 * @param src
	 * @param dest
	 * @param beginDate 格式yyyy-MM-dd
	 * @param isOracle
	 * @throws Exception
	 */
	public void deploymentFolder(String siteName, DeploymentConfigure deploymentConfigure, String sourceContentPath, final String srcFilePath, String destFilePath, final long lastModified) throws Exception {
		//检查目录是否需要发布
		if(!needDeploy(siteName, deploymentConfigure, sourceContentPath, srcFilePath)) {
			return;
		}
		final boolean deploymentDirectoryExists = FileUtils.isExists(srcFilePath + "/" + "deployment/"); //是否有名称为"deployment"的子目录
		File srcDir = new File(srcFilePath);
		File[] files = srcDir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if(file.isDirectory()) {
					if(file.getAbsolutePath().indexOf("classes")!=-1) {
						return !file.getName().equals("deployment");
					}
					//检查是否属于不需要发布的目录
					int i = UNDEPLOY_FOLDER_NAMES.length - 1;
					String path = srcFilePath + "/" + file.getName() + "/";
					for(; i>=0 && path.indexOf("/" + UNDEPLOY_FOLDER_NAMES[i] + "/")==-1; i--);
					return i==-1;
				}
				else if(deploymentDirectoryExists) {
					return true;
				}
				else {
					return (file.lastModified()>=lastModified);
				}
			}
		});
		if(files.length==0) {
			return;
		}
		File destDir = new File(destFilePath);
		destDir.mkdir();
		for(int i=0; i<files.length; i++) {
			if(files[i].isDirectory()) { //目录
				deploymentFolder(siteName, deploymentConfigure, sourceContentPath, srcFilePath + "/" + files[i].getName(), destFilePath + "/" + files[i].getName(), lastModified);
				continue;
			}
			String srcFileName = srcFilePath + "/" + files[i].getName();
			if(deploymentDirectoryExists) {
				//检查发布目录中是否有相同的文件
				String deployFile = srcFilePath + "/deployment/" + siteName + "/" + files[i].getName();
				if(FileUtils.isExists(deployFile)) {
					srcFileName = deployFile;
				}
				else if(FileUtils.isExists(deployFile=srcFilePath + "/deployment/default/" + files[i].getName())) {  //检查默认文件
					srcFileName = deployFile;
				}
				else if(FileUtils.isExists(deployFile=srcFilePath + "/deployment/nonecms/" + files[i].getName()) && matchFolder(deploymentConfigure, "cms")) {  //检查无CMS时的文件,且没有网站
					srcFileName = deployFile;
				}
				if(new File(srcFileName).lastModified()<lastModified) { //文件不需要复制
					continue;
				}
			}
			System.out.println("copy file " + srcFileName + " to " + destFilePath + "/" + files[i].getName());
			//部署文件
			deploymentFile(deploymentConfigure, srcFileName, destFilePath + "/" + files[i].getName());
		}
		//检查目录中是否有文件
		if(destDir.listFiles().length==0) { //目录为空,删除空目录
			destDir.delete();
		}
	}
	
	/**
	 * 检查是否需要发布
	 * @param siteName
	 * @param deploymentConfigure
	 * @param sourceContentPath
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private boolean needDeploy(String siteName, DeploymentConfigure deploymentConfigure, String sourceContentPath, String filePath) throws Exception {
		filePath = filePath.substring(sourceContentPath.length()).replaceAll("\\x5c", "/");
		if(filePath.startsWith("/WEB-INF")) {
			filePath = filePath.substring("/WEB-INF".length());
			if(filePath.startsWith("/classes/" + CLASS_PATH)) {
				filePath = filePath.substring(("/classes/" + CLASS_PATH).length());
			}
		}
		if(filePath.startsWith("/")) {
			filePath = filePath.substring(1);
		}
		if(filePath.equals("")) {
			return true;
		}
		if(filePath.indexOf("/js/classes/")!=-1) { //js组成文件
			return false;
		}
		if(filePath.startsWith("jeaf/dataimport/services/")) { //数据导入程序
			filePath = filePath.substring("jeaf/dataimport/services/".length());
			return filePath.indexOf("general")!=-1 || siteName.indexOf(filePath)!=-1;
		}
		//检查是否在不发布的应用中
		return !matchFolder(deploymentConfigure, filePath);
	}
	
	/**
	 * 检查目录是否匹配
	 * @param deploymentConfigure
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private boolean matchFolder(DeploymentConfigure deploymentConfigure, String filePath) throws Exception {
		String[] undeployFolders = deploymentConfigure.getUndeployFolders();
		int i = undeployFolders.length - 1;
		for(; i>=0 && (undeployFolders[i].equals("") || !filePath.startsWith(undeployFolders[i])); i--);
		return (i>=0);
	}
	
	/**
	 * 部署文件
	 * @param dbName
	 * @param srcFile
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentFile(DeploymentConfigure deploymentConfigure, String srcFileName, String destFileName) throws Exception {
		String fileName = new File(srcFileName).getName();
		if(fileName.equals("web.xml")) {
			deploymentWebXmlFile(deploymentConfigure, srcFileName, destFileName);
		}
		else if(fileName.equals("applications-config.xml")) {
			deploymentApplicationsConfigFile(deploymentConfigure, srcFileName, destFileName);
		}
		else if(fileName.equals("hibernate-config.xml")) {
			if(srcFileName.endsWith("WEB-INF/hibernate-config.xml")) {
				deploymentHibernateConfigFile(deploymentConfigure, srcFileName, destFileName);
			}
			else { //hibernate配置文件
				deploymentHbmConfigFile(deploymentConfigure, srcFileName, destFileName);
			}
		}
		else if(fileName.equals("log4j.xml")) {
			deploymentLog4jConfigFile(deploymentConfigure, srcFileName, destFileName);
		}
		else if(fileName.equals("objectcache-config.xml")) {
			deploymentObjectCacheConfigFile(deploymentConfigure, srcFileName, destFileName);
		}
		else if(fileName.equals("schedule-config.xml")) {
			deploymentScheduleConfigFile(deploymentConfigure, srcFileName, destFileName);
		}
		else if(fileName.equals("server-config.wsdd")) {
			deploymentAxisConfigFile(deploymentConfigure, srcFileName, destFileName);
		}
		else if(fileName.equals("runtime-config.xml")) {
			deploymentRuntimeConfigFile(deploymentConfigure, srcFileName, destFileName);
		}
		else if(fileName.endsWith(".js")) { //JS文件
			deploymentJSFile(srcFileName, destFileName);
		}
		else if(fileName.endsWith(".class")) { //class文件
			deploymentClassFile(srcFileName, destFileName);
		}
		else {
			FileUtils.copyFile(srcFileName, destFileName, false, false);
		}
	}
	
	/**
	 * 部署web.xml
	 * @param deploymentConfigure
	 * @param srcFile
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentWebXmlFile(DeploymentConfigure deploymentConfigure, String srcFileName, String destFileName) throws Exception {
		//解析xml
		Document document = parseXML(srcFileName);
		Element rootElement = document.getRootElement();
		//处理spring配置文件
		Element springFilesElement = rootElement.element("context-param").element("param-value");
		List springFileNames = ListUtils.generateList(springFilesElement.getText(), ",");
		for(Iterator iterator = springFileNames.iterator(); iterator.hasNext();) {
			String springFileName = ((String)iterator.next()).trim();
			if(matchFolder(deploymentConfigure, springFileName.substring("/WEB-INF/".length()))) {
				iterator.remove();
			}
		}
		springFilesElement.setText(ListUtils.join(springFileNames, ",", false));
		//处理struts配置文件
		for(Iterator iterator = rootElement.elementIterator("servlet"); iterator.hasNext();) {
			Element servletElement = (Element)iterator.next();
			String servletClass = servletElement.element("servlet-class").getText();
			String servletName = servletElement.element("servlet-name").getText();
			if("action".equals(servletName)) { //struts
				Iterator iteratorParam = servletElement.elementIterator("init-param");
				for(iteratorParam.next(); iteratorParam.hasNext();) {
					Element paramElement = (Element)iteratorParam.next();
					if(matchFolder(deploymentConfigure, paramElement.elementText("param-name").substring("config/".length()))) {
						iteratorParam.remove();
					}
				}
			}
			else if(servletClass.startsWith("com.yuanluesoft.cms.monitor")) {
				if(matchFolder(deploymentConfigure, "cms/monitor")) {
					iterator.remove();
					for(Iterator iteratorMapping = rootElement.elementIterator("servlet-mapping"); iteratorMapping.hasNext();) {
						Element servletMappingElement = (Element)iteratorMapping.next();
						if(servletName.equals(servletMappingElement.element("servlet-name").getText())) {
							iteratorMapping.remove();
							break;
						}
					}
				}
			}
		}
		writeXML(document, destFileName);
	}
	
	/**
	 * 解析应用配置文件
	 * @param deploymentConfigure
	 * @param srcFileName
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentApplicationsConfigFile(DeploymentConfigure deploymentConfigure, String srcFileName, String destFileName) throws Exception {
		//解析xml
		Document document = parseXML(srcFileName);
		Element rootElement = document.getRootElement();
		for(Iterator iterator = rootElement.elementIterator("Group"); iterator.hasNext();) {
			Element groupElement = (Element)iterator.next();;
			for(Iterator iteratorApplication = groupElement.elementIterator("Application"); iteratorApplication.hasNext();) {
				Element applicationElement = (Element)iteratorApplication.next();
				if(matchFolder(deploymentConfigure, applicationElement.attributeValue("Name"))) {
					iteratorApplication.remove();
				}
			}
			if(groupElement.elements().isEmpty() && !"其它".equals(groupElement.attributeValue("Name"))) {
				iterator.remove();
			}
		}
		writeXML(document, destFileName);
	}
	
	/**
	 * 部署hibernate配置文件
	 * @param deploymentConfigure
	 * @param srcFileName
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentHibernateConfigFile(DeploymentConfigure deploymentConfigure, String srcFileName, String destFileName) throws Exception {
		//解析xml
		Document document = parseXML(srcFileName);
		Element rootElement = document.getRootElement();
		for(Iterator iterator = rootElement.elementIterator("bean"); iterator.hasNext();) {
			Element beanElement = (Element)iterator.next();
			if("sessionFactory".equals(beanElement.attributeValue("id"))) {
				for(Iterator propertyIterator = beanElement.elementIterator("property"); propertyIterator.hasNext();) {
					Element propertyElement = (Element)propertyIterator.next();
					if("mappingResources".equals(propertyElement.attributeValue("name"))) {
						for(Iterator valueIterator = propertyElement.element("list").elementIterator("value"); valueIterator.hasNext();) {
							Element valueElement = (Element)valueIterator.next();
							if(matchFolder(deploymentConfigure, valueElement.getText().substring("../".length()))) {
								valueIterator.remove();
							}
						}
					}
					else if("hibernateProperties".equals(propertyElement.attributeValue("name"))) {
						for(Iterator propIterator = propertyElement.element("props").elementIterator("prop"); propIterator.hasNext();) {
							Element propElement = (Element)propIterator.next();
							if("hibernate.dialect".equals(propElement.attributeValue("key"))) {
								propElement.setText(deploymentConfigure.getDbDialect());
							}
							else if("hibernate.show_sql".equals(propElement.attributeValue("key"))) {
								propElement.setText("false"); //不显示SQL
							}
						}
					}
				}
			}
			else if("dataSource".equals(beanElement.attributeValue("id"))) {
				for(Iterator propertyIterator = beanElement.elementIterator("property"); propertyIterator.hasNext();) {
					Element propertyElement = (Element)propertyIterator.next();
					if("driverClassName".equals(propertyElement.attributeValue("name"))) {
						if(deploymentConfigure.getDbURL().indexOf("oracle")!=-1) {
							propertyElement.element("value").setText(Encoder.getInstance().desBase64Encode("oracle.jdbc.driver.OracleDriver", "yu050718", null));
						}
						else if(deploymentConfigure.getDbURL().indexOf("mysql")!=-1) {
							propertyElement.element("value").setText(Encoder.getInstance().desBase64Encode("org.gjt.mm.mysql.Driver", "yu050718", null));
						}
						else if(deploymentConfigure.getDbURL().indexOf("postgresql")!=-1) {
							propertyElement.element("value").setText(Encoder.getInstance().desBase64Encode("org.postgresql.Driver", "yu050718", null));
						}
					}
					else if("url".equals(propertyElement.attributeValue("name"))) {
						propertyElement.element("value").setText(Encoder.getInstance().desBase64Encode(deploymentConfigure.getDbURL(), "yu050718", null));
					}
					else if("username".equals(propertyElement.attributeValue("name"))) {
						propertyElement.element("value").setText(Encoder.getInstance().desBase64Encode(deploymentConfigure.getDbUser(), "yu050718", null));
					}
					else if("password".equals(propertyElement.attributeValue("name"))) {
						propertyElement.element("value").setText(Encoder.getInstance().desBase64Encode(deploymentConfigure.getDbPassword(), "yu050718", null));
					}
				}
			}
			else if("databaseDialect".equals(beanElement.attributeValue("id"))) { //数据库方言
				if(deploymentConfigure.getDbURL().indexOf("oracle")!=-1) {
					beanElement.addAttribute("class", OracleDatabaseDialect.class.getName());
				}
				else if(deploymentConfigure.getDbURL().indexOf("postgresql")!=-1) {
					beanElement.addAttribute("class", PostgresDatabaseDialect.class.getName());
				}
				else if(deploymentConfigure.getDbURL().indexOf("mysql")!=-1) {
					beanElement.addAttribute("class", MysqlDatabaseDialect.class.getName());
				}
				else {
					beanElement.addAttribute("class", DatabaseDialect.class.getName());
				}
			}
			else if("databaseMonitorAddress".equals(beanElement.attributeValue("id"))) { //数据库监控地址
				beanElement.element("constructor-arg").element("value").setText(deploymentConfigure.getDbMonitor()==null ? "" : deploymentConfigure.getDbMonitor());
			}
		}
		writeXML(document, destFileName);
	}
	
	/**
	 * 部署log4j配置文件
	 * @param deploymentConfigure
	 * @param srcFileName
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentLog4jConfigFile(DeploymentConfigure deploymentConfigure, String srcFileName, String destFileName) throws Exception {
		Document document = parseXML(srcFileName);
		Element rootElement = document.getRootElement();
		for(Iterator iterator = rootElement.elementIterator("appender"); iterator.hasNext();) {
			Element appenderElement = (Element)iterator.next();
			if("file.log".equals(appenderElement.attributeValue("name"))) {
				appenderElement.element("param").addAttribute("value", deploymentConfigure.getDeploymentPath() + "/WEB-INF/logs/stdout.log");
			}
			else if("console.log".equals(appenderElement.attributeValue("name"))) {
				appenderElement.element("filter").element("param").addAttribute("value", "DEBUG"); //控制台最小级别调整为DEBUG
			}
		}
		writeXML(document, destFileName);
	}
	
	/**
	 * 部署缓存配置文件
	 * @param deploymentConfigure
	 * @param srcFileName
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentObjectCacheConfigFile(DeploymentConfigure deploymentConfigure, String srcFileName, String destFileName) throws Exception {
		Document document = parseXML(srcFileName);
		Element rootElement = document.getRootElement();
		rootElement.element("diskStore").element("property").setText(deploymentConfigure.getDeploymentPath() + "/WEB-INF/cache");
		if(!deploymentConfigure.isDistribution()) {
			rootElement.remove(rootElement.element("distribution"));
		}
		for(Iterator iterator = rootElement.elementIterator("cache"); iterator.hasNext();) {
			Element cacheElement = (Element)iterator.next();
			if(deploymentConfigure.getSessionTimeoutSeconds()>0 && "ssoSession".equals(cacheElement.attributeValue("name"))) {
				for(Iterator iteratorProperty = cacheElement.elementIterator("property"); iteratorProperty.hasNext();) {
					Element propertyElement = (Element)iteratorProperty.next();
					if("timeToIdleSeconds".equals(propertyElement.attributeValue("name"))) {
						propertyElement.setText(deploymentConfigure.getSessionTimeoutSeconds() + "");
					}
				}
			}
		}
		writeXML(document, destFileName);
	}
	
	/**
	 * 部署定时器配置文件
	 * @param deploymentConfigure
	 * @param srcFileName
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentScheduleConfigFile(DeploymentConfigure deploymentConfigure, String srcFileName, String destFileName) throws Exception {
		//解析xml
		Document document = parseXML(srcFileName);
		Element rootElement = document.getRootElement();
		for(Iterator iterator = rootElement.element("bean").element("property").element("list").elementIterator("bean"); iterator.hasNext();) {
			Element beanElement = (Element)iterator.next();;
			if(beanElement.attributeValue("id")!=null) {
				if(beanElement.attributeValue("id").startsWith("bidding")) {
					if(matchFolder(deploymentConfigure, "bidding")) { //没有招投标应用
						iterator.remove();
					}
				}
				else if(beanElement.attributeValue("id").startsWith("cleanTemporaryMail")) {
					if(matchFolder(deploymentConfigure, "webmail")) { //没有web邮件
						iterator.remove();
					}
				}
				else if(beanElement.attributeValue("id").startsWith("weather")) {
					if(matchFolder(deploymentConfigure, "jeaf/weather")) { //没有天气预报
						iterator.remove();
					}
				}
				else if(beanElement.attributeValue("id").startsWith("unpublishTimeoutRecords")) { //公众服务撤销发布超时的记录
					if(matchFolder(deploymentConfigure, "cms/publicservice")) { //没有公众服务
						iterator.remove();
					}
				}
				else if(beanElement.attributeValue("id").startsWith("logistics")) {
					if(matchFolder(deploymentConfigure, "logistics")) { //没有物流联盟
						iterator.remove();
					}
				}
				else if(beanElement.attributeValue("id").startsWith("chd")) {
					if(matchFolder(deploymentConfigure, "chd")) { //没有华电
						iterator.remove();
					}
				}
				else if(beanElement.attributeValue("id").startsWith("appraise")) {
					if(matchFolder(deploymentConfigure, "appraise")) { //没有民主评议
						iterator.remove();
					}
				}
				else if(beanElement.attributeValue("id").startsWith("fjwssp")) { //福建省网上审批办件更新
					if(matchFolder(deploymentConfigure, "cms/onlineservice/accept")) { //没有网上办事受理
						iterator.remove();
					}
				}
				else if(beanElement.attributeValue("id").startsWith("synchContributeInfos")) { //信息采编
					if(matchFolder(deploymentConfigure, "j2oa/info")) { //没有信息采编
						iterator.remove();
					}
				}
			}
		}
		writeXML(document, destFileName);
	}
	
	/**
	 * 部署AXIS配置文件
	 * @param deploymentConfigure
	 * @param srcFileName
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentAxisConfigFile(DeploymentConfigure deploymentConfigure, String srcFileName, String destFileName) throws Exception {
		Document document = parseXML(srcFileName);
		Element rootElement = document.getRootElement();
		for(Iterator iterator = rootElement.elementIterator("service"); iterator.hasNext();) {
			Element serviceElement = (Element)iterator.next();
			for(Iterator parameterIterator=serviceElement.elementIterator(); parameterIterator.hasNext();) {
				Element parameterElement = (Element)parameterIterator.next();
				if("className".equals(parameterElement.attributeValue("name"))) {
					if(matchFolder(deploymentConfigure, parameterElement.attributeValue("value").substring(CLASS_PATH.length() + 1).replaceAll("\\x2e", "/"))) {
						iterator.remove();
						break;
					}
				}
				else if("beanMapping".equals(parameterElement.getName())) {
					String languageSpecificType = parameterElement.attributeValue("languageSpecificType");
					if(languageSpecificType!=null &&
					   languageSpecificType.startsWith("java:") &&
					   matchFolder(deploymentConfigure, languageSpecificType.substring(CLASS_PATH.length() + 6).replaceAll("\\x2e", "/"))) {
						parameterIterator.remove();
					}
				}
			}
		}
		writeXML(document, destFileName);
	}
	
	/**
	 * 部署runtime配置文件
	 * @param deploymentConfigure
	 * @param srcFileName
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentRuntimeConfigFile(DeploymentConfigure deploymentConfigure, String srcFileName, String destFileName) throws Exception {
		Document document = parseXML(srcFileName);
		Element rootElement = document.getRootElement();
		for(Iterator iterator = rootElement.elementIterator("bean"); iterator.hasNext();) {
			Element beanElement = (Element)iterator.next();
			if("webApplicationUrl".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText(deploymentConfigure.getURL() + deploymentConfigure.getContextPath());
			}
			else if("webApplicationSafeUrl".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText(deploymentConfigure.getSafeURL() + deploymentConfigure.getContextPath());
			}
			else if("webApplicationLocalUrl".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText("http://localhost:" + deploymentConfigure.getLocalPort() + deploymentConfigure.getContextPath());
			}
			else if("webApplicationDistributeUrl".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText((deploymentConfigure.getDistributeURL()==null ? "http://localhost:" + deploymentConfigure.getLocalPort() :  deploymentConfigure.getDistributeURL()) + deploymentConfigure.getContextPath());
			}
			else if("tempatePath".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText(deploymentConfigure.getAttachmentFilePath() + "/cms/templates/");
			}
			else if("tempateUrl".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText(deploymentConfigure.getAttachmentServerUrl() + "/templates/");
			}
			else if("sitePagePath".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText(deploymentConfigure.getAttachmentFilePath() + "/cms/pages/");
			}
			else if("sitePageUrl".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText(deploymentConfigure.getAttachmentServerUrl() + "/pages/");
			}
			else if("staticPagePath".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText((deploymentConfigure.getAttachmentServerUrl().indexOf("://")!=-1 ? deploymentConfigure.getDeploymentPath() : deploymentConfigure.getAttachmentFilePath()) + "/cms/html/");
			}
			else if("staticPageUrl".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText(deploymentConfigure.getAttachmentServerUrl().indexOf("://")!=-1 ? deploymentConfigure.getContextPath() + "/cms/html/" : deploymentConfigure.getAttachmentServerUrl() + "/html/");
			}
			else if("attachmentDirectory".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText(deploymentConfigure.getAttachmentFilePath() + "/WEB-INF/attachments/");
			}
			else if("temporaryDirectory".equals(beanElement.attributeValue("id"))) {
				beanElement.element("constructor-arg").element("value").setText(deploymentConfigure.getDeploymentPath() + "/WEB-INF/temp/");
			}
			else if("localSoapPassport".equals(beanElement.attributeValue("id"))) {
				beanElement.element("property").element("value").setText("http://localhost:" + deploymentConfigure.getLocalPort() + deploymentConfigure.getContextPath() + "/services/");
			}
			else if("initializeServices".equals(beanElement.attributeValue("id"))) {
				for(Iterator refIterator = beanElement.element("constructor-arg").element("list").elementIterator("ref"); refIterator.hasNext();) {
					Element refElement = (Element)refIterator.next();
					if("siteService".equals(refElement.attributeValue("bean"))) {
						if(matchFolder(deploymentConfigure, "cms")) { //没有网站
							refIterator.remove();
						}
					}
					else if("forumService".equals(refElement.attributeValue("bean"))) {
						if(matchFolder(deploymentConfigure, "bbs")) { //没有BBS
							refIterator.remove();
						}
					}
					else if("publicDirectoryService".equals(refElement.attributeValue("bean"))) {
						if(matchFolder(deploymentConfigure, "cms/infopublic")) { //没有信息公开
							refIterator.remove();
						}
					}
					else if("onlineServiceDirectoryService".equals(refElement.attributeValue("bean"))) {
						if(matchFolder(deploymentConfigure, "cms/onlineservice")) { //没有网上办事
							refIterator.remove();
						}
					}
					else if("databankDirectoryService".equals(refElement.attributeValue("bean"))) {
						if(matchFolder(deploymentConfigure, "j2oa/databank")) { //没有资料库
							refIterator.remove();
						}
					}
					else if("keywordService".equals(refElement.attributeValue("bean"))) {
						if(matchFolder(deploymentConfigure, "j2oa/dispatch") && matchFolder(deploymentConfigure, "j2oa/receival")) { //没有收发文
							refIterator.remove();
						}
					}
					else if("isoDirectoryService".equals(refElement.attributeValue("bean"))) {
						if(matchFolder(deploymentConfigure, "enterprise/iso")) { //没有ISO
							refIterator.remove();
						}
					}
					else if("chdEvaluationDirectoryService".equals(refElement.attributeValue("bean"))) {
						if(matchFolder(deploymentConfigure, "chd/evaluation")) { //没有华电星级企业评价
							refIterator.remove();
						}
					}
					else if("jobParameterService".equals(refElement.attributeValue("bean"))) {
						if(matchFolder(deploymentConfigure, "job")) { //没有人才网
							refIterator.remove();
						}
					}
				}
			}
			else if("userSynchClientList".equals(beanElement.attributeValue("id"))) {
				for(Iterator refIterator = beanElement.element("property").element("list").elementIterator("ref"); refIterator.hasNext();) {
					Element refElement = (Element)refIterator.next();
					if("mailUserSynchService".equals(refElement.attributeValue("bean"))) {
						if(matchFolder(deploymentConfigure, "webmail")) { //没有邮件系统
							refIterator.remove();
						}
					}
				}
			}
			else if("dataBackupService".equals(beanElement.attributeValue("id"))) { //备份
				//存放目录
				beanElement.element("property").element("list").element("bean").element("property").element("value").setText(deploymentConfigure.getBackupTo());
				//目录备份
				Element dataBackupProcesses = ((Element)beanElement.elements("property").get(1)).element("list");
				dataBackupProcesses.clearContent();
				//备份程序
				String[] toBackupFolders = ("cms|" + deploymentConfigure.getAttachmentFilePath() + (deploymentConfigure.getToBackupFolders()==null ? "" : "," + deploymentConfigure.getToBackupFolders())).split(",");
				for(int i=0; i<toBackupFolders.length; i++) {
					Element directoryBackupProcessor = dataBackupProcesses.addElement("bean");
					directoryBackupProcessor.addAttribute("class", "com.yuanluesoft.jeaf.databackup.processor.impl.DirectoryBackupProcessor");
					Element property = directoryBackupProcessor.addElement("property");
					property.addAttribute("name", "path");
					property.addElement("value").setText(toBackupFolders[i].split("\\x7c")[1]); //备份的目录
					property = directoryBackupProcessor.addElement("property");
					property.addAttribute("name", "alias");
					property.addElement("value").setText(toBackupFolders[i].split("\\x7c")[0]); //别名
				}
				//备份oracle
				if(deploymentConfigure.getOracleExportCommandPath()!=null) {
					Element backupProcessor = dataBackupProcesses.addElement("bean");
					backupProcessor.addAttribute("class", "com.yuanluesoft.jeaf.databackup.processor.impl.OracleBackupProcessor");
					Element property = backupProcessor.addElement("property");
					property.addAttribute("name", "exportCommandPath");
					property.addElement("value").setText(Encoder.getInstance().desBase64Encode(deploymentConfigure.getOracleExportCommandPath(), "yu050718", null)); //exp.exe所在路径,如果系统path里面有,允许为空
					
					property = backupProcessor.addElement("property");
					property.addAttribute("name", "userName");
					property.addElement("value").setText(Encoder.getInstance().desBase64Encode(deploymentConfigure.getDbUser(), "yu050718", null)); //oracle用户名
					
					property = backupProcessor.addElement("property");
					property.addAttribute("name", "password");
					property.addElement("value").setText(Encoder.getInstance().desBase64Encode(deploymentConfigure.getDbPassword(), "yu050718", null)); //oracle用户密码
					
					property = backupProcessor.addElement("property");
					property.addAttribute("name", "tnsName");
					property.addElement("value").setText(Encoder.getInstance().desBase64Encode(deploymentConfigure.getOracleBackupTnsName(), "yu050718", null)); //oracle TNS名称, 必须事先建, 10G可以不建, 直接用IP
					
					property = backupProcessor.addElement("property");
					property.addAttribute("name", "owners");
					property.addElement("value").setText(Encoder.getInstance().desBase64Encode(deploymentConfigure.getOracleBackupOwners()==null ? deploymentConfigure.getDbUser() : deploymentConfigure.getOracleBackupOwners(), "yu050718", null)); //需要备份的数据库隶属的用户,用逗号分隔,默认=userName
				}
				//备份postgresql
				if(deploymentConfigure.getPostgresDumpCommandPath()!=null) {
					Element backupProcessor = dataBackupProcesses.addElement("bean");
					backupProcessor.addAttribute("class", "com.yuanluesoft.jeaf.databackup.processor.impl.PostgresBackupProcessor");
					Element property = backupProcessor.addElement("property");
					property.addAttribute("name", "dumpCommandPath");
					property.addElement("value").setText(Encoder.getInstance().desBase64Encode(deploymentConfigure.getPostgresDumpCommandPath(), "yu050718", null)); //pg_dump.exe所在路径,如果系统path里面有,允许为空
					
					property = backupProcessor.addElement("property");
					property.addAttribute("name", "userName");
					property.addElement("value").setText(Encoder.getInstance().desBase64Encode(deploymentConfigure.getDbUser(), "yu050718", null)); //用户名
					
					property = backupProcessor.addElement("property");
					property.addAttribute("name", "password");
					property.addElement("value").setText(Encoder.getInstance().desBase64Encode(deploymentConfigure.getDbPassword(), "yu050718", null)); //用户密码
					
					//解析JDBC URL（如:jdbc:postgresql://localhost:5432/cms）, 获取数据库主机名、端口和数据库名称
					String[] values = deploymentConfigure.getDbURL().split("/");
					property = backupProcessor.addElement("property");
					property.addAttribute("name", "dbName");
					property.addElement("value").setText(Encoder.getInstance().desBase64Encode(values[3], "yu050718", null)); //数据库名称

					values = values[2].split(":");
					property = backupProcessor.addElement("property");
					property.addAttribute("name", "host");
					property.addElement("value").setText(Encoder.getInstance().desBase64Encode(values[0], "yu050718", null)); //数据库服务器主机名
					
					property = backupProcessor.addElement("property");
					property.addAttribute("name", "port");
					property.addElement("value").setText(Encoder.getInstance().desBase64Encode(values.length>1 ? values[1] : "5432", "yu050718", null)); //数据库服务器端口
				}
			}
		}
		writeXML(document, destFileName);
	}

	/**
	 * 部署hbm配置文件
	 * @param dbName
	 * @param srcFile
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentHbmConfigFile(DeploymentConfigure deploymentConfigure, String srcFileName, String destFileName) throws Exception {
		Document document = parseXML(srcFileName);
		Element rootElement = document.getRootElement();
		boolean mysql = (deploymentConfigure.getDbURL()).indexOf("mysql")!=-1;
		for(Iterator iterator = rootElement.elementIterator("class"); iterator.hasNext();) {
			Element classElement = (Element)iterator.next();
			if(mysql) { //删除type="com.yuanluesoft.jeaf.database.hibernate.StringClobType"
				for(Iterator propertyIterator = classElement.elementIterator("property"); propertyIterator.hasNext();) {
					Element propertyElement = (Element)propertyIterator.next();
					if("com.yuanluesoft.jeaf.database.hibernate.StringClobType".equals(propertyElement.attributeValue("type"))) {
						propertyElement.remove(propertyElement.attribute("type"));
					}
				}
			}
			for(Iterator subclassIterator = classElement.elementIterator("joined-subclass"); subclassIterator.hasNext();) {
				Element subclassElement = (Element)subclassIterator.next();
				if(matchFolder(deploymentConfigure, subclassElement.attributeValue("name").substring(CLASS_PATH.length() + 1).replaceAll("\\x2e", "/"))) {
					subclassIterator.remove();
				}
				else if(mysql) { //删除type="com.yuanluesoft.jeaf.database.hibernate.StringClobType"
					for(Iterator propertyIterator = subclassElement.elementIterator("property"); propertyIterator.hasNext();) {
						Element propertyElement = (Element)propertyIterator.next();
						if("com.yuanluesoft.jeaf.database.hibernate.StringClobType".equals(propertyElement.attributeValue("type"))) {
							propertyElement.remove(propertyElement.attribute("type"));
						}
					}
				}
			}
		}
		writeXML(document, destFileName);
	}
	
	/**
	 * 解析XML
	 * @param xmlFileName
	 * @return
	 * @throws Exception
	 */
	private Document parseXML(String xmlFileName) throws Exception {
		SAXReader reader = new SAXReader();
		reader.setEntityResolver(new NoOpEntityResolver());
		return reader.read(new File(xmlFileName));
	}
	
	
	public class NoOpEntityResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId) {
			return new InputSource(new ByteArrayInputStream("".getBytes()));
		}
	}

	
	/**
	 * 写XML
	 * @param document
	 * @param xmlFileName
	 * @throws Exception
	 */
	private void writeXML(Document document, String xmlFileName) throws Exception {
		OutputFormat format = OutputFormat.createPrettyPrint();      
		format.setEncoding("UTF-8");   
		XMLWriter writer = new XMLWriter(new FileOutputStream(new File(xmlFileName)), format);   
		writer.write(document);
		writer.close();
	}
	
	/**
	 * 部署JS文件
	 * @param srcFile
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentJSFile(String srcFile, String destFileName) throws Exception {
		//检查是否在不需要转换的目录中
		for(int i=0; i<JS_KEEP_FOLDER_NAMES.length; i++) {
			if(srcFile.indexOf(JS_KEEP_FOLDER_NAMES[i])!=-1) {
				FileUtils.copyFile(srcFile, destFileName, false, false);
				return;
			}
		}
		FileWriter output = new FileWriter(destFileName);
		BufferedWriter writer = new BufferedWriter(output);
		JsUtils.rewriteJsFile(srcFile, writer);
		writer.close();
		output.close();
	}
	
	/**
	 * 发布类文件
	 * @param srcFile
	 * @param destFileName
	 * @throws Exception
	 */
	private void deploymentClassFile(String srcFile, String destFileName) throws Exception {
		if(srcFile.indexOf("taglib/")!=-1 || //标签库
		   srcFile.indexOf("forms/")!=-1 || //表单
		   srcFile.indexOf("form/")!=-1 || //表单
		   srcFile.indexOf("pojo/")!=-1 || //pojo
		   srcFile.indexOf("model/")!=-1 || //模型
		   srcFile.indexOf("Callback")!=-1 || //回调
		   srcFile.indexOf("Exception")!=-1) { //异常
			FileUtils.copyFile(srcFile, destFileName, false, false); //不加密
			return;
		}
		//检查文件是否是jsp引用过的文件
		String className = srcFile.substring(srcFile.indexOf("classes/") + "classes/".length(), srcFile.length() - ".class".length()).replace('/', '.');
		for(Iterator iterator = javaClassesInJsp.iterator(); iterator.hasNext();) {
			String classInJsp = (String)iterator.next();
			if(classInJsp.indexOf(className)!=-1 || className.indexOf(classInJsp)!=-1) {
				FileUtils.copyFile(srcFile, destFileName, false, false);
				return;
			}
		}
		//加密类文件
		List command = new ArrayList();
		command.add("C:\\workspace\\Visual C\\JavaCodeProtect\\Encrypt\\Release\\Encrypt.exe");
		command.add(srcFile.replace('/', '\\'));
		command.add(destFileName.replace('/', '\\'));
		ProcessUtils.executeCommand(command, null, null);
	}

	/**
	 * 在文件夹中查找
	 * @param folder
	 */
	private void searchClassJspFolder(File folder) throws Exception {
		File[] files = folder.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if(file.isDirectory()) {
					for(int i=0; i<UNDEPLOY_FOLDER_NAMES.length; i++) {
						if(file.getName().equalsIgnoreCase(UNDEPLOY_FOLDER_NAMES[i])) {
							return false;
						}
					}
					return !file.getName().equalsIgnoreCase("WEB-INF");
				}
				else {
					return file.getName().endsWith(".jsp");
				}
			}
		});
		if(files==null || files.length==0) {
			return;
		}
		for(int i=0; i<files.length; i++) {
			if(files[i].isDirectory()) {
				searchClassJspFolder(files[i]);
			}
			else {
				searchClassJspFile(files[i]);
			}
		}
	}
	
	/**
	 * 在jsp文件中查找
	 * @param jspFile
	 */
	private void searchClassJspFile(File jspFile) throws Exception {
		String jspContent = FileUtils.readStringFromFile(jspFile.getAbsolutePath(), "UTF-8");
		if(jspContent==null) {
			return;
		}
		int beginIndex=0, endIndex;
		for(beginIndex = jspContent.indexOf(CLASS_PREFIX, beginIndex); beginIndex!=-1; beginIndex = jspContent.indexOf(CLASS_PREFIX, beginIndex)) {
			endIndex = -1;
			for(int i=0; i<CLASS_POSTFIX.length; i++) { //查找类的结束位置
				int classEnd = jspContent.indexOf(CLASS_POSTFIX[i], beginIndex);
				if(classEnd==-1) {
					continue;
				}
				if(endIndex==-1 || classEnd<endIndex) {
					endIndex = classEnd;
				}
			}
			if(endIndex==-1) {
				beginIndex += CLASS_PREFIX.length();
				continue;
			}
			String className = jspContent.substring(beginIndex, endIndex);
			if(javaClassesInJsp.indexOf(className)==-1) {
				System.out.println("class " + (javaClassesInJsp.size()+1)  + " found in jsp: " + className);
				javaClassesInJsp.add(className);
			}
			beginIndex = endIndex + 1;
		}
	}
	
	/**
	 * 发布配置
	 * @author linchuan
	 *
	 */
	private class DeploymentConfigure {
		private String URL;
		private String safeURL;
		private String distributeURL;
		private String localPort;
		private String contextPath;
		private String deploymentPath; //部署目录
		private String attachmentServerUrl; //附件服务器URL,包括pages、templates、html
		private String attachmentFilePath; //附件文件路径
		private String[] undeployFolders;
		private String dbURL;
		private String dbDialect; //数据库方言
		private String dbUser;
		private String dbPassword;
		private String dbMonitor; //数据库监控
		private String backupTo;
		private String toBackupFolders; //格式:别名1|目录1,...,别名n|目录n
		private String oracleExportCommandPath; //exp.exe所在路径,如果系统path里面有,允许为空
		private String oracleBackupTnsName;
		private String oracleBackupOwners; //默认就是dbUser
		private String postgresDumpCommandPath; //pd_dump.exe所在路径,如果系统path里面有,允许为空
		private boolean distribution = false; //是否分布式部署
		private int sessionTimeoutSeconds; //会话时间
		
		/**
		 * @return the backupTo
		 */
		public String getBackupTo() {
			return backupTo;
		}
		/**
		 * @param backupTo the backupTo to set
		 */
		public void setBackupTo(String backupTo) {
			this.backupTo = backupTo;
		}
		/**
		 * @return the contextPath
		 */
		public String getContextPath() {
			return contextPath;
		}
		/**
		 * @param contextPath the contextPath to set
		 */
		public void setContextPath(String contextPath) {
			this.contextPath = contextPath;
		}
		/**
		 * @return the dbPassword
		 */
		public String getDbPassword() {
			return dbPassword;
		}
		/**
		 * @param dbPassword the dbPassword to set
		 */
		public void setDbPassword(String dbPassword) {
			this.dbPassword = dbPassword;
		}
		/**
		 * @return the dbURL
		 */
		public String getDbURL() {
			return dbURL;
		}
		/**
		 * @param dbURL the dbURL to set
		 */
		public void setDbURL(String dbURL) {
			this.dbURL = dbURL;
		}
		/**
		 * @return the dbUser
		 */
		public String getDbUser() {
			return dbUser;
		}
		/**
		 * @param dbUser the dbUser to set
		 */
		public void setDbUser(String dbUser) {
			this.dbUser = dbUser;
		}
		/**
		 * @return the localPort
		 */
		public String getLocalPort() {
			return localPort;
		}
		/**
		 * @param localPort the localPort to set
		 */
		public void setLocalPort(String localPort) {
			this.localPort = localPort;
		}
		/**
		 * @return the safeURL
		 */
		public String getSafeURL() {
			return safeURL;
		}
		/**
		 * @param safeURL the safeURL to set
		 */
		public void setSafeURL(String safeURL) {
			this.safeURL = safeURL;
		}
		/**
		 * @return the undeployFolders
		 */
		public String[] getUndeployFolders() {
			return undeployFolders;
		}
		/**
		 * @param undeployFolders the undeployFolders to set
		 */
		public void setUndeployFolders(String[] undeployFolders) {
			this.undeployFolders = undeployFolders;
		}
		/**
		 * @return the uRL
		 */
		public String getURL() {
			return URL;
		}
		/**
		 * @param url the uRL to set
		 */
		public void setURL(String url) {
			URL = url;
		}
		/**
		 * @return the oracleBackupOwners
		 */
		public String getOracleBackupOwners() {
			return oracleBackupOwners;
		}
		/**
		 * @param oracleBackupOwners the oracleBackupOwners to set
		 */
		public void setOracleBackupOwners(String oracleBackupOwners) {
			this.oracleBackupOwners = oracleBackupOwners;
		}
		/**
		 * @return the oracleBackupTnsName
		 */
		public String getOracleBackupTnsName() {
			return oracleBackupTnsName;
		}
		/**
		 * @param oracleBackupTnsName the oracleBackupTnsName to set
		 */
		public void setOracleBackupTnsName(String oracleBackupTnsName) {
			this.oracleBackupTnsName = oracleBackupTnsName;
		}
		/**
		 * @return the toBackupFolders
		 */
		public String getToBackupFolders() {
			return toBackupFolders;
		}
		/**
		 * @param toBackupFolders the toBackupFolders to set
		 */
		public void setToBackupFolders(String toBackupFolders) {
			this.toBackupFolders = toBackupFolders;
		}
		/**
		 * @return the dbDialect
		 */
		public String getDbDialect() {
			return dbDialect;
		}
		/**
		 * @param dbDialect the dbDialect to set
		 */
		public void setDbDialect(String dbDialect) {
			this.dbDialect = dbDialect;
		}
		/**
		 * @return the oracleExportCommandPath
		 */
		public String getOracleExportCommandPath() {
			return oracleExportCommandPath;
		}
		/**
		 * @param oracleExportCommandPath the oracleExportCommandPath to set
		 */
		public void setOracleExportCommandPath(String oracleExportCommandPath) {
			this.oracleExportCommandPath = oracleExportCommandPath;
		}
		/**
		 * @return the postgresDumpCommandPath
		 */
		public String getPostgresDumpCommandPath() {
			return postgresDumpCommandPath;
		}
		/**
		 * @param postgresDumpCommandPath the postgresDumpCommandPath to set
		 */
		public void setPostgresDumpCommandPath(String postgresDumpCommandPath) {
			this.postgresDumpCommandPath = postgresDumpCommandPath;
		}
		/**
		 * @return the distributeURL
		 */
		public String getDistributeURL() {
			return distributeURL;
		}
		/**
		 * @param distributeURL the distributeURL to set
		 */
		public void setDistributeURL(String distributeURL) {
			this.distributeURL = distributeURL;
		}
		/**
		 * @return the localPath
		 */
		public String getDeploymentPath() {
			return deploymentPath;
		}
		/**
		 * @param localPath the localPath to set
		 */
		public void setDeploymentPath(String localPath) {
			this.deploymentPath = localPath;
		}
		/**
		 * @return the dataContextPath
		 */
		public String getAttachmentServerUrl() {
			return attachmentServerUrl;
		}
		/**
		 * @param dataContextPath the dataContextPath to set
		 */
		public void setAttachmentServerUrl(String dataContextPath) {
			this.attachmentServerUrl = dataContextPath;
		}
		/**
		 * @return the distribution
		 */
		public boolean isDistribution() {
			return distribution;
		}
		/**
		 * @param distribution the distribution to set
		 */
		public void setDistribution(boolean distribution) {
			this.distribution = distribution;
		}
		/**
		 * @return the sessionTimeoutSeconds
		 */
		public int getSessionTimeoutSeconds() {
			return sessionTimeoutSeconds;
		}
		/**
		 * @param sessionTimeoutSeconds the sessionTimeoutSeconds to set
		 */
		public void setSessionTimeoutSeconds(int sessionTimeoutSeconds) {
			this.sessionTimeoutSeconds = sessionTimeoutSeconds;
		}
		/**
		 * @return the attachmentFilePath
		 */
		public String getAttachmentFilePath() {
			return attachmentFilePath;
		}
		/**
		 * @param attachmentFilePath the attachmentFilePath to set
		 */
		public void setAttachmentFilePath(String attachmentFilePath) {
			this.attachmentFilePath = attachmentFilePath;
		}
		/**
		 * @return the dbMonitor
		 */
		public String getDbMonitor() {
			return dbMonitor;
		}
		/**
		 * @param dbMonitor the dbMonitor to set
		 */
		public void setDbMonitor(String dbMonitor) {
			this.dbMonitor = dbMonitor;
		}
	}
}