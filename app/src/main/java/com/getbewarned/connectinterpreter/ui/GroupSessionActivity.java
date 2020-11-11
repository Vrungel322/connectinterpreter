package com.getbewarned.connectinterpreter.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.GroupSessionView;
import com.getbewarned.connectinterpreter.presenters.GroupSessionPresenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GroupSessionActivity extends AppCompatActivity implements GroupSessionView {

    private FrameLayout interpreterContainer;
    private TextView sessionName;
    private ProgressDialog loadingDialog;
    private TextView waitingText;
    private FrameLayout askerContainer;
    private Button askButton;
    private Button endCallButton;
    private LinearLayout videoContainer;

    private GroupSessionPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_session);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        interpreterContainer = findViewById(R.id.interpreter_container);
        askerContainer = findViewById(R.id.asker_container);
        sessionName = findViewById(R.id.session_title);
        waitingText = findViewById(R.id.waiting_text);
        askButton = findViewById(R.id.ask_button);
        endCallButton = findViewById(R.id.end_call_button);
        videoContainer = findViewById(R.id.video_container);
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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoContainer.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            videoContainer.setOrientation(LinearLayout.VERTICAL);
        }

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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoContainer.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            videoContainer.setOrientation(LinearLayout.VERTICAL);
        }
    }

    @Override
    public void showError(String message) {
        if (isFinishing()) {
            return;
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_LoaderDialog);
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        builder.setTitle(getString(R.string.group_later))
                .setMessage(getString(R.string.group_later_desc, sessionName, dateFormat.format(sessionDate), timeFormat.format(sessionDate)))
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
