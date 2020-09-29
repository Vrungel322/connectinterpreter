package com.getbewarned.connectinterpreter.ui.requests;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.NewRequestView;
import com.getbewarned.connectinterpreter.models.Request;
import com.getbewarned.connectinterpreter.presenters.NewRequestPresenter;
import com.getbewarned.connectinterpreter.presenters.RequestPresenter;
import com.getbewarned.connectinterpreter.ui.NoStatusBarActivity;
import com.getbewarned.connectinterpreter.ui.RequestActivity;

public class NewRequestActivity extends NoStatusBarActivity implements NewRequestView {
    NewRequestPresenter presenter;
    private ProgressDialog loading;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_request_activity);

        ((ImageView) findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView image = ((ImageView) findViewById(R.id.iv_image_request));
        image.setImageBitmap(RequestBitmapHolder.bitmap);

        findViewById(R.id.b_send_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.createRequest(RequestBitmapHolder.bitmap);
            }
        });

        presenter = new NewRequestPresenter(this, this);
        presenter.onCreate(getIntent().getExtras());
    }

    @Override
    protected void onDestroy() {
        RequestBitmapHolder.bitmap = null;
        super.onDestroy();
    }

    @Override
    public void goToRequest(Request request) {
        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra(RequestPresenter.REQUEST_ID, request.getId());
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoading() {
        this.loading = new ProgressDialog(this, R.style.AppTheme_LoaderDialog);
        this.loading.setMessage(getString(R.string.file_uploading));
        this.loading.setCancelable(false);
        this.loading.show();
    }

    @Override
    public void hideLoading() {
        if (this.loading != null) {
            this.loading.hide();
            this.loading = null;
        }
    }

    @Override
    public void showError(String message) {
        if (isFinishing()) {
            return;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
