package com.example.kartik.geo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {

    Helper formdb;
    EditText ed1,ed2;
    Button b1,b2,b3,b4;
    String codeSent;
    boolean clicked=false;
    String CurrentDate,CurrentTime;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        formdb = new Helper(this);
        mAuth= FirebaseAuth.getInstance();

        Calendar calender = Calendar.getInstance();
        CurrentDate = DateFormat.getDateInstance().format(calender.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        CurrentTime = format.format(calender.getTime());

        ed1= (EditText)findViewById(R.id.editText);
        ed2= (EditText)findViewById(R.id.editText2);
        b1 =(Button)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendcode();
            }
        });

        b2 =(Button)findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clicked=true;
                if(clicked)
                {
                    verifycode();
                }

            }
        });
        b3 = (Button)findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addata();
            }
        });
        b4 = (Button)findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Attdtb.class);
                startActivity(intent);
            }
        });

    }

    public void addata() {
                if (clicked) {
                    boolean result = formdb.insertData(CurrentDate.toString(), CurrentTime.toString());
                    if (result == true) {
                        Toast.makeText(getApplicationContext(), "Attendance Recorded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(otp.this, "No Data Found ", Toast.LENGTH_SHORT).show(); }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Verify the Code", Toast.LENGTH_SHORT).show();
                }
            }

    protected void onStart()
    {

        super.onStart();
        formdb.openDb();
    }


    protected void onStop()
    {
        super.onStop();
        formdb.closeDb();
    }


    private void sendcode()
    {

        String phoneNumber = ed1.getText().toString().trim();
        if(phoneNumber.isEmpty())
        {
            ed1.setError("Number is required");
            ed1.requestFocus();
            return;
        }
        if(phoneNumber.length()<10)
        {
            ed1.setError("Number is required");
            ed1.requestFocus();
            return;

        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent =s;
        }
    };

    private void verifycode()
    {
        String code = ed2.getText().toString().trim();
        if(code.isEmpty())
        {
            ed1.setError("Number is required");
            ed1.requestFocus();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext()," Code Verification Successfull ",Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Toast.makeText(getApplicationContext(),"invalid code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}


