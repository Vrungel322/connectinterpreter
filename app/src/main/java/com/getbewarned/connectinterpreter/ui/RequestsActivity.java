package com.getbewarned.connectinterpreter.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.RequestFileSelection;
import com.getbewarned.connectinterpreter.interfaces.RequestsView;
import com.getbewarned.connectinterpreter.models.Request;
import com.getbewarned.connectinterpreter.presenters.RequestPresenter;
import com.getbewarned.connectinterpreter.presenters.RequestsPresenter;

public class RequestsActivity extends NoStatusBarActivity implements RequestsView {

    private FloatingActionButton createRequestButton;
    private RecyclerView requestsList;
    private View noRequestsView;

    private ProgressDialog loading;

    private RequestsPresenter presenter;

    private RequestFileSelection requestFileSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_v2);

        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.drawer_requests);
        ((ImageView) findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        requestFileSelector = new RequestFileSelector(this);

        createRequestButton = findViewById(R.id.create_request);
        requestsList = findViewById(R.id.requests_list);
        noRequestsView = findViewById(R.id.no_requests_view);

        createRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.createRequestPressed();
            }
        });

        this.presenter = new RequestsPresenter(this, this);
        this.presenter.onCreate(getIntent().getExtras());


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.requestsList.setLayoutManager(layoutManager);
        this.requestsList.setAdapter(this.presenter.getAdapter());

    }

    @Override
    public void showError(String message) {
        if (isFinishing()) {
            return;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void toggleListVisibility(boolean visible) {
        if (visible) {
            requestsList.setVisibility(View.VISIBLE);
            noRequestsView.setVisibility(View.INVISIBLE);
        } else {
            requestsList.setVisibility(View.INVISIBLE);
            noRequestsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void openImagePicker() {
        requestFileSelector.showChoiceSheet();
    }

    @Override
    public void openVideoPicker() {

    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap image = requestFileSelector.getImageFromActivityResult(requestCode, resultCode, data);
                if (image != null) {
                    presenter.onImageSelected(image);
                }
            }
        };
        Thread thread = new Thread() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        };

        thread.start();

    }

    @Override
    public void goToRequest(Request request) {
        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra(RequestPresenter.REQUEST_ID, request.getId());
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        this.loading = new ProgressDialog(this, R.style.AppTheme_LoaderDialog);
//        this.loading.setTitle();
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
}
