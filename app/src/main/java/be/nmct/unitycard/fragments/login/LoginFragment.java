package be.nmct.unitycard.fragments.login;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import be.nmct.unitycard.R;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.AccountContract;
import be.nmct.unitycard.databinding.FragmentLoginBinding;
import be.nmct.unitycard.helpers.ConnectionChecker;
import be.nmct.unitycard.models.viewmodels.fragment.LoginFragmentVM;

import static be.nmct.unitycard.activities.login.AccountActivity.REQUEST_PERMISSION_GET_ACCOUNTS;

public class LoginFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private LoginFragmentListener mListener;
    private FragmentLoginBinding mBinding;
    private LoginFragmentVM mLoginFragmentVM;
    private AccountManager mAccountManager; // todo: nog nodig straks als alles in helper zit?
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        mLoginFragmentVM = new LoginFragmentVM(mBinding, getActivity());

        mAccountManager = AccountManager.get(getActivity());

        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mBinding.txtUsername.getText().toString(); // input ophalen
                String password = mBinding.txtPassword.getText().toString();

                if (username.length() == 0) { // een basic client side validatie
                    mListener.handleError("Username is empty"); // todo zelfde controles als op server gebruiken
                }
                else if (password.length() == 0) {
                    mListener.handleError("Password is empty");
                }
                else {
                    // hide keyboard
                    InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    // show progress scroller
                    mBinding.progressCircleLogin.setVisibility(View.VISIBLE);

                    signIn("lorenz.vercoutere@student.howest.be", "-Password1");
                }
            }
        });

        mBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.btnRegisterClicked();
            }
        });

        if (savedInstanceState != null) {
            String username = savedInstanceState.getString(STATE_TXT_USERNAME);
            String password = savedInstanceState.getString(STATE_TXT_PASSWORD);

            this.mBinding.txtUsername.setText(username);
            this.mBinding.txtPassword.setText(password);
        }

        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();

        this.mBinding.buttonLoginFacebook.setReadPermissions("email");
        this.mBinding.buttonLoginFacebook.setFragment(this);

        this.mBinding.buttonLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // TODO: Navigate to MyLoyaltyCard (from Facebook)

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        //mGoogleApiClient = new GoogleApiClient.Builder(getContext()).enableAutoManage(getActivity(), /* OnConnectionFailedListener */).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        this.mBinding.buttonLoginGoogle.setSize(SignInButton.SIZE_STANDARD);
        this.mBinding.buttonLoginGoogle.setColorScheme(SignInButton.COLOR_AUTO);
        this.mBinding.buttonLoginGoogle.setScopes(gso.getScopeArray());

        this.mBinding.buttonLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.buttonLoginGoogle:
                        signInWithGoogle();
                        break;
                }
            }
        });


        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // todo: perform load actions here
    }

    private void signInWithGoogle(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully


        } else {
            // Signed out

        }
    }

    private void signIn(final String userName, String password) {
        if (ConnectionChecker.isInternetAvailable(getActivity())) { // check of er verbinding is
            Account[] accountsByType = AuthHelper.getStoredAccountsByType(getActivity()); // haal alle bestaande accounts op

            if (accountsByType != null) {
                // Permission ok
                Account account = null;
                if (accountsByType.length != 0) {
                    if (!userName.equals(accountsByType[0].name)) {
                        // Er bestaat reeds een account met andere naam, verwijder de vorige account
                        AuthHelper.removeStoredAccount(accountsByType[0], getActivity());
                    }
                    else {
                        // Account met de zelfde username terug gevonden
                        account = accountsByType[0];

                        // Check of er een Refresh token aanwezig is
                        String refreshToken = mAccountManager.peekAuthToken(account, AccountContract.TOKEN_REFRESH);
                        if (TextUtils.isEmpty(refreshToken)) { // Is er geen refresh token meer aanwezig?
                            // Remove account
                            AuthHelper.removeStoredAccount(account, getActivity());
                            account = null;
                        }
                    }
                }

                if (account != null) { // Als er al een account is
                    // Direct melden dat login OK is
                    mListener.onLoginSuccessful(userName);
                }
                else { // Als er nog geen account is
                    // Add new account
                    AuthHelper.addAccount(userName, password, getContext(), mAccountManager, new AuthHelper.AddAccountListener() {
                        @Override
                        public void accountAdded(Account account) {
                            mListener.onLoginSuccessful(userName);
                        }

                        @Override
                        public void accountAddError(String error) {
                            mListener.handleError(error);
                            mBinding.progressCircleLogin.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
            else {
                // No permission = null
                mListener.handlePermissionRequest(REQUEST_PERMISSION_GET_ACCOUNTS);
                mBinding.progressCircleLogin.setVisibility(View.INVISIBLE);
            }
        }
        else {
            mListener.handleError("No internet connection");
            mBinding.progressCircleLogin.setVisibility(View.INVISIBLE);
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
        String username = mBinding.txtUsername.getText().toString();
        String password = mBinding.txtPassword.getText().toString();

        if (!username.equals(""))
            outState.putString(STATE_TXT_USERNAME, username);
        if (!password.equals(""))
            outState.putString(STATE_TXT_PASSWORD, password);

        super.onSaveInstanceState(outState);
    }
}
