package com.ayeshaapp.ayesha.mojadarhomekitchen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ayeshaapp.ayesha.mojadarhomekitchen.Model.Profile;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String finalUid = "mee";
    public static String finalemail = "mee";
    public static String finalname = "mee";
    public static String finalurl = "mee";


    public static final int RC_SIGN_IN = 1;
    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private Profile mprofileObject;
    public static int userflag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        mprofileObject = new Profile();


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);


        mDrawerLayout = findViewById(R.id.drawer_layout);

        display(R.id.nav_Home);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        int idd = menuItem.getItemId();
                        display(idd);


                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    private void display(int id) {
        Fragment ff = null;

        switch (id) {
            case R.id.nav_Home:
                ff = new HomePage();
                break;
            case R.id.nav_profile:
                ff = new UserProfile();
                break;
            case R.id.nav_manage:
                ff = new EditProfile();
                break;
            case R.id.nav_Logout:
                ff = new HomePage();
                AuthUI.getInstance().signOut(MainActivity.this);
                break;
        }
        if (ff != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, ff);
            ft.commit();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        userflag=0;
        // Check if user is signed in (non-null) and update UI accordingly.
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());

                if (user != null) {


                    finalUid = user.getUid();
                    finalemail = user.getEmail();
                    finalname = user.getDisplayName();
                    //final String Displayname = user.getDisplayName();



                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String uid = snapshot.getKey();
                                //String Uidd = snapshot.child("uid").getValue(String.class);
                                //Toast.makeText(MainActivity.this,Uidd,Toast.LENGTH_LONG).show();

                                if (finalUid.equals(uid)) {
                                    userflag=1;
                                    finalname=snapshot.child("name").getValue(String.class);
                                    finalurl=snapshot.child("photourl").getValue(String.class);
                                    Toast.makeText(MainActivity.this,"user paiciiii",Toast.LENGTH_LONG).show();


                                    break;

                                }
                            }
                            if(userflag==0){
                                Toast.makeText(MainActivity.this,"userk dhukalam",Toast.LENGTH_LONG).show();

                                mprofileObject.setEmail(finalemail);
                                mprofileObject.setName(finalname);
                                mprofileObject.setUid(finalUid);
                                mprofileObject.setPhotourl("Nope");
                                mDatabaseReference.child(finalUid).setValue(mprofileObject);
                                //finalname=Displayname;
                                finalurl="Nope";
                                userflag=1;

                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });


                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .setTheme(R.style.LoginTheme)
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };

    }


    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(MainActivity.this,"pause hochi",Toast.LENGTH_LONG).show();

        if(userflag==0)
        {
            Toast.makeText(MainActivity.this,"pause er vetor dhukalam",Toast.LENGTH_LONG).show();

            //mDatabaseReference.child(finalUid).setValue(mprofileObject);
            //userflag=1;
        }
        if (mFirebaseAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }


}