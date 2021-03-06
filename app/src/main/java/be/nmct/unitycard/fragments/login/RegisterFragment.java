package be.nmct.unitycard.fragments.login;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;
import java.util.regex.Matcher;

import be.nmct.unitycard.R;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.AccountContract;
import be.nmct.unitycard.databinding.FragmentRegisterBinding;
import be.nmct.unitycard.helpers.ConnectionChecker;
import be.nmct.unitycard.helpers.HideKeyboardHelper;
import be.nmct.unitycard.models.postmodels.RegisterUserBody;
import be.nmct.unitycard.models.viewmodels.fragment.RegisterFragmentVM;
import be.nmct.unitycard.repositories.AuthRepository;

public class RegisterFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RegisterFragmentListener mListener;
    private FragmentRegisterBinding mBinding;
    private RegisterFragmentVM mRegisterFragmentVM;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        mRegisterFragmentVM = new RegisterFragmentVM(mBinding, getActivity());

        ((AppCompatActivity)getActivity()).setSupportActionBar(mBinding.toolbar);
        ((AppCompatActivity)getActivity()).setTitle(getString(R.string.FragmentLabelRegister));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.mBinding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = mBinding.txtFirstName.getText().toString(); // input ophalen
                final String lastName = mBinding.txtLastName.getText().toString();
                final String email = mBinding.txtEmail.getText().toString();
                final String password1 = mBinding.txtPassword.getText().toString();
                final String password2 = mBinding.txtRepeatpassword.getText().toString();

                Matcher passwordMatcher = AccountContract.POLICY_PASSWORD_PATTERN.matcher(password1);

                if (firstName.length() == 0) {
                    mListener.handleError(getString(R.string.first_name_empty));
                }
                else if (firstName.length() < AccountContract.POLICY_FIRSTNAME_MIN_LENGTH) {
                    mListener.handleError(getString(R.string.first_name_min_chars) + AccountContract.POLICY_FIRSTNAME_MIN_LENGTH + getString(R.string.characters));
                }
                else if (firstName.length() > AccountContract.POLICY_FIRSTNAME_MAX_LENGTH) {
                    mListener.handleError(getString(R.string.first_name_max_chars) + AccountContract.POLICY_FIRSTNAME_MAX_LENGTH + getString(R.string.characters));
                }
                else if (lastName.length() == 0) {
                    mListener.handleError(getString(R.string.last_name_empty));
                }
                else if (lastName.length() < AccountContract.POLICY_LASTNAME_MIN_LENGTH) {
                    mListener.handleError(getString(R.string.last_name_min_chars) + AccountContract.POLICY_LASTNAME_MIN_LENGTH + getString(R.string.characters));
                }
                else if (lastName.length() > AccountContract.POLICY_LASTNAME_MAX_LENGTH) {
                    mListener.handleError(getString(R.string.last_name_max_chars) + AccountContract.POLICY_LASTNAME_MAX_LENGTH + getString(R.string.characters));
                }
                else if (email.length() == 0) { // een basic client side validatie
                    mListener.handleError(getString(R.string.email_empty));
                }
                else if (email.length() < AccountContract.POLICY_EMAIL_MIN_LENGTH) {
                    mListener.handleError(getString(R.string.email_min_chars) + AccountContract.POLICY_EMAIL_MIN_LENGTH + getString(R.string.characters));
                }
                else if (email.length() > AccountContract.POLICY_EMAIL_MAX_LENGTH) {
                    mListener.handleError(getString(R.string.email_max_chars) + AccountContract.POLICY_EMAIL_MAX_LENGTH + getString(R.string.characters));
                }
                else if (password1.length() == 0) {
                    mListener.handleError(getString(R.string.password_empty));
                }
                else if (password1.length() < AccountContract.POLICY_PASSWORD_MIN_LENGTH) {
                    mListener.handleError(getString(R.string.password_min_chars) + AccountContract.POLICY_PASSWORD_MIN_LENGTH + getString(R.string.characters));
                }
                else if (password1.length() > AccountContract.POLICY_PASSWORD_MAX_LENGTH) {
                    mListener.handleError(getString(R.string.password_max_chars) + AccountContract.POLICY_PASSWORD_MAX_LENGTH + getString(R.string.characters));
                }
                else if (!password1.equals(password2)) {
                    mListener.handleError(getString(R.string.passwords_not_matching));
                }
                else if (!passwordMatcher.matches()) {
                    mListener.handleError(getString(R.string.password_invalid_format));
                }
                else {
                    // hide keyboard
                    HideKeyboardHelper.hideKeyboard(getActivity());

                    // show progress scroller
                    mBinding.progressCircleRegister.setVisibility(View.VISIBLE);

                    if (ConnectionChecker.isInternetAvailable(getActivity())) { // check of er verbinding is
                        AuthRepository authRepository = new AuthRepository(getContext());

                        String locale = Locale.getDefault().toString();
                        if (locale == null || locale.length() != AccountContract.POLICY_LANGUAGE_LENGTH) {
                            locale = Locale.US.toString(); // default locale en_US
                        }
                        RegisterUserBody registerUserBody = new RegisterUserBody(email, password1, password2, firstName, lastName, locale);

                        authRepository.registerUser(registerUserBody, new AuthRepository.RegisterResponseListener() {
                            @Override
                            public void userRegistered() {
                                mBinding.progressCircleRegister.setVisibility(View.INVISIBLE);

                                AuthHelper.signIn(email, password1, getActivity(), getContext(), new AuthHelper.SignInListener() {
                                    @Override
                                    public void onSignInSuccessful(String userNameString) {
                                        mListener.onLoginSuccessful(userNameString);
                                    }

                                    @Override
                                    public void handleError(String error) {
                                        mListener.handleError(error);
                                        mBinding.progressCircleRegister.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void handlePermissionRequest(int permissionRequestCode) {
                                        mListener.handlePermissionRequest(permissionRequestCode);
                                        mBinding.progressCircleRegister.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }

                            @Override
                            public void registerRequestError(String error) {
                                mListener.handleError(error);
                                mBinding.progressCircleRegister.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else {
                        mListener.handleError(getString(R.string.no_internet_connection));
                        mBinding.progressCircleRegister.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        return mBinding.getRoot();
    }

    public interface RegisterFragmentListener {
        void onLoginSuccessful(String userNameString);
        void handleError(String error);
        void handlePermissionRequest(final int permissionRequestCode);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof RegisterFragmentListener) {
            mListener = (RegisterFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + RegisterFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
