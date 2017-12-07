package it.lucavallerini.kenamobile;

final class Constants {

    /**
     * Account type string.
     */
    static final String ACCOUNT_TYPE = "it.lucavallerini.kenamobile";
    /**
     * Authtoken type string.
     */
    static final String AUTHTOKEN_TYPE = "it.lucavallerini.kenamobile";

    /**
     * Site URIs.
     */
    static final String COOKIE_URI = "https://www.kenamobile.it";
    static final String MYKENA_URI = "https://www.kenamobile.it/wp-admin/admin-ajax.php";
    static final String RECHARGE_URI = "https://www.kenamobile.it/ricarica";

    /**
     * Connections parameters keys.
     */
    static final String ACTION = "action";
    static final String MAYA_ACTION = "maya_action";
    static final String MSISDN = "msisdn";
    static final String USER = "user";
    static final String PASSWORD = "userPassword";
    static final String TRAFFIC_DATE_FROM = "dateFrom";
    static final String TRAFFIC_DATE_TO = "dateTo";
    static final String TRAFFIC_VOICE = "service_VOCE";
    static final String TRAFFIC_SMS = "service_SMS";
    static final String TRAFFIC_MMS = "service_MMS";
    static final String TRAFFIC_DATA = "service_DATI";

    /**
     * Connections parameters values.
     */
    static final String MAYA_ACTION_CREDIT = "getCreditInfo";
    static final String MAYA_ACTION_PROMO = "getUserPromoBags";
    static final String MAYA_ACTION_LOGIN = "ldapLogin";
    static final String MAYA_INTERROGATE = "maya_interrogate";
    static final String MAYA_ACTION_LOGOUT = "logout";
    static final String MAYA_ACTION_SERVICES = "getServices";
    static final String MAYA_ACTION_CUSTOMER = "getCustomerByMsisdn";
    static final String MAYA_ACTION_INFO_SIM = "getInfoSimByMSISDN";
    static final String MAYA_ACTION_CHECK_LOGIN = "getLogin";
    static final String MAYA_ACTION_TRAFFIC = "getTraffic";

    /**
     * Shared preferences file keys.
     */
    static final String PREF_FILE_LOGIN_NAME = "prefLoggedInUser";
    static final String PREF_LOGIN_USER_NAME = "userName";


    private Constants() {
        throw new AssertionError();
    }
}
