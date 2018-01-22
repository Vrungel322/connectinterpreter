package com.getbewarned.connectinterpreter.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.MainView;
import com.getbewarned.connectinterpreter.models.TariffResponse;
import com.getbewarned.connectinterpreter.presenters.CallPresenter;
import com.getbewarned.connectinterpreter.presenters.MainPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final int RC_VIDEO_APP_PERM = 387;
    private static final int RC_PHONE_STATE_PERM = 483;

    private ImageButton callBtn;
    private TextView minutesLeft;
    private TextView callToAction;
    private TextView leftLabel;
    private View availableHolder;
    private View notAvailableHolder;

    private Button buyUnlimButton;

    private Menu menu;
    private String userName;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        callBtn = findViewById(R.id.call_button);
        minutesLeft = findViewById(R.id.left_value_label);
        callToAction = findViewById(R.id.call_to_action);
        leftLabel = findViewById(R.id.left_label);
        buyUnlimButton = findViewById(R.id.buy_unlim);

        availableHolder = findViewById(R.id.available_holder);
        notAvailableHolder = findViewById(R.id.not_available_holder);

        presenter = new MainPresenter(this);


        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onStartCallPressed();
            }
        });

        buyUnlimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.buyUnlimPressed();
            }
        });

        presenter.onCreate(getIntent().getExtras());
    }

    @Override
    public void showLeftTime(String leftTime) {
        leftLabel.setText(R.string.main_minutes_left);
        minutesLeft.setText(leftTime);
    }

    @Override
    public void toggleCallAvailability(boolean available) {
        callBtn.setEnabled(available);
        if (available) {
            availableHolder.setVisibility(View.VISIBLE);
            notAvailableHolder.setVisibility(View.GONE);
            callBtn.setBackgroundResource(R.drawable.call_button_background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                callBtn.setImageDrawable(getDrawable(R.drawable.ic_telephone));
            } else {
                callBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_telephone));
            }
            callToAction.setText(R.string.main_call_to_action);
        } else {
            availableHolder.setVisibility(View.GONE);
            notAvailableHolder.setVisibility(View.VISIBLE);
            callBtn.setBackgroundResource(R.drawable.no_call_button_background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                callBtn.setImageDrawable(getDrawable(R.drawable.ic_hourglass));
            } else {
                callBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_hourglass));
            }
            callToAction.setText(R.string.no_minutes_left);
        }
    }

    @Override
    public void showChecking() {
        callBtn.setEnabled(false);
        callBtn.setBackgroundResource(R.drawable.no_call_button_background);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            callBtn.setImageDrawable(getDrawable(R.drawable.ic_hourglass));
        } else {
            callBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_hourglass));
        }
        callToAction.setText(R.string.main_wait_minutes_loading);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showError(String message) {
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
    public void navigateToCall() {
        Intent intent = new Intent(this, CallActivity.class);
        startActivity(intent);
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
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_name, null);
        final EditText nameInput = view.findViewById(R.id.name_input);
        AlertDialog nameDialog = builder.setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.proceed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String name = nameInput.getText().toString();
                        if (name.isEmpty()) {
                            return;
                        }
                        presenter.nameChanged(name);
                        dialog.dismiss();
                    }
                }).create();
        nameInput.setText(name);
        nameDialog.show();
    }

    @Override
    public void updateUserName(String name) {
        if (this.menu == null) {
            userName = name;
            return;
        }
        this.menu.findItem(R.id.item_user_name).setTitle(name);
    }


    @Override
    public void askForReason() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_LoaderDialog);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
        arrayAdapter.addAll(getResources().getStringArray(R.array.call_reasons));

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
                        String reason = arrayAdapter.getItem(which);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        updateUserName(userName);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_user_name) {
            presenter.userNamePressed();
            return true;
        }
        if (item.getItemId() == R.id.item_logout) {
            presenter.logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showDateTill(String dateTill) {
        leftLabel.setText(R.string.main_active_till);
        minutesLeft.setText(dateTill);
    }

    @Override
    public void toggleBuyUnlimEnabled(boolean enabled) {
        buyUnlimButton.setEnabled(enabled);
    }

    @Override
    public void showTariffsSelector(final List<TariffResponse> tariffs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        final List<String> tariffNames = new ArrayList<>();
        for (TariffResponse response : tariffs) {
            tariffNames.add(getString(R.string.tariff_pattern, response.getName(), response.getPrice()));
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
}
