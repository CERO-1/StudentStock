package com.pedidos.reservadegrass.activity;import android.Manifest;import android.app.Activity;import android.app.AlertDialog;import android.content.Context;import android.content.DialogInterface;import android.content.Intent;import android.content.pm.PackageManager;import android.graphics.Bitmap;import android.net.Uri;import android.os.Bundle;import android.provider.MediaStore;import android.view.View;import android.widget.Button;import android.widget.ImageView;import android.widget.ProgressBar;import android.widget.Toast;import com.google.android.gms.tasks.OnFailureListener;import com.google.android.gms.tasks.OnSuccessListener;import com.google.firebase.database.DatabaseReference;import com.google.firebase.database.FirebaseDatabase;import com.google.firebase.database.ServerValue;import com.google.firebase.firestore.CollectionReference;import com.google.firebase.firestore.DocumentReference;import com.google.firebase.firestore.FirebaseFirestore;import com.google.firebase.storage.FirebaseStorage;import com.google.firebase.storage.StorageReference;import com.google.firebase.storage.UploadTask;import com.pedidos.reservadegrass.R;import com.pedidos.reservadegrass.data.Comprasdata;import java.io.ByteArrayOutputStream;import java.util.HashMap;import java.util.UUID;import androidx.annotation.NonNull;import androidx.annotation.Nullable;import androidx.appcompat.app.AppCompatActivity;import androidx.core.app.ActivityCompat;import androidx.core.content.ContextCompat;public class SubirActivy extends AppCompatActivity {//	private FirebaseStorage storageReference = null;//	private DatabaseReference databaseReference = null;	private ProgressBar progressBar;	private ImageView imagePreviw;	private Uri filePath = null;	private final int PICK_IMAGE_GALLERY_CODE = 78;	private final int CAMERA_PERMISSION_REQUEST_CODE = 12345;	private final int CAMERA_PICTURE_REQUEST_CODE = 56789;	private  StorageReference storageRef;	//private  FirebaseFirestore  firebaseFirestore;	private FirebaseDatabase firebaseDatabase;	private FirebaseFirestore db;	@Override	protected void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		setContentView(R.layout.activity_subirfoto);		storageRef = FirebaseStorage.getInstance().getReference().child("images/"+ UUID.randomUUID().toString());	//	storageRef.child("images/"+ UUID.randomUUID().toString());		firebaseDatabase = FirebaseDatabase.getInstance();	//	firebaseFirestore = FirebaseFirestore.getInstance();		db = FirebaseFirestore.getInstance();//		FirebaseDatabase database = FirebaseDatabase.getInstance();	//	StorageReference firebaseStorage = StorageReference();//		databaseReference =  database.getReference().child("user_images");//		storageReference = firebaseStorage.getReference();		Button selectButton = findViewById(R.id.selectButton);		Button uploadButton = findViewById(R.id.uploadButton);		Button showUPloadedImages = findViewById(R.id.showUPloadedImages);		imagePreviw = findViewById(R.id.imagePreview);		progressBar = findViewById(R.id.progressBar);		selectButton.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View v) {				showImageSelectedDialog();			}		});		uploadButton.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View v) {				uploadImage();			}		});		showUPloadedImages.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View v) {				startActivity(new Intent(SubirActivy.this, DownloadUploadedImages.class));			}		});	}	private void uploadImage() {		if(filePath != null) {			progressBar.setVisibility(View.VISIBLE);//			storageRef = FirebaseStorage.getInstance().reference.child("Images");			//FirebaseStorage firebaseFirestore = FirebaseFirestore.getInstance();			 storageRef.child("images/"+ UUID.randomUUID().toString());			storageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {				@Override				public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {					storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {						@Override						public void onSuccess(Uri uri) {							CollectionReference dbProducts = db.collection("compras");								Comprasdata comprasdata = new Comprasdata(									"",uri.toString(),1);							dbProducts.add(comprasdata)									.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {										@Override										public void onSuccess(DocumentReference documentReference) {											     Toast.makeText(SubirActivy.this, "Registro exitoso", Toast.LENGTH_LONG).show();										}									})									.addOnFailureListener(new OnFailureListener() {										@Override										public void onFailure(@NonNull Exception e) {											//    Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();										}									});							progressBar.setVisibility(View.GONE);						}					});				}			}).addOnFailureListener(new OnFailureListener() {				@Override				public void onFailure(@NonNull Exception e) {					progressBar.setVisibility(View.GONE);					Toast.makeText(SubirActivy.this, "Image uploaded failed", Toast.LENGTH_SHORT).show();				}			});		}	}	private void showImageSelectedDialog() {		AlertDialog.Builder builder = new AlertDialog.Builder(this);		builder.setTitle("Select Image");		builder.setMessage("Please select an option");		builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {			@Override			public void onClick(DialogInterface dialog, int which) {				checkCameraPermission();				dialog.dismiss();			}		});		builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {			@Override			public void onClick(DialogInterface dialog, int which) {				dialog.dismiss();			}		});		builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {			@Override			public void onClick(DialogInterface dialog, int which) {				selectFromGallery();				dialog.dismiss();			}		});		AlertDialog dialog = builder.create();		dialog.show();	}	private void checkCameraPermission() {		if(ContextCompat.checkSelfPermission(SubirActivy.this, Manifest.permission.CAMERA)				   != PackageManager.PERMISSION_GRANTED				   || ContextCompat.checkSelfPermission(SubirActivy.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)						      != PackageManager.PERMISSION_GRANTED ) {			ActivityCompat.requestPermissions(SubirActivy.this, new String[] {					Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE			}, CAMERA_PERMISSION_REQUEST_CODE);		} else {			openCamera();		}	}	@Override	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {		super.onRequestPermissionsResult(requestCode, permissions, grantResults);		if(requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults[1] == PackageManager.PERMISSION_GRANTED){			openCamera();		}	}	private void openCamera() {		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		if(intent.resolveActivity(getPackageManager()) != null) {			startActivityForResult(intent, CAMERA_PICTURE_REQUEST_CODE);		}	}	private void selectFromGallery() {		Intent intent = new Intent();		intent.setType("image/*");		intent.setAction(Intent.ACTION_GET_CONTENT);		startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_GALLERY_CODE);	}	@Override	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {		super.onActivityResult(requestCode, resultCode, data);		if(requestCode  ==  PICK_IMAGE_GALLERY_CODE && resultCode == Activity.RESULT_OK) {			if(data == null || data.getData() == null)				return;			try {				filePath = data.getData();				Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);				imagePreviw.setImageBitmap(bitmap);			}catch (Exception e) {			}		} else if(requestCode == CAMERA_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {			Bundle extras = data.getExtras();			Bitmap bitmap  = (Bitmap)extras.get("data");			imagePreviw.setImageBitmap(bitmap);			filePath =getImageUri(getApplicationContext(), bitmap);		}	}	private Uri getImageUri(Context context, Bitmap bitmap) {		ByteArrayOutputStream bytes = new ByteArrayOutputStream();		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);		String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);		return Uri.parse(path);	}}