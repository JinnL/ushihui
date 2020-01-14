package com.ekabao.oil.http;


import com.ekabao.oil.global.LocalApplication;

public class UrlConfig {
    //版本升级的时候
    public static final String version = LocalApplication.localVersion;
    //友盟埋点
    public static final int point = 1000000;

    /*
    * 富友支付需要传透的参数
    * */
    public static final String MCHNT_CD = "mchnt_cd";
    public static final String MCHNT_SDK_VERSION = "mchnt_SDK_version";
    public static final String MCHNT_SDK_TYPE = "mchnt_sdk_type";
    public static final String MCHNT_SDK_SIGNTP = "mchnt_sdk_signtp";
    public static final String MCHNT_SING_KEY = "mchnt_sing_key";
    public static final String MCHNT_ORDER_ID = "mchnt_order_id";
    public static final String MCHNT_USER_ID = "mchnt_user_id";
    public static final String MCHNT_AMT = "mchnt_amt";
    public static final String MCHNT_BANK_NUMBER = "mchnt_bank_number";
    public static final String MCHNT_BACK_URL = "mchnt_url";
    public static final String MCHNT_USER_NAME = "mchnt_user_name";
    public static final String MCHNT_USER_IDNU = "mchnt_user_idnu";
    public static final String MCHNT_USER_IDCARD_TYPE = "mchnt_user_idcard_type";

    //推送的appkey（正式）
//	public static final String PUSHAPPKEY = "";  //极光正式key
    public static final String PUSHAPPKEY = "5e0db1d1570df3bf9e00010e";  //友盟正式key

    //本地测试
    //=====================================================
//	public static final String HTTPTITLE = "http://192.168.1.115:8080/jsjf_app";//张文的地址
//	public static final String XIEYITITLE = "http://192.168.1.115:8080";//张文的地址

//  192.168.3.31:8888(啄米金服)
//  192.168.1.250:8888(米优金服)
//	61.153.6.26:8088(外网测试)
//外网测试服务器
//	public static final String HTTPTITLE="http://61.153.6.26:8088";
//	public static final String XIEYITITLE = "http://61.153.6.26:8088";

    //正式服务器  http://47.97.120.129:9999






    // TODO: 2019/2/27 m.ekabao.cn
    
    public static final String HTTPTITLE = "http://112.124.109.225:8080";
//    public static final String HTTPTITLE = "https://m.ekabao.cn";
//    public static final String HTTPTITLE = "http://47.111.112.73:8080";
    public static final String XIEYITITLE = "https://m.ekabao.cn";




    //测试服务器
    //public static final String HTTPTITLE = "http://m.shanyouxia.com";
   // public static final String HTTPTITLE = "http://192.168.2.21:8080"; //易卡宝 内网
    //public static final String HTTPTITLE="https://m.xhjlc.com";
   // public static final String HTTPTITLE = "http://192.168.2.90:8081"; //卢成文本地
  //  public static final String HTTPTITLE="http://101.37.64.90:8080";

  // public static final String HTTPTITLE = "http://192.168.2.69:8081"; //本地
    //public static final String XIEYITITLE = "https://www.xhjlc.com";


    public static final String APK = HTTPTITLE + "/apk/app-ymc";
    //http://m.middleman.cn apk升级地址

    //***********,*********************************
    //维护
    public static final String WEIHU = HTTPTITLE + "/template/pages/maintenance.html";
    //首页
    public static final String HOMEINFO = HTTPTITLE + "/index/index.dos";
    //最后一次登录
    public static final String POSTTIME = HTTPTITLE + "/login/lastLogin.dos";

