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

        mBinding.btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.btnLoginFacebookClicked();
            }
        });

        mBinding.btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.btnLoginGoogleClicked();
            }
        });

        if (savedInstanceState != null) {
            String username = savedInstanceState.getString(STATE_TXT_USERNAME);
            String password = savedInstanceState.getString(STATE_TXT_PASSWORD);

            this.mBinding.txtUsername.setText(username);
            this.mBinding.txtPassword.setText(password);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // todo: perform load actions here
    }

    public interface LoginFragmentListener {
        void btnRegisterClicked();
        void btnLoginFacebookClicked();
        void btnLoginGoogleClicked();
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
