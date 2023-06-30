package com.example.yatra_receipt;

import static io.realm.Realm.getApplicationContext;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;


public class SavePdf {
    String nameTexts, gamTexts, mobileNoTexts, tithiYatraTexts, peopleTexts, childrenTexts, amountTexts, svikarnarTexts;
    String depositTexts, bakiTexts, amountInWords;
    Integer receiptNumber;
    Integer count = 1;

    Bitmap bmp, scaledbmp, bmp1, scaledbmp1;

    // Get Application Context;
    Context context = getApplicationContext();

    public SavePdf() {
    }

    public void createAndSavePdf(Bitmap userImage, String name, String gam, String mobileNo, String tithiYatra, String people, String children,
                                 String amount, String deposit, String baki, String currentData, String sivkarnar) {
        // Assigning fetched value to variable of this class.
        nameTexts = name;
        gamTexts = gam;
        mobileNoTexts = mobileNo;
        tithiYatraTexts = tithiYatra;
        peopleTexts = people;
        childrenTexts = children;
        amountTexts = amount;
        depositTexts = deposit;
        bakiTexts = baki;
        svikarnarTexts = sivkarnar;

//        fetchReceiptCount();

        System.out.println("Count : " + receiptNumber);

        // RETRIVE BHET AMOUNT AND PASS IT TO CONVERT IN WORDS
        String donation = amountTexts;
        amountInWords = NumbersToWords.convertToIndianCurrency(donation);

        // Creating PDF.
        if (userImage == null) {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.img1);
        } else {
            bmp = userImage;
        }
        scaledbmp = Bitmap.createScaledBitmap(bmp, 832, 1020, false);

        bmp1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img2);
        scaledbmp1 = Bitmap.createScaledBitmap(bmp1, 1099, 1020, false);

        PdfDocument myPdfDocument = new PdfDocument();

        Paint leftImagePaint = new Paint();
        Paint rightImagePaint = new Paint();
        Paint receiptPaint = new Paint();
        Paint pochPaint = new Paint();
        Paint datePaint = new Paint();
        Paint bhavdiyaPaint = new Paint();
        Paint gamPaint = new Paint();
        Paint mobileNoPaint = new Paint();
        Paint tithiYatraPaint = new Paint();
        Paint amountWordPaint = new Paint();
        Paint obtainedPaint = new Paint();
        Paint amountPaint = new Paint();
        Paint tarikhPaint = new Paint();
        Paint peoplePaint = new Paint();
        Paint childrenPaint = new Paint();
        Paint depositPaint = new Paint();
        Paint bakiPaint = new Paint();
        Paint sivkarnarPaint = new Paint();

        // TEXT PAINTS.
        Paint receiptNoText = new Paint();
        Paint nameText = new Paint();
        Paint gamText = new Paint();
        Paint mobileNoText = new Paint();
        Paint tithYatraText = new Paint();
        Paint totalPeople = new Paint();
        Paint totalChildren = new Paint();
        Paint depositText = new Paint();
        Paint bakiText = new Paint();
        Paint dateText = new Paint();
        Paint amountText = new Paint();
        Paint wordText = new Paint();
        Paint svikarnarText = new Paint();


        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1935, 1020, 1).create();
        PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage1.getCanvas();

        canvas.drawBitmap(scaledbmp, 0,0,leftImagePaint);
        canvas.drawBitmap(scaledbmp1, 832, 0, rightImagePaint);

        // Receipt Number
        receiptPaint.setTextAlign(Paint.Align.LEFT);
        receiptPaint.setTextSize(38f);
        receiptPaint.setColor(Color.rgb(160, 32, 240));
        canvas.drawText("Receipt No. ", 838, 480, receiptPaint);

        // Receipt Number Text
        receiptNoText.setColor(Color.BLACK);
        receiptNoText.setTextSize(34f);
        receiptNoText.setTextAlign(Paint.Align.LEFT);
