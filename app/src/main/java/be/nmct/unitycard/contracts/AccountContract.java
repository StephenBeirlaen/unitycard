package be.nmct.unitycard.contracts;

/**
 * Created by Stephen on 28/10/2016.
 */

public class AccountContract {
    public static final String ACCOUNT_TYPE = "be.nmct.unitycard.account";

    public static final String TOKEN_ACCESS = "be.nmct.unitycard.account.accesstoken";
    public static final String TOKEN_REFRESH = "be.nmct.unitycard.account.refreshtoken";
    public static final String TOKEN_ACCESS_LABEL = "OAuth 2.0 Access Token";
    public static final String TOKEN_REFRESH_LABEL = "OAuth 2.0 Refresh Token";

    public static final String KEY_USER_ID = "be.nmct.unitycard.account.userid";
    public static final String KEY_USER_ROLE = "be.nmct.unitycard.account.userrole";

    public static final String ROLE_CUSTOMER = "be.nmct.unitycard.account.role.customer";
    public static final String ROLE_RETAILER = "be.nmct.unitycard.account.role.retailer";

    // Alle synchronisatie timestamps voor de tabellen:
    public static final String KEY_LAST_SYNC_TIMESTAMP_RETAILERS = "be.nmct.unitycard.account.last_sync_timestamp.retailers";
    public static final String KEY_LAST_SYNC_TIMESTAMP_LOYALTY_CARD = "be.nmct.unitycard.account.last_sync_timestamp.loyaltycard";
}
