package com.example.home.hajjability;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity  implements View.OnClickListener {


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email, user_id;
    TextView hello;
    String name, blood_type, allergies, disability, birthdate, emergency_contact_1, emergency_contact_2,
            vaccination, meds, location, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        email = "habibayassin@aucegypt.edu";
        name = "habiba";
        blood_type = "O";
        allergies = "peanuts";
        disability = "No disabilities";
        birthdate = "12/3/2006";
        emergency_contact_1 = "01234565767";
        emergency_contact_2 = "01233465767";
        vaccination = "no vaccine";
        meds = "vitamins";
        location = "cairo";
        password = "hello1234";

        if (auth == null) {
            Log.d("gazar", "auth is null");
        }

        //add_user();

        //sign_in();


    }

    private void sign_in()
    {
        Log.d("gazar", "Attempting to sign in");
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("gazar", "Signing in");

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("gazar", "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //updateUI(user);

                            DocumentReference docRef = db.collection("users").document(user.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("gazar", "DocumentSnapshot data: " + document.getData());
                                            //hello.setText((CharSequence) document.getData().get("allergies"));
                                            Intent int1 = new Intent(login.this, home.class);
                                            startActivity(int1);
                                        } else {
                                            Log.d("gazar", "No such document");
                                        }
                                    } else {
                                        Log.d("gazar", "get failed with ", task.getException());
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("gazar", "signInWithEmail:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                sign_in();
//                Intent int1 = new Intent(this, home.class);
//                startActivity(int1);
                break;


        }
    }

    public void voiceAssist(String query) {

    }

}