//        canvas.drawText(String.valueOf(receiptNumber), 850, 230, receiptNoText);
        canvas.drawText("1501", 1040, 480, receiptNoText);

        // પહોંચ
        pochPaint.setTextAlign(Paint.Align.CENTER);
        pochPaint.setTextSize(44f);
        pochPaint.setColor(Color.rgb(255, 0, 0));
        canvas.drawText("પહોંચ", 1380, 480, pochPaint);

        // Date
        datePaint.setTextAlign(Paint.Align.RIGHT);
        datePaint.setTextSize(34f);
        datePaint.setColor(Color.rgb(160, 32, 240));
        canvas.drawText("Dt. ", 1935 - 250, 480, datePaint);

        // Date text
        dateText.setTextAlign(Paint.Align.RIGHT);
        dateText.setTextSize(34f);
        dateText.setColor(Color.BLACK);
        canvas.drawText(currentData, 1935 - 50, 480, dateText);

        // Bhavdiya shree
        bhavdiyaPaint.setTextAlign(Paint.Align.LEFT);
        bhavdiyaPaint.setColor(Color.rgb(0, 0, 139));
        bhavdiyaPaint.setTextSize(36f);
        canvas.drawText("ભવદિય શ્રી : ", 842, 560, bhavdiyaPaint);

        // Bhavdiya name text
        nameText.setTextAlign(Paint.Align.LEFT);
        nameText.setColor(Color.BLACK);
        nameText.setTextSize(34f);
        canvas.drawText(nameTexts.toUpperCase(Locale.ROOT), 1008, 560, nameText);

        // gam
        gamPaint.setTextAlign(Paint.Align.LEFT);
        gamPaint.setTextSize(36f);
        gamPaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("ગામ : ", 842, 620, gamPaint);

        // gam text
        String tempGamText = gamTexts.substring(0, 1).toUpperCase() + gamTexts.substring(1).toLowerCase();
        gamText.setTextAlign(Paint.Align.LEFT);
        gamText.setTextSize(34f);
        gamText.setColor(Color.BLACK);
        canvas.drawText(tempGamText, 934, 620, gamText);

        // mobileNumber
        mobileNoPaint.setTextAlign(Paint.Align.RIGHT);
        mobileNoPaint.setTextSize(36f);
        mobileNoPaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("મોબાઈલ નંબર : ", 1935 - 240, 620, mobileNoPaint);

        // mobileNumber text
        mobileNoText.setTextAlign(Paint.Align.RIGHT);
        mobileNoText.setTextSize(34f);
        mobileNoText.setColor(Color.BLACK);
        canvas.drawText(mobileNoTexts, 1935 - 50, 620, mobileNoText);

        // tithi yatra
        tithiYatraPaint.setTextAlign(Paint.Align.LEFT);
        tithiYatraPaint.setTextSize(36f);
        tithiYatraPaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("તિર્થયાત્રા : ", 842, 680, tithiYatraPaint);

        // tithi Yatra text
        String tempTirthText = tithiYatraTexts.substring(0, 1).toUpperCase() + tithiYatraTexts.substring(1).toLowerCase();
        tithYatraText.setTextAlign(Paint.Align.LEFT);
        tithYatraText.setTextSize(34f);
        tithYatraText.setColor(Color.BLACK);
        canvas.drawText(tempTirthText, 978, 680, tithYatraText);

        // Rupees in word
        amountWordPaint.setTextAlign(Paint.Align.LEFT);
        amountWordPaint.setTextSize(36f);
        amountWordPaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("માટે આપના તરફથી અંકે રૂ. : ", 842, 740, amountWordPaint);

        // Rupees in word text
        wordText.setTextAlign(Paint.Align.LEFT);
        wordText.setTextSize(34f);
        wordText.setColor(Color.BLACK);
        canvas.drawText(amountInWords, 1188, 740, wordText);

        // Today
        tarikhPaint.setTextAlign(Paint.Align.LEFT);
        tarikhPaint.setTextSize(36f);
        tarikhPaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("આજરોજ : ", 842, 800, tarikhPaint);

        // Today date text
        dateText.setTextAlign(Paint.Align.LEFT);
        dateText.setTextSize(34f);
        dateText.setColor(Color.BLACK);
        canvas.drawText(currentData, 990, 800, dateText);

        // મળેલ છે
        obtainedPaint.setTextAlign(Paint.Align.RIGHT);
        obtainedPaint.setTextSize(36f);
        obtainedPaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("મળેલ છે", 1935 - 240, 800, obtainedPaint);

        //people
        peoplePaint.setTextAlign(Paint.Align.LEFT);
        peoplePaint.setTextSize(36f);
        peoplePaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("કુલયાત્રી : ", 842, 860, peoplePaint);

        //people text
        totalPeople.setTextAlign(Paint.Align.LEFT);
        totalPeople.setTextSize(34f);
        totalPeople.setColor(Color.BLACK);
        canvas.drawText(peopleTexts, 968, 860, totalPeople);

        // Children
        childrenPaint.setTextAlign(Paint.Align.LEFT);
        childrenPaint.setTextSize(36f);
        childrenPaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("બાળકો : ", 1935 - 400, 860, childrenPaint);

        // children text
        totalChildren.setTextAlign(Paint.Align.LEFT);
        totalChildren.setTextSize(34f);
        totalChildren.setColor(Color.BLACK);
        canvas.drawText(childrenTexts, 1935 - 290, 860, totalChildren);

        // deposit
        depositPaint.setTextAlign(Paint.Align.LEFT);
        depositPaint.setTextSize(36f);
        depositPaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("ડિપોઝિટ : ", 842, 920, depositPaint);

        // deposit text
        depositText.setTextAlign(Paint.Align.LEFT);
        depositText.setTextSize(34f);
        depositText.setColor(Color.BLACK);
        canvas.drawText(depositTexts, 988, 920, depositText);

        // baki
        bakiPaint.setTextAlign(Paint.Align.LEFT);
        bakiPaint.setTextSize(36f);
        bakiPaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("બાકી : ", 1935 - 400, 920, bakiPaint);

        // baki text
        bakiText.setTextAlign(Paint.Align.LEFT);
        bakiText.setTextSize(34f);
        bakiText.setColor(Color.BLACK);
        canvas.drawText(bakiTexts, 1935 - 310, 920, bakiText);

        // rupiya
        amountPaint.setTextAlign(Paint.Align.LEFT);
        amountPaint.setTextSize(44f);
        amountPaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("રૂ. : ", 860, 990, amountPaint);

        // rupiya text
        amountText.setTextAlign(Paint.Align.LEFT);
        amountText.setTextSize(38f);
        amountText.setColor(Color.BLACK);
        canvas.drawText(amountTexts, 920, 990, amountText);

        // sivkarnar
        sivkarnarPaint.setTextAlign(Paint.Align.LEFT);
        sivkarnarPaint.setTextSize(36f);
        sivkarnarPaint.setColor(Color.rgb(0, 0, 139));
        canvas.drawText("સ્વીકારનાર : ", 1935 - 540, 990, sivkarnarPaint);

        // sivkarnar text
        svikarnarText.setTextAlign(Paint.Align.LEFT);
        svikarnarText.setTextSize(34f);
        svikarnarText.setColor(Color.BLACK);
        canvas.drawText(svikarnarTexts.toUpperCase(Locale.ROOT), 1935 - 350, 990, svikarnarText);

        myPdfDocument.finishPage(myPage1);

        String fileName = "Receipt(" + mobileNoTexts + ").pdf";
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            myPdfDocument.writeTo(new FileOutputStream(file));
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            myPdfDocument.close();
            Toast.makeText(context, "File Saved Successfully", Toast.LENGTH_SHORT).show();
//            if (file.exists()) {
//                // Append 1 to the new file.
//                String newName = "Receipt(" + mobileNoTexts + ")" + count + ".pdf";
//                count = count + 1;
//                File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), newName);
//                myPdfDocument.writeTo(new FileOutputStream(file1));
//                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//                Toast.makeText(context, "File Saved Successfully", Toast.LENGTH_SHORT).show();
//            } else {
//                // Same name file does not exists then it will save with original name.
//                myPdfDocument.writeTo(new FileOutputStream(file));
//                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//
//                Toast.makeText(context, "File Saved Successfully", Toast.LENGTH_SHORT).show();
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error Occurred in saving file!! Maybe same named file exists (Delete same name pdf)", Toast.LENGTH_LONG).show();
        }

    }

    // FUNCTION TO FETCH THE INCREMENTED VALUE OF THE RECEIPT COUNTER.
    private void fetchReceiptCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference receiptDocumentRef = db.collection("receipt_count").document("receipt_document");

        receiptDocumentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    receiptNumber = documentSnapshot.getLong("receiptNumber").intValue();
                    System.out.println("COunt here : " + receiptNumber);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Handle any errors that occurred during the fetch
                Log.e("Firestore", "Error fetching receipt number", e);
            }
        });
    }


}
