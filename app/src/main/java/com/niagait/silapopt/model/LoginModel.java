package com.niagait.silapopt.model;

public class LoginModel {
    private String nama,
            nip,
            jabatan,
            email,
            password,
            token_firebase,
            fbclid,
            status,
            result;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
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

    public String getToken_firebase() {
        return token_firebase;
    }

    public void setToken_firebase(String token_firebase) {
        this.token_firebase = token_firebase;
    }

    public String getFbclid() {
        return fbclid;
    }

    public void setFbclid(String fbclid) {
        this.fbclid = fbclid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
