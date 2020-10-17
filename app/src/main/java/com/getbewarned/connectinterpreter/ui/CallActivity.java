package com.getbewarned.connectinterpreter.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.adapters.MessagesAdapter;
import com.getbewarned.connectinterpreter.interfaces.CallView;
import com.getbewarned.connectinterpreter.presenters.CallPresenter;

public class CallActivity extends AppCompatActivity implements CallView {

    private FrameLayout selfContainer;
    private FrameLayout interpreterContainer;
    private ImageButton endCallButton;
    private TextView timeLeft;
    private TextView duration;
    private RecyclerView messagesList;
    private EditText messageField;
    private ImageButton sendButton;

    private CallPresenter presenter;

    private MessagesAdapter messagesAdapter;
    private AlertDialog videoDialog;

    private AudioManager audioManager;
    private int volume;
    private int streamType = AudioManager.STREAM_VOICE_CALL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

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

        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        messagesAdapter = new MessagesAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        messagesList.setLayoutManager(linearLayoutManager);
        messagesList.setAdapter(messagesAdapter);

        presenter = new CallPresenter(this);
        presenter.onCreate(getIntent().getExtras());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
    }

    @Override
    public void showIndicator() {
        Intent intent = new Intent(CallActivity.this, WaitCallResponseActivity.class);
        startActivityForResult(intent, WaitCallResponseActivity.RC);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == WaitCallResponseActivity.RC) {
                presenter.endCall(); // case when user tap "Cancel" in WaitCallResponseActivity
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void hideIndicator() {
        if (WaitCallResponseActivity.activity != null) {
            WaitCallResponseActivity.activity.finish();
        }
    }

    @Override
    public void showError(String message) {
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
    }


    @Override
    public void showWaitVideo() {
        if (isFinishing()) {
            return;
        }
        if (videoDialog == null) {
            final VideoView videoView = new VideoView(this);
            videoView.setMediaController(new MediaController(this));
            final Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.please_wait);
            videoDialog = new AlertDialog.Builder(this)
                    .setView(videoView)
                    .setCancelable(false)
                    .setNegativeButton(
                            getResources().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    presenter.endCall();
                                    dialog.dismiss();
                                }
                            }
                    )
                    .create();
            videoDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    videoView.setVideoURI(video);
                    videoView.start();
                }
            });

            videoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    videoView.stopPlayback();
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoDialog.dismiss();
                    showIndicator();
                }
            });
        }
        videoDialog.show();
    }

    @Override
    public void hideWaitVideo() {
        if (videoDialog != null) {
            videoDialog.dismiss();
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
}
