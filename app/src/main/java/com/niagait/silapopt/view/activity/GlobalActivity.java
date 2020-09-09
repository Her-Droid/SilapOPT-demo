package com.niagait.silapopt.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.niagait.silapopt.R;
import com.niagait.silapopt.view.helper.PermissionHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class GlobalActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private Context context;
    private Intent intent;
    private PermissionHelper permissionHelper;
    private File mediaFile;

    private BottomNavigationView navigation;
    private Button btnChooseImage,
            btnTambahKab,
            btnSimpanDataGlobal;
    private ImageView imgSpecimen;
    private SwipeRefreshLayout swipeRefresh;
    private EditText edtLokasiKebun,
            edtLuasKomoditas,
            edtLuasWilayah,
            edtBagianYangTerserang,
            edtDeskripsi;

    private static final int bitmap_size = 40;
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;

    private Bitmap bitmapSpecimen = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global);
        inisialisasiView();
        setView();
    }

    @SuppressLint("InflateParams")
    @Override
    public void onClick(View v) {
        if (v == btnChooseImage) {
            selectImage();
        } else if (v == btnSimpanDataGlobal) {
            AlertDialog.Builder alertbuilder = new AlertDialog.Builder(context);
            alertbuilder.setCancelable(true);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            alertbuilder.setView(inflater.inflate(R.layout.alert_simpan_data, null));
            AlertDialog alert = alertbuilder.create();
            alert.show();

            TextView lblTitle = alert.findViewById(R.id.lblTitle);
            TextView lblMessage = alert.findViewById(R.id.lblMessage);
            Button btnBatal = alert.findViewById(R.id.btnBatal);
            Button btnOke = alert.findViewById(R.id.btnOke);

            if (lblTitle != null && lblMessage != null && btnBatal != null && btnOke != null) {
                lblMessage.setText(getResources().getString(R.string.konfirimasisimpan));
                lblTitle.setText(getResources().getString(R.string.konfirmasi));
                btnOke.setOnClickListener(v1 -> {
                    alert.dismiss();
                    simpanData();
                });
                btnBatal.setOnClickListener(v1 -> alert.dismiss());
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_dataopt:
                intent = new Intent(context, DataActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_maps:
                break;
            case R.id.nav_weather:
                intent = new Intent(context, IklimActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_workplan:
                intent = new Intent(context, RencanaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap bitmap;
            int max_resolution_image = 800;
            if (requestCode == REQUEST_CAMERA) {
                try {
                    bitmap = BitmapFactory.decodeFile(mediaFile.getPath());

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.MediaColumns.DATA, mediaFile.getPath());
                    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    setToImageView(rotateImage(getResizedBitmap(bitmap, max_resolution_image)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
                try {
                    if (android.os.Build.VERSION.SDK_INT >= 29) {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), data.getData()));
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    }
                    mediaFile = new File(getPath(data.getData()));
                    setToImageView(rotateImage(getResizedBitmap(bitmap, max_resolution_image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void inisialisasiView() {
        context = GlobalActivity.this;
        permissionHelper = new PermissionHelper(GlobalActivity.this);

        navigation = findViewById(R.id.navigation);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        imgSpecimen = findViewById(R.id.imgSpecimen);
        btnTambahKab = findViewById(R.id.btnTambahKab);
        btnSimpanDataGlobal = findViewById(R.id.btnSimpanDataGlobal);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        edtLokasiKebun = findViewById(R.id.edtLokasiKebun);
        edtLuasKomoditas = findViewById(R.id.edtLuasKomoditas);
        edtLuasWilayah = findViewById(R.id.edtLuasWilayah);
        edtBagianYangTerserang = findViewById(R.id.edtBagianYangTerserang);
        edtDeskripsi = findViewById(R.id.edtDeskripsi);
    }

    private void setView() {
        permissionHelper.checkAndRequestPermission();
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.nav_maps).setChecked(true);

        btnChooseImage.setOnClickListener(this);
        btnSimpanDataGlobal.setOnClickListener(this);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.birumuda);
        swipeRefresh.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            swipeRefresh.setRefreshing(false);
            edtLokasiKebun.setText(null);
            edtLuasKomoditas.setText(null);
            edtLuasWilayah.setText(null);
            edtBagianYangTerserang.setText(null);
            edtDeskripsi.setText(null);
        }, 2000));
    }

    @SuppressLint("IntentReset")
    private void selectImage() {
        final CharSequence[] items = {"Ambil Foto", "Pilih dari Galerry", "Batal"};
        //Context context = getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Ambil Foto")) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri fileUri = getOutputMediaFileUri();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, REQUEST_CAMERA);
            } else if (items[item].equals("Pilih dari Galerry")) {
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
            } else {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public String getPath(Uri uri) {
        String schema = uri.getScheme();
        if ("http".equals(schema) || "https".equals(schema)) {
            return uri.toString();
        } else if (ContentResolver.SCHEME_CONTENT.equals(schema)) {
            String[] projection = new String[]{MediaStore.MediaColumns._ID};
            Cursor cursor = null;
            String filePath = "";
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(0);
                    }

                    cursor.close();
                }
            } catch (Exception e) {
                // do nothing
            } finally {
                try {
                    if (null != cursor) {
                        cursor.close();
                    }
                } catch (Exception e2) {
                    // do nothing
                }
            }
            if (TextUtils.isEmpty(filePath)) {
                try {
                    ContentResolver contentResolver = context.getContentResolver();
                    String selection = MediaStore.Images.Media._ID + "= ?";
                    String id = uri.getLastPathSegment();
                    if (id != null && !TextUtils.isEmpty(id) && id.contains(":")) {
                        id = id.split(":")[1];
                    }
                    String[] selectionArgs = new String[]{id};
                    cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        filePath = cursor.getString(0);
                        cursor.close();
                    }

                } catch (Exception e) {
                    // do nothing
                } finally {
                    try {
                        if (cursor != null) {
                            cursor.close();
                        }
                    } catch (Exception e) {
                        // do nothing
                    }
                }
            }
            return filePath;
        }

        return "";
    }

    public Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/silapopt/");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Monitoring", "Failed created directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String path = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";
        mediaFile = new File(path);

        return FileProvider.getUriForFile(context, "com.niagait.silapopt", Objects.requireNonNull(mediaFile));
    }

    private Bitmap rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mediaFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = 0;
        if (exifInterface != null) {
            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        }
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(0);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void setToImageView(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        bitmapSpecimen = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        Glide.with(context)
                .load(bitmapSpecimen)
                .into(imgSpecimen);
    }


    //METHOD SIMPAN DATA
    private void simpanData() {

    }

}
