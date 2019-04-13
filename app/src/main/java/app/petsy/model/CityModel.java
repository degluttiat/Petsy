package app.petsy.model;

import android.support.annotation.Keep;

@Keep
public class CityModel {
    private String en;
    private String he;
    private String ru;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getHe() {
        return he;
    }

    public void setHe(String he) {
        this.he = he;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }
}
