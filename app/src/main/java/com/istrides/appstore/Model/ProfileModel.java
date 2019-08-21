package com.istrides.appstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mohankumar S on 8/20/2019$.
 */
public class ProfileModel {



    @SerializedName("Output")
    @Expose
    private List<Output> output = null;

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }




    public class Output {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("profile")
        @Expose
        private List<Profile> profile = null;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Profile> getProfile() {
            return profile;
        }

        public void setProfile(List<Profile> profile) {
            this.profile = profile;
        }

        public class Profile {

            @SerializedName("Client Name")
            @Expose
            private String clientName;
            @SerializedName("Email Id")
            @Expose
            private String emailId;

            public String getClientName() {
                return clientName;
            }

            public void setClientName(String clientName) {
                this.clientName = clientName;
            }

            public String getEmailId() {
                return emailId;
            }

            public void setEmailId(String emailId) {
                this.emailId = emailId;
            }

        }

    }


}
