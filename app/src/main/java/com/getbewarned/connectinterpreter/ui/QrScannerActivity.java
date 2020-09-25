package com.getbewarned.connectinterpreter.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.managers.QrCodeManager;
import com.getbewarned.connectinterpreter.presenters.GroupSessionPresenter;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class QrScannerActivity extends NoStatusBarActivity {

    private SurfaceView scannerSurface;
    private QREader qrEader;
    private QrCodeManager qrCodeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        // toolbar
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.qr_scanner);
        ((ImageView) findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        scannerSurface = findViewById(R.id.scanner_surface);
        qrCodeManager = new QrCodeManager();

        qrEader = new QREader.Builder(this, scannerSurface, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                QrScannerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkQrResult(data);
                    }
                });

            }
        })
                .facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(scannerSurface.getHeight())
                .width(scannerSurface.getWidth())
                .build();
    }

    private void checkQrResult(String data) {
        String sessionId = qrCodeManager.getGroupSessionId(data);
        if (sessionId != null) {
            qrEader.releaseAndCleanup();
            Intent intent = new Intent(this, GroupSessionActivity.class);
            intent.putExtra(GroupSessionPresenter.SESSION_ID, sessionId);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrEader.initAndStart(scannerSurface);
    }

    @Override
    protected void onPause() {
        qrEader.releaseAndCleanup();
        super.onPause();
    }
}
