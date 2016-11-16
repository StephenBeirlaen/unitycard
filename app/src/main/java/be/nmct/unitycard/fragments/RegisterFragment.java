package be.nmct.unitycard.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.FragmentRegisterBinding;
import be.nmct.unitycard.models.viewmodels.RegisterFragmentVM;

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


        this.mBinding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nieuwe gebruiker aanmaken met gegevens
                String firstname = mBinding.txtFirstName.getText().toString();
                String lastname = mBinding.txtLastName.getText().toString();
                String username = mBinding.txtUsername.getText().toString();
                String password1 = mBinding.txtPassword.getText().toString();
                String password2 = mBinding.txtRepeatpassword.getText().toString();
                String password = "";
                if(password1.equals(password2)){
                    password = mBinding.txtPassword.getText().toString();
                }

                //gebruiker maken van gegevens
                if(!firstname.equals("") && !lastname.equals("") && !username.equals("") && !password.equals("")){
                    //verder gaan naar startscherm
                }
                else{
                    //fout medelen in snackbar
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
