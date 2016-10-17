package be.nmct.unitycard.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import be.nmct.unitycard.R;
import be.nmct.unitycard.fragments.LoginFragment;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity
        implements
        LoginFragment.LoginFragmentListener {

    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            // add initial fragment
            showFragmentLogin();
        }
    }

    private void showFragmentLogin() {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, LoginFragment.newInstance(), LoginFragment.class.getSimpleName()) // replace = remove + add
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() == 0) {
            setResult(RESULT_CANCELED);
            finish(); // teruggeven aan MainActivity
        }
        else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void btnRegisterClicked() {
        // todo: btnRegisterClicked
    }

    @Override
    public void onLoginSuccessful() {
        setResult(RESULT_OK);
        finish();
        // todo: review
    }

    @Override
    public void handleError(String error) {
        // todo: handleerror
    }
}
