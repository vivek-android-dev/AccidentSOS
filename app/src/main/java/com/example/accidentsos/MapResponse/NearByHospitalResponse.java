package com.example.accidentsos.MapResponse;

import java.util.ArrayList;

public class NearByHospitalResponse {
    public class Geometry{
        public Location location;
        public Viewport viewport;
    }

    public class Location{
        public double lat;
        public double lng;
    }

    public class Northeast{
        public double lat;
        public double lng;
    }

    public class OpeningHours{
        public boolean open_now;
    }

    public class Photo{
        public int height;
        public ArrayList<String> html_attributions;
        public String photo_reference;
        public int width;
    }

    public class PlusCode{
        public String compound_code;
        public String global_code;
    }

    public class Result{
        public String business_status;
        public Geometry geometry;
        public String icon;
        public String icon_background_color;
        public String icon_mask_base_uri;
        public String name;
        public OpeningHours opening_hours;
        public ArrayList<Photo> photos;
        public String place_id;
        public PlusCode plus_code;
        public int price_level;
        public double rating;
        public String reference;
        public String scope;
        public ArrayList<String> types;
        public int user_ratings_total;
        public String vicinity;

        public String getBusiness_status() {
            return business_status;
        }

        public void setBusiness_status(String business_status) {
            this.business_status = business_status;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon_background_color() {
            return icon_background_color;
        }

        public void setIcon_background_color(String icon_background_color) {
            this.icon_background_color = icon_background_color;
        }

        public String getIcon_mask_base_uri() {
            return icon_mask_base_uri;
        }

        public void setIcon_mask_base_uri(String icon_mask_base_uri) {
            this.icon_mask_base_uri = icon_mask_base_uri;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public OpeningHours getOpening_hours() {
            return opening_hours;
        }

        public void setOpening_hours(OpeningHours opening_hours) {
            this.opening_hours = opening_hours;
        }

        public ArrayList<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(ArrayList<Photo> photos) {
            this.photos = photos;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public PlusCode getPlus_code() {
            return plus_code;
        }

        public void setPlus_code(PlusCode plus_code) {
            this.plus_code = plus_code;
        }

        public int getPrice_level() {
            return price_level;
        }

        public void setPrice_level(int price_level) {
            this.price_level = price_level;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public ArrayList<String> getTypes() {
            return types;
        }

        public void setTypes(ArrayList<String> types) {
            this.types = types;
        }

        public int getUser_ratings_total() {
            return user_ratings_total;
        }

        public void setUser_ratings_total(int user_ratings_total) {
            this.user_ratings_total = user_ratings_total;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }
    }


        public ArrayList<Object> html_attributions;
        public ArrayList<Result> results;
        public String status;
        public String error_message;

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public ArrayList<Object> getHtml_attributions() {
            return html_attributions;
        }

        public void setHtml_attributions(ArrayList<Object> html_attributions) {
            this.html_attributions = html_attributions;
        }

        public ArrayList<Result> getResults() {
            return results;
        }

        public void setResults(ArrayList<Result> results) {
            this.results = results;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


    public class Southwest{
        public double lat;
        public double lng;
    }

    public class Viewport{
        public Northeast northeast;
        public Southwest southwest;
    }
}
