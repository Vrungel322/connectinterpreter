package com.getbewarned.connectinterpreter.ui.compensation.steps;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.ui.compensation.BaseCompensationStep;

public class CompensationFragmentFinal extends BaseCompensationStep {
    VideoView vvCompensationFinal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compensation_fragent_final, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vvCompensationFinal = view.findViewById(R.id.video_view);

        final Uri video = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.final_compensation);
        vvCompensationFinal.setVideoURI(video);
    }

    @Override
    public void initData() {
        vvCompensationFinal.start();
        super.initData();
    }

    @Override
    public void storeData() {
    }

    @Override
    public String getTitle() {
        return this.getString(R.string.final_step);
    }

    @Override
    public String getNextButtonText() {
        return this.getString(R.string.action_send);
    }

    @Override
    public Boolean getIsNextButtonActive() {
        return true;
    }

    public static CompensationFragmentFinal newInstance() {
        Bundle args = new Bundle();
        CompensationFragmentFinal fragment = new CompensationFragmentFinal();
        fragment.setArguments(args);
        return fragment;
    }

}
