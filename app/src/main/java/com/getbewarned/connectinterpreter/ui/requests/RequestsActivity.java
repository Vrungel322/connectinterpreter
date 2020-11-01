package com.getbewarned.connectinterpreter.ui.requests;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.UiUtils;
import com.getbewarned.connectinterpreter.interfaces.RequestFileSelection;
import com.getbewarned.connectinterpreter.interfaces.RequestsView;
import com.getbewarned.connectinterpreter.models.Request;
import com.getbewarned.connectinterpreter.presenters.RequestPresenter;
import com.getbewarned.connectinterpreter.presenters.RequestsPresenter;
import com.getbewarned.connectinterpreter.ui.NoStatusBarActivity;
import com.getbewarned.connectinterpreter.ui.RequestActivity;
import com.getbewarned.connectinterpreter.ui.RequestFileSelector;

import java.io.File;

import static com.getbewarned.connectinterpreter.ui.RequestFileSelector.CAPTURE_IMAGE;
import static com.getbewarned.connectinterpreter.ui.RequestFileSelector.PICK_IMAGE;

public class RequestsActivity extends NoStatusBarActivity implements RequestsView {

    // Fab anim settings
    private static final Long ANIMATION_DURATION = 250L;
    private static final int FROM_ANGLE = 0;
    private static final int TO_ANGLE = 45;
    private static final float FROM_SCALE_DOWN = 1.f;
    private static final float TO_SCALE_DOWN = 0.7f;

    private ImageView createRequestButton;
    private RecyclerView requestsList;
    private View noRequestsView;
    private LinearLayout llAddImage;

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
        llAddImage = findViewById(R.id.ll_add_image);

        createRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.createRequestPressed();
            }
        });
        findViewById(R.id.tv_pick_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });
        findViewById(R.id.tv_make_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });


        this.presenter = new RequestsPresenter(this, this);
        this.presenter.onCreate(getIntent().getExtras());


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.requestsList.setLayoutManager(layoutManager);
        this.requestsList.setAdapter(this.presenter.getAdapter());

        // help dialog
        Intent intent = new Intent(RequestsActivity.this, HelpRequestActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (llAddImage.getVisibility() == View.VISIBLE) {
            hideSelectionImageMenu();
            return;
        }
        super.onBackPressed();
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
        if (llAddImage.getVisibility() == View.GONE) {
            showSelectionImageMenu();
        } else {
            hideSelectionImageMenu();
        }
//        requestFileSelector.showChoiceSheet();
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
                hideSelectionImageMenu();
                Bitmap image = requestFileSelector.getImageFromActivityResult(requestCode, resultCode, data);
                if (image != null) {
//                    presenter.onImageSelected(image);
                    RequestBitmapHolder.bitmap = image;
                    Intent intent = new Intent(RequestsActivity.this, NewRequestActivity.class);
                    startActivity(intent);
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

    private void showSelectionImageMenu() {
        UiUtils.showAnimated(llAddImage, Techniques.SlideInUp, ANIMATION_DURATION);
        UiUtils.rotateAnimated(createRequestButton, FROM_ANGLE, TO_ANGLE, ANIMATION_DURATION);
        UiUtils.scaleToCenterAnimated(createRequestButton, FROM_SCALE_DOWN, TO_SCALE_DOWN, ANIMATION_DURATION);
    }

    private void hideSelectionImageMenu() {
        UiUtils.hideAnimated(llAddImage, Techniques.SlideOutDown, ANIMATION_DURATION);
        UiUtils.rotateAnimated(createRequestButton, TO_ANGLE, FROM_ANGLE, ANIMATION_DURATION);
        UiUtils.scaleToCenterAnimated(createRequestButton, TO_SCALE_DOWN, FROM_SCALE_DOWN, ANIMATION_DURATION);
    }

    private void takePhoto() {
        File photo = null;
        try {
            photo = File.createTempFile("picture", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        } catch (Exception e) {
            Toast.makeText(RequestsActivity.this, "Please check SD card! Image shot is impossible!", Toast.LENGTH_LONG).show();
        }
        RequestFileSelector.imageUri = FileProvider.getUriForFile(RequestsActivity.this, getPackageName() + ".provider", photo);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, RequestFileSelector.imageUri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
        }
    }

    private void pickPhoto() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        startActivityForResult(getIntent, PICK_IMAGE);
    }
}
