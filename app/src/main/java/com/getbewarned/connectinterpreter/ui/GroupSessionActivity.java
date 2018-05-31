package com.getbewarned.connectinterpreter.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.GroupSessionView;
import com.getbewarned.connectinterpreter.presenters.GroupSessionPresenter;

import java.util.Date;

public class GroupSessionActivity extends AppCompatActivity implements GroupSessionView {

    private FrameLayout interpreterContainer;
    private TextView sessionName;
    private ProgressDialog loadingDialog;
    private TextView waitingText;
    private FrameLayout askerContainer;
    private Button askButton;
    private Button endCallButton;

    private GroupSessionPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_session);

        interpreterContainer = findViewById(R.id.interpreter_container);
        askerContainer = findViewById(R.id.asker_container);
        sessionName = findViewById(R.id.session_title);
        waitingText = findViewById(R.id.waiting_text);
        askButton = findViewById(R.id.ask_button);
        endCallButton = findViewById(R.id.end_call_button);
        loadingDialog = new ProgressDialog(this, R.style.AppTheme_LoaderDialog);
        loadingDialog.setCancelable(false);
        loadingDialog.setButton(
                DialogInterface.BUTTON_NEGATIVE,
                getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }
        );
        loadingDialog.setTitle(getString(R.string.group_waiting_title));
        loadingDialog.setMessage(getString(R.string.group_waiting_text));

        endCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.leave();
            }
        });

        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.toggleAsking();
            }
        });

        presenter = new GroupSessionPresenter(this, this);
        presenter.onCreate(getIntent().getExtras());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void showError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_call_failed)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void showSessionInfo(String sessionName, Date sessionDate, boolean connected) {
        if (connected) {
            this.sessionName.setText(sessionName);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("")
                .setMessage(sessionName + " at " + sessionDate.toString())
                .setCancelable(false)
                .setPositiveButton(R.string.btn_reconnect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.reconnect();
                    }
                })
                .setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.leave();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void updateInterpreterView(View interpreterView) {
        if (interpreterView != null) {
            waitingText.setVisibility(View.GONE);
            interpreterContainer.addView(interpreterView);
        } else {
            waitingText.setVisibility(View.VISIBLE);
            interpreterContainer.removeAllViews();
        }
    }

    @Override
    public void updateAskerView(View askerView) {
        if (askerView == null) {
            askerContainer.removeAllViews();
            askerContainer.setVisibility(View.GONE);
        } else {
            askerContainer.setVisibility(View.VISIBLE);
            askerContainer.addView(askerView);
        }
    }

    @Override
    public void showReconnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_call_failed)
                .setMessage(R.string.error_call_failed)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_reconnect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.reconnect();
                    }
                })
                .setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.leave();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        if (isLoading) {
            loadingDialog.show();
        } else {
            loadingDialog.hide();
        }
    }

    @Override
    protected void onDestroy() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void toggleAsking(boolean asking) {
        if (asking) {
            askButton.setText(R.string.asking_a_question);
        } else {
            askButton.setText(R.string.ask_a_question);
        }
    }

    @Override
    public void toggleAskButtonVisibility(boolean visible) {
        if (visible) {
            askButton.setVisibility(View.VISIBLE);
        } else {
            askButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void leave() {
        finish();
    }

    @Override
    public void runOnUi(Runnable action) {
        runOnUiThread(action);
    }
}