    //我要出借页
    public static final String INVESTINFO = HTTPTITLE + "/product/list.dos";
    //已募集的
    public static final String PRODUCTLISTINFO = HTTPTITLE + "/product/productList.dos";
    //已募集 已还款数量
    public static final String RAISEDINFO = HTTPTITLE + "/product/raisedAndRepayedNum.dos";
    //我的账户
    public static final String PERSONINFO = HTTPTITLE + "/accountIndex/info.dos";
    //我的资产
    public static final String MYASSETSINFO = HTTPTITLE + "/accountIndex/myFunds.dos";
    //我的出借
    public static final String MYINVESTDAISINFO = HTTPTITLE + "/investCenter/productList.dos";
    //活动中心
    public static final String ACTIVITYCENTER = HTTPTITLE + "/activity/getMyPrizeRecords.dos";
    //回款记录
    public static final String BACKRECOND = HTTPTITLE + "/investCenter /repayInfoDetail.dos";
    //我的红包未使用
    public static final String CONPONSUNUSE = HTTPTITLE + "/activity/index.dos";
    //我的红包已使用
    public static final String CONPONSUSED = HTTPTITLE + "/investCenter/productList.dos";
    //我的红包已失效
    public static final String CONPONSUOFFTIME = HTTPTITLE + "/investCenter/productList.dos";
    //我的明细
    public static final String MYDETAIL = HTTPTITLE + "/assetRecord/index.dos";
    //资金记录
    //public static final String ASSETRECORD = HTTPTITLE + "/assetRecord/index.dos";
    //验证银行卡预留手机号
    public static final String BANKCARDMSG = HTTPTITLE + "/memberSetting/sendBankMsg.dos";
    //充值验证银行卡
//	public static final String CASHINMSG = HTTPTITLE + "/recharge/sendRechargeMsg.dos";
    public static final String CASHINMSG = HTTPTITLE + "/recharge/sendRechargeSms.dos";
    //发送富友充值验证码
    public static final String FYCASHINMSG = HTTPTITLE + "/recharge/sendFYRechargeMsg.dos";
    //API充值
    public static final String APIRECHGE = HTTPTITLE + "/recharge/apiRechge.dos";
    //银行卡详情
    public static final String BANKDETAIL = HTTPTITLE + "/memberSetting/myBankInfo.dos";
    //四要素认证
    public static final String FOURPART = HTTPTITLE + "/memberSetting/bankInfoVerify.dos";
    //银行限额
    public static final String XIANE = HTTPTITLE + "/CP080";
    //充值界面获取数据
    public static final String CASHINDETAIL = HTTPTITLE + "/recharge/index.dos";
    //中奖者信息
    public static final String WINNERINFO = HTTPTITLE + "/activity/getPrizeInfoByProductId.dos";
    //提现界面获取数据
    public static final String CASHOUTDETAIL = HTTPTITLE + "/withdrawals/index.dos";
    //充值创建订单
    public static final String CASHINNEXT = HTTPTITLE + "/recharge/createPayOrder.dos";
    //创建富友的充值订单
    public static final String CASHINNEXTFY = HTTPTITLE + "/recharge/wapRechge.dos";
    //充值
    public static final String CASHIN = HTTPTITLE + "/recharge/goPay.dos";
    //充值--富友网银
    public static final String FUCASHIN = HTTPTITLE + "/recharge/FYRecharge.dos";

    //通知
    public static final String NOTICE = HTTPTITLE + "/index/urgentNotice.dos";

    //发现页最新资讯
    public static final String DISCOVER_INFO = HTTPTITLE + "/activity/friendNewDynamic.dos";

    //公告
    public static final String NOTICES = HTTPTITLE + "/system/urgentNotice.dos";
    //提现
    public static final String CASHOUT = HTTPTITLE + "/withdrawals/addWithdrawals.dos";
    //更新版本2.0
    public static final String RENEWAL = HTTPTITLE + "/renewal.dos";

