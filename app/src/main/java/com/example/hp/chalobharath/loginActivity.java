package com.example.hp.chalobharath;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import javax.xml.transform.Templates;

public class loginActivity extends AppCompatActivity {
    private Button uLogin;
    private TextView uSignup,uForgotPassword;
    private EditText uEmail,uPass;
    private ProgressDialog progressDialog;
private FirebaseAuth auth;
private String password,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uLogin = (Button)findViewById(R.id.login);
        uSignup = (TextView) findViewById(R.id.signup);
        uForgotPassword = (TextView)findViewById(R.id.forgot);
        uEmail = (EditText)findViewById(R.id.lemail);
        uPass = (EditText)findViewById(R.id.lpassword);
        progressDialog = new ProgressDialog(loginActivity.this);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(loginActivity.this,optionActivity.class));
            finish();
        }

        uLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 email = uEmail.getText().toString().trim();
                 password = uPass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    uEmail.setError("Enter Valid Email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    uEmail.setError("Enter Valid Email");
                    return;
                }

                progressDialog.setMessage("Logging you In.....");
                progressDialog.show();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        uPass.setError("enter correct password");
                                    } else {
                                        uPass.setText("");
                                        uPass.setError("enter correct password");
                                        Toast.makeText(loginActivity.this, " Please check your Email & Password", Toast.LENGTH_LONG).show();
                                    }
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(loginActivity.this,"Login Succesfull", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(loginActivity.this,optionActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
    }
        });
        uForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this,forgotActivity.class));

            }
        });
        uSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this,registerActivity.class));
            }
        });
    }
}
