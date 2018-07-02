package com.delex.a_introduction;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delex.customer.R;
import com.delex.customer.databinding.FragmentIntroductionBinding;
import com.delex.pojos.Data;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroductionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroductionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private FragmentIntroductionBinding binding;
    private TextView textView;

    public IntroductionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment IntroductionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntroductionFragment newInstance(String param1) {
        IntroductionFragment fragment = new IntroductionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_introduction, container, false);
        binding = DataBindingUtil.bind(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.text.setText(mParam1);
    }

//    public void changeImageView(String text) {
//        if (binding.text != null) {
//            textView.setText(text);
//        }
//    }
}
