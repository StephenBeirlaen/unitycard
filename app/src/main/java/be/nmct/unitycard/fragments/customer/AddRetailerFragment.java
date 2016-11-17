package be.nmct.unitycard.fragments.customer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.FragmentAddRetailerBinding;
import be.nmct.unitycard.models.viewmodels.fragment.AddRetailerFragmentVM;

public class AddRetailerFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private AddRetailerFragmentListener mListener;
    private FragmentAddRetailerBinding mBinding;
    private AddRetailerFragmentVM mAddRetailerFragmentVM;

    public AddRetailerFragment() {
        // Required empty public constructor
    }

    public static AddRetailerFragment newInstance() {
        return new AddRetailerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_retailer, container, false);

        mBinding.recyclerViewAddRetailerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewAddRetailerList.setHasFixedSize(true);
        mBinding.recyclerViewAddRetailerList.setItemAnimator(new DefaultItemAnimator());

        mAddRetailerFragmentVM = new AddRetailerFragmentVM(mBinding, getActivity());

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        mAddRetailerFragmentVM.loadRetailers();
    }

    public interface AddRetailerFragmentListener {
        // todo: events, indien er geen events zijn, onAttach en ondetach overrides wegdoen
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof AddRetailerFragmentListener) {
            mListener = (AddRetailerFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + AddRetailerFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
