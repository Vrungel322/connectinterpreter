package com.getbewarned.connectinterpreter.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.UiUtils;
import com.getbewarned.connectinterpreter.analytics.Analytics;
import com.getbewarned.connectinterpreter.analytics.Events;
import com.getbewarned.connectinterpreter.interfaces.MainView;
import com.getbewarned.connectinterpreter.models.Reason;
import com.getbewarned.connectinterpreter.models.TariffResponse;
import com.getbewarned.connectinterpreter.presenters.CallPresenter;
import com.getbewarned.connectinterpreter.presenters.MainPresenter;
import com.getbewarned.connectinterpreter.ui.dialogs.ErrorDialog;
import com.getbewarned.connectinterpreter.ui.dialogs.HelpDialog;
import com.getbewarned.connectinterpreter.ui.dialogs.RateInterpreterListener;
import com.getbewarned.connectinterpreter.ui.dialogs.RateInterpreterDialog;
import com.getbewarned.connectinterpreter.ui.requests.RequestsActivity;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class NewMainActivity extends NoStatusBarActivity implements MainView {

    private static final int RC_VIDEO_APP_PERM = 387;
    private static final int RC_PHONE_STATE_PERM = 483;
    public static final String ZERO_TIME = "00:00";

    TextView availabilityTitleLabel;
    TextView tvMinutesExpiration;
    ImageView callBtn;
    ImageView requests;
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
        callBtn = findViewById(R.id.iv_call);
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
                // new ui
                Intent intent = new Intent(NewMainActivity.this, PurchaseActivity.class);
                startActivity(intent);
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
                getSupportFragmentManager().beginTransaction().add(new HelpDialog(), HelpDialog.TAG).commitAllowingStateLoss();
                Analytics.getInstance().trackEvent(Events.EVENT_ACTION_HELP);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMainActivity.this, ProfileActivityV2.class);
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

        // set shadows
        UiUtils.actionMainScreen(requests);
        UiUtils.actionMainScreen(help);


        presenter = new MainPresenter(this, this);
        presenter.onCreate(getIntent().getExtras());

        Analytics.getInstance().trackEvent(Events.EVENT_MAIN_OPENED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void showLeftTime(String leftTime) {
        if (leftTime.equals(ZERO_TIME)) {
            timer.setTextColor(ContextCompat.getColor(this, R.color.timer_color_with_minutes));
        } else {
            timer.setTextColor(ContextCompat.getColor(this, R.color.timer_color_without_minutes));
        }

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
//            availabilityTitleLabel.setText(R.string.not_available_title);
            availabilityTitleLabel.setText(R.string.available_minutes);
            noMinutesContainer.setVisibility(View.VISIBLE);
            callBtn.setVisibility(View.GONE);
            showLeftTime(ZERO_TIME);
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

    @Override
    public void showErrorNewUI(String message) {
        ErrorDialog dialog = new ErrorDialog();
        dialog.setErrorText(message);
        getSupportFragmentManager().beginTransaction().add(dialog, ErrorDialog.TAG).commitAllowingStateLoss();
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
        RateInterpreterDialog dialog = new RateInterpreterDialog();
        dialog.setListener(new RateInterpreterListener() {
            @Override
            public void onRateDone(int rating, String feedback) {
                presenter.onReview(rating, feedback);
            }

            @Override
            public void rateSkipped() {
                presenter.onReviewSkipped();
            }
        });
        getSupportFragmentManager().beginTransaction().add(dialog, "RateInterpreterDialog").commitAllowingStateLoss();
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
