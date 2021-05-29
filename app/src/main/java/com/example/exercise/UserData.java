package com.example.exercise;

public class UserData {
    private String idToken;
    private String Name;
    private String Pw;
    private String Email;
    private String cm;
    private String kg;

    public UserData() { }

    public UserData(String idToken, String Name, String Pw, String Email, String cm, String kg) {
        this.idToken = idToken; //사용자 고유 IdToken
        this.Name = Name; //사용자 이름
        this.Pw = Pw; //비밀번호
        this.Email = Email; //아이디이자 이메일
        this.cm = cm; //키
        this.kg = kg; //몸무게
    }

    public String getIdToken() { return idToken; }

    public String getEmail() { return Email; }

    public String getName() { return Name; }

    public String getPw() { return Pw; }

    public String getCm() { return cm; }

    public String getKg() { return kg; }

    public void setIdToken(String idToken) { this.idToken = idToken; }

    public void setEmail(String email) { Email = email; }

    public void setName(String name) { Name = name; }

    public void setPw(String pw) { Pw = pw; }

    public void setCm(String cm) { this.cm = cm; }

    public void setKg(String kg) { this.kg = kg; }
}
