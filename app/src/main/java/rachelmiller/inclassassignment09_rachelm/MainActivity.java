package rachelmiller.inclassassignment09_rachelm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference userRef;
    private FirebaseAuth.AuthStateListener authListener;

    private TextView displayText;
    private EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayText = (TextView) findViewById(R.id.displayText);
        inputText = (EditText) findViewById(R.id.input_text);

        authListener = new FirebaseAuth.AuthStateListener() { //creating a new Listener
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) { //method to listen for a change in authentication
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {//if the user is not logged in go to the login page


                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    userRef = database.getReference(user.getUid());
                    DatabaseReference userRef = database.getReference(user.getUid()); //pulling from firebase
                    userRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String text = dataSnapshot.getValue(String.class);
                            displayText.setText(displayText.getText() + "\n" + text);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }

    public void logOut(View view) {
        auth.signOut();
        displayText.setText("");
        inputText.setText("");
    }

    public void send(View view) {
        userRef.push().setValue(inputText.getText().toString());
    }
}