
package com.getbewarned.connectinterpreter.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.adapters.RequestMessagesAdapter;
import com.getbewarned.connectinterpreter.interfaces.RequestFileSelection;
import com.getbewarned.connectinterpreter.interfaces.RequestView;
import com.getbewarned.connectinterpreter.models.Request;
import com.getbewarned.connectinterpreter.presenters.RequestPresenter;

public class RequestActivity extends NoStatusBarActivity implements RequestView {

    private RecyclerView messagesList;
    private Button sendPhotoBtn;
    private View sendPhotoBtnHolder;

    private RequestPresenter presenter;

    private ProgressDialog loading;

    private RequestFileSelection requestFileSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_v2);

        ((ImageView) findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
    public void updateUi(Request request) {
        ((TextView)findViewById(R.id.tv_toolbar_title)).setText(request.getName());
        TextView tvStatus = findViewById(R.id.tv_request_state);
        tvStatus.setText(getStatus(request));
        if (request.getStatus().equals("new")){
            tvStatus.setTextColor(getResources().getColor(R.color.blue_new_ui));
            tvStatus.setBackgroundResource(R.drawable.request_new_list_item);
        }
        if (request.getStatus().equals("assigned")){
//            todo set valid colors
            tvStatus.setTextColor(getResources().getColor(android.R.color.white));
            tvStatus.setBackgroundResource(R.drawable.request_in_progress_list_item);
        }
        if (request.getStatus().equals("closed")){
            tvStatus.setTextColor(getResources().getColor(android.R.color.white));
            tvStatus.setBackgroundResource(R.drawable.request_closed_list_item);
        }
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

    public String getStatus(Request request) {
        switch (request.getStatus()) {
            case "new":
                return getString(R.string.status_new);
            case "assigned":
                return getString(R.string.status_assigned);
            case "closed":
                return getString(R.string.status_closed);
            default:
                return "";
        }
    }
}
