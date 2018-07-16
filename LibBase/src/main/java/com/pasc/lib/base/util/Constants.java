package com.pasc.lib.base.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量类
 */

public class Constants {
    public static final String CITY = "南通";

    public static boolean IS_DEBUG = false;

    //    public static final String[] PROVINCES = new String[]{"京", "冀", "蒙", "辽", "吉", "黑", "甘", "陕", "宁", "晋", "新", "青",
    //            "鲁", "鄂", "湘", "赣", "豫", "皖", "闽", "浙", "沪", "苏", "藏", "贵", "渝", "云", "川", "粤", "琼"};

    public static class BundleKey {

        /**
         * 违章订单类型
         */
        public static final String ORDER_STATUS = "order_status";
        /**
         * 违章订单ID
         */
        public static final String ORDER_ID = "order_id";
        /**
         * 违章订单业务订单号
         */
        public static final String ORDER_BUSINESS_NO = "order_business_no";
        /**
         * 车牌号
         */
        public static final String CAR_NO = "carNo";
        /**
         * 车架号
         */
        public static final String FRAME_NO = "frameNo";
        /**
         * 发动机号
         */
        public static final String ENGINE_NO = "engineNo";
        /**
         * 车主电话
         */
        public static final String CAR_OWNER_PHONE = "carOwnerPhone";
        /**
         * 车主姓名
         */
        public static final String CAR_OWNER_NAME = "carOwnerName";
        /**
         * 车主身份证
         */
        public static final String CAR_OWNER_ID_CARD = "carOwnerIdCard";

        public static final String PAY_URL = "pay_url";
        /**
         * 是否是已失效列表进入
         */
        public static final String IS_FROM_INVALID = "is_from_invalid";
        /**
         * 是否是待付款列表进入
         */
        public static final String IS_FROM_READY = "is_from_ready";

        /**
         * 列表进入分类
         */
        public static final String ITEM_TYPE = "item_type";

        /**
         * 问题Id
         */
        public static final String QUESTION_ID = "question_id";
        /**
         * 问题Title
         */
        public static final String QUESTION_TITLE = "question_title";

        public static final String TRAFFIC_VIOLATION_TYPE = "traffic_violation_type";
    }

    public static class PreferenceKey {
        /**
         * 记录退出注册页面时，获取验证码的倒计时时间的Key
         */
        public static final String REGISTER_COUNT_DOWN_TIME = "register_count_down_time";

        /**
         * 记录退出注册页面时的系统时间戳的Key
         */
        public static final String REGISTER_EXIT_TIME = "register_exit_time";

        /**
         * 记录退出注册页面时，用于获取验证码的手机号的Key
         */
        public static final String REGISTER_EXIT_PHONE = "register_exit_phone";
    }

    public static class OrderStatus {

        /**
         * 所有
         */
        public static final String SELF_ALL = "0";
        /**
         * 进行中
         */
        public static final String SELF_ONGOING = "1";
        /**
         * 已完成
         */
        public static final String SELF_FINISHED = "2";
        /**
         * 已失效
         */
        public static final String SELF_INVALID = "3";
        /**
         * 第三方支付 -> 待付款
         */
        public static final String THIRD_WAITING_FOR_PAYED = "1";
        /**
         * 第三方支付 -> 已删除
         */
        public static final String THIRD_DELETED = "2";
        /**
         * 第三方支付 -> 已付款/正在办理
         */
        public static final String THIRD_PAYED = "3";
        /**
         * 第三方支付 -> 已退款
         */
        public static final String THIRD_REFUND = "4";
        /**
         * 第三方支付 -> 已完成
         */
        public static final String THIRD_FINISHED = "5";

        public static String valueOfSelf(String type) {
            switch (type) {
                case SELF_ONGOING:
                    return "进行中";
                case SELF_FINISHED:
                    return "已完成";
                case SELF_INVALID:
                    return "已失效";
                default:
                    return "";
            }
        }

        public static String valueOfThird(String type) {
            switch (type) {
                case THIRD_WAITING_FOR_PAYED:
                    return "待付款";
                case THIRD_DELETED:
                    return "已删除";
                case THIRD_PAYED:
                    return "已付款";
                case THIRD_REFUND:
                    return "已退款";
                case THIRD_FINISHED:
                    return "已完成";
                default:
                    return "";
            }
        }
    }

    public static class AttrType {

        public static final String PHOTO = "0";
        public static final String VIDEO = "1";
        public static final String AUDIO = "2";
        public static final String OTHER = "3";
    }

    /**
     * smt目录
     */
    public static final String APP_FOLDER_PATH = "nt/";
    /**
     * smt目录
     */
    public static final String APP_IMG_FOLDER_PATH = "img/";

    /**
     * bugly appiId
     */
    public static final String BUGLY_APPID = "b0ae1485d5";

    /**
     * 登录时rxbus传递的统一参数code
     */
    public static final int LOGIN_REQUEST_CODE = 10;

    public static final int LOGOUT_REQUEST_CODE = 11;

    /**
     * 分页大小
     */
    public static final int PAGE_SIZE = 10;

    /**
     * 是否处于编辑状态
     */
    public static boolean sIsEdit = false;

    /**
     * 是否是来自违章历史页面
     */
    public static boolean sIsFromHistory = false;
    /**
     * 车牌号格式：汉字 + A-Z + 5-9位A-Z或0-9
     */
    public static String carnumRegex = "[A-Z]{1}[A-Z_0-9]{5,9}";

    public static final List<String> PROVINCES = new ArrayList<>();

    /**
     * 更多服务
     * 1-生活缴费,2-健康服务,3-交通出行,4-民生服务,5-社会保障,6-住房保障,7-就业服务,8-文体教育
     */
    public static final String SERVICE_TYPE = "service_type";
    public static final int LIVING_PAYMENT = 1;
    public static final int GOVERNMENT_AFFAIRS = 2;
    public static final int HEALTH_SERVICE = 3;
    public static final int TRANSPORTATION = 4;
    public static final int PEOPLE_SERVICE = 5;
    public static final int SOCIAL_INSURANCE = 6;
    public static final int HOUSING_SECURITY = 7;
    public static final int JOB_SERVICE = 8;
    public static final int LITERARY_FORM = 9;
}
