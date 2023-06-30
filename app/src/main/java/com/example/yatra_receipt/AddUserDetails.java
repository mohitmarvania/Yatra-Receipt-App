package com.example.yatra_receipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AddUserDetails extends AppCompatActivity {

    EditText editName, editGam, editMobileNo, editTithiYatra, editPeople, editChildren, editAmount;
    EditText editDeposit, editBaki, editSvikarnar;
    MaterialButton createReceipt;
    ImageView imageView;

    // FOR RETRIEVING DATA.
    String nameRetrieved, gamRetrieved, mobileNoRetrieved, tithiYatraRetrieved, peopleRetrieved, childrenRetrieved, amountRetrieved;
    String depositRetrieved, bakiRetrieved, svikarnarRetrieved, dataIdRetrieved;
    String uniqueId = "123";
    long receiptNo = 0;
    Boolean isEdit = false;

    // FOR IMAGE PICKER.
    private final int GALLERY_REQUEST_CODE = 1;
    private final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 123;
    Bitmap bmp;

    // DATA class initialization
    Data data = new Data();

    // Realm initialization
    Realm realm;

    // Firebase initializations.
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("data");
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_details);

        assign();
        retriveData();
        receiptMainFunction();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                receiptNo = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    // FUNCTION TO CREATE THE OPTION OF DROPDOWN
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_image, menu);
        return true;
    }

    // FUNCTION TO TAKE ACTION AGAINST THE BUTTON CLICKED EVENT
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_upload_image) {
            openImagePicker();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // FUNCTION TO OPEN IMAGE PICKER.---------------------------------------------------------------
    public void openImagePicker() {
        // Request the READ_EXTERNAL_STORAGE permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST);
        } else {
            launchImagePicker();
        }
    }

    private void showCustomPermissionDialog() {
        // Display a custom dialog explaining why the permission is needed and how to manually grant it from app settings
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Required")
                .setMessage("This permission is required to access external storage.")
                .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openAppSettings();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            bmp = loadBitmapFromUri(imageUri);

            if (bmp != null) {
                imageView.setImageBitmap(bmp);
            } else {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap loadBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchImagePicker();
            } else {
                showCustomPermissionDialog();
                System.out.println("Grantresule length = " + grantResults.length);
                System.out.println("grantResults[0] = " + grantResults[0]);
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //.---------------------------------------------------------------------------------------------

    // FUNCTION WHICH ASSIGNS EACH VARIABLE TO ITS CORRECT DATA. (LIKE EDITTEXT, BUTTON, ETC.)
    private void assign() {
        editName = findViewById(R.id.editName);
        editGam = findViewById(R.id.editGam);
        editMobileNo = findViewById(R.id.editMobileNumber);
        editTithiYatra = findViewById(R.id.editTirthYatra);
        editPeople = findViewById(R.id.editTotalPeople);
        editChildren = findViewById(R.id.editTotalChildren);
        editAmount = findViewById(R.id.editAmountPaid);
        editDeposit = findViewById(R.id.editDeposit);
        editBaki = findViewById(R.id.editBaki);
        editSvikarnar = findViewById(R.id.editSvikarnar);
        createReceipt = findViewById(R.id.generateRecieptBtn);
        imageView = findViewById(R.id.imageView);
        uniqueId = UUID.randomUUID().toString();
    }

    // FUNCTION USED TO RETRIVE AND SET DATA AT THE EDITTEXT FIELD IF EDITING IS CLICKED.
    private void retriveData() {
        nameRetrieved = getIntent().getStringExtra("name");
        gamRetrieved = getIntent().getStringExtra("gam");
        mobileNoRetrieved = getIntent().getStringExtra("mobileNumber");
        tithiYatraRetrieved = getIntent().getStringExtra("tirthYatra");
        peopleRetrieved = getIntent().getStringExtra("totalPeople");
        childrenRetrieved = getIntent().getStringExtra("totalChildren");
        amountRetrieved = getIntent().getStringExtra("amount");
        depositRetrieved = getIntent().getStringExtra("deposit");
        bakiRetrieved = getIntent().getStringExtra("baki");
        svikarnarRetrieved = getIntent().getStringExtra("svikarnar");
        dataIdRetrieved = getIntent().getStringExtra("dataId");

        if (dataIdRetrieved != null && !dataIdRetrieved.isEmpty()) {
            isEdit = true;
        }

        if (isEdit) {
            createReceipt.setText("Save Changes");
            Toast.makeText(AddUserDetails.this, "Mobile No cannot be changed.", Toast.LENGTH_SHORT).show();
            editMobileNo.setInputType(0);

            editName.setText(nameRetrieved);
            editGam.setText(gamRetrieved);
            editMobileNo.setText(mobileNoRetrieved);
            editTithiYatra.setText(tithiYatraRetrieved);
            editPeople.setText(peopleRetrieved);
            editChildren.setText(childrenRetrieved);
            editAmount.setText(amountRetrieved);
            editDeposit.setText(depositRetrieved);
            editBaki.setText(bakiRetrieved);
            editSvikarnar.setText(svikarnarRetrieved);
            isEdit = false;
        }

    }

    // FUNCTION WHICH CHECKS INPUT AND MOVE TO NEXT SCREEN.
    public void receiptMainFunction() {

        createReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If new user data is being created then this code goes on.
                if (createReceipt.getText().toString().equals("Generate Invoice")) {
                    Realm.init(getApplicationContext());
                    realm = Realm.getDefaultInstance();

                    if (editName.getText().toString().length() == 0 ||
                            editGam.getText().toString().length() == 0  ||
                            editMobileNo.getText().toString().length() == 0 ||
                            editTithiYatra.getText().toString().length() == 0 ||
                            editPeople.getText().toString().length() == 0 ||
                            editChildren.getText().toString().length() == 0 ||
                            editAmount.getText().toString().length() == 0 ||
                            editSvikarnar.getText().toString().length() == 0 ||
                            editDeposit.getText().toString().length() == 0 ||
                            editBaki.getText().toString().length() == 0) {

                        Toast.makeText(AddUserDetails.this, "Some Fields Are Empty !!", Toast.LENGTH_SHORT).show();
                    } else if (editMobileNo.getText().toString().length() != 10) {
                        Toast.makeText(AddUserDetails.this, "Mobile No. Invalid !!", Toast.LENGTH_SHORT).show();
                    } else {

                        String dataName = editName.getText().toString();
                        String dataGam = editGam.getText().toString();
                        String dataMobileNo = editMobileNo.getText().toString();
                        String dataTithiYatra = editTithiYatra.getText().toString();
                        String dataPeople = editPeople.getText().toString();
                        String dataChildren = editPeople.getText().toString();
                        String dataAmount = editAmount.getText().toString();
                        String dataDeposit = editDeposit.getText().toString();
                        String dataBaki = editBaki.getText().toString();
                        String dataSvikarnar = editSvikarnar.getText().toString();

                        // Get current data and time.
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String currentData = sdf.format(new Date());
                        long createdTime = System.currentTimeMillis();

                        // Uploading data to firebase realtime.
                        Data dataF = new Data();
                        dataF.receiptNo = receiptNo + 1;
                        dataF.name = dataName;
                        dataF.gam = dataGam;
                        dataF.mobileNo = dataMobileNo;
                        dataF.tithiYatra = dataTithiYatra;
                        dataF.people = dataPeople;
                        dataF.children = dataChildren;
                        dataF.amount = dataAmount;
                        dataF.deposit = dataDeposit;
                        dataF.baki = dataBaki;
                        dataF.svikarnar = dataSvikarnar;
                        dataF.createdTime = new Date().getTime();

                        myRef.child(String.valueOf(receiptNo + 1)).setValue(dataF);

                        // Uploading data to Realm storage.
                        realm.beginTransaction();
                        data = realm.createObject(Data.class);

                        data.setName(dataName);
                        data.setGam(dataGam);
                        data.setMobileNo(dataMobileNo);
                        data.setTithiYatra(dataTithiYatra);
                        data.setPeople(dataPeople);
                        data.setChildren(dataChildren);
                        data.setAmount(dataAmount);
                        data.setDeposit(dataDeposit);
                        data.setBaki(dataBaki);
                        data.setSvikarnar(dataSvikarnar);
                        data.setCreatedData(currentData);
                        data.setCreatedTime(createdTime);
                        Bitmap selectedImage = bmp;

                        realm.commitTransaction();
                        realm.close();
                        Toast.makeText(AddUserDetails.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();

                        incrementReceiptCounter();
                        createPdf(selectedImage, dataName, dataGam, dataMobileNo, dataTithiYatra, dataPeople, dataChildren, dataAmount, dataDeposit, dataBaki, currentData, dataSvikarnar);

                        // Move to view pdf activity.
                        Intent intent = new Intent(AddUserDetails.this, ViewPdf.class);
                        intent.putExtra("mobileNo", dataMobileNo);
                        intent.putExtra("name", dataName);
                        startActivity(intent);
                    }

                } else {
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();

                    Data data = realm.where(Data.class).equalTo("mobileNo", editMobileNo.getText().toString()).findFirst();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String currentDate = sdf.format(new Date());
                    long createdTime = System.currentTimeMillis();

                    data.setName(editName.getText().toString());
                    data.setGam(editGam.getText().toString());
                    data.setMobileNo(editMobileNo.getText().toString());
                    data.setTithiYatra(editTithiYatra.getText().toString());
                    data.setPeople(editPeople.getText().toString());
                    data.setChildren(editChildren.getText().toString());
                    data.setAmount(editAmount.getText().toString());
                    data.setDeposit(editDeposit.getText().toString());
                    data.setBaki(editBaki.getText().toString());
                    data.setSvikarnar(editSvikarnar.getText().toString());
                    data.setCreatedData(currentDate);
                    data.setCreatedTime(createdTime);

                    realm.commitTransaction();

                    String name = editName.getText().toString();
                    String gam = editGam.getText().toString();
                    String mobileNo = editMobileNo.getText().toString();
                    String tithiYatra = editTithiYatra.getText().toString();
                    String people = editPeople.getText().toString();
                    String children = editChildren.getText().toString();
                    String amount = editAmount.getText().toString();
                    String deposit = editDeposit.getText().toString();
                    String baki = editBaki.getText().toString();
                    String svikarnar = editSvikarnar.getText().toString();
                    Bitmap selectedImage = bmp;

                    // Updation in firebase realtime.
                    Data dataF = new Data();
                    dataF.receiptNo = receiptNo + 1;
                    dataF.name = name;
                    dataF.gam = gam;
                    dataF.mobileNo = mobileNo;
                    dataF.tithiYatra = tithiYatra;
                    dataF.people = people;
                    dataF.children = children;
                    dataF.amount = amount;
                    dataF.deposit = deposit;
                    dataF.baki = baki;
                    dataF.svikarnar = svikarnar;
                    dataF.createdTime = new Date().getTime();

                    myRef.child(String.valueOf(receiptNo + 1)).setValue(dataF);

                    Toast.makeText(AddUserDetails.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                    createPdf(selectedImage, name, gam, mobileNo, tithiYatra, people, children, amount, deposit, baki, currentDate, svikarnar);

                    Intent intent = new Intent(AddUserDetails.this, ViewPdf.class);
                    intent.putExtra("mobileNo", mobileNo);
                    intent.putExtra("name", name);
                    startActivity(intent);

                }
            }
        });

    }

    private void createPdf(Bitmap selectedImage, String name, String gam, String mobileNo, String tithiYatra, String people, String children, String amount, String deposit, String baki, String currentData, String svikarnar) {
        SavePdf saveClass = new SavePdf();
        saveClass.createAndSavePdf(selectedImage, name, gam, mobileNo, tithiYatra, people, children, amount, deposit, baki, currentData, svikarnar);
    }

    private void incrementReceiptCounter() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference receiptCountRef = db.collection("receipt_count").document("receipt_document");

        receiptCountRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Document exists fetch the current receipt number.
                    int receiptNo = documentSnapshot.getLong("receiptNumber").intValue();
                    int newReceiptNo = receiptNo + 1;

                    // Update the receipt number in the document.
                    receiptCountRef.update("receiptNumber", newReceiptNo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // Receipt number incremented and document updated successfully
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    // Handle any errors that occurred during the update
                                    Log.e("Firestore", "Error updating receipt number", e);
                                }
                            });
                } else {
                    // Document does not exist, create it with an initial receipt number
                    Map<String, Object> receiptData = new HashMap<>();
                    receiptData.put("receiptNumber", 1501); // Initial receipt number.

                    receiptCountRef.set(receiptData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // Receipt document created successfully
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    // Handle any errors that occurred during the document creation
                                    Log.e("Firestore", "Error creating receipt document", e);
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Handle any errors that occurred during the fetch
                Log.e("Firestore", "Error fetching receipt document", e);
            }
        });
    }

//    private void fetchReceiptCounter() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        final DocumentReference receiptDocumentRef = db.collection("receipt_count").document("receipt_document");
//
//        receiptDocumentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    receiptNumber = documentSnapshot.getLong("receiptNumber").intValue();
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception e) {
//                // Handle any errors that occurred during the fetch
//                Log.e("Firestore", "Error fetching receipt number", e);
//            }
//        });
//    }
}