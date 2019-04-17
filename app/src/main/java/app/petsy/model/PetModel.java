package app.petsy.model;

import android.support.annotation.Keep;


import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Keep
public class PetModel {
    private String city;
    private String address;
    private String description;
    private String imgId;
    private String contacts;
    private String date;
    private String cachedImageUrl;
    @ServerTimestamp
    private Date timestamp;

    public PetModel() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public void setDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        this.date = (dateFormat.format(date));
    }

    public String getDate() {
        return date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getCachedImageUrl() {
        return cachedImageUrl;
    }

    public void setCachedImageUrl(String cachedImageUrl) {
        this.cachedImageUrl = cachedImageUrl;
    }
}
