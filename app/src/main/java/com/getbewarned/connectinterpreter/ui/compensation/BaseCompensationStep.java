package com.getbewarned.connectinterpreter.ui.compensation;

import android.support.v4.app.Fragment;

import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataConsumer;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationStep;

public abstract class BaseCompensationStep extends Fragment implements CompensationStep {

    public abstract String getTitle();

    public abstract String getNextButtonText();

    public abstract Boolean getIsNextButtonActive();

    @Override
    public void initData() {
        updateParent();
    }

    @Override
    public void updateParent() {
        if (getActivity() instanceof CompensationDataConsumer) {
            ((CompensationDataConsumer) getActivity()).update(getTitle(), getNextButtonText(), getIsNextButtonActive());
        }
    }
}
