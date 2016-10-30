package be.nmct.unitycard.fragments;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import be.nmct.unitycard.R;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.AccountContract;
import be.nmct.unitycard.helpers.ConnectionChecker;
import butterknife.Bind;
import butterknife.ButterKnife;

import static be.nmct.unitycard.activities.AccountActivity.REQUEST_PERMISSION_GET_ACCOUNTS;

public class LoginFragment extends Fragment {

    @Bind(R.id.btn_login) Button btnLogin;
    @Bind(R.id.btn_register) Button btnRegister;
    @Bind(R.id.txt_username) EditText txtUsername;
    @Bind(R.id.txt_password) EditText txtPassword;
    @Bind(R.id.progress_bar) ProgressBar progressBar;

    private final String LOG_TAG = this.getClass().getSimpleName();
    private LoginFragmentListener mListener;
    private AccountManager mAccountManager; // todo: nog nodig straks als alles in helper zit?
    public static final String STATE_TXT_USERNAME = "be.nmct.unitycard.state_login_txt_username";
    public static final String STATE_TXT_PASSWORD = "be.nmct.unitycard.state_login_txt_password";

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        mAccountManager = AccountManager.get(getActivity());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString(); // input ophalen
                String password = txtPassword.getText().toString();

                if (username.length() == 0) { // een basic client side validatie
                    mListener.handleError("Username is empty");
                }
                else if (password.length() == 0) {
                    mListener.handleError("Password is empty");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);

                    signIn("stephenbeirlaen@gmail.com");
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.btnRegisterClicked();
            }
        });

        if (savedInstanceState != null) {
            String username = savedInstanceState.getString(STATE_TXT_USERNAME);
            String password = savedInstanceState.getString(STATE_TXT_PASSWORD);

            this.txtUsername.setText(username);
            this.txtPassword.setText(password);
        }

        return view;
    }

    private void signIn(String userNameString) {
        if (ConnectionChecker.isInternetAvailable(getActivity())) { // check of er verbinding is
            Account[] accountsByType = AuthHelper.getStoredAccountsByType(getActivity()); // haal alle bestaande accounts op

            if (accountsByType != null) {
                // Permission ok
                Account account;
                if (accountsByType.length == 0) {
                    // nog geen account aanwezig
                    account = AuthHelper.addAccount(userNameString, mAccountManager);
                }
                else if (!userNameString.equals(accountsByType[0].name)) {
                    // er bestaat reeds een account met andere naam
                    AuthHelper.removeStoredAccount(accountsByType[0], getActivity()); // verwijder de vorige account

                    account = AuthHelper.addAccount(userNameString, mAccountManager); // voeg een nieuwe toe
                }
                else {
                    // account met de zelfde username terug gevonden
                    account = accountsByType[0];
                }

                mListener.onLoginSuccessful(userNameString);
            }
            else {
                // No permission = null
                mListener.handlePermissionRequest(REQUEST_PERMISSION_GET_ACCOUNTS);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
        else {
            mListener.handleError("No internet connection");
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public interface LoginFragmentListener {
        void btnRegisterClicked();
        void onLoginSuccessful(String userNameString);
        void handleError(String error);
        void handlePermissionRequest(final int permissionRequestCode);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof LoginFragmentListener) {
            mListener = (LoginFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + LoginFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if (!username.equals(""))
            outState.putString(STATE_TXT_USERNAME, username);
        if (!password.equals(""))
            outState.putString(STATE_TXT_PASSWORD, password);

        super.onSaveInstanceState(outState);
    }
}
