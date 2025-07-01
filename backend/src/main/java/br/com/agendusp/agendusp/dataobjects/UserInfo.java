package br.com.agendusp.agendusp.dataobjects;

public class UserInfo {
    // { "id": "105745592184278943666", "email": "joao.v.teodoro@usp.br",
    // "verified_email": true, "name": "Joao Victor Costa Teodoro",
    // "given_name": "Joao", "family_name": " Victor Costa Teodoro",
    // "picture":
    // "https://lh3.googleusercontent.com/a/ACg8ocKxJn8YjnEzg11_c6X6GMSuLeU2sMRuFbeI2D29tWKMQYMHtf0=s96-c",
    // "hd": "usp.br" }
    String id;
    String email;
    Boolean verified_email;
    String name;
    String given_name;
    String family_name;
    String picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVerified_email() {
        return verified_email;
    }

    public void setVerified_email(Boolean verified_email) {
        this.verified_email = verified_email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getPciture() {
        return picture;
    }

    public void setPciture(String pciture) {
        this.picture = pciture;
    }

}
