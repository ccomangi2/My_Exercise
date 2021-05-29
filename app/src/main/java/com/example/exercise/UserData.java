package com.example.exercise;

public class UserData {
    private String idToken;
    private String Name;
    private String Pw;
    private String Email;

    public UserData() { }

    public String getIdToken() { return idToken; }

    public String getEmail() { return Email; }

    public String getName() { return Name; }

    public String getPw() { return Pw; }

    public void setIdToken(String idToken) { this.idToken = idToken; }

    public void setEmail(String email) { Email = email; }

    public void setName(String name) { Name = name; }

    public void setPw(String pw) { Pw = pw; }
}
