package be.nmct.unitycard.fragments.login;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import be.nmct.unitycard.R;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.databinding.FragmentRegisterBinding;
import be.nmct.unitycard.helpers.ConnectionChecker;
import be.nmct.unitycard.models.postmodels.RegisterUserBody;
import be.nmct.unitycard.models.viewmodels.fragment.RegisterFragmentVM;
import be.nmct.unitycard.repositories.AuthRepository;

public class RegisterFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private final String LANGUAGE = "nl-BE";
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
        ((AppCompatActivity)getActivity()).setTitle("Register"); // todo: multi lang
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.mBinding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nieuwe gebruiker aanmaken met gegevens
                String firstName = mBinding.txtFirstName.getText().toString();
                String lastName = mBinding.txtLastName.getText().toString();
                final String email = mBinding.txtEmail.getText().toString();
                final String password1 = mBinding.txtPassword.getText().toString();
                String password2 = mBinding.txtRepeatpassword.getText().toString();
                String password = "";
                if (password1.equals(password2)){
                    password = mBinding.txtPassword.getText().toString();
                }
                // todo: password equal validation
                // todo: rework validation...

                // gebruiker maken van gegevens
                if (!firstName.equals("") && !lastName.equals("") && !email.equals("") && !password1.equals("") && !password2.equals("")){
                    // hide keyboard
                    InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    // show progress scroller
                    mBinding.progressCircleRegister.setVisibility(View.VISIBLE);

                    if (ConnectionChecker.isInternetAvailable(getActivity())) { // check of er verbinding is
                        AuthRepository authRepository = new AuthRepository(getContext());
                        RegisterUserBody registerUserBody = new RegisterUserBody(email, password1, password2, firstName, lastName, LANGUAGE);
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
                        mListener.handleError("No internet connection");
                        mBinding.progressCircleRegister.setVisibility(View.INVISIBLE);
                    }

                }
                else {
                    //todo:fouten meedelen in snackbar

                    mBinding.progressCircleRegister.setVisibility(View.INVISIBLE);
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
