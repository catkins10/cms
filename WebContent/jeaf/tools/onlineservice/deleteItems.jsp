<%@page import="com.yuanluesoft.jeaf.database.Record"%>
<%@page import="com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.yuanluesoft.jeaf.database.DatabaseService"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
	if(!request.getServerName().equals("localhost") &&
	   (request.getSession().getAttribute("SessionInfo")==null || 
	    !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName")))) {
		out.print("failed");
		return;
	}
	String[] codes = {
			"4601,4602,4605,1201,1202,7701,7101,7901,7902,7702,6109,6110,6111,7703,6112,7903,7401,7704,7705,7402,7904,7403,7404,7706,7405,7406,7707,7708,7905",
			"7102,8301,7501,8202,7906,7502,7503,7504,7907,7505,8101,7601,8102,8201,8302,8501,8203,8204,7908,8205,8103,8502,8206,8303,8104,8503,8105,8106,8304",
			"8107,8305,8306,7602,8311,7407,8108,8207,7408,8504,8505,8001,8506,8209,8507,8002,8003,8508,8004,8005,8509,7103,8006,8307,8510,8007,8211,8511,8008",
			"8009,8308,8010,8512,8212,8011,8012,8513,7603,8210,4614,8514,7201,7604,4615,8208,8309,4616,8310,8515,8516,7801,8517,8518,8519,4622,7802,7605,8520",
			"7803,8521,8522,7606,7607,8528,8529,7104,8530,7105,8531,8532,8533,5701,8534,8312,7202,8313,7106,8535,8536,8314,8315,8537,8538,8539,7107,7108,7909",
			"7910,7911,7912,7913,8316,7301,7302,8317,8318,7303,7304,7804,8319,7305,7306,8320,7805,7806,7203,8321,7307,7807,7204,7808,7205,7206,7109,7110,8601",
			"4302,8602,7111,8603,4303,8604,8605,8606,6302,6303,7112,7113,7114,8607,4308,7115,4309,7116,7117,8608,8609,8610,8611,6601,8612,8613,7118,8614,6602",
			"7119,8615,6603,8616,7120,6604,8617,6605,6606,8618,8619,7121,8620,8621,8622,8623,8624,7122,8625,8626,7123,8627,8629,7124,8630,7125,7126,7127,7128",
			"7129,5301,5302,5601,5303,5304,5305,5306,3503,7130,9002,5307,5308,7131,5309,5310,5311,5312,7132,3504,7133,3505,3506,3507,7134,3802,7135,7136,6006",
			"6005,8631,8632,6001,6002,6003,6004,6007,3807,5501,5502,5201,5202,5203,3808,3809,3810,6201,3811,7137,7207,7208,6202,6203,6401,4201,6402,6204,6403",
			"4202,4203,4204,8322,8323,4205,4206,3882,3613,4207,4208,4209,3884,3708,3885,3709,3886,4210,4211,4212,4213,4214,4215,4216,4217,4218,4219,3717,9018",
			"2001,2002,9019,2003,2004,2005,9020,2006,2007,9021,2008,2009,9022,2010,2011,4802,2012,9023,2013,2014,2015,2016,9024,3891,3892,3893,3894,3895,3896",
			"3897,3900,3880,3879,3878,2036,2037,2038,2039,2040,2041,2042,4001,3876,4002,4003,4004,4006,4007,4008,4009,4010,4011,9025,9026,9027,3001,4803,4804",
			"4805,4807,4808,4809,4810,4817,4818,4819,4820,4821,4824,2043,3510,7209,3515,801,5401,5402,5101,5102,5103,5104,5105,5106,5107,5108,5109,5110,5111",
			"5112,5113,5114,5115,5116,5117,5118,5119,2045,9033,8901,1301,1303,1306,1310,9301,1311,8801,2046,6607,6608,8324,8325,8326,8327,8328,8329,8330,3734",
			"3812,3813,3814,3815,3816,3406,3402,7059,6915,6811,6708,6609,6509,6136,5914,5819,3633,4012,4346,6404,9035,7210,7308,7506,6008,6205,7060,7409,3926",
			"3520,6510,6916,6813,6709,6610,6405,6206,6137,6009,5820,4709,4347,4013,3634,3521,2844,2845,2847,1312,7062,5821,7211,7309,7410,7507,2864,2203,7063",
			"7064,7065,7066,7067,7068,7069,7070,7071,7072,7073,4220,6010,6138,6917,6918,6919,4221,21103,2204,2865,2866,3827,7075,3817,6511,21104,21105,21106",
			"21107,3736,7076,6139,6920,5824,2205,21108,3737,1316,1317,4825,4826,4827,4828,4829,4714,3524,3749,3750,3752,6611,6612,6613,6614,4014,4015,4016,4017",
			"1319,901,902,1001,1002,802,803,804,8401,8402,808201,808104,8540,8541,8542,8543,2947,3753,3754,3755,5920,5921,5825,5826,7079,7080,7081,7082,7083,7084",
			"7085,7086,7087,7088,7089,7090,7091,7092,6921,6922,4719,4720,6815,6816,6715,6716,6615,6616,6512,6513,6406,6407,6207,6208,6140,6141,6011,6012,4830",
			"4831,3756,3757,3645,3646,4222,4223,3927,3928,2206,2207,2948,2949,3929,3758,7097,3759,3760,6617,6618,6619,6620,6621,6622,4359,1320,2509,3761,3533",
			"3534,3539,3648,3649,4135,4136,3762,3937,3938,3939,3940,6719,6720,6721,6722,6723,6724,6725,6726,6727,6728,6729,4368,3763,4018,4019,2344,4020,4021",
			"4022,3946,3947,3948,3765,4836,4837,4838,1003,1004,1005,1006,1007,1008,1009,1010,1321,6408,6409,3849,9036,805,9901,4639,3540,3546,3548,9037,9038",
			"9039,9040,9041,9042,9043,9044,9045,9046,9047,9048,9049,9050,9051,9052,9053,3767,3650,2973,2974,2975,2976,4023,4561,5830,4742,4562,4932,4139,4563",
			"4933,3425,4564,5831,4934,4565,2977,4566,5832,4567,4935,4568,21200,4936,4024,4569,4439,4570,4743,4571,4937,4142,4572,5833,2978,4573,4938,4574,4143",
			"4744,2737,4575,4440,5834,4144,4025,4939,4745,4940,4576,2738,4941,5835,4441,4577,4942,4442,5836,4943,2979,4578,3428,4944,5837,4579,2739,4443,2980",
			"5838,4444,2740,4580,5839,4581,2741,3429,4582,4445,5840,2742,4583,2981,2646,6923,4746,3430,2743,4584,2982,3431,4585,2744,4446,4026,4747,4586,3432",
			"4587,4588,2647,2983,4748,6924,4447,2984,4027,4589,2745,2985,4448,2746,2648,2747,4028,2986,4449,2421,2748,2749,2422,21201,4029,2649,4450,2750,2423",
			"4030,4451,2751,21202,2650,4031,4452,2987,4453,21203,4749,4032,21204,4455,4750,4751,4145,3433,2988,4033,4752,21205,2989,4034,4146,4753,70137,2990",
			"4035,4147,6925,4036,4148,3434,6926,21206,4149,2991,4037,3435,21207,2992,4150,2993,2651,4038,2652,4151,2994,4039,21208,6927,4152,2995,2653,4040,4153",
			"4041,2996,4042,2654,4043,2997,6928,2655,2998,21209,2999,6929,2656,21210,6930,21211,2657,21212,4154,21213,2658,21214,21215,2659,21216,4155,2424,21217",
			"4156,21218,21219,21220,4157,2660,70138,4158,2661,4159,4160,70139,21221,21222,21223,21224,2425,3436,2426,70140,3437,21225,21226,21228,2427,2428,21229",
			"2429,5957,2430,5958,3768,5959,4161,5960,4162,5961,5962,5963,4163,4164,5964,2431,70141,21230,21231,5965,21232,5966,21233,21234,70142,5967,70143,70144",
			"70145,70146,70147,70148,70149,70150,70151,70152,70153,70154,70155,70156,70157,70158,70159,70160,70161,70162,70163,70164,70165,70166,70167,1322,70168",
			"70169,21238,21239,21240,21241,21242,21243,4640,70170,29100,2209,3949,29101,29102,2210,4641,29103,70171,29104,3950,4642,1323,3951,29105,70172,29106,3952",
			"70173,1324,3953,70174,70175,3954,64010,3955,6411,3956,3957,6820,3958,70176,70177,6821,4643,29107,6822,29108,29109,29110,29111,2346,2347,6823,2348,1325",
			"4165,1326,1327,2349,1328,2350,1329,1330,21244,2351,1331,21245,1332,21246,4644,4645,70178,70179,70180,70181,2352,70182,2353,29112,70183,4646,29113,2354",
			"70184,2355,2356,29114,70185,29115,70186,29116,29117,70187,29118,21247,21248,70188,21249,21250,70189,21251,21252,70190,2357,29119,21253,70191,29120",
			"21254,2358,29121,70192,2359,70193,29122,29123,29124,29125,29126,29127,29128,29129,4647,4648,2511,4649,29130,4650,2512,2513,2514,2515,29131,29132,4652",
			"4653,4654,4655,4656,4657,4658,9806,6824,6825,5968,9807,6514,6826,5969,9808,9809,6515,98010,6827,9811,6516,5970,6517,29133,29134,6518,29135,29136,29137",
			"6519,29138,29139,29140,29141,29142,29143,29144,29145,29146,29147,29148,29149,6520,29150,6521,29151,29152,29153,29154,6522,29155,29156,29157,6523",
			"29158,29159,4369,4370,4371,4372,4373,4374,4375,4376,4377,4378,4379,4380,4381,4382,4383,29161,29162,29163,29164,3651,3652,3653,3654,3655,3656,3657",
			"3658,3659,3660,3661,3662,3663,4384,4385,4386,4388,4389,4390,4391,4392,1119,1120,1121,1122,1123,1124,1125,1126,1127,6147,6148,6149,6150,6151,6152",
			"6153,6154,6155,6156,6157,6158,6159,6160,6161,6162,6163,6164,6165,6166,6167,6308,3550,3551,3552,3553,3554,3555,3556,3557,3558,3559,2051,2052,2053",
			"2054,2055,2056,2057,2058,2059,2060,2061,2062,2063,2064,2065,2066,2067,2068,2069,2070,4754,4755,4756,4757,4758,4759,4760,4761,4762,4763,2071,2072",
			"2073,2074,2075,2076,2077,2078,2079,2080,2081,2082,2083,2084,2085,2086,2087,2088,2089,2090,2091,2092,2093,2094,2095,2096,2097,2098,2126,2200,2211",
			"2212,2214,4456,4457,4458,3438,29165,29166,29167,3960,3961,3965,3966,3967,3968,70194,70195,70196,70197,70198,70199,70200,70201,70202,70203,70204",
			"70205,10001,10101"};
	DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
	Object onlineServiceItemService = Environment.getService("onlineServiceItemService");
	String hql = "select OnlineServiceItem" +
				 " from OnlineServiceItem OnlineServiceItem, OnlineServiceItemSubjection OnlineServiceItemSubjection, OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection" +
				 " where OnlineServiceItemSubjection.itemId=OnlineServiceItem.id" +
				 " and OnlineServiceDirectorySubjection.directoryId=OnlineServiceItemSubjection.directoryId" +
				 " and OnlineServiceDirectorySubjection.parentDirectoryId=20229309033290000" + //20229309033290000
				 " and not OnlineServiceItem.code is null order by OnlineServiceItem.id";
	for(int i=0; ; i+=200) {
		List items = databaseService.findRecordsByHql(hql, i, 200);
		if(items==null || items.isEmpty()) {
			break;
		}
		for(Iterator iterator = items.iterator(); iterator.hasNext();) {
			OnlineServiceItem item = (OnlineServiceItem)iterator.next();
			//out.println(item.getName() + "<br>");
			if(item.getCode()==null || item.getCode().isEmpty()) {
				continue;
			}
			int j=0;
			for(; j<codes.length && ("," + codes[j] + ",").indexOf("," + item.getCode() + ",")==-1; j++);
			if(j>=codes.length) { //编号已经删除
				out.println(item.getCode() + "<br>");
				//onlineServiceItemService.getClass().getMethod("delete", new Class[]{Record.class}).invoke(onlineServiceItemService, new Object[]{item}); //delete(item);
				//i--;
			}
		}
	}
%>