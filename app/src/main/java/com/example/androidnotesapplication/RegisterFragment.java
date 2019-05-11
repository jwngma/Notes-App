package com.example.androidnotesapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    private FirebaseAuth mAuth;
    private EditText email, password,confirm_password;
    private String email_text,password_text,confirm_password_text;
    private Button regBtn;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_register, container, false);

        mAuth=FirebaseAuth.getInstance();
        email= view.findViewById(R.id.reg_email);
        password=view.findViewById(R.id.reg_pass);
        confirm_password=view.findViewById(R.id.reg_Con_pass);

        regBtn=view.findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_text=email.getText().toString();
                password_text=password.getText().toString();
                confirm_password_text=confirm_password.getText().toString();

                if (!TextUtils.isEmpty(email_text)&&!TextUtils.isEmpty(password_text)&&!TextUtils.isEmpty(confirm_password_text)){

                    if (password_text.equals(confirm_password_text)){
                        createUser(email_text,password_text);
                    }
                }
            }
        });












        return view;
    }

    private void createUser(String email_text, String password_text) {
        mAuth.createUserWithEmailAndPassword(email_text,password_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent= new Intent(getContext(),MainActivity.class);
                    startActivity(intent);
                }
                else {
                    String error= task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
