package com.example.group.myrestaurant.models;

import java.util.ArrayList;
import java.util.List;

import org.parceler.Parcel;

@Parcel
public class RestaurantModel {
    String name;
    String phone;
    String website;
    double rating;
    String imageUrl;
    List<String> address = new ArrayList<>();
    double latitude;
    double longitude;
    List<String> categories = new ArrayList<>();

    public RestaurantModel() {}

    public RestaurantModel(String name, String phone, String website,
                      double rating, String imageUrl, ArrayList<String> address,
                      double latitude, double longitude, ArrayList<String> categories) {
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.rating = rating;
        //this.imageUrl = imageUrl;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categories = categories;
        this.imageUrl = getLargeImageUrl(imageUrl);
    }

    public String getLargeImageUrl(String imageUrl) {
        String largeImageUrl = imageUrl.substring(0, imageUrl.length() - 5).concat("o.jpg");
        return largeImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return  website;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public List<String> getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<String> getCategories() {
        return categories;
    }
}
