package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RegisterUserErrorResponse {
    public class ModelState {
        @SerializedName("")
        private ArrayList<String> errors;

        public ArrayList<String> getErrors() {
            return errors;
        }
    }

    @SerializedName("Message")
    private String message;

    public String getMessage() {
        return message;
    }

    @SerializedName("ModelState")
    private ModelState modelState;

    public ModelState getModelState() {
        return modelState;
    }
}
