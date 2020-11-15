package com.yq.tjnetwork;

/**
 * Created by YangQiang on 2016/3/21.
 */
public class Const {

    public static final int IMAGE_PROGRESS = 103;
    public static final int NETWORK_FAILURE = 104;
    public static final int PARSER_FAILURE = 105;
    public static final int PARSER_SUCCESS = 106;
    public static final int VERIFY_ERROR = 107;

    public static final int NETWORK_TIMEOUT = -3; // 网络连接超时

    public static final int REFRESH = 1; // 首先尝试读取本地数据，然后联网
    public static final int FORCE_REFRESH = 2; // 不考虑本地数据，联网取得数据
    public static final int MORE = 3; // 不考虑本地数据，联网取得更多数据
    public static final int GET_CACHE_FILE = 4; // 读取本地数据，没有数据才联网
    public static final int FORCE_REFRESH_MALL_POI = 5;
    public static final int FORCE_REFRESH_MALL = 6;
    public static final int FORCE_FINISH_MALL = 7;
    public static final int REFRESH_ACCESS_TOKEN = 14; //
    public static final int REFRESH_REFRESH_TOKEN = 15; //
    public static final int REFRESH_ACCESS_TOKEN_SUCCESS = 16; //

    public static String SERVER_URL = "http://140.205.155.250/real";

    //通用参数
    public static final String AUTONAVI_KEY_VERSION = "version";
    public static final String AUTONAVI_KEY_VERIFY = "verify";
    public static final String AUTONAVI_KEY_IMEI = "imei";
    public static final String AUTONAVI_KEY_SIGN = "sign";
    public static final String AUTONAVI_KEY_RT = "rt";
    public static final String POI_KEY_PHONE = "phone";
    public static final String AUTONAVI_KEY_FROM = "from";
    public static final String LOGIN_NAME = "login_name"; // 如:手机号/昵称/邮箱/用户名
    public static final String LOGIN_STYLE = "login_style"; // 0 淘宝账号 1 高德账号
    public static final String LOGIN_TAO_ID = "tbid"; // 淘宝登陆userid

    public static final int USER_UID_NULL_ERROR = 4010;

    public class ModelTypeDefine {
        public static final String GET_METHOD = "GET";
        public static final String POST_METHOD = "POST";

        public static final int IMAGE_MODEL_MANAGER = 1001;
        public static final int JP_FOUND_CATEGROY_IMAGE_MODEL = 1012;

        public static final int AUTONAVI_POI_ROAD_SEARCH_MODEL = 8001; // 获取周边POI
    }
}
