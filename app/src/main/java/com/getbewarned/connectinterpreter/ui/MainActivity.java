package com.getbewarned.connectinterpreter.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.MainView;
import com.getbewarned.connectinterpreter.models.Reason;
import com.getbewarned.connectinterpreter.models.TariffResponse;
import com.getbewarned.connectinterpreter.presenters.CallPresenter;
import com.getbewarned.connectinterpreter.presenters.MainPresenter;
import com.getbewarned.connectinterpreter.ui.requests.RequestsActivity;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_VIDEO_APP_PERM = 387;
    private static final int RC_PHONE_STATE_PERM = 483;

    private ImageButton callBtn;
    private TextView minutesLeft;
    private TextView callToAction;
    private TextView leftLabel;
    private View availableHolder;
    private View notAvailableHolder;
    private TextView notAvailableDesc;
    private TextView userNameLabel;
    private ImageButton editUserNameBtn;
    private TextView workTime;

    private Button buyUnlimButton;

    private Menu menu;
    private String userName;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.menu = navigationView.getMenu();
        callBtn = findViewById(R.id.call_button);
        minutesLeft = findViewById(R.id.left_value_label);
        callToAction = findViewById(R.id.call_to_action);
        leftLabel = findViewById(R.id.availability_desc);
        buyUnlimButton = findViewById(R.id.buy_unlim);
        View headerLayout = navigationView.getHeaderView(0);
        userNameLabel = headerLayout.findViewById(R.id.drawer_username);
        editUserNameBtn = headerLayout.findViewById(R.id.edit_user_name);

        availableHolder = findViewById(R.id.available_holder);
        notAvailableHolder = findViewById(R.id.not_available_holder);

        notAvailableDesc = findViewById(R.id.not_available_desc);
        workTime = findViewById(R.id.work_time);

        presenter = new MainPresenter(this, this);


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

        editUserNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                presenter.userNamePressed();
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
    public void toggleCallAvailability(boolean available, boolean isUtog) {
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
            if (isUtog) {
                notAvailableDesc.setText(R.string.not_available_desc_utog);
            } else {
                notAvailableDesc.setText(R.string.not_available_desc);
            }
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
        userNameLabel.setText(name);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.drawer_utog) {
            presenter.utogConfirmed();
        } else if (id == R.id.drawer_unlim) {
            presenter.buyUnlimPressed();
        } else if (id == R.id.drawer_exit) {
            presenter.logout();
        } else if (id == R.id.drawer_support) {
            openHelpPage();
        } else if (id == R.id.drawer_requests) {
            Intent intent = new Intent(this, RequestsActivity.class);
            startActivity(intent);
        } else if (id == R.id.drawer_share) {
            String share = getString(R.string.share_text);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, share);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.drawer_share)));
        } else if (id == R.id.drawer_facebook) {
            String url = "https://www.facebook.com/GetBeWarned";
            Uri uri = Uri.parse(url);
            try {
                ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo("com.facebook.katana", 0);
                if (applicationInfo.enabled) {
                    uri = Uri.parse("fb://facewebmodal/f?href=" + url);
                }
            } catch (PackageManager.NameNotFoundException ignored) {
            }
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } else if (id == R.id.drawer_instagram) {

            Uri uri = Uri.parse("http://instagram.com/_u/get_bewarned");
            Intent instagramIntent = new Intent(Intent.ACTION_VIEW, uri);

            instagramIntent.setPackage("com.instagram.android");

            try {
                startActivity(instagramIntent);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/get_bewarned")));
            }

        } else if (id == R.id.drawer_telegram) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=BeWarned"));
            intent.setPackage("org.telegram.messenger");

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/BeWarned")));
            }
        } else if (id == R.id.drawer_scan_qr) {
            Intent intent = new Intent(MainActivity.this, QrScannerActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_help) {
            openHelpPage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openHelpPage() {
        Uri uri = Uri.parse("https://interpreter.getbw.me/help");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
