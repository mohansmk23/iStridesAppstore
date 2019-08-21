package com.istrides.appstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.istrides.appstore.Connection.ApiConstant;
import com.istrides.appstore.Connection.ApiGetPost;
import com.istrides.appstore.Connection.ObjectBody;
import com.istrides.appstore.Model.AppListModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class AppList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    DrawerLayout dLayout;
    ActionBarDrawerToggle mytoggle;
    NavigationView navView;
    TextView heading;
    RecyclerView recyclerView;
    ProgressDialog pDialog;
    Call<AppListModel> list;
    Applistadapter adapter;
    RelativeLayout nodata;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        dLayout   = (DrawerLayout) findViewById(R.id.drawer);
        heading = findViewById(R.id.heading);
        heading.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_up);
        heading.startAnimation(bottomUp);
        heading.setVisibility(View.VISIBLE);

        dLayout   = (DrawerLayout) findViewById(R.id.drawer);
        nodata = (RelativeLayout) findViewById(R.id.nodatalay);
        mytoggle = new ActionBarDrawerToggle(this,dLayout,R.string.open,R.string.close);
        dLayout.addDrawerListener(mytoggle);
        mytoggle.syncState();

        setNavigationDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        loadapplist();
    }
    private void loadapplist() {
        pDialog = new ProgressDialog(AppList.this);
        pDialog.show();
        pDialog.setMessage("Loading");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        ObjectBody.applist tech = new ObjectBody.applist ("list_apk");
        ApiGetPost service = ApiConstant.getamainurl(getApplicationContext()).create(ApiGetPost.class);
        list = service.applist(tech);

        list.enqueue(new Callback<AppListModel>() {
            @Override
            public void onResponse(Call<AppListModel> call, Response<AppListModel> response) {
                pDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);


                try {
                    AppListModel userResponse = response.body();
                    String strsuccess = userResponse.getOutput().get(0).getStatus();

                    if (strsuccess.equalsIgnoreCase("success")) {

                        if (userResponse.getOutput().get(0).getAppsList().size()==0){

                            nodata.setVisibility(View.VISIBLE);
                            Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.bottom_up);
                            heading.startAnimation(bottomUp);
                            heading.setVisibility(View.VISIBLE);

                        }else{
                            nodata.setVisibility(View.GONE);

                            setuprecyclerview(userResponse.getOutput().get(0).getAppsList());
                        }


                    }else{
                        nodata.setVisibility(View.VISIBLE);

                        Toast.makeText(getApplicationContext(), userResponse.getOutput().get(0).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AppListModel> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                nodata.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please try again ", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });





    }


    private void setuprecyclerview(List<AppListModel.Output.AppsList> applist) {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.scheduleLayoutAnimation();

        adapter = new Applistadapter (getApplicationContext(),applist);
        recyclerView.setAdapter(adapter);

        final Context context = recyclerView.getContext();
        int resId = R.anim.list_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
        recyclerView.setLayoutAnimation(animation);

    }

    @Override
    public void onRefresh() {
        loadapplist();
    }

    public class Applistadapter extends RecyclerView.Adapter<Applistadapter.MyViewHolder> {

        private Context mContext;
        private List<AppListModel.Output.AppsList> tasklist;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView nametxt,datetxt,appVersion;
            LinearLayout applay;
            ImageView applogo;


            public MyViewHolder(View view) {
                super(view);

                nametxt = (TextView) view.findViewById(R.id.appname);
                applogo = (ImageView) view.findViewById(R.id.applogo);
                datetxt = (TextView) view.findViewById(R.id.datetxt);
                appVersion = (TextView) view.findViewById(R.id.txt_version);
                applay = (LinearLayout) view.findViewById(R.id.applay);


            }
        }

        public Applistadapter(Context mContext, List<AppListModel.Output.AppsList> tasklist) {
            this.mContext = mContext;
            this.tasklist = tasklist;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_app_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            final AppListModel.Output.AppsList list = tasklist.get(position);

         holder.nametxt.setText(list.getAppsName());
         holder.datetxt.setText(list.getDate());
         holder.appVersion.setText(list.getAppVersion());
         Glide.with(AppList.this).load(list.getAppsLogo()).into(new GlideDrawableImageViewTarget(holder.applogo));

          holder.applay.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent i = new Intent(getApplicationContext(),AppDetails.class);
                  i.putExtra("APPID",list.getAppId());
                  startActivity(i);
                  finish();
              }
          });
        }

        @Override
        public int getItemCount() {
            return tasklist.size();
        }
    }




    private void setNavigationDrawer() {

        dLayout         = (DrawerLayout) findViewById(R.id.drawer);
        navView         = (NavigationView) findViewById(R.id.navigation);
        View header     = navView.getHeaderView(0);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(false);
                int itemId = menuItem.getItemId();
                if(itemId == R.id.home){
                    dLayout.closeDrawers();
                    return true;
                }
                else if (itemId == R.id.logout) {
                    logoutalert();
                    dLayout.closeDrawers();
                    return true; }
                else if (itemId == R.id.profile ){
                    Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(i);
                    dLayout.closeDrawers();

                }

                return false;
            }
        });
    }


    private void logoutalert() {

        AlertDialog.Builder ad =  new AlertDialog.Builder(AppList.this);
        ad.setTitle("Logout?");
        ad.setMessage("Are you sure you want to logout?");

        // Specifying a listener allows you to take an action before dismissing the dialog.
        // The dialog is automatically dismissed when a dialog button is clicked.
        ad.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences preferences = getSharedPreferences("login", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("login_name", null);
                editor.putString("login_key", null);
                editor.apply();
                dialog.dismiss();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
                // Continue with delete operation
            }
        });

        // A null listener allows the button to dismiss the dialog and take no further action.
        ad.setNegativeButton(android.R.string.no, null);
        ad.setIcon(android.R.drawable.ic_dialog_alert);
        ad.show();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mytoggle.onOptionsItemSelected(item)){
            return  true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                dLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                if (dLayout.isDrawerVisible(GravityCompat.START)) {
                    dLayout.closeDrawer(GravityCompat.START);
                } else {
                    dLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            moveTaskToBack(true);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }


}

