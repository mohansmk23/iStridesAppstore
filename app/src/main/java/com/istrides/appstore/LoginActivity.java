package com.istrides.appstore;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.istrides.appstore.Connection.ApiConstant;
import com.istrides.appstore.Connection.ApiGetPost;
import com.istrides.appstore.Connection.ObjectBody;
import com.istrides.appstore.Model.LoginModel;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{
    CircularProgressButton loginbtn;
    EditText username, password;
    String struser, strpass;
    RelativeLayout middle;
    Animation shake;
    Call<LoginModel> login;
    Handler mHandler = new Handler();
    String strsuccess;
    String token;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        loginbtn = findViewById( R.id.loginbtn );
        username = findViewById( R.id.usertxt );
        password = findViewById( R.id.passtxt );
        middle = findViewById( R.id.relative_middle );
        shake = AnimationUtils.loadAnimation( getApplicationContext(), R.anim.shake );
        getSupportActionBar().hide();
        if( checkSelfPermission( android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED )
        {
        }
        else
        {
            ActivityCompat.requestPermissions( LoginActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1 );
        }
        SharedPreferences preferences = getApplicationContext().getSharedPreferences( "login", 0 );
        if( preferences.getString( "login_name", null ) != null )
        {
            Intent i = new Intent( getApplicationContext(), AppList.class );
            startActivity( i );
            finish();
        }
        loginbtn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                validatelogin();
            }
        } );
        loginbtn.setFinalCornerRadius( 150 );
        loginbtn.setInitialCornerRadius( 150 );
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener( new OnCompleteListener<InstanceIdResult>()
        {
            @Override
            public void onComplete( @NonNull Task<InstanceIdResult> task )
            {
                if( !task.isSuccessful() )
                {
                    Log.w( "petta", "getInstanceId failed", task.getException() );
                    return;
                }
                // Get new Instance ID token
                token = task.getResult().getToken();
                // Log and toast
                //String msg = getString(R.string.msg_token_fmt, token);
                Log.d( "petta", token );
            }
        } );
    }

    private void validatelogin()
    {
        struser = username.getText().toString();
        strpass = password.getText().toString();
        if( struser.length() == 0 )
        {
            Toast.makeText( this, "Please enter username", Toast.LENGTH_SHORT ).show();
            middle.startAnimation( shake );
        }
        else if( strpass.length() == 0 )
        {
            Toast.makeText( this, "Please enter password", Toast.LENGTH_SHORT ).show();
            middle.startAnimation( shake );
        }
        else
        {
            if( checkSelfPermission( android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED )
            {
                loginretro();
            }
            else
            {
                ActivityCompat.requestPermissions( LoginActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1 );
            }
        }
    }

    private void loginretro()
    {
        loginbtn.startAnimation();
        ObjectBody.login signin = new ObjectBody.login( struser, strpass, token );
        ApiGetPost service = ApiConstant.getloginurl().create( ApiGetPost.class );
        login = service.Login( signin );
        login.enqueue( new Callback<LoginModel>()
        {
            @Override
            public void onResponse( Call<LoginModel> call, Response<LoginModel> response )
            {
                try
                {
                    LoginModel userResponse = response.body();
                    strsuccess = userResponse.getOutput().get( 0 ).getStatus();
                    if( strsuccess.equalsIgnoreCase( "success" ) )
                    {
                        SharedPreferences preferences = getSharedPreferences( "login", 0 );
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString( "access_token", userResponse.getOutput().get( 0 ).getAuthKey() );
                        editor.putString( "login_name", userResponse.getOutput().get( 0 ).getLoginName() );
                        editor.putString( "login_key", userResponse.getOutput().get( 0 ).getLoginKey() );
                        editor.apply();
                        loginbtn.doneLoadingAnimation( Color.parseColor( "#4caf50" ), BitmapFactory.decodeResource( LoginActivity.this.getResources(), R.mipmap.whitetick ) );
                        mHandler.postDelayed( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Intent i = new Intent( getApplicationContext(), AppList.class );
                                startActivity( i );
                                finish();
                            }
                        }, 1000 );
                    }
                    else
                    {
                        Toast.makeText( LoginActivity.this, "Check your credentials", Toast.LENGTH_SHORT ).show();
                        Animation shake = AnimationUtils.loadAnimation( getApplicationContext(), R.anim.shake );
                        middle.startAnimation( shake );
                        loginbtn.startAnimation( shake );
                        //loginbtn.revertAnimation();
                        loginbtn.doneLoadingAnimation( Color.parseColor( "#f44336" ), BitmapFactory.decodeResource( LoginActivity.this.getResources(), R.mipmap.whitewrong ) );
                        mHandler.postDelayed( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                loginbtn.revertAnimation();
                            }
                        }, 1500 );
                    }
                }
                catch( NumberFormatException e )
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure( Call<LoginModel> call, Throwable t )
            {
                Toast.makeText( getApplicationContext(), "Please  ", Toast.LENGTH_SHORT ).show();
                loginbtn.revertAnimation();
                call.cancel();
            }
        } );
    }
}