    //验证手机号是否存在
    public static final String EXISTMOBILEPHONE = HTTPTITLE + "/register/existMobilePhone.dos";
    // 注册验证码短信(老接口)
    public static final String SENDREGMSG = HTTPTITLE + "/register/sendRegMsg.dos";
    // 注册验证码短信(2.0)
    public static final String SENDREGMSGAPP = HTTPTITLE + "/register/sendAppRegMsg.dos";
    // 注册
    public static final String REGISTER_REG = HTTPTITLE + "/register/reg.dos";
    //发送登录验证码
    public static final String LOGINMSGSEND = HTTPTITLE + "/login/loginMsgSend.dos";
    // 登录
    public static final String DOLOGIN = HTTPTITLE + "/login/doLogin.dos";
    // 续投详情
    public static final String XUTOUDTAIL = HTTPTITLE + "/product/getContinueReward.dos";
    // 续投详情
    public static final String XUTOU = HTTPTITLE + "/product/addContinueReward.dos";
    // 产品详情
    public static final String PRODUCT_DETAIL = HTTPTITLE + "/product/detail.dos";
    // 标的更多详情
    public static final String PRODUCT_DETAIL_PART = HTTPTITLE + "/product/detailPart.dos";
    // 出借详情选择优惠券
    public static final String CONPONS_CHOSE = HTTPTITLE + "/activity/getUsableCoupon.dos";
    // 体验出借
    public static final String TIYAN_INVEST = HTTPTITLE + "/product/experienceInvest.dos";
    // 体验产品详情
    public static final String PRODUCT_DETAIL_TIYAN = HTTPTITLE + "/product/experienceDetail.dos";
    // 产品出借记录及产品图片
    public static final String DETAIL_INFO = HTTPTITLE + "/product/detail_info.dos";
    // 获取用户银行卡信息
    public static final String MEMBERSETTING = HTTPTITLE + "/memberSetting/index.dos";
    //发送重置登录验证码
    public static final String FORGETPWDSMSCODE = HTTPTITLE + "/memberSetting/forgetPwdSmsCode.dos";
    //判断短信验证码是否正确
    public static final String CHECKSMSCODE = HTTPTITLE + "/memberSetting/checkSmsCode.dos";
    //修改登录密码
    public static final String UPDATELOGINPASSWORD = HTTPTITLE + "/memberSetting/updateLoginPassWord.dos";
    //忘记登录密码
    public static final String FORGETPASSWORD = HTTPTITLE + "/memberSetting/forgetPassWord.dos";
    // 重置交易密码发送短信验证码
    public static final String SENDFORGETPWSCODE = HTTPTITLE + "/memberSetting/sendForgetTpwdCode.dos";
    // 设置交易密码
    public static final String UPDATETPWDBYSMS = HTTPTITLE + "/memberSetting/updateTpwdBySms.dos";
    // 修改交易密码
    public static final String UPDATETRADEPASSWORD = HTTPTITLE + "/memberSetting/updateTradePassWord.dos";
    //获取个人消息列表
    public static final String GETMESSAGE = HTTPTITLE + "/messageCenter/getMessage.dos";
    //消息标记为已读
    public static final String UPDATEUNREADMSG = HTTPTITLE + "/messageCenter/updateUnReadMsg.dos";
    //消息删除消息
    public static final String DELMSG = HTTPTITLE + "/messageCenter/delMsg.dos";
    //支付发送验证码接口
    public static final String SENDINVESTMSG = HTTPTITLE + "/product/sendFYInvestMsg.dos";
    //出借
    public static final String INVEST = HTTPTITLE + "/product/invest.dos";
    //我的邀请
    public static final String MYINVITATION = HTTPTITLE + "/activity/myInvitation.dos";
    //获取产品可用优惠券
    public static final String USABLE = HTTPTITLE + "/activity/usable.dos";
    //反馈
    public static final String FEEDBACK = HTTPTITLE + "/system/feedback.dos";
    //任务中心
    public static final String TASKCENTER = HTTPTITLE + "/memberJob/getMemberJobList.dos";
    //领取任务
    public static final String RECEIVETASK = HTTPTITLE + "/memberJob/jobCouponReceive.dos";
    //上传头像
    public static final String UPLOADAVATAR = HTTPTITLE + "/memberSetting/imgUpload.dos";
    //所有的收货地址
    // public static final String RECEIPTADDRESS = HTTPTITLE + "/member/getReceiptAddress.dos";
    public static final String RECEIPTADDRESS = HTTPTITLE + "/member/memberAddressList.dos";


    //设置默认收货地址
    //public static final String RECEIPTADDRESSDEFAULT = HTTPTITLE + "/member/updateReceiptAddressDefault.dos";
    public static final String RECEIPTADDRESSDEFAULT = HTTPTITLE + "/member/setDefaultReceiptAddress.dos";

