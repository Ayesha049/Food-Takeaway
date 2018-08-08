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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ayeshaapp.ayesha.mojadarhomekitchen.Model.Food;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class AddFood extends Fragment {

    private EditText mfoodName;
    private EditText mdescription;
    private ImageView mfoodimage;
    private EditText mfoodprice;
    private EditText mfullAddress;
    private EditText mphoneNumber;
    private String mfoodType;
    private String mlocation;
    private String mprocessingTime;
    private Food mfoodObject;


    private static final int RC_PHOTO_PICKER =  2;



    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPhotoStrorageReference;

    private static int photoupload = 0;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_food_form, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add Food");


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Foods");

        mFirebaseStorage = FirebaseStorage.getInstance();
        mPhotoStrorageReference = mFirebaseStorage.getReference().child("myfood_photos");





        mfoodName = (EditText) getView().findViewById(R.id.add_food_name);
        mdescription = (EditText) getView().findViewById(R.id.add_food_description);
        mfoodprice = (EditText) getView().findViewById(R.id.add_food_price);
        mfullAddress = (EditText) getView().findViewById(R.id.add_food_adress);
        mphoneNumber = (EditText) getView().findViewById(R.id.add_food_phone);


        Spinner typespinner = (Spinner) getView().findViewById(R.id.add_food_type);
        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mfoodType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Spinner locationspinner = (Spinner) getView().findViewById(R.id.add_food_location);
        locationspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mlocation = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner processingpinner = (Spinner) getView().findViewById(R.id.add_food_processingTime);
        processingpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mprocessingTime = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        mfoodObject  = new Food();
        //mDatabaseReference.push().setValue(mfoodObject);

        mfoodimage = (ImageView) getView().findViewById(R.id.add_food_image);


        mfoodimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        Button addFood = (Button) getView().findViewById(R.id.add_food);

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mfoodObject.setFoodname(mfoodName.getText().toString());
                mfoodObject.setFoodtype(mfoodType);
                mfoodObject.setDescription(mdescription.getText().toString());
                mfoodObject.setPrice(mfoodprice.getText().toString());
                mfoodObject.setLocation(mlocation);
                mfoodObject.setFullAddress(mfullAddress.getText().toString());
                mfoodObject.setPhoneNumber(mphoneNumber.getText().toString());
                mfoodObject.setProcessingTime(mprocessingTime);


                //mDatabaseReference.child(BuySell.finalUid).push().setValue(mbookObject);

                mDatabaseReference.push().setValue(mfoodObject);




                Fragment ff = new HomePage();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, ff);
                ft.commit();
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

                            mfoodObject.setPhotoUrl(taskSnapshot.getDownloadUrl().toString());
                            photoupload = 1;
                            Glide.with(mfoodimage.getContext())
                                    .load(taskSnapshot.getDownloadUrl().toString())
                                    .into(mfoodimage);
                        }
                    });
        }

    }
}


