package com.vokasi.mecanic.model;

public class UserMontir {
    String accType;
    String email;
    String password;
    String firstName;
    String keahlian;
    String bengkel;
    String kota;
    String area;
    String alamat;
    String buka;
    String tutup;
    String phoneNum;
    String photo_Uri;
    String rating;

    public UserMontir(){
    }

    public UserMontir(String accType, String email,  String password,
                      String firstName, String keahlian, String bengkel, String kota, String area, String alamat,
                      String buka, String tutup, String phoneNum, String photo_Uri, String rating) {
        this.accType = accType;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.keahlian = keahlian;
        this.bengkel = bengkel;
        this.kota=kota;
        this.area = area;
        this.alamat = alamat;
        this.buka = buka;
        this.tutup = tutup;
        this.phoneNum = phoneNum;
        this.photo_Uri = photo_Uri;
        this.rating = rating;

    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public void setKeahlian(String keahlian) {
        this.keahlian = keahlian;
    }

    public String getBengkel() {
        return bengkel;
    }

    public void setBengkel(String bengkel) {
        this.bengkel = bengkel;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getBuka() {
        return buka;
    }

    public void setBuka(String buka) {
        this.buka = buka;
    }

    public String getTutup() {
        return tutup;
    }

    public void setTutup(String tutup) {
        this.tutup = tutup;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoto_Uri() {
        return photo_Uri;
    }

    public void setPhoto_Uri(String photo_Uri) {
        this.photo_Uri = photo_Uri;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }




}
