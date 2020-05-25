package com.example.cst2335_finalproject.NASA_Imagery;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Class to store NASA_Images variable with their respective getters and setters for use in other classes in the application
 */
public class NASA_Images implements Serializable {
    /**
     * Holds the direction value of the image
     */
    protected String direction;
    /**
     * holds the latitude value of the image
     */
    protected double latitude;
    /**
     * holds the longitude value of the image
     */
    protected double longitude;
    /**
     * Holds the bitmap value of the image
     */
    protected Bitmap image;
    /**
     * Holds the id value of the image in the database
     */
    protected long id;

    /**
     * Full constructor to store create an object of the class
     * @param direction string value
     * @param latitude double value
     * @param longitude double value
     * @param image bitmap value
     * @param id int value
     */
    public NASA_Images(String direction, double latitude, double longitude, Bitmap image, long id){
        this.direction = direction;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.id = id;
    }

    public String getDirection(){
        return direction;
    }
    public double getLatitude(){
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Bitmap getImage() {
        return image;
    }

    public long getId() {
        return id;
    }

}
