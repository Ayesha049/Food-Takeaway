package com.ayeshaapp.ayesha.mojadarhomekitchen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayeshaapp.ayesha.mojadarhomekitchen.Model.Profile;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;

public class UserProfile extends Fragment {

    private TextView name;
    private ImageView photo;
    private ListView list;
    private Button myOrder;
    private ImageView edit;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Profile");

        name = (TextView) getView().findViewById(R.id.user_profile_name);
        photo = (ImageView) getView().findViewById(R.id.user_profile_image);
        list = (ListView) getView().findViewById(R.id.user_profile_list);
        myOrder = (Button) getView().findViewById(R.id.user_profile_myOrder);
        edit = (ImageView) getView().findViewById(R.id.user_profile_edit);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        name.setText(MainActivity.finalname);

        if(!MainActivity.finalurl.equals("Nope"))
        {
            Glide.with(photo.getContext())
                    .load(MainActivity.finalurl)
                    .into(photo);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ff = new EditProfile();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, ff);
                ft.commit();
            }
        });






    }

    @Override
    public void onStart() {
        super.onStart();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Profile obj = dataSnapshot.getValue(Profile.class);
                    String Uidd = snapshot.getKey();
                    //Toast.makeText(MainActivity.this,Uidd,Toast.LENGTH_LONG).show();

                    if (MainActivity.finalUid.equals(Uidd)) {
                        Toast.makeText(getActivity(),"profile e user paiciiii",Toast.LENGTH_LONG).show();
                        String oldname = snapshot.child("name").getValue(String.class);
                        String oldurl = snapshot.child("photourl").getValue(String.class);
                        name.setText(oldname);

                        if(!oldurl.equals("Nope"))
                        {
                            Glide.with(photo.getContext())
                                    .load(oldurl)
                                    .into(photo);
                        }

                        break;

                    }
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }
}