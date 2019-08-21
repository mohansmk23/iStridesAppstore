package com.istrides.appstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.istrides.appstore.Connection.ApiConstant;
import com.istrides.appstore.Connection.ApiGetPost;
import com.istrides.appstore.Connection.ObjectBody;
import com.istrides.appstore.Model.AppDetailModel;
import com.istrides.appstore.Model.LoginModel;
import com.istrides.appstore.Model.ProfileModel;
import com.istrides.appstore.Model.SuccessModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    EditText usertxt, emailtxt, passtxt, newpasstxt, confirmpasstxt;
    TextView edtname, edtpass;
    TextInputLayout old, newp, confirm;
    int n=0, p=0;
    ProgressDialog pDialog;
    Call<ProfileModel> list;
    Call<SuccessModel> res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        usertxt = findViewById(R.id.usertxt);
        emailtxt = findViewById(R.id.emailtxt);
        passtxt = findViewById(R.id.passtxt);
        edtname = findViewById(R.id.edtname);
        newpasstxt = findViewById(R.id.newpasstxt);
        confirmpasstxt = findViewById(R.id.confirmpasstxt);
        edtpass = findViewById(R.id.edtpass);

        old = findViewById(R.id.oldpasscontainer);
        newp = findViewById(R.id.newpasscontainer);
        confirm = findViewById(R.id.confirmcontainer);

        usertxt.setEnabled(false);
        emailtxt.setEnabled(false);

        passtxt.setVisibility(View.GONE);
        confirmpasstxt.setVisibility(View.GONE);
        newpasstxt.setVisibility(View.GONE);
        newpasstxt.setVisibility(View.GONE);
        passtxt.setVisibility(View.GONE);
        confirmpasstxt.setVisibility(View.GONE);


        edtname.setPaintFlags(edtname.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        edtpass.setPaintFlags(edtname.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        loadprofile();

        edtname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (n == 0) {

                    n = 1;
                    usertxt.setEnabled(true);
                    emailtxt.setEnabled(true);
                    edtname.setText("Save");

                } else {


                    if(usertxt.getText().length()==0){

                        Toast.makeText(ProfileActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    }else if(emailtxt.getText().length()==0){

                        Toast.makeText(ProfileActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    }else {


                        changeemailandpass(usertxt.getText().toString(),emailtxt.getText().toString());



                    }

                }


            }
        });

        edtpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (p == 0) {

                    p = 1;
                    passtxt.setVisibility(View.VISIBLE);
                    confirmpasstxt.setVisibility(View.VISIBLE);
                    newpasstxt.setVisibility(View.VISIBLE);
                    passtxt.setVisibility(View.VISIBLE);
                    confirmpasstxt.setVisibility(View.VISIBLE);
                    newpasstxt.setVisibility(View.VISIBLE);
                    edtpass.setText("Save new pass");
                } else {



                    if (passtxt.getText().toString().length()==0){

                        Toast.makeText(ProfileActivity.this, "Please enter old password", Toast.LENGTH_SHORT).show();
                    }else if(newpasstxt.getText().toString().length()==0){
                        Toast.makeText(ProfileActivity.this, "Please enter new password", Toast.LENGTH_SHORT).show();

                    }else if(confirmpasstxt.getText().toString().length()==0){
                        Toast.makeText(ProfileActivity.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
                    }else if(!newpasstxt.getText().toString().equals(confirmpasstxt.getText().toString())){

                        Toast.makeText(ProfileActivity.this, "New password and confirm password not matching", Toast.LENGTH_SHORT).show();
                    }else{

                        changepass(passtxt.getText().toString(),confirmpasstxt.getText().toString());


                    }




                }

            }
        });


    }




   private void changepass(String oldpass, String newpass){

       pDialog = new ProgressDialog(ProfileActivity.this);
       pDialog.show();
       pDialog.setMessage("Loading");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(true);

       ObjectBody.changepass tech = new ObjectBody.changepass(usertxt.getText().toString(),newpass,oldpass);
       ApiGetPost service = ApiConstant.getamainurl(getApplicationContext()).create(ApiGetPost.class);
       res = service.changepass(tech);

       res.enqueue(new Callback<SuccessModel>() {
           @Override
           public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
               pDialog.dismiss();


               try {
                   SuccessModel userResponse = response.body();
                   String strsuccess = userResponse.getStatus();

                   if (strsuccess.equalsIgnoreCase("success")) {

                       Toast.makeText(getApplicationContext(), userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                       SharedPreferences preferences = getSharedPreferences("login", 0);
                       SharedPreferences.Editor editor = preferences.edit();
                       editor.putString("login_name", null);
                       editor.putString("login_key", null);
                       editor.apply();
                       Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                       startActivity(i);
                       finish();




                   } else {


                       Toast.makeText(getApplicationContext(), userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               } catch (NumberFormatException e) {
                   e.printStackTrace();
               }
           }

           @Override
           public void onFailure(Call<SuccessModel> call, Throwable t) {
               pDialog.dismiss();
               Toast.makeText(getApplicationContext(), "Please try again ", Toast.LENGTH_SHORT).show();
               call.cancel();
           }
       });









   }


   private void changeemailandpass(final String name, final String email){


       pDialog = new ProgressDialog(ProfileActivity.this);
       pDialog.show();
       pDialog.setMessage("Loading");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(true);

       ObjectBody.changename tech = new ObjectBody.changename(name,email);
       ApiGetPost service = ApiConstant.getamainurl(getApplicationContext()).create(ApiGetPost.class);
       res = service.changename(tech);

       res.enqueue(new Callback<SuccessModel>() {
           @Override
           public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
               pDialog.dismiss();


               try {
                   SuccessModel userResponse = response.body();
                   String strsuccess = userResponse.getStatus();

                   if (strsuccess.equalsIgnoreCase("success")) {





                       Toast.makeText(getApplicationContext(), userResponse.getMessage(), Toast.LENGTH_SHORT).show();

                       usertxt.setText(name);
                       emailtxt.setText(email);
                       n = 0;
                       usertxt.setEnabled(false);
                       emailtxt.setEnabled(false);
                       edtname.setText("Edit profile details");




                   } else {


                       Toast.makeText(getApplicationContext(), userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               } catch (NumberFormatException e) {
                   e.printStackTrace();
               }
           }

           @Override
           public void onFailure(Call<SuccessModel> call, Throwable t) {
               pDialog.dismiss();
               Toast.makeText(getApplicationContext(), "Please try again ", Toast.LENGTH_SHORT).show();
               call.cancel();
           }
       });








    }


    private void loadprofile(){

        pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        ObjectBody.applist tech = new ObjectBody.applist("my_profile");
        ApiGetPost service = ApiConstant.getamainurl(getApplicationContext()).create(ApiGetPost.class);
        list = service.getprofile(tech);

        list.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                pDialog.dismiss();


                try {
                    ProfileModel userResponse = response.body();
                    String strsuccess = userResponse.getOutput().get(0).getStatus();

                    if (strsuccess.equalsIgnoreCase("success")) {

                        usertxt.setText(userResponse.getOutput().get(0).getProfile().get(0).getClientName());
                        emailtxt.setText(userResponse.getOutput().get(0).getProfile().get(0).getEmailId());






                    } else {


                        Toast.makeText(getApplicationContext(), userResponse.getOutput().get(0).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please try again ", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
