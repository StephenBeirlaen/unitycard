package be.nmct.unitycard.fragments.login;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
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
import be.nmct.unitycard.helpers.HideKeyboardHelper;
import be.nmct.unitycard.models.viewmodels.fragment.LoginFragmentVM;

public class LoginFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private LoginFragmentListener mListener;
    private FragmentLoginBinding mBinding;
    private LoginFragmentVM mLoginFragmentVM;
    private CallbackManager callbackManager;
    private Uri uri;
    private Profile profile = new Profile("1", "", "", "","", uri);
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

        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mBinding.txtUsername.getText().toString(); // input ophalen
                String password = mBinding.txtPassword.getText().toString();

                if (username.length() == 0) { // een basic client side validatie
                    mListener.handleError("Email is empty");
                }
                else if (username.length() < AccountContract.POLICY_EMAIL_MIN_LENGTH) {
                    mListener.handleError("Email must have a minimum of " + AccountContract.POLICY_EMAIL_MIN_LENGTH + " characters"); // todo: multi language
                }
                else if (username.length() > AccountContract.POLICY_EMAIL_MAX_LENGTH) {
                    mListener.handleError("Email must have a maximum of " + AccountContract.POLICY_EMAIL_MAX_LENGTH + " characters");
                }
                else if (password.length() == 0) {
                    mListener.handleError("Password is empty");
                }
                else if (password.length() < AccountContract.POLICY_PASSWORD_MIN_LENGTH) {
                    mListener.handleError("Password must have a minimum of " + AccountContract.POLICY_PASSWORD_MIN_LENGTH + " characters");
                }
                else if (password.length() > AccountContract.POLICY_PASSWORD_MAX_LENGTH) {
                    mListener.handleError("Password must have a maximum of " + AccountContract.POLICY_PASSWORD_MAX_LENGTH + " characters");
                }
                else {
                    // hide keyboard
                    HideKeyboardHelper.hideKeyboard(getActivity());

                    // show progress scroller
                    mBinding.progressCircleLogin.setVisibility(View.VISIBLE);

                    AuthHelper.signIn(username, password, getActivity(), getContext(), new AuthHelper.SignInListener() {
                        @Override
                        public void onSignInSuccessful(String userNameString) {
                            mListener.onLoginSuccessful(userNameString);
                        }

                        @Override
                        public void handleError(String error) {
                            mListener.handleError(error);
                            mBinding.progressCircleLogin.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void handlePermissionRequest(int permissionRequestCode) {
                            mListener.handlePermissionRequest(permissionRequestCode);
                            mBinding.progressCircleLogin.setVisibility(View.INVISIBLE);
                        }
                    });
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

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        this.mBinding.buttonLoginFacebook.setReadPermissions("email");
        this.mBinding.buttonLoginFacebook.setFragment(this);

        this.mBinding.buttonLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // TODO: Navigate to MyLoyaltyCard (from Facebook)

                Log.d(LOG_TAG, "hallo");
            }

            @Override
            public void onCancel() {
                Log.d(LOG_TAG, "Facebook login error");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(LOG_TAG, "Facebook login error");
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

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }


    }

    private void handleSignInResult(GoogleSignInResult result) { // todo: implement and rename method (have google in the name)
        if (result.isSuccess()) {
            // Signed in successfully


        } else {
            // Signed out

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
