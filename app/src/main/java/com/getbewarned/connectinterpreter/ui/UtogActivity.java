package com.getbewarned.connectinterpreter.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.UtogView;
import com.getbewarned.connectinterpreter.presenters.UtogPresenter;

public class UtogActivity extends AppCompatActivity implements UtogView {

    private EditText lastNameField;
    private EditText firstNameField;
    private EditText patronymicField;
    private EditText memberIdField;
    private Button cancelBtn;
    private Button continueBtn;

    private UtogPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utog);

        lastNameField = findViewById(R.id.last_name);
        firstNameField = findViewById(R.id.first_name);
        patronymicField = findViewById(R.id.patronymic);
        memberIdField = findViewById(R.id.member_id);
        cancelBtn = findViewById(R.id.cancel_button);
        continueBtn = findViewById(R.id.continue_button);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.continuePressed(
                        firstNameField.getText().toString(),
                        lastNameField.getText().toString(),
                        patronymicField.getText().toString(),
                        memberIdField.getText().toString()
                );
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.cancelPressed();
            }
        });


        presenter = new UtogPresenter(this);
    }


    @Override
    public void navigateBack() {
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showError(String message) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_global)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void showSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.utog_success)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        presenter.successOkPressed();
                    }
                })
                .create()
                .show();
    }
}
