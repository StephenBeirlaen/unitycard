package be.nmct.unitycard.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.FragmentRegisterBinding;
import be.nmct.unitycard.models.postmodels.RegisterUserBody;
import be.nmct.unitycard.models.viewmodels.RegisterFragmentVM;
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

        this.mBinding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nieuwe gebruiker aanmaken met gegevens
                String firstName = mBinding.txtFirstName.getText().toString();
                String lastName = mBinding.txtLastName.getText().toString();
                String email = mBinding.txtEmail.getText().toString();
                String password1 = mBinding.txtPassword.getText().toString();
                String password2 = mBinding.txtRepeatpassword.getText().toString();
                String password = "";
                if (password1.equals(password2)){
                    password = mBinding.txtPassword.getText().toString();
                }

                //gebruiker maken van gegevens
                if (!firstName.equals("") && !lastName.equals("") && !email.equals("") && !password1.equals("") && !password2.equals("")){
                    AuthRepository authRepository = new AuthRepository(getContext());
                    RegisterUserBody registerUserBody = new RegisterUserBody(email, password1, password2, firstName, lastName, LANGUAGE);
                    authRepository.registerUser(registerUserBody, new AuthRepository.RegisterResponseListener() {
                        @Override
                        public void userRegistered() {
                            mListener.handleError("tes in orde");
                        }

                        @Override
                        public void registerRequestError(String error) {
                            mListener.handleError(error);
                        }
                    });

                }
                else{
                    //fout meedelen in snackbar
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
        void onRegisterSuccessful();
        void onRegisterCanceled();
        void handleError(String error);
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
