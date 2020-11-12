package com.getbewarned.connectinterpreter.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.UiUtils;
import com.getbewarned.connectinterpreter.adapters.MessagesAdapter;
import com.getbewarned.connectinterpreter.interfaces.CallView;
import com.getbewarned.connectinterpreter.presenters.CallPresenter;
import com.getbewarned.connectinterpreter.ui.dialogs.WaitCallResponseDialog;
import com.getbewarned.connectinterpreter.ui.dialogs.WaitCallResponseListener;

public class CallActivity extends NoStatusBarActivity implements CallView {

    private Boolean debug = false; // hide all error dialogs and show "end call" btn and show list of messages with stubs
    private Boolean useCountDownTimer = true; // make messages hide in X sec
    private static final Long CHAT_ANIM_DURATION = 500L;

    private FrameLayout selfContainer;
    private FrameLayout interpreterContainer;
    private ImageView endCallButton;
    private TextView timeLeft;
    private TextView duration;
    private RecyclerView messagesList;
    private EditText messageField;
    private ImageButton sendButton;

    private CallPresenter presenter;

    private MessagesAdapter messagesAdapter;

    private AudioManager audioManager;
    private int volume;
    private int streamType = AudioManager.STREAM_VOICE_CALL;

    private CountDownTimer timerForChatGone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_v2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        selfContainer = findViewById(R.id.self_container);
        interpreterContainer = findViewById(R.id.interpreter_container);
        endCallButton = findViewById(R.id.end_call_button);
        timeLeft = findViewById(R.id.availability_title);
        duration = findViewById(R.id.duration);
        messagesList = findViewById(R.id.messages);
        messageField = findViewById(R.id.message_field);
        sendButton = findViewById(R.id.send_btn);

        endCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.endCall();
            }
        });
        UiUtils.shadow(endCallButton, 24, 1.2f, 0.5f);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        messageField.setImeActionLabel(getString(R.string.message_enter_label), KeyEvent.KEYCODE_ENTER);
        messageField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (keyEvent == null) {
                    return false;
                }
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });
        if (useCountDownTimer) {
            messageField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()) {
                        launchChatVisibilityCountDownTimer();
                    } else {
                        if (messagesList.getVisibility() != View.VISIBLE) {
                            UiUtils.showAnimated(messagesList, Techniques.FadeIn, CHAT_ANIM_DURATION);
                        }

                    }
                }
            });
        }

        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        messagesAdapter = new MessagesAdapter();
        LinearLayoutManager llManager = new LinearLayoutManager(this, LinearLayout.VERTICAL, true);
        messagesList.setLayoutManager(llManager);
        messagesList.setAdapter(messagesAdapter);

        presenter = new CallPresenter(this);
        presenter.onCreate(getIntent().getExtras());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (debug) {
            for (int i = 0; i < 20; i++) {
                showOneMoreMessage("Some message to interpreter " + i);
            }
        }

    }

    private void sendMessage() {
        presenter.sendMessage(messageField.getText().toString());
        messageField.setText("");
    }

    @Override
    public void updateLeftTime(String leftTime) {
        this.timeLeft.setText(leftTime);
    }

    @Override
    public void updateCurrentCallDuration(String duration) {
        this.duration.setText(duration);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void toggleEndCallButtonVisibility(boolean visible) {
        if (visible) {
            endCallButton.setVisibility(View.VISIBLE);
        } else {
            endCallButton.setVisibility(View.INVISIBLE);
        }
        if (debug) {
            endCallButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showIndicator(Long pleaseWaiteVideoDelayMillis) {
        if (debug == false) {
            WaitCallResponseDialog dialog = new WaitCallResponseDialog();
            dialog.setDialogData(pleaseWaiteVideoDelayMillis, new WaitCallResponseListener() {
                @Override
                public void onDeclineToWait() {
                    presenter.endCall(); // case when user tap "Cancel" in WaitCallResponseActivity
                }
            });
            getSupportFragmentManager().beginTransaction().add(dialog, WaitCallResponseDialog.TAG).commitAllowingStateLoss();
        }
    }

    @Override
    public void hideIndicator() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(WaitCallResponseDialog.TAG);
        if (prev != null) ((DialogFragment) prev).dismissAllowingStateLoss();
    }

    @Override
    public void showError(String message) {
        if (debug == false) {
            if (isFinishing()) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error_call_failed)
                    .setMessage(R.string.err_call_try_later)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            navigateBack();
                        }
                    })
                    .create()
                    .show();
        }
    }

    @Override
    public void showErrorNewUI(String message) {
        if (debug == false) {
            Intent intent = new Intent(CallActivity.this, ErrorActivity.class);
            intent.putExtra(ErrorActivity.TEXT_KEY, message);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }


    @Override
    public void navigateBack() {
        finish();
    }

    @Override
    public void showSelfView(View view) {
        selfContainer.addView(view);
    }

    @Override
    public void showInterpreterView(View view) {
        interpreterContainer.addView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showOneMoreMessage(String message) {
        messagesAdapter.addMessage(message);
        if (useCountDownTimer) {
            launchChatVisibilityCountDownTimer();
        }
    }

    @Override
    public void setMaxVolume() {
        volume = audioManager.getStreamVolume(streamType);
        audioManager.setStreamVolume(streamType, audioManager.getStreamMaxVolume(streamType), 0);
    }

    @Override
    public void resetVolume() {
        audioManager.setStreamVolume(streamType, volume, 0);
    }

    private void launchChatVisibilityCountDownTimer() {
        if (timerForChatGone != null) {
            timerForChatGone.cancel();
            timerForChatGone = null;
        }
        timerForChatGone = new CountDownTimer(5 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                UiUtils.hideAnimated(messagesList, Techniques.FadeOut, CHAT_ANIM_DURATION);
            }
        }.start();
    }
}
