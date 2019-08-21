package com.istrides.appstore;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.istrides.appstore.Connection.ApiConstant;
import com.istrides.appstore.Connection.ApiGetPost;
import com.istrides.appstore.Connection.ObjectBody;
import com.istrides.appstore.Model.AppDetailModel;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;


public class AppDetails extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ImageView applogo, blurbg;
    LinearLayout rootlin;
    TextView appname, date, description, download, share,appVersion;
    ProgressDialog pDialog;
    String strappid, strappname, strshare = " ", strdownload = " ", strlogo;
    Call<AppDetailModel> list;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RelativeLayout nodata;
    RecyclerView recyclerView;
    Applistadapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);


        applogo = findViewById(R.id.applogo);
        appname = findViewById(R.id.appname);
        date = findViewById(R.id.datetxt);
        appVersion = findViewById(R.id.txt_version);
        description = findViewById(R.id.destxt);
        download = findViewById(R.id.downloadtxt);
        share = findViewById(R.id.sharetxt);
        nodata = findViewById(R.id.nodatalay);
        recyclerView = findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);



        strappid = getIntent().getStringExtra("APPID");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        loadapkdetais();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    downloadapk(strdownload);

                } else {
                    ActivityCompat.requestPermissions(AppDetails.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }


            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shareapk(strdownload);
            }
        });

    }

    private void loadapkdetais() {

        pDialog = new ProgressDialog(AppDetails.this);
        pDialog.show();
        pDialog.setMessage("Loading");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        ObjectBody.appdetails tech = new ObjectBody.appdetails("app_details", strappid);
        ApiGetPost service = ApiConstant.getamainurl(getApplicationContext()).create(ApiGetPost.class);
        list = service.appdetails(tech);

        list.enqueue(new Callback<AppDetailModel>() {
            @Override
            public void onResponse(Call<AppDetailModel> call, Response<AppDetailModel> response) {
                pDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);


                try {
                    AppDetailModel userResponse = response.body();
                    String strsuccess = userResponse.getOutput().get(0).getStatus();

                    if (strsuccess.equalsIgnoreCase("success")) {
                        //blurbg.setVisibility(View.GONE);
                        Glide.with(AppDetails.this).load(userResponse.getOutput().get(0).getAppsLogo()).into(new GlideDrawableImageViewTarget(applogo));
                        appname.setText(userResponse.getOutput().get(0).getAppsName());
                        strappname = userResponse.getOutput().get(0).getAppsName();
                        strlogo = userResponse.getOutput().get(0).getAppsLogo();
                        date.setText(userResponse.getOutput().get(0).getDate());
                        appVersion.setText(userResponse.getOutput().get(0).getAppVersion());
                        strdownload = userResponse.getOutput().get(0).getApkurl();
                        description.setText(userResponse.getOutput().get(0).getAppsDescription());





                        if (userResponse.getOutput().get(0).getAppsList().size() == 0) {

                            nodata.setVisibility(View.VISIBLE);

                        } else {
//                            new Handler().postDelayed(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    blurbg.setVisibility(View.VISIBLE);
//                                    Blurry.with(AppDetails.this).capture(applogo).into(blurbg);
//                                }
//                            }, 1000);
                            nodata.setVisibility(View.GONE);

                            setuprecyclerview(userResponse.getOutput().get(0).getAppsList());
                        }


                    } else {
                        nodata.setVisibility(View.VISIBLE);

                        Toast.makeText(getApplicationContext(), userResponse.getOutput().get(0).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AppDetailModel> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                nodata.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please try again ", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });


    }

    private void setuprecyclerview(List<AppDetailModel.Output.AppsList> appsList) {


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.scheduleLayoutAnimation();
        adapter = new Applistadapter(getApplicationContext(), appsList);
        recyclerView.setAdapter(adapter);
        final Context context = recyclerView.getContext();
        int resId = R.anim.list_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
        recyclerView.setLayoutAnimation(animation);

    }

    private void shareapk(String url) {

        String shareBody = url;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "App link");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Via"));

    }


    @Override
    public void onRefresh() {
        loadapkdetais();
    }


    public class Applistadapter extends RecyclerView.Adapter<Applistadapter.MyViewHolder> {

        private Context mContext;
        private List<AppDetailModel.Output.AppsList> tasklist;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView applogo;
            TextView appname, date, download, share,appVersion;


            public MyViewHolder(View view) {
                super(view);

                appname = view.findViewById(R.id.appname);
                applogo = view.findViewById(R.id.applogo);
                date = view.findViewById(R.id.datetxt);
                appVersion = view.findViewById(R.id.txt_version);
                download = view.findViewById(R.id.downloadtxt);
                share = view.findViewById(R.id.sharetxt);


            }
        }

        public Applistadapter(Context mContext, List<AppDetailModel.Output.AppsList> tasklist) {
            this.mContext = mContext;
            this.tasklist = tasklist;
        }

        @Override
        public Applistadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_app_details, parent, false);

            return new Applistadapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final Applistadapter.MyViewHolder holder, int position) {

            final AppDetailModel.Output.AppsList list = tasklist.get(position);

            holder.appname.setText(strappname);
            holder.date.setText(list.getDate());
            holder.appVersion.setText(list.getAppVersion());
            Glide.with(AppDetails.this).load(strlogo).into(new GlideDrawableImageViewTarget(holder.applogo));

            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        downloadapk(list.getUploadApk());

                    } else {
                        ActivityCompat.requestPermissions(AppDetails.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }


                }
            });

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareapk(list.getUploadApk());
                }
            });
        }

        @Override
        public int getItemCount() {
            return tasklist.size();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), AppList.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void downloadapk(String url) {

        File folder1 = new File(Environment.getExternalStorageDirectory()
                + "/Download/" + "istridesappstore/" + strappname + ".apk");

        String s = url.replaceAll(" ", "%20");
        Uri link = Uri.parse(s);

        DownloadManager.Request request = new DownloadManager.Request(link);
        request.setDescription(strappname);
        request.setTitle(strappname);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        if (folder1.exists()) {
            folder1.delete();
        }
        request.setDestinationUri(Uri.fromFile(folder1));
        // request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "istridesappstore/"+strappname+".apk");
        final DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {


            Log.i("petta","komali");

            File file = new File(Environment.getExternalStorageDirectory()
                    + "/Download/" + "istridesappstore/" + strappname + ".apk");//name here is the name of any string you want to pass to the method

            Intent d = new Intent(Intent.ACTION_VIEW);
            d.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            d.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(d);
            unregisterReceiver(this);
            Toast.makeText(AppDetails.this, "Download complete", Toast.LENGTH_SHORT).show();

        }
    };


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), AppList.class);
        startActivity(i);
        finish();
    }


}
