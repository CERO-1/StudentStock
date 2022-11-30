
package com.pedidos.reservadegrass;


import android.app.ProgressDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pedidos.reservadegrass.data.usuarios_data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

	private EditText editDNI;
	private EditText editxtName;
	private EditText editxAPP;
	private EditText editxtAPM;
	private EditText editxtDireccion;
	private EditText editxtPhone;
	private EditText editEMAIL;
	private EditText editPASWD;

	private FirebaseFirestore db;


    private Button Registro;
    private TextView signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityregistration);

        initializeGUI();
        db = FirebaseFirestore.getInstance();
        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String inputName = editxtName.getText().toString().trim();
                final String inputPw = editPASWD.getText().toString().trim();
                final String inputEmail = editEMAIL.getText().toString().trim();

                if(validateInput(inputName, inputPw, inputEmail))
                         registerUser(inputName, inputPw, inputEmail);

            }
        });

/*
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });*/

    }


    private void initializeGUI(){

	    editDNI = findViewById(R.id.atvdni);
	    editxtName = findViewById(R.id.atvUsernameReg);
	    editxAPP = findViewById(R.id.atvApp);
	    editxtAPM = findViewById(R.id.atvApm);
	    editxtDireccion = findViewById(R.id.atvdirec);
	    editxtPhone = findViewById(R.id.atvTelefono);
	    editEMAIL = findViewById(R.id.atvEmailReg);
	    editPASWD = findViewById(R.id.atvPasswordReg);

	    Registro=findViewById(R.id.btnREGISTRAR);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerUser(final String inputName, final String inputPw, String inputEmail) {

        progressDialog.setMessage("Verificando...");
        progressDialog.show();


            firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
	                    String dni = editxtName.getText().toString().trim();
	                    String name = editxtName.getText().toString().trim();
	                    String apellidop = editxAPP.getText().toString().trim();
	                    String apellidom = editxtAPM.getText().toString().trim();
	                    String direccion = editxtDireccion.getText().toString().trim();
	                    String telefono = editxtPhone.getText().toString().trim();
	                    String email = editEMAIL.getText().toString().trim();
	                    String password = editPASWD.getText().toString().trim();

                        CollectionReference dbProducts = db.collection("usuarios");

		/*	usuarios_data clientes = new usuarios_data(
					name,
					apellidop,
					apellidom,
					Double.parseDouble(direccion),
					Integer.parseInt(telefono)

			);
*/

	                    usuarios_data clientes = new usuarios_data(
			                    dni,
			                    name,
			                    apellidop,
			                    apellidom,
			                    direccion,
			                    telefono,
			                    email,
			                    password
	                    );


	                    dbProducts.add(clientes)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                  //      Toast.makeText(RegistrationActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    //    Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                        sendUserData(inputName, inputPw);
	                    Toast.makeText(RegistrationActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
	                    finish();

//                        startActivity(new Intent(RegistrationActivity.this,MainActivity2.class));
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this,"el correo ya existe .",Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }


    private void sendUserData(String username, String password){

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference users = firebaseDatabase.getReference("users");
       // UserProfile user = new UserProfile(username, password);
    //    users.push().setValue(user);

    }

    private boolean validateInput(String inName, String inPw, String inEmail){

        if(inName.isEmpty()){
            editxtName.setError("Ingregse un nombre");
            return false;
        }
        if(inPw.isEmpty()){
            editPASWD.setError("Ingrese una contraseña");
            return false;
        }
        if(inEmail.isEmpty()){
            editEMAIL.setError("Ingrese un correo");
            return false;
        }

        return true;
    }


}