    //修改收货地址(之前接口有)
    //public static final String UPDATERECEIPTADDRESS = HTTPTITLE + "/member/updateReceiptAddress.dos";
    //新增收货地址
    public static final String INSERTRECEIPTADDRESS = HTTPTITLE + "/member/insertReceiptAddress.dos";
    //删除收货地址
    //public static final String DELETERECEIPTADDRESS = HTTPTITLE + "/member/deleteReceiptAddress.dos";
    public static final String DELETERECEIPTADDRESS = HTTPTITLE + "/member/deleteMemberAddr.dos";

    //获取收货地址详情
    public static final String GETRECEIPTADDRESS = HTTPTITLE + "/member/getReceiptAddress.dos";
    //添加收货地址
    public static final String INSERTRECERPTADDRESS = HTTPTITLE + "/member/insertReceiptAddress.dos";
    //修改收货地址
    public static final String UPDATERECEIPTADDRESS = HTTPTITLE + "/member/updateReceiptAddress.dos";

    //资金记录
    public static final String ASSETRECORD = HTTPTITLE + "/assetRecord/index.dos";
    //资产分析
    public static final String ACCOUNTINDEX = HTTPTITLE + "/accountIndex/myFunds.dos";
    //常见问题
    public static final String COMMONQUESTION = HTTPTITLE + "/product/commonQuestion.dos";
    //我的积分记录
    public static final String MYPOINTSRECORD = HTTPTITLE + "/activity/myPointsRecord.dos";
    //积分排行榜
    public static final String POINTSRANK = HTTPTITLE + "/activity/pointsRank.dos";
    //第三重礼
    public static final String GETRANKINGLIST = HTTPTITLE + "/activity/getRankingList.dos";
    //出借详情
    public static final String INVESTDETAIL = HTTPTITLE + "/investCenter/productInvest/detail.dos";
    //客服电话
    public static final String GETPLATFORMINFO = HTTPTITLE + "/index/getPlatFormInfo.dos";

    //Apk
    public static final String APKDOWNLOAD = APK + ".apk";
    //阿里分发 1
    public static final String APKDOWNLOAD1 = APK + "_1.apk";
    //360平台Apk 2
    public static final String APKDOWNLOAD360 = APK + "_2.apk";
    //小米Apk 3
    public static final String APKDOWNLOADXIAOMI = APK + "_3.apk";
    //华为Apk 4
    public static final String APKDOWNLOADHUAWEI = APK + "_4.apk";
    //OPPO平台Apk 5
    public static final String APKDOWNLOADOPPO = APK + "_5.apk";
    //VIVO平台Apk 6
    public static final String APKDOWNLOADVIVO = APK + "_6.apk";
    //腾讯应用宝 应用宝Apk 7
    public static final String APKDOWNLOADYINGYONGBAO = APK + "_7.apk";
    //联想乐商店 lenovo平台 Apk 8
    public static final String APKDOWNLOADLENOVO = APK + "_8.apk";
    //安智平台Apk
    //public static final String APKDOWNLOADANZHI = "https://www.xhjlc.com/apk/anzhi/zhc_app_anzhi.apk";
    public static final String APKDOWNLOADANZHI = APK + "_9.apk";
    //木蚂蚁平台Apk
    public static final String APKDOWNLOADMUMAYI = APK + "_10.apk";
    //应用汇 11
    public static final String APKDOWNLOAD11 = APK + "_11.apk";
    //魅族平台Apk
    public static final String APKDOWNLOADMEIZU = APK + "_12.apk";
    //乐视平台Apk
    public static final String APKDOWNLOADLETV = APK + "_13.apk";
    //锤子平台Apk
    public static final String APKDOWNLOADCHUIZI = APK + "_14.apk";
    //金立 15
    public static final String APKDOWNLOAD15 = APK + "_15.apk";
    //搜狗平台Apk
    public static final String APKDOWNLOADSOUGOU = APK + "_16.apk";
    //酷市场 17
    public static final String APKDOWNLOAD17 = APK + "_17.apk";
    //samsung平台Apk
    public static final String APKDOWNLOADSANXING = APK + "_18.apk";
    //百度平台APK
    public static final String APKDOWNLOADBAIDU = APK + "_19.apk";
    //feipao1平台Apk
    public static final String APKDOWNLOADFEIPAO = APK + "_20.apk";
    //今日头条Apk
    public static final String toutiao = APK + "_21.apk";

    //小熊油耗Apk
    public static final String xxyh = APK + "_22.apk";



    //PP平台Apk
    public static final String APKDOWNLOADPP = APK + "/zhc_app_pp.apk";
    //feipao2平台Apk
    public static final String APKDOWNLOADFEIPAO2 = APK + "/zhc_app_feipao2.apk";
    //dandanzhuan平台Apk
    public static final String APKDOWNLOADDANDANZHUAN = APK + "/zhc_app_dandanzhuan.apk";
    //tangzhuan平台Apk
    public static final String APKDOWNLOADTANGZHUAN = APK + "/zhc_app_tangzhuan.apk";
    //shihai平台的APK
    public static final String APKDOWNLOADSHIHAI = APK + "/zhc_app_shihai.apk";


    //产品说明
    public static final String PRODUCT_SEE = HTTPTITLE + "/aqbzDetail";

    //活动图片
    public static final String INVEST_ACTIVITY = XIEYITITLE + "/images/app/invest_activity.png";
    //转让协议
    public static final String ZHUANRANG = XIEYITITLE + "/mytransfer";
    //借款协议
    //public static final String JIEKUAN = XIEYITITLE + "/loan";
    public static final String JIEKUAN = XIEYITITLE + "/agreement";
    //认证支付协议
    public static final String ZHIFU = XIEYITITLE + "/pay";
    //出借协议
    public static final String TOUZI = XIEYITITLE + "/qy";
    //委托协议
    public static final String WEITUO = XIEYITITLE + "/entrust";
    //注册协议
    //public static final String ZHUCE = XIEYITITLE + "/zc";
    //public static final String ZHUCE = XIEYITITLE + "/useragreement";
    public static final String ZHUCE = XIEYITITLE + "/useragreement";

    //新手标详情
    public static final String NEWHAND = HTTPTITLE + "/XSXQ";
    //更多安全保障
    public static final String MOREANQUAN = HTTPTITLE + "/aqbz";
    //标的安全保障
    public static final String BIAOANQUAN = HTTPTITLE + "/newhand?pid=1153";
    //一亿验资
    public static final String YANZI = HTTPTITLE + "/YYYZ";
    //走进米优金服
    public static final String GONGSI = HTTPTITLE + "/companyIntroduction";
    //股东介绍
    public static final String GDJS = HTTPTITLE + "/GDJS";
    //团队管理
    public static final String GLTD = HTTPTITLE + "/GLTD";
    //公司资质
    public static final String GSZZ = HTTPTITLE + "/companyDocument";
    //一亿验资
    public static final String YYYZ = HTTPTITLE + "/YYYZ";
    //网站公告
    public static final String WEBSITEAN = HTTPTITLE + "/noticeDetail";
    //https://192.168.1.250:8888/noticeDetail?id=2

    //媒体报道
    public static final String MEITI = HTTPTITLE + "/report";
    //邀请注册
    public static final String YAOZHUCE = HTTPTITLE + "/newcomer?wap=true&toFrom=zjdfxfx";
    //邀请注册2
    public static final String YAOZHUCE2 = HTTPTITLE + "/friendreg";
    //邀请期数
    public static final String YAOQINGURL = HTTPTITLE + "/inviteFriend";
    //邀请注册正常
    // public static final String ZHUCEZHENGCHANG = HTTPTITLE + "/invite";
    public static final String ZHUCEZHENGCHANG = HTTPTITLE + "/signIn";

    //邀请奖励领取
    public static final String LINQU = HTTPTITLE + "/assetRecord/getTheRewards.dos";
    //首投，复投列表
    public static final String FIRSTINVESTLIST = HTTPTITLE + "/activity/firstInvestList.dos";
    //邀请图片
    public static final String TUPIAN = XIEYITITLE + "/images/applogo.png";

    //砸金蛋
    public static final String ZADAN = HTTPTITLE + "/activity/getRandomCouponByProductId.dos";
    //活动列表
    public static final String ACTIVITYLIST = HTTPTITLE + "/activity/getAllActivity.dos";
    //預約
    public static final String GET_RESERVATON = HTTPTITLE + "/product/getReservation.dos";
    //网站公告
    public static final String WEB_AN = HTTPTITLE + "/aboutus/newsInformationList.dos";
    //添加预约
    public static final String INSERT_PRIZEINFO = HTTPTITLE + "/activity/insertPrizeInfo.dos";
    //查询产品绑定的奖品奖品
    public static final String SELECTPRODUCTPRIZE = HTTPTITLE + "/activity/selectProductPrize.dos";

    //领取压岁钱（分享）
    public static final String GETLUCKYMONEY = HTTPTITLE + "/activity/getLuckyMoney.dos";
    //领取压岁钱（分享）
    public static final String FANPAIAGIN = HTTPTITLE + "/activity/flopShare.dos";
    //yearend活动二分享
    public static final String NEWYEARSHARE = HTTPTITLE + "/newyearshare";
    //累计收益
    public static final String LEIJISHOUYI = HTTPTITLE + "/assetRecord/getAccumulatedIncome.dos";
    //奖品详情
    public static final String PRIZEINFO = HTTPTITLE + "/investCenter/prizeInfo.dos";
    //
    public static final String GETPICCODE = HTTPTITLE + "/login/validateCode.dos";
    //银行限额
    public static final String BANKNAMELIST = HTTPTITLE + "/recharge/getBankQuotaList.dos";
    //城市列表
    public static final String CITYNAMELIST = HTTPTITLE + "/recharge/getCityList.dos";
    //获取jpush tag
    public static final String GETJPUSHTAG = HTTPTITLE + "/application/selectPushAudience.dos";
    //注册推送 PushRegistrationId
    public static final String PUSHREGISTRATIONID = HTTPTITLE + "/application/setPushRegistrationId.dos";
    //启动页广告
    public static final String STARTADVERTISEMENT = HTTPTITLE + "/index/startAdvertisement.dos";
    //开放日
    public static final String ACTLIST = HTTPTITLE + "/openday";
    //开放日详情
    public static final String GGXQ = HTTPTITLE + "/GGXQ";
    //优惠券的说明
    public static final String REMINDER = HTTPTITLE + "/reminder";
    //体验金的问号说明
    public static final String EXPERIENCEGOLD = HTTPTITLE + "/noticeDetail?id=2";

    //翻牌活动
    public static final String FAIPAI = HTTPTITLE + "/app2lottery?wap=true";
    //iphone7活动
    public static final String IPHONE7 = HTTPTITLE + "/special?upgrade=1&wap=true";
    //线下活动跳转url
    public static final String OFFLINEURL = HTTPTITLE + "/publicWelfare";

    //项目详情-->安全保障
    //public static final String SAFE = HTTPTITLE + "/safe?app=true";
    public static final String SAFE = HTTPTITLE + "/companyIntroduction?app=true";

    /**
     * 油卡
     */
    //1. 套餐列表  http://domain//product/getPorductList.dos
    public static final String SetMeal = HTTPTITLE + "/product/getPorductList.dos";
    //2.绑定油卡
    public static final String bindFuelCard = HTTPTITLE + "/member/bindFuelCard.dos";
    //3.我的油卡
    public static final String myFuelCard = HTTPTITLE + "/member/myFuelCard.dos";
    //4.油卡详情
    public static final String fuelCardDetail = HTTPTITLE + "/member/fuelCardDetail.dos";
    //6.我的订单
    public static final String myOrders = HTTPTITLE + "/member/myOrders.dos";
    //7.我的订单
    public static final String orderDetail = HTTPTITLE + "/member/orderDetail.dos";
    //7.删除油卡
    public static final String deleteFuelCard = HTTPTITLE + "/member/deleteFuelCard.dos";
    //8.下单
    public static final String rechargeFuelCardToPay = HTTPTITLE + "/member/rechargeFuelCardToPay.dos";

