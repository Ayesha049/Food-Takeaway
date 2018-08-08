package com.ayeshaapp.ayesha.mojadarhomekitchen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfile extends Fragment {

    private TextView name;
    private ImageView photo;
    private Button save;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Profile mprofileObject;

    private static final int RC_PHOTO_PICKER =  2;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPhotoStrorageReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Edit Profile");

        name = (TextView) getView().findViewById(R.id.edit_profile_name);
        photo = (ImageView) getView().findViewById(R.id.edit_profile_image);
        save = (Button) getView().findViewById(R.id.edit_profile_save);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        mFirebaseStorage = FirebaseStorage.getInstance();
        mPhotoStrorageReference = mFirebaseStorage.getReference().child("user_photos");

        mprofileObject = new Profile();
        mprofileObject.setUid(MainActivity.finalUid);
        mprofileObject.setEmail(MainActivity.finalemail);


        name.setText(MainActivity.finalname);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = name.getText().toString().trim();
                if(!newName.equals(""))
                {
                    MainActivity.finalname = newName;
                    mprofileObject.setName(newName);
                }
                MainActivity.finalurl = mprofileObject.getPhotourl();
                mDatabaseReference.child(MainActivity.finalUid).setValue(mprofileObject);


                Fragment ff = new UserProfile();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, ff);
                ft.commit();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });




    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_PHOTO_PICKER && resultCode==-1)
        {
            //Toast.makeText(getActivity(),"Hello world",Toast.LENGTH_LONG).show();
            Uri SelectedImageUri = data.getData();
            StorageReference photoRef =
                    mPhotoStrorageReference.child(SelectedImageUri.getLastPathSegment());

            photoRef.putFile(SelectedImageUri).addOnSuccessListener(
                    getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(getActivity(),"Hello world",Toast.LENGTH_LONG).show();

                            mprofileObject.setPhotourl(taskSnapshot.getDownloadUrl().toString());
                            //photoupload = 1;
                            Glide.with(photo.getContext())
                                    .load(taskSnapshot.getDownloadUrl().toString())
                                    .into(photo);
                        }
                    });
        }

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
                        Toast.makeText(getActivity(),"edit e user paiciiii",Toast.LENGTH_LONG).show();
                        String oldname = snapshot.child("name").getValue(String.class);
                        String oldurl = snapshot.child("photourl").getValue(String.class);
                        mprofileObject.setName(oldname);
                        mprofileObject.setPhotourl(oldurl);
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
