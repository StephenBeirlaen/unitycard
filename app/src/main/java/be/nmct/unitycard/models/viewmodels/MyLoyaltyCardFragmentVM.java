package be.nmct.unitycard.models.viewmodels;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import be.nmct.unitycard.R;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.contracts.LoyaltyCardContract;
import be.nmct.unitycard.databinding.FragmentMyLoyaltyCardBinding;
import be.nmct.unitycard.helpers.DatabaseHelper;
import be.nmct.unitycard.models.LoyaltyCard;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static be.nmct.unitycard.contracts.ContentProviderContract.LOYALTYCARDS_URI;

/**
 * Created by Stephen on 9/11/2016.
 */

public class MyLoyaltyCardFragmentVM extends BaseObservable {

    private FragmentMyLoyaltyCardBinding mBinding;
    private Context mContext;

    public MyLoyaltyCardFragmentVM(FragmentMyLoyaltyCardBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }

    // http://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code
    private Bitmap encodeAsBitmap(String content, BarcodeFormat format, int width, int height) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(content, format, width, height, null);
        } catch (IllegalArgumentException e) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        return bitmap;
    }

    public void loadQRcode() {
        String[] columns = new String[] {
                DatabaseContract.LoyaltyCardColumns.COLUMN_SERVER_ID,
                DatabaseContract.LoyaltyCardColumns.COLUMN_USER_ID,
                DatabaseContract.LoyaltyCardColumns.COLUMN_CREATED_TIMESTAMP,
                DatabaseContract.LoyaltyCardColumns.COLUMN_UPDATED_TIMESTAMP
        };

        Cursor data = mContext.getContentResolver().query(LOYALTYCARDS_URI, columns, null, null, null);

        if (data.moveToNext()) {
            LoyaltyCard loyaltyCard = null;
            try {
                loyaltyCard = new LoyaltyCard(
                        data.getInt(data.getColumnIndex(DatabaseContract.LoyaltyCardColumns.COLUMN_SERVER_ID)),
                        data.getString(data.getColumnIndex(DatabaseContract.LoyaltyCardColumns.COLUMN_USER_ID)),
                        DatabaseHelper.convertStringToDate(data.getString(data.getColumnIndex(DatabaseContract.LoyaltyCardColumns.COLUMN_CREATED_TIMESTAMP))),
                        DatabaseHelper.convertStringToDate(data.getString(data.getColumnIndex(DatabaseContract.LoyaltyCardColumns.COLUMN_UPDATED_TIMESTAMP)))
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                int size = (int)mContext.getResources().getDimension(R.dimen.qr_code_size);
                // Genereer een QR code
                Bitmap bm = encodeAsBitmap(
                        LoyaltyCardContract.getQRcodeContent(loyaltyCard.getId()),
                        BarcodeFormat.QR_CODE,
                        size,
                        size
                );

                if (bm != null) {
                    mBinding.imageViewQR.setImageBitmap(bm);
                }
            } catch (WriterException e) {
                // todo
            }
        }
    }
}
