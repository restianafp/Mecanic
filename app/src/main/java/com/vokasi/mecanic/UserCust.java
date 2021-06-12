package com.vokasi.mecanic;

public class UserCust {
    String accType;
    String email;
    String username;
    String password;
    String confirmPassword;
    String firstName;
    String lastName;
    String phoneNum;
    String photo_uri;


    public UserCust(){
    }



    public UserCust(String accType, String email, String username, String password, String confirmPassword,
                    String firstName, String lastName, String phoneNum, String photo_uri) {
        this.accType = accType;
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
