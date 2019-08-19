package com.istrides.appstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mohankumar S on 6/20/2019$.
 */
public class AppListModel {

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
        @SerializedName("apps_list")
        @Expose
        private List<AppsList> appsList = null;

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

        public List<AppsList> getAppsList() {
            return appsList;
        }

        public void setAppsList(List<AppsList> appsList) {
            this.appsList = appsList;
        }


        public class AppsList {

            @SerializedName("apps_logo")
            @Expose
            private String appsLogo;
            @SerializedName("apps_name")
            @Expose
            private String appsName;
            @SerializedName("app_id")
            @Expose
            private String appId;
            @SerializedName("apps_description")
            @Expose
            private String appsDescription;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("upload_apk")
            @Expose
            private String uploadApk;

            public String getAppVersion()
            {
                return appVersion;
            }

            public void setAppVersion( String appVersion )
            {
                this.appVersion = appVersion;
            }

            @SerializedName( "app_version")
            @Expose
            private String appVersion;

            public String getAppsLogo() {
                return appsLogo;
            }

            public void setAppsLogo(String appsLogo) {
                this.appsLogo = appsLogo;
            }

            public String getAppsName() {
                return appsName;
            }

            public void setAppsName(String appsName) {
                this.appsName = appsName;
            }

            public String getAppId() {
                return appId;
            }

            public void setAppId(String appId) {
                this.appId = appId;
            }

            public String getAppsDescription() {
                return appsDescription;
            }

            public void setAppsDescription(String appsDescription) {
                this.appsDescription = appsDescription;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getUploadApk() {
                return uploadApk;
            }

            public void setUploadApk(String uploadApk) {
                this.uploadApk = uploadApk;
            }

        }

    }


}
