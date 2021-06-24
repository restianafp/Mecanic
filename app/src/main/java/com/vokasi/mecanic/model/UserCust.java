package com.vokasi.mecanic.model;

public class UserCust {
    String accType;
    String email;
    String password;
    String confirmPassword;
    String firstName;
    String phoneNum;
    String photo_uri;


    public UserCust(){
    }

    public UserCust(String accType, String email, String password, String confirmPassword,
                    String firstName, String phoneNum, String photo_uri) {
        this.accType = accType;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.phoneNum = phoneNum;
        this.photo_uri = photo_uri;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType =  accType;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoto_uri() {
        return photo_uri;
    }

    public void setPhoto_uri(String photo_uri) {
        this.photo_uri = photo_uri;
    }
}
