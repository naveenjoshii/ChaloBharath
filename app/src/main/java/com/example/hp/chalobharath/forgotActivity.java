package com.example.hp.chalobharath;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotActivity extends AppCompatActivity {

    private EditText passwordEmail;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        passwordEmail=(EditText)findViewById(R.id.etPasswordEmail);
        resetPassword=(Button)findViewById(R.id.btnPasswordReset);
        firebaseAuth = FirebaseAuth.getInstance();
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = passwordEmail.getText().toString().trim();
                if(useremail.equals("")){
                    passwordEmail.setError("Enter your Email");
                    Toast.makeText(forgotActivity.this, "please your enter your registered emailID", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(forgotActivity.this,"Password Reset Email sent",Toast.LENGTH_SHORT).show();
                                finish();
                                firebaseAuth.signOut();
                                startActivity(new Intent(getApplicationContext(),loginActivity.class));
                            }else{
                                Toast.makeText(forgotActivity.this,"Error in sending password reset email",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }
}
