package com.getbewarned.connectinterpreter.ui.compensation;

import android.support.v4.app.Fragment;

import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataConsumer;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationStep;

public abstract class BaseCompensationStep extends Fragment implements CompensationStep {

    abstract String getTitle();

    abstract String getNextButtonText();

    abstract Boolean getIsNextButtonActive();

    @Override
    public void updateParent() {
        if (getActivity() instanceof CompensationDataConsumer) {
            ((CompensationDataConsumer) getActivity()).update(getTitle(), getNextButtonText(), getIsNextButtonActive());
        }
    }
}
