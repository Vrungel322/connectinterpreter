package com.getbewarned.connectinterpreter.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.MainView;
import com.getbewarned.connectinterpreter.models.Reason;
import com.getbewarned.connectinterpreter.models.TariffResponse;
import com.getbewarned.connectinterpreter.presenters.CallPresenter;
import com.getbewarned.connectinterpreter.presenters.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class NewMainActivity extends NoStatusBarActivity implements MainView {

    private static final int RC_VIDEO_APP_PERM = 387;
    private static final int RC_PHONE_STATE_PERM = 483;

    TextView availabilityTitleLabel;
    TextView tvMinutesExpiration;
    LinearLayout callBtn;
    FloatingActionButton requests;
    ImageView help;
    LinearLayout noMinutesContainer;
    ImageView profile;
    TextView timer;
    ImageView buyButton;
    ImageView menu;

    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_v2);
        availabilityTitleLabel = findViewById(R.id.tv_avaliavle_minutes_label);
        tvMinutesExpiration = findViewById(R.id.tv_minutes_expiration);
        callBtn = findViewById(R.id.ll_call);
        buyButton = findViewById(R.id.iv_add_minutes);
        menu = findViewById(R.id.iv_menu);

        requests = findViewById(R.id.iv_requests);
        timer = findViewById(R.id.tv_timer);
        help = findViewById(R.id.iv_help);
        noMinutesContainer = findViewById(R.id.ll_no_minutes);
        profile = findViewById(R.id.iv_profile);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onStartCallPressed();
            }
        });
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.buyUnlimPressed();
            }
        });

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMainActivity.this, RequestsActivity.class);
                startActivity(intent);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://interpreter.getbw.me/help");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMainActivity.this, ActionsMenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        presenter = new MainPresenter(this, this);
        presenter.onCreate(getIntent().getExtras());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void showLeftTime(String leftTime) {
        timer.setText(leftTime);
    }

    @Override
    public void toggleCallAvailability(boolean available, boolean isUtog) {
        if (available) {
            if (isUtog) {
                availabilityTitleLabel.setText(R.string.utog_free_call_title);
            }
            noMinutesContainer.setVisibility(View.GONE);
            callBtn.setVisibility(View.VISIBLE);
        } else {
            availabilityTitleLabel.setText(R.string.not_available_title);
            noMinutesContainer.setVisibility(View.VISIBLE);
            callBtn.setVisibility(View.GONE);
            timer.setText("00:00");
        }
    }

    @Override
    public void showChecking() {
        availabilityTitleLabel.setText(R.string.minutes_check_title);
        callBtn.setVisibility(View.GONE);
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

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    public void requestPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            presenter.onPermissionsGranted();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_rationale_call), RC_VIDEO_APP_PERM, perms);
        }
    }

    @AfterPermissionGranted(RC_PHONE_STATE_PERM)
    public void requestLiqPayPermissions() {
        String[] perms = {Manifest.permission.READ_PHONE_STATE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            presenter.onLiqPayPermissionsGranted();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_rationale_liqpay), RC_PHONE_STATE_PERM, perms);
        }
    }

    @Override
    public void askForName(String name) {

    }

    @Override
    public void updateUserName(String name) {

    }

    @Override
    public void askForReason(final List<Reason> reasons) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_LoaderDialog);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
        List<String> values = new ArrayList<String>();
        for (Reason reason : reasons) {
            values.add(reason.getLabel());
        }
        arrayAdapter.addAll(values);

        builder.setTitle(getString(R.string.reason_alert_title))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Reason reason = reasons.get(which);
                        presenter.reasonSelected(reason);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void navigateToCallWith(String token, String sessionId, String apiKey, long maxSeconds) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra(CallPresenter.TOKEN_EXTRA, token);
        intent.putExtra(CallPresenter.SESSION_EXTRA, sessionId);
        intent.putExtra(CallPresenter.KEY_EXTRA, apiKey);
        intent.putExtra(CallPresenter.SECONDS_EXTRA, maxSeconds);
        startActivity(intent);
    }

    @Override
    public void navigateToLogin() {

    }

    @Override
    public void showDateTill(String dateTill) {
        availabilityTitleLabel.setText("Безлимит");
        tvMinutesExpiration.setVisibility(View.VISIBLE);
        tvMinutesExpiration.setText(getString(R.string.till_text, dateTill));
    }

    @Override
    public void toggleBuyUnlimEnabled(boolean enabled) {
        callBtn.setEnabled(enabled);
    }

    @Override
    public void showTariffsSelector(final List<TariffResponse> tariffs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        final List<String> tariffNames = new ArrayList<>();
        for (TariffResponse response : tariffs) {
            tariffNames.add(getString(R.string.tariff_pattern, response.getName(), String.valueOf(response.getPrice())));
        }
        arrayAdapter.addAll(tariffNames);
        builder.setTitle(getString(R.string.reason_alert_title))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tariff = tariffs.get(which).getId();
                        presenter.tariffSelected(tariff);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void askAboutUtog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.utog_alert_title)
                .setMessage(R.string.utog_alert_text)
                .setCancelable(false)
                .setNegativeButton(R.string.utog_never, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        presenter.utogNever();
                    }
                })
                .setNeutralButton(R.string.utog_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        presenter.utogLater();
                    }
                })
                .setPositiveButton(R.string.utog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        presenter.utogConfirmed();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void navigateToUtog() {
        Intent intent = new Intent(this, UtogActivity.class);
        startActivity(intent);
    }


    @Override
    public void askAboutLastCall() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_LoaderDialog);
        builder.setTitle(R.string.review_title)
                .setView(R.layout.dialog_rate_call)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RatingBar ratingBar = ((AlertDialog) dialogInterface).findViewById(R.id.rating);
                        EditText review = ((AlertDialog) dialogInterface).findViewById(R.id.review);
                        presenter.onReview((int) ratingBar.getRating(), review.getText().toString());
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.onReviewSkipped();
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