    //服务协议
    public static final String FW = XIEYITITLE + "/useragreement";

    //分享logo https://m.yinongfin.com/logo-new.jpg
    public static final String fenxinaglogo = HTTPTITLE + "/upload/logo-new.jpg";
    //14.查询手机账户信息
    public static final String queryPhone = HTTPTITLE + "/member/getPhoneInfo.dos";

    //查询领取油卡的
    public static final String canBuyFuelCard = HTTPTITLE + "/member/canBuyFuelCard.dos";

    //浙商银行资金监管协议
    public static final String supervise = XIEYITITLE + "/supervise";

    //邀请好友排行榜
    public static final String friendRankingList = HTTPTITLE + "/activity/friendRankingList.dos";

    /**
     * 商城的
     */
    //首页的 下面的商城
    public static final String hotList = HTTPTITLE + "/shop/hotList.dos";

    //易商城
    public static final String shopIndex = HTTPTITLE + "/shop/index.dos";
    //2.类别查询排序页
    public static final String shopCategory = HTTPTITLE + "/shop/category.dos";
    //3.商品详情
    public static final String shopDetail = HTTPTITLE + "/shop/detail.dos";
    //4.下单
    public static final String shoptopay = HTTPTITLE + "/shop/topay.dos";
    //5.订单列表
    public static final String shoporderList = HTTPTITLE + "/shop/orderList.dos";

    //6.订单详情（订单列表）
    public static final String shoporderDetail = HTTPTITLE + "/shop/orderDetail.dos";
    //7.订单详情修改
    public static final String shoporderupdate = HTTPTITLE + "/shop/orderupdate.dos";


    //定位
    public static final String oilLocation = HTTPTITLE + "/member/getOilLocal.dos";
    //违章车辆列表
    public static final String carBreak = HTTPTITLE + "/member/selectMemberCarList.dos";
    //根据车牌查询城市参数
    public static final String cityTypeByCar = HTTPTITLE + "/member/selectCarPre.dos";
    //添加车辆
    public static final String addCar = HTTPTITLE + "/member/bindMemberCar.dos";
    //编辑车辆
    public static final String carEdit = HTTPTITLE + "/member/editMemberCar.dos";
    //删除车辆
    public static final String carDelete = HTTPTITLE + "/member/deleteMemberCar.dos";
    //今日油价
    public static final String todayOilPrice = HTTPTITLE + "/member/getOilCity.dos";

    public static final String welfareBanner = HTTPTITLE + "/index/welfareBanner.dos";

    //--》首頁弹框

    public static final String startPopup = HTTPTITLE + "/index/startPopup.dos";


    //26.待支付-支付
    public static final String rechargeToPay = HTTPTITLE + "/member/rechargeToPay.dos";

    //27.余额支付
    public static final String rechargeByFunds = HTTPTITLE + "/member/rechargeByFunds.dos";

    //28.余额查询
    public static final String myFunds = HTTPTITLE + "/member/myFunds.dos";
    //29.余额明细
    public static final String myFundsList = HTTPTITLE + "/member/myFundsList.dos";

    //30.取消订单
    public static final String cancelInvest = HTTPTITLE + "/member/cancelInvest.dos";
    //31.删除
    public static final String deleteInvest = HTTPTITLE + "/member/deleteInvest.dos";

    //1.套餐退订
    public static final String refund = HTTPTITLE + "/product/refund.dos";
    //32.退订金额-`
    public static final String refundMoney = HTTPTITLE + "/product/refundMoney.dos";
    //33.余额支付-商城
    public static final String shopRechargeByFunds = HTTPTITLE + "/shop/shopRechargeByFunds.dos";

    //34.领取油卡下单
    public static final String getOilCard = HTTPTITLE + "/member/getOilCard.dos";

    //35 预览套餐订单金额
    public static final String previewPackageProduct = HTTPTITLE + "/product/previewPackageProduct.dos";
}
