package com.istrides.appstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mohankumar S on 6/21/2019$.
 */
public class AppDetailModel {


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
        @SerializedName("apps_logo")
        @Expose
        private String appsLogo;
        @SerializedName("apps_name")
        @Expose
        private String appsName;
        @SerializedName("apps_description")
        @Expose
        private String appsDescription;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("apps_list")
        @Expose
        private List<AppsList> appsList = null;

        @SerializedName("latest_apk")
        @Expose
        private String apkurl;

        public String getApkurl() {
            return apkurl;
        }

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

        public List<AppsList> getAppsList() {
            return appsList;
        }

        public void setAppsList(List<AppsList> appsList) {
            this.appsList = appsList;
        }



        public class AppsList {

            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("upload_id")
            @Expose
            private String uploadId;
            @SerializedName("app_description")
            @Expose
            private String appDescription;
            @SerializedName("upload_apk")
            @Expose
            private String uploadApk;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

            public String getAppDescription() {
                return appDescription;
            }

            public void setAppDescription(String appDescription) {
                this.appDescription = appDescription;
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
