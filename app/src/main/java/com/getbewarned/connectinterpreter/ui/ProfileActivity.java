package com.getbewarned.connectinterpreter.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.BaseRequestCompleted;
import com.getbewarned.connectinterpreter.interfaces.LiqPayDataReceived;
import com.getbewarned.connectinterpreter.interfaces.NameChanged;
import com.getbewarned.connectinterpreter.interfaces.TariffsReceived;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.LiqPayResponse;
import com.getbewarned.connectinterpreter.models.NameResponse;
import com.getbewarned.connectinterpreter.models.TariffResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponse;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import ua.privatbank.paylibliqpay.ErrorCode;
import ua.privatbank.paylibliqpay.LiqPay;
import ua.privatbank.paylibliqpay.LiqPayCallBack;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.drawer_profile);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ProfileFragment())
                .commit();


    }


    public static class ProfileFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

        private static final int RC_PHONE_STATE_PERM = 483;

        UserManager userManager;
        NetworkManager networkManager;
        private String selectedTariff;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            userManager = new UserManager(getContext());
            networkManager = new NetworkManager(getContext());
            networkManager.setAuthToken(userManager.getUserToken());
            addPreferencesFromResource(R.xml.profile);
            PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
            fillPreferences();
            findPreference(UserManager.USER_UTOG).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (userManager.getUserUtog()) {
                        return false;
                    }
                    Intent intent = new Intent(getContext(), UtogActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            findPreference("user_exit").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    logout();
                    return true;
                }
            });

            findPreference("user_buy").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    buyPressed();
                    return true;
                }
            });
        }

        private void fillPreferences() {
            findPreference(UserManager.USER_NAME).setSummary(userManager.getUserName());
            findPreference(UserManager.USER_PHONE).setSummary(userManager.getUserPhone());
            if (userManager.getUserUkrainian()) {
                findPreference(UserManager.USER_UTOG).setSummary(userManager.getUserUtog() ? R.string.utog_assigned : R.string.press_to_fill_utog);
            } else {
                findPreference(UserManager.USER_UTOG).setVisible(false);
            }

            if (userManager.getUserSeconds() > 0) {
                findPreference("user_buy").setVisible(false);
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case UserManager.USER_NAME:
                    networkManager.updateName(sharedPreferences.getString(key, ""), new NameChanged() {
                        @Override
                        public void onNameChanged(NameResponse response) {
                            fillPreferences();
                        }

                        @Override
                        public void onErrorReceived(Error error) {

                        }
                    });
            }
        }

        public void buyPressed() {
            networkManager.getTariffs(new TariffsReceived() {
                @Override
                public void onTariffsReceived(TariffsResponse response) {
                    showTariffsSelector(response.getTariffs());
                }

                @Override
                public void onErrorReceived(Error error) {

                }
            });
        }

        public void showTariffsSelector(final List<TariffResponse> tariffs) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item);
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
                            selectedTariff = tariffs.get(which).getId();
                            requestLiqPayPermissions();
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        @AfterPermissionGranted(RC_PHONE_STATE_PERM)
        public void requestLiqPayPermissions() {
            String[] perms = {Manifest.permission.READ_PHONE_STATE};
            if (EasyPermissions.hasPermissions(getContext(), perms)) {
                networkManager.buyUnlim(selectedTariff, new LiqPayDataReceived() {
                    @Override
                    public void onDataReceived(LiqPayResponse response) {
                        LiqPay.checkout(getContext(), response.getData(), response.getSignature(), new LiqPayCallBack() {
                            @Override
                            public void onResponseSuccess(String s) {
                                Log.i("PAYMENT SUCCESS", s);
                                getActivity().finish();
                            }

                            @Override
                            public void onResponceError(ErrorCode errorCode) {
                                Log.i("PAYMENT FAIL", errorCode.toString());
                            }
                        });
                    }

                    @Override
                    public void onErrorReceived(Error error) {
                        Log.i("PAYMENT FAIL", error.getMessage());

                    }
                });
                selectedTariff = null;
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.permission_rationale_liqpay), RC_PHONE_STATE_PERM, perms);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            fillPreferences();
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }

        @Override
        public void onDestroy() {
            PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
            super.onDestroy();
        }

        public void logout() {
            networkManager.logout(new BaseRequestCompleted() {
                @Override
                public void onComplete(ApiResponseBase response) {
                    if (response.isSuccess()) {
                        navigateToLogin();
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.deleteAll();
                            }
                        });
                    } else {
                        navigateToLogin();
                    }
                }

                @Override
                public void onErrorReceived(Error error) {
                    navigateToLogin();
                }
            });
        }

        public void navigateToLogin() {
            userManager.updateUserToken(null);
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
