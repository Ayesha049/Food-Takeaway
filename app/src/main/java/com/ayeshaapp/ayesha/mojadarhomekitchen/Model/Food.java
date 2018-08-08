package com.ayeshaapp.ayesha.mojadarhomekitchen.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {

    private String foodname;
    private String description;
    private String price;
    private String foodtype;
    private String photoUrl;
    private String email;
    private String uidd;
    private String location;
    private String fullAddress;
    private String phoneNumber;
    private String processingTime;
    private Integer count;
    

    public Food() {super();}

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFoodtype() {
        return foodtype;
    }

    public void setFoodtype(String foodtype) {
        this.foodtype = foodtype;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUidd() {
        return uidd;
    }

    public void setUidd(String uidd) {
        this.uidd = uidd;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(String processingTime) {
        this.processingTime = processingTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public static Creator<Food> getCreator() { return CREATOR;}

    public Food(Parcel parcel){
        this.foodname = parcel.readString();
        this.description = parcel.readString();
        this.price = parcel.readString();
        this.foodtype = parcel.readString();
        this.photoUrl=parcel.readString();
        this.email=parcel.readString();
        this.uidd=parcel.readString();
        this.fullAddress=parcel.readString();
        this.location=parcel.readString();
        this.phoneNumber=parcel.readString();
        this.processingTime=parcel.readString();
        this.count=parcel.readInt();
    }


    public static final Parcelable.Creator<Food> CREATOR
            = new Parcelable.Creator<Food>() {
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        public Food[] newArray(int size) {
            return new Food[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.foodname);
        parcel.writeString(this.description);
        parcel.writeString(this.price);
        parcel.writeString(this.foodtype);
        parcel.writeString(this.photoUrl);
        parcel.writeString(this.email);
        parcel.writeString(this.uidd);
        parcel.writeString(this.fullAddress);
        parcel.writeString(this.location);
        parcel.writeString(this.phoneNumber);
        parcel.writeString(this.processingTime);
        parcel.writeInt(this.count);
        
    }
}
