package com.getbewarned.connectinterpreter.ui.compensation;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.fragment.app.Fragment;

import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataConsumer;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationStep;

public abstract class BaseCompensationStep extends Fragment implements CompensationStep {

    protected TextWatcher defaultTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateParent();
            storeData();
        }
    };

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
