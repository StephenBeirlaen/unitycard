package be.nmct.unitycard.fragments.login;


import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import be.nmct.unitycard.R;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.AccountContract;
import be.nmct.unitycard.databinding.FragmentAssociateAccountBinding;
import be.nmct.unitycard.helpers.ConnectionChecker;
import be.nmct.unitycard.helpers.HideKeyboardHelper;
import be.nmct.unitycard.models.GetTokenResponse;
import be.nmct.unitycard.models.postmodels.RegisterExternalBindingBody;
import be.nmct.unitycard.models.viewmodels.fragment.AssociateAccountFragmentVM;
import be.nmct.unitycard.repositories.AuthRepository;
import okhttp3.HttpUrl;

import static be.nmct.unitycard.activities.login.ExternalAuthenticationActivity.EXTERNAL_AUTHENTICATION_RESPONSE_URL;

public class AssociateAccountFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private AssociateAccountFragmentListener mListener;
    private FragmentAssociateAccountBinding mBinding;
    private AssociateAccountFragmentVM mAssociateAccountFragmentVM;
    private String mProvider;
    private String mExternalAccessToken;

    public AssociateAccountFragment() {
        // Required empty public constructor
    }

    public static AssociateAccountFragment newInstance(Bundle args) {
        AssociateAccountFragment fragment = new AssociateAccountFragment();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_associate_account, container, false);
        mAssociateAccountFragmentVM = new AssociateAccountFragmentVM(mBinding, getActivity());

        ((AppCompatActivity)getActivity()).setSupportActionBar(mBinding.toolbar);
        ((AppCompatActivity)getActivity()).setTitle(getString(R.string.FragmentLabelAssociateAccount));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle args = getArguments();
        String responseUrl = args.getString(EXTERNAL_AUTHENTICATION_RESPONSE_URL);
        parseExternalAuthResponse(responseUrl, getActivity());

        this.mBinding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = mBinding.txtFirstName.getText().toString(); // input ophalen
                final String lastName = mBinding.txtLastName.getText().toString();
                final String email = mBinding.txtEmail.getText().toString();

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
                        RegisterExternalBindingBody registerExternalBindingBody = new RegisterExternalBindingBody(
                                email,
                                mProvider,
                                mExternalAccessToken,
                                firstName,
                                lastName,
                                locale
                        );

                        authRepository.associateExternalUser(registerExternalBindingBody, new AuthRepository.TokenResponseListener() {
                            @Override
                            public void tokenReceived(GetTokenResponse getTokenResponse) {
                                mBinding.progressCircleRegister.setVisibility(View.INVISIBLE);

                                AuthHelper.signInExternalAccount(email, getTokenResponse, getActivity(), getContext(), new AuthHelper.SignInListener() {
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
                            public void tokenRequestError(String error) {
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

    private void parseExternalAuthResponse(String url, Activity activity) {
        HttpUrl parsedUrl = HttpUrl.parse(url);
        mExternalAccessToken = parsedUrl.queryParameter("external_access_token");
        mProvider = parsedUrl.queryParameter("provider");
        final Boolean hasLocalAccount = Boolean.parseBoolean(parsedUrl.queryParameter("haslocalaccount"));
        final String externalUserName = parsedUrl.queryParameter("external_user_name");
        final String email = parsedUrl.queryParameter("email");
        final String firstName = parsedUrl.queryParameter("firstname");
        final String lastName = parsedUrl.queryParameter("lastname");

        AuthRepository authRepo = new AuthRepository(activity);

        if (hasLocalAccount) {
            mBinding.txtEmail.setVisibility(View.GONE);
            mBinding.txtFirstName.setVisibility(View.GONE);
            mBinding.txtLastName.setVisibility(View.GONE);
            mBinding.buttonRegister.setVisibility(View.GONE);
            ((AppCompatActivity)getActivity()).setTitle(getString(R.string.FragmentLabelAssociateAccountLoggingIn));

            // show progress scroller
            mBinding.progressCircleRegister.setVisibility(View.VISIBLE);

            if (ConnectionChecker.isInternetAvailable(getActivity())) { // check of er verbinding is
                // Inloggen met bestaande account (lokale access token aanvragen via externe token)
                authRepo.obtainLocalAccessToken(mProvider, mExternalAccessToken, new AuthRepository.TokenResponseListener() {
                    @Override
                    public void tokenReceived(GetTokenResponse getTokenResponse) {
                        mBinding.progressCircleRegister.setVisibility(View.INVISIBLE);

                        AuthHelper.signInExternalAccount(email, getTokenResponse, getActivity(), getContext(), new AuthHelper.SignInListener() {
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
                    public void tokenRequestError(String error) {
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
        else {
            // De eventuele vorige account uitloggen
            AuthHelper.logUserOff(activity, null);

            // Extra info vragen aan de user (suggesties worden meegegeven, moeten bevestigd worden)
            this.mBinding.txtEmail.setText(email);
            this.mBinding.txtFirstName.setText(firstName);
            this.mBinding.txtLastName.setText(lastName);
        }
    }

    public interface AssociateAccountFragmentListener {
        void onLoginSuccessful(String userNameString);
        void handleError(String error);
        void handlePermissionRequest(final int permissionRequestCode);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof AssociateAccountFragmentListener) {
            mListener = (AssociateAccountFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + AssociateAccountFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
