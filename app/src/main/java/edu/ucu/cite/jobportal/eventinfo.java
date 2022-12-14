package edu.ucu.cite.jobportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class eventinfo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    ActionBarDrawerToggle mtoggle=null,mytoggle=null;
    ProgressBar progressBar;
    TextView TextViewTitleNav;
    DrawerLayout mydrawer;
    RecyclerView recyclerViewEvent;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager managerevent;
    private List<eventlist> productListEvent;
    eventinfo eventinfo;
    TextView TextViewNavFullname, TextViewNavIdno;
    ImageView ImageViewNavProfile;
    RecyclerAdapterEvent adapterEvent;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventinfo);
        ImageViewNavProfile = findViewById(R.id.navprofile);
        TextViewNavFullname = findViewById(R.id.navfullname);
        TextViewNavIdno = (TextView)  findViewById(R.id.navidno);


        String firstname = SharedPrefManager.getInstance(this).getFirstname();
        String middlename = SharedPrefManager.getInstance(this).getMiddlename();
        String lastname = SharedPrefManager.getInstance(this).getLastname();
        String idno = SharedPrefManager.getInstance(this).getIDno();
        String graduatedimage = SharedPrefManager.getInstance(this).getGraduatedimage();



        Glide.with(eventinfo.this).load(graduatedimage).into(ImageViewNavProfile);
        TextViewNavFullname.setText(firstname + " " + middlename + " " + lastname);
        TextViewNavIdno.setText(idno);

        recyclerViewEvent =findViewById(R.id.recycleviewevent);
        managerevent = new GridLayoutManager(eventinfo.this, 1);
        recyclerViewEvent.setLayoutManager(managerevent);
        productListEvent= new ArrayList<>();

        loadEvent();
        mydrawer = findViewById(R.id.mydrawer);
        navigationView = findViewById(R.id.navigationdrawer);

        toolbar=findViewById(R.id.sidetoolbar);
        mytoggle = new ActionBarDrawerToggle(this,mydrawer, toolbar, R.string.open, R.string.close);

        mydrawer.addDrawerListener(mytoggle);
        navigationView.bringToFront();
        mytoggle.syncState();

        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);

        //nav bar
        TextViewTitleNav = findViewById(R.id.titlenav);
        TextViewTitleNav.setText("Event");


        searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }
    private void filter(String newText) {
        List<eventlist>filteredList = new ArrayList<>();
        for (eventlist item : productListEvent){
            if (item.getEventDetaill().toLowerCase().contains(newText.toLowerCase()) || item.getEventTopic().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);

            }
        }
        adapterEvent.filterList(filteredList);
    }


    private void loadEvent() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_EVENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        productListEvent.clear();
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject event = array.getJSONObject(i);


                                productListEvent.add(new eventlist(
                                        event.getString("uploadedevent"),
                                        event.getString("eventdetail"),
                                        event.getString("startdate"),
                                        event.getString("starttime"),
                                        event.getString("enddate"),
                                        event.getString("endtime"),
                                        event.getString("venue"),
                                        event.getString("address"),
                                        event.getString("description"),
                                        event.getString("eventtype"),
                                        event.getString("eventtopic"),
                                        event.getString("organizer"),
                                        event.getString("sponsor"),
                                        event.getString("eventimage"),
                                        event.getString("eventdate"),
                                        event.getString("interested"),
                                        event.getString("notinterested"),
                                        event.getString("eventid")




                                ));





                            }

                            //creating adapter object and setting it to recyclerview
                            adapterEvent = new RecyclerAdapterEvent(eventinfo.this, productListEvent);
                            recyclerViewEvent.setAdapter(adapterEvent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("anyText",response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }









    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.mydrawer);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mytoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.profile:
                Intent intent1 = new Intent(eventinfo.this,profile.class);
                startActivity(intent1);
                break;
            case R.id.jobhiring:
                Intent intent2 = new Intent(eventinfo.this,jobhiringinfo.class);
                startActivity(intent2);
                break;
            case R.id.news:
                Intent intent3 = new Intent(eventinfo.this,newsinfo.class);
                startActivity(intent3);
                break;
            case R.id.event:
                Intent intent4 = new Intent(eventinfo.this,eventinfo.class);
                startActivity(intent4);
                break;
            case R.id.logout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                Intent intent5 = new Intent(eventinfo.this,login.class);
                startActivity(intent5);
                break;


        }

        return true;
    }
}