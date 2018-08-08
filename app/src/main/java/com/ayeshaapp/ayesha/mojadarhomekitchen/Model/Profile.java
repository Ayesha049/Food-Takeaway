package com.ayeshaapp.ayesha.mojadarhomekitchen.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private String name;
    private String photourl;
    private String email;
    private String Uid;

    public Profile() { super(); }

    public Profile(String email,String name, String photourl,String Uid){
        this.name = name;
        this.photourl = photourl;
        this.email = email;
        this.Uid = Uid;;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    protected Profile(Parcel in) {
        this.name = in.readString();
        this.photourl = in.readString();
        this.email = in.readString();
        this.Uid = in.readString();
    }
    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }
        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.photourl);
        parcel.writeString(this.email);
        parcel.writeString(this.Uid);
    }
}
