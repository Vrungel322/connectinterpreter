package com.getbewarned.connectinterpreter.ui;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.YandexKassaDataHolder;
import com.getbewarned.connectinterpreter.interfaces.PurchaseView;
import com.getbewarned.connectinterpreter.presenters.PurchasePresenter;

import java.math.BigDecimal;
import java.util.Currency;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import ru.yandex.money.android.sdk.Amount;
import ru.yandex.money.android.sdk.Checkout;
import ru.yandex.money.android.sdk.PaymentParameters;
import ru.yandex.money.android.sdk.SavePaymentMethod;
import ru.yandex.money.android.sdk.TokenizationResult;
import ru.yandex.money.android.sdk.utils.WebViewActivity;

public class PurchaseActivity extends NoStatusBarActivity implements PurchaseView {

    private static final int RC_PHONE_STATE_PERM = 483;
    private static final int REQUEST_CODE_TOKENIZE = 125;

    private VideoView videoView;
    private ImageView ivPlayingVideo;
    private Button bChooseTariff;
    private RecyclerView rvTariffs;

    private PurchasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        // presenter
        presenter = new PurchasePresenter(this, this);
        presenter.onCreate(savedInstanceState);

        // toolbar
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.tariff_choose);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // video
        setupVideo();

        // tariff list
        rvTariffs = findViewById(R.id.rv_tariffs);
        rvTariffs.setLayoutManager(new LinearLayoutManager(this));
        rvTariffs.setAdapter(presenter.getAdapter());

        // done
        bChooseTariff = findViewById(R.id.b_choose_tariff);
        bChooseTariff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bChooseTariff.isActivated()) {

                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        videoView.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void updateDoneBtn() {
        bChooseTariff.setActivated(presenter.getAdapter().selectedItem != null);
    }

    @Override
    public void errorReceivingTariffs(Error error) {
        Toast.makeText(this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void successPurchase() {
        onBackPressed();
    }

    @Override
    public void failPurchase(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    public void continueCheckout(String item, String itemDescription, float price, String currency) {
        PaymentParameters paymentParameters = new PaymentParameters(
                new Amount(BigDecimal.valueOf(price), Currency.getInstance(currency)),
                item,
                itemDescription,
                YandexKassaDataHolder.getClientApplicationKey(),
                YandexKassaDataHolder.getShopId(),
                SavePaymentMethod.OFF
        );
        Intent intent = Checkout.createTokenizeIntent(this, paymentParameters);
        startActivityForResult(intent, REQUEST_CODE_TOKENIZE);
//        WebViewActivity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TOKENIZE) {
            switch (resultCode) {
                case RESULT_OK:
                    // successful tokenization
                    TokenizationResult result = Checkout.createTokenizationResult(data);
                    //todo continue
                    break;
                case RESULT_CANCELED:
                    // user canceled tokenization
                    break;
            }
        }
    }

    private void setupVideo() {
        final Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tariff);
        videoView = findViewById(R.id.vv_purchse);
        ivPlayingVideo = findViewById(R.id.iv_play);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivPlayingVideo.setVisibility(View.VISIBLE);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        ivPlayingVideo.setVisibility(View.GONE);
                        return true;
                    }
                    return false;
                }
            });
        }

        ivPlayingVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivPlayingVideo.setVisibility(View.GONE);
                if (!videoView.isPlaying()) videoView.start();
            }
        });
        videoView.setVideoURI(video);
    }
}
