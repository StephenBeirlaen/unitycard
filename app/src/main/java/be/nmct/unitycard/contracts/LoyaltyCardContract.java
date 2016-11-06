package be.nmct.unitycard.contracts;

/**
 * Created by Stephen on 6/11/2016.
 */

public class LoyaltyCardContract {
    public static String getQRcodeContent(int id) {
        return "be.nmct.unitycard.loyaltycard." + id;
    }
}
