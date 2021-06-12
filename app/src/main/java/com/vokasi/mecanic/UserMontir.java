package com.vokasi.mecanic;

public class UserMontir {
    String accType;
    String email;
    String username;
    String password;
    String firstName;
    String lastName;
    String keahlian;
    String bengkel;
    String kota;
    String alamat;
    String buka;
    String tutup;
    String phoneNum;


    public UserMontir(){
    }



    public UserMontir(String accType, String email, String username, String password,
                      String firstName, String lastName, String keahlian, String bengkel,  String kota, String alamat,
                      String buka, String tutup, String phoneNum) {
        this.accType = accType;
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.keahlian = keahlian;
        this.bengkel = bengkel;
        this.kota=kota;
        this.alamat = alamat;
        this.buka = buka;
        this.tutup = tutup;
        this.phoneNum = phoneNum;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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



}
