package be.nmct.unitycard.contracts;

/**
 * Created by Stephen on 6/11/2016.
 */

public class LoyaltyCardContract {
    public static final String QR_CODE_CONTENT_PREFIX = "be.nmct.unitycard.loyaltycard.";
    public static String getQRcodeContent(int id) {
        return QR_CODE_CONTENT_PREFIX + id;
    }
}
