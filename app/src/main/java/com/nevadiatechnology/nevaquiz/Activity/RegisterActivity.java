package com.nevadiatechnology.nevaquiz.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.User;
import com.nevadiatechnology.nevaquiz.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private DatabaseReference user_obj;
    private ProgressDialog progressDialog;
    private TextInputLayout nameTextInputLayout, emailTextInputLayout, passwordTextInputLayout, conpasswordTextInputLayout;
    private TextInputEditText nameTextInputEditText, emailTextInputEditText, passwordTextInputEditText, conpasswordTextInputEditText;
    private Button sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initView();
    }

    private void initView() {
        auth = FirebaseAuth.getInstance();

        user_obj = FirebaseDatabase.getInstance().getReference("User");

        nameTextInputLayout = (TextInputLayout) findViewById(R.id.nameTextInputLayout);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);
        conpasswordTextInputLayout = (TextInputLayout) findViewById(R.id.conpasswordTextInputLayout);

        nameTextInputEditText = (TextInputEditText) findViewById(R.id.nameTextInputEditText);
        emailTextInputEditText = (TextInputEditText) findViewById(R.id.emailTextInputEditText);
        passwordTextInputEditText = (TextInputEditText) findViewById(R.id.passwordTextInputEditText);
        conpasswordTextInputEditText = (TextInputEditText) findViewById(R.id.conpasswordTextInputEditText);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Please wait...");

        sign_up = (Button) findViewById(R.id.sign_up);

        sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_up) {
            registerMethod();
        }
    }

    private void registerMethod() {
        String name = nameTextInputEditText.getText().toString().trim();
        String email = emailTextInputEditText.getText().toString().trim();
        String password = passwordTextInputEditText.getText().toString().trim();
        String confirm_password = conpasswordTextInputEditText.getText().toString().trim();

        if (name.isEmpty()) {
            nameTextInputLayout.setError("Name is empty");
            return;
        } else if (email.isEmpty()) {
            emailTextInputLayout.setError("Email is empty");
            return;
        } else if (password.isEmpty()) {
            passwordTextInputLayout.setError("Password is empty");
            return;
        } else if (confirm_password.isEmpty()) {
            conpasswordTextInputLayout.setError("Confirm password is empty");
            return;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailTextInputLayout.setError("Please enter a valid email address");
                return;
            } else if (!password.equals(confirm_password)) {
                Common.showToast(RegisterActivity.this, "Password are not match");
            } else {
                tryToRegister(name, email, password, Common.imageUrl);
            }
        }
    }

    private void tryToRegister(final String fullname, final String email, final String password, final String image) {
        progressDialog.show();

        user_obj.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    Common.showToast(RegisterActivity.this, "Email address already register");
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                User user = new User(fullname, auth.getCurrentUser().getUid(), email, image);

                                user_obj.child(auth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Common.showToast(RegisterActivity.this, "Register Successfully");
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
                            } else if (task.isCanceled()) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
