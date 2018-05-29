
package com.getbewarned.connectinterpreter.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.adapters.RequestMessagesAdapter;
import com.getbewarned.connectinterpreter.interfaces.RequestFileSelection;
import com.getbewarned.connectinterpreter.interfaces.RequestView;
import com.getbewarned.connectinterpreter.presenters.RequestPresenter;

public class RequestActivity extends AppCompatActivity implements RequestView {

    private RecyclerView messagesList;
    private Button sendPhotoBtn;
    private View sendPhotoBtnHolder;

    private RequestPresenter presenter;

    private ProgressDialog loading;

    private RequestFileSelection requestFileSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        messagesList = findViewById(R.id.messages_list);
        sendPhotoBtn = findViewById(R.id.send_photo);
        sendPhotoBtnHolder = findViewById(R.id.send_photo_holder);

        requestFileSelector = new RequestFileSelector(this);

        this.presenter = new RequestPresenter(this, this);
        this.presenter.onCreate(getIntent().getExtras());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messagesList.setLayoutManager(layoutManager);
        final RequestMessagesAdapter adapter = presenter.getAdapter();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                layoutManager.scrollToPosition(adapter.getItemCount());
            }
        });
        messagesList.setAdapter(adapter);
        layoutManager.scrollToPosition(adapter.getItemCount());


        sendPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestFileSelector.showChoiceSheet();
            }
        });

    }

    @Override
    public void goBack() {
        finish();
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void hideSendButtons() {
        sendPhotoBtnHolder.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap image = requestFileSelector.getImageFromActivityResult(requestCode, resultCode, data);
                if (image != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            presenter.imageSelected(image);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void showImagePreview(String url) {
//        startActivity(ImagePreviewActivity.getInstance(this, url));
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "image/png");
        startActivity(intent);
    }

    @Override
    public void showVideoPreview(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "video/mp4");
        startActivity(intent);
//        startActivity(VideoPlayerActivity.getInstance(this, url));
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
    public void scrollToBottom() {
        this.messagesList.getLayoutManager().scrollToPosition(messagesList.getAdapter().getItemCount() - 1);
    }
}
