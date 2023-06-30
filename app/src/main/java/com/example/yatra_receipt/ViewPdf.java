package com.example.yatra_receipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.button.MaterialButton;

import java.io.File;

public class ViewPdf extends AppCompatActivity {

    PDFView pdfView;
    TextView pdfName;
    MaterialButton shareBtn;
    String mobileNumber;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        pdfView = findViewById(R.id.viewPdf);
        pdfName = findViewById(R.id.pdfName);
        shareBtn = findViewById(R.id.sharePdfBtn);
        mobileNumber = getIntent().getStringExtra("mobileNo");
        name = getIntent().getStringExtra("name");

        // POLICY
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        // VIEW PDF.
        String receiptName = "Receipt(" + mobileNumber + ").pdf";
        String receiptDisplayName = "Receipt(" + name + ").pdf";
        pdfName.setText(receiptDisplayName);
        String path = getDownloadsFilePath(receiptName);

        System.out.println("PATH HERE IS :" + path);

        Context context = getApplicationContext();
        // Save to firebase function goes here.
        saveToFirebase(context, receiptName, path);

        File file = new File(path);

        System.out.println("FILE HERE IS : " + file);
        pdfView.fromFile(file)
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .enableAnnotationRendering(true)
                .defaultPage(0)
                .scrollHandle(null)
                .password(null)
                .load();

        // SHARE PDF FUNCTION.
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSharePdf();
            }
        });

    }

    private void onSharePdf() {
        String receiptName = "Receipt(" + mobileNumber + ").pdf";
        String path = getDownloadsFilePath(receiptName);
        File file = new File(path);
        
        if (file.exists()) {
            Uri pdfUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    file
            );

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share PDF"));
        } else {
            Toast.makeText(ViewPdf.this, "Error in sharing !!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getDownloadsFilePath(String receiptName) {
        String filePath = null;
        if (isExternalStorageAvailable()) {
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (downloadsDir != null) {
                File file = new File(downloadsDir, receiptName);
                filePath = file.getAbsolutePath();
            }
        }
        return filePath;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void saveToFirebase(Context context, String fileName, String filePath) {
        SaveToFirebase save = new SaveToFirebase(context);
        save.SaveToStorage(fileName, filePath);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}