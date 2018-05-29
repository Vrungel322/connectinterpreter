package com.getbewarned.connectinterpreter.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.MenuItem;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.RequestFileSelection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ru.whalemare.sheetmenu.SheetMenu;

import static android.app.Activity.RESULT_OK;

public class RequestFileSelector implements RequestFileSelection {

    private Context context;
    private Activity activity;

    private Uri imageUri;

    private static final int PICK_IMAGE = 1;
    private static final int CAPTURE_IMAGE = 2;
    private static final int PICK_VIDEO = 3;

    public RequestFileSelector(Activity activity) {
        this.context = activity;
        this.activity = activity;
    }

    @Override
    public void showChoiceSheet() {
        SheetMenu.with(context)
                .setMenu(R.menu.menu_media_selector)
                .setClick(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.media_image_gallery) {
                            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            getIntent.setType("image/*");

                            activity.startActivityForResult(getIntent, PICK_IMAGE);
                            return true;
                        } else if (item.getItemId() == R.id.media_image_camera) {
                            File photo;
                            try {
                                photo = File.createTempFile("picture", ".jpg", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                            } catch (Exception e) {
                                Toast.makeText(context, "Please check SD card! Image shot is impossible!", Toast.LENGTH_LONG).show();
                                return false;
                            }
                            imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", photo);
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                                activity.startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
                            }
                            return true;
                        }
                        return false;
                    }
                }).show();
    }

    public Bitmap getImageFromActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            try {
                InputStream stream = context.getContentResolver().openInputStream(data.getData());
                return BitmapFactory.decodeStream(stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {
            try {
                InputStream stream = context.getContentResolver().openInputStream(imageUri);
                return BitmapFactory.decodeStream(stream);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }
}
