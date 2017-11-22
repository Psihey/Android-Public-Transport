package com.provectus.public_transport.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.provectus.public_transport.BuildConfig;
import com.provectus.public_transport.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AboutProgramFragment extends Fragment {

    @BindView(R.id.tv_version_code)
    TextView mTextViewVersionCode;
    @BindView(R.id.tv_version_name)
    TextView mTextViewVersionName;
    @BindView(R.id.tv_profuct_owner_email)
    TextView mTextViewProductOwnerEmail;

    public static final String TAG_ABOUT_PROGRAM_FRAGMENT = "about_program";
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_program, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mTextViewVersionCode.setText(getResources().getString(R.string.text_view_version_code, BuildConfig.VERSION_CODE));
        mTextViewVersionName.setText(getResources().getString(R.string.text_view_version_name, BuildConfig.VERSION_NAME));
    }

    @OnClick(R.id.im_btn_arrow_back)
    public void backToPreviousFragment() {
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

}
