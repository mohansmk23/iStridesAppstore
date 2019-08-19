package com.istrides.appstore.Connection;

/**
 * Created by Mohankumar S on 6/18/2019$.
 */
public class ObjectBody {


    public static class login {
        private String username;
        private String password;
        private String notification_id;

        public login(String username, String password, String notification_id) {
            this.username = username;
            this.password = password;
            this.notification_id = notification_id;
        }
    }



    public static class applist {
        private String apk_key;

        public applist(String apk_key) {
            this.apk_key = apk_key;
        }
    }


    public static class appdetails {
        private String apk_key;
        private String app_id;


        public appdetails(String apk_key, String app_id) {
            this.apk_key = apk_key;
            this.app_id = app_id;
        }
    }

}
