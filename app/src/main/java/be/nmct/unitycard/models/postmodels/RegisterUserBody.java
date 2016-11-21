package be.nmct.unitycard.models.postmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lorenzvercoutere on 18/11/16.
 */

public class RegisterUserBody {

    @SerializedName("Email")
    private String email;

    @SerializedName("Password")
    private String password;

    @SerializedName("ConfirmPassword")
    private String confirmPassword;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("language")
    private String language;

    public RegisterUserBody(String email, String password, String confirmPassword, String firstName, String lastName, String language) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.language = language;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
