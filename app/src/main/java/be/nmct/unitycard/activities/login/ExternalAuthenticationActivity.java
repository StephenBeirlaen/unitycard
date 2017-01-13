package be.nmct.unitycard.activities.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import be.nmct.unitycard.R;

import static be.nmct.unitycard.activities.login.AccountActivity.EXTERNAL_AUTHENTICATION_PROVIDER;

public class ExternalAuthenticationActivity extends AppCompatActivity {

    private final String LOG_TAG = this.getClass().getSimpleName();
    public static final String EXTERNAL_AUTHENTICATION_RESPONSE_URL = "be.nmct.unitycard.activities.login.ExternalAuthResponse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }

        Bundle extras = intent.getExtras();
        if (extras == null) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }

        String provider = extras.getString(EXTERNAL_AUTHENTICATION_PROVIDER);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("");
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        final WebView webview = new WebView(this);
        webview.clearCache(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        /*CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(false);*/

        WebSettings ws = webview.getSettings();
        ws.setSaveFormData(false);
        ws.setSavePassword(false);
        ws.setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (!progressDialog.isShowing())
                {
                    progressDialog.show();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(ExternalAuthenticationActivity.this, "Webview error! " + description, Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_CANCELED);
                finish();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (progressDialog != null && progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }

                if (url.contains("/api/Account/ExternalLoginSuccess")) {
                    //Log.d(LOG_TAG, "Social login flow: external user authenticated");

                    Intent intent = new Intent();
                    intent.putExtra(EXTERNAL_AUTHENTICATION_RESPONSE_URL, url);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        setContentView(webview);

        setTitle(getString(R.string.log_in_with_external_auth_provider) + provider);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        webview.loadUrl("https://unitycard.azurewebsites.net/api/Account/ExternalLogin?provider=" + provider + "&response_type=token&client_id=nativeApp&redirect_uri=https://unitycard.azurewebsites.net/api/Account/ExternalLoginSuccess");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // handle back arrow click
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
