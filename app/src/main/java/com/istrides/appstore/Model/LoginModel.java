package com.istrides.appstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mohankumar S on 6/18/2019$.
 */
public class LoginModel {


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
        @SerializedName("login_key")
        @Expose
        private String loginKey;
        @SerializedName("login_name")
        @Expose
        private String loginName;
        @SerializedName("client_id")
        @Expose
        private Integer clientId;
        @SerializedName("auth_key")
        @Expose
        private String authKey;
        @SerializedName("client_name")
        @Expose
        private String clientName;
        @SerializedName("login_unique_key")
        @Expose
        private String loginUniqueKey;

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

        public String getLoginKey() {
            return loginKey;
        }

        public void setLoginKey(String loginKey) {
            this.loginKey = loginKey;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public Integer getClientId() {
            return clientId;
        }

        public void setClientId(Integer clientId) {
            this.clientId = clientId;
        }

        public String getAuthKey() {
            return authKey;
        }

        public void setAuthKey(String authKey) {
            this.authKey = authKey;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public String getLoginUniqueKey() {
            return loginUniqueKey;
        }

        public void setLoginUniqueKey(String loginUniqueKey) {
            this.loginUniqueKey = loginUniqueKey;
        }

    }



}
