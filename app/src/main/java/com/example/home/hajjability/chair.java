package com.example.home.hajjability;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.hajjability.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class chair extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email, user_id;
    TextView hello;
    String name, blood_type, allergies, disability, birthdate, emergency_contact_1, emergency_contact_2,
            vaccination, meds, location, password;

    LocationManager locationManager;
    LocationListener locationListener;
    String current_location;
    TextToSpeech t1;
    Boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chair);
        //FirebaseApp.initializeApp(this);
        email = "habibayassin@aucegypt.edu";
        password = "hello1234";
        hello = (TextView) findViewById(R.id.text);

        //////////////////////////////////////////////////////////
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR)
                {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });
        /////////////////////////////////////////////////////////////////
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location: ", location.toString());
                current_location = String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        ////////////////////////////////////////////////////////////////


        //sign_up();
        user_id  = user.getUid();

        Map<String, Object> user = new HashMap<>();
        user.put("current_location", current_location);


        db.collection("users").document(user_id)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("gazar", "DocumentSnapshot successfully written!");
                        if (flag)
                            t1.speak("your request has been issued", TextToSpeech.QUEUE_FLUSH, null, "request_done");

                        hello.setText("Your request has been issued");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("gazar", "Error writing document", e);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = auth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private void sign_in()
    {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
                                            hello.setText((CharSequence) document.getData().get("allergies"));
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
                            Toast.makeText(chair.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    public void onPause()
    {
        if(t1 != null)
        {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    private void action()
    {
        String url = "http://www.hajj-hackathon-7d7d3.firebaseapp.com/verify?uid=" + user.getUid();
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl(url)
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.example.android",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();
    }

    private void verify()
    {
        Intent intent = getIntent();
        String emailLink = intent.getData().toString();
        // Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink)) {
            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Log.d("gazar", "Successfully signed in with email link!");
                                AuthResult result = (AuthResult) task.getResult();
                                // You can access the new user via result.getUser()
                                FirebaseUser user = result.getUser();
                                user_id  = user.getUid();
                                // Additional user info profile *not* available via:
                                // result.getAdditionalUserInfo().getProfile() == null
                                // You can check if the user is new or existing:
                                // result.getAdditionalUserInfo().isNewUser()
                            } else {
                                Log.e("gazar", "Error signing in with email link: "
                                        + task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    private void sign_up()
    {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("gazar", "createUserWithEmail:success");
                            user = auth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("gazar", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(chair.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void add_user()
    {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("blood_type", blood_type);
        user.put("allergies", allergies);
        user.put("disability", disability);
        user.put("birthdate", birthdate);
        user.put("emergency_contact_1", emergency_contact_1);
        user.put("emergency_contact_2", emergency_contact_2);
        user.put("vaccination", vaccination);
        user.put("meds", meds);
        user.put("location",location);


        db.collection("users").document(user_id)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("gazar", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("gazar", "Error writing document", e);
                    }
                });
    }
}

