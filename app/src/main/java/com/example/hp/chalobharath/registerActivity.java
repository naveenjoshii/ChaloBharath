package com.example.hp.chalobharath;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class registerActivity extends AppCompatActivity {
    private EditText sname,semail,sPassword,sphone;
    RadioButton sMale,sFemale;
    private Button Register;
    TextView userLogin;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog pb;
     Firebase firebase;

     String name,email,password,phone,gender;
    public static final String Firebase_Server_URL = "https://chalobharath-2810c.firebaseio.com/";
    DatabaseReference databaseReference;
    public static final String Database_Path = "users_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sname = (EditText)findViewById(R.id.sName);
        semail = (EditText)findViewById(R.id.sEmail);
        sPassword = (EditText)findViewById(R.id.sPassword);
        sphone = (EditText)findViewById(R.id.sMobile);
        userLogin = (TextView)findViewById(R.id.llogin) ;
        sMale = (RadioButton)findViewById(R.id.male);
        sFemale = (RadioButton)findViewById(R.id.female);
        Register = (Button)findViewById(R.id.sRegister);
        Firebase.setAndroidContext(registerActivity.this);
        firebase = new Firebase(Firebase_Server_URL);
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        firebaseAuth=FirebaseAuth.getInstance();
        pb=new ProgressDialog(registerActivity.this);
        Register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(Validate()) {
                    pb.setMessage("Registering...:-)");
                    pb.show();
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                insert();
                                sendEmailVerification();
                                pb.dismiss();
                            }else{
                                Toast.makeText(registerActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                                pb.dismiss();
                            }
                        }
                    });
                }
            }
        });
        userLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(registerActivity.this,loginActivity.class));
                finish();
            }
        });
    }

    private void insert() {
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserDetails userDetails = new UserDetails();
        userDetails.setmUserId(currentuser);
        userDetails.setU_name(name);
        userDetails.setU_email(email);
        userDetails.setU_password(computeMD5Hash(password));
        userDetails.setU_phone(phone);
        userDetails.setU_gender(getGender(gender));
        String UserRecordIDFromServer = currentuser;
        databaseReference.child(UserRecordIDFromServer).setValue(userDetails);


    }

    private String getGender(String gender) {
        String result = "others";
        if(sMale.isChecked()){
            result = "male";
        }else if(sFemale.isChecked()){
           result = "female";

        }
        return result;
    }

    private String computeMD5Hash(String password) {
        String s="";
        try{
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for(int i=0;i<messageDigest.length;i++){
                String h= Integer.toHexString(0XFF & messageDigest[i]);
                while(h.length()<2){
                    h= "0"+h;
                    stringBuffer.append(h);

                }
            }
            s = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return s;
    }

    private Boolean Validate() {
        Boolean result =false;
        name = sname.getText().toString().trim();
        password = sPassword.getText().toString().trim();
        email = semail.getText().toString().trim();
        phone  = sphone.getText().toString().trim();
        if(name.isEmpty()||password.isEmpty()||email.isEmpty()||phone.isEmpty()||(!(sMale.isChecked()||sFemale.isChecked()))) {
            Toast.makeText(this, "Please Enter all the details...", Toast.LENGTH_SHORT).show();
        }else {
            result =true;
        }
        return result;
    }
    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Successfully Registered Verification Email sent",Toast.LENGTH_SHORT).show();

                        firebaseAuth.signOut();
                        startActivity(new Intent(registerActivity.this,loginActivity.class));
                        finish();

                    }else{
                        Toast.makeText(registerActivity.this,"Verification has'nt sent",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}
