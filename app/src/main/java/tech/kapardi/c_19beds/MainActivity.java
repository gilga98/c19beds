package tech.kapardi.c_19beds;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.ionbit.ionalert.IonAlert;
import tech.kapardi.c_19beds.adapters.HospitalsAdapter;
import tech.kapardi.c_19beds.models.Hospital;
import tech.kapardi.c_19beds.utils.Constants;
import tech.kapardi.c_19beds.utils.LoadingDialog;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private List<Hospital> hospitalListMain = new ArrayList<>();
    private HospitalsAdapter mAdapter;

    private double latitude;
    private double longitude;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    LoadingDialog loadingDialog;
    boolean isCalled = false;

    BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;

    private Button btn_filter_all_hp, btn_filter_distance_25, btn_filter_distance_10, btn_sort_comfortness, btn_sort_travel_distance, btn_sort_bed_availability, refresh_btn;
    private ImageButton hp_search_btn;
    private TextView tv_privacy, tv_about_us, tv_terms;
    private EditText hp_search_text;
    private LinearLayout oops_no_data_layout;
    private ImageView options_arrow_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        loadingDialog = new LoadingDialog(MainActivity.this);
        recyclerView =  findViewById(R.id.hospitals_rv);
        swipeContainer =  findViewById(R.id.swipeContainer);
        tv_about_us =  findViewById(R.id.tv_about_us);
        tv_terms =  findViewById(R.id.tv_terms);
        tv_privacy =  findViewById(R.id.tv_privacy);
        hp_search_text =  findViewById(R.id.hp_search_text);
        hp_search_btn =  findViewById(R.id.hp_search_btn);
        oops_no_data_layout =  findViewById(R.id.oops_no_data_layout);
        refresh_btn =  findViewById(R.id.refresh_btn);
        options_arrow_img =  findViewById(R.id.options_arrow_img);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        options_arrow_img.setRotation(0);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        options_arrow_img.setRotation(180);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchHospitalAsync(latitude, longitude);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
                AboutUsFragment bottomSheetFragment = new AboutUsFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });*/

        btn_filter_all_hp =  findViewById(R.id.btn_filter_all_hp);
        btn_filter_distance_25 =  findViewById(R.id.btn_filter_distance_25);
        btn_filter_distance_10 =  findViewById(R.id.btn_filter_distance_10);
        btn_sort_comfortness =  findViewById(R.id.btn_sort_comfortness);
        btn_sort_travel_distance =  findViewById(R.id.btn_sort_travel_distance);
        btn_sort_bed_availability =  findViewById(R.id.btn_sort_bed_availability);

        btn_filter_all_hp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFilterBy("all");
            }
        });

        btn_filter_distance_25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFilterBy("25km");
            }
        });

        btn_filter_distance_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFilterBy("10km");
            }
        });

        btn_sort_comfortness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListSortBy("comfortness");
            }
        });

        btn_sort_travel_distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListSortBy("travel_dist");
            }
        });

        btn_sort_bed_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListSortBy("bed_avail");
            }
        });

        tv_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                AboutUsFragment bottomSheetFragment = new AboutUsFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });


        hp_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_text = hp_search_text.getText().toString().trim();
                ListSearchBy(search_text);
                hp_search_text.setText("");
                hp_search_text.clearFocus();

            }
        });


        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchHospitalAsync(latitude,longitude);
            }
        });

        if (mGoogleApiClient == null) {
            loadingDialog.showDialog( "");
            buildGoogleApiClient();
            loadingDialog.dismissDialog();
        }
    }

    private void ListSearchBy(String searchKey){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        List<Hospital> hospitalList = new ArrayList<>();
        for (Hospital hospital : hospitalListMain) {
            if (hospital.getName().toLowerCase().contains(searchKey.toLowerCase())) {
                hospitalList.add(hospital);
            }
        }
        RenderList(hospitalList);
    }
    private void ShowNoData(int Count){
        if(Count == 0){
            oops_no_data_layout.setVisibility(View.VISIBLE);
            swipeContainer.setVisibility(View.GONE);
        }
       else{
            oops_no_data_layout.setVisibility(View.GONE);
            swipeContainer.setVisibility(View.VISIBLE);
        }
    }
    private void ListSortBy(String SortBy){
        List<Hospital> hospitalList = new ArrayList<>();
        hospitalList = hospitalListMain;
        switch (SortBy){
            case "bed_avail" :
                Collections.sort(hospitalList, new Comparator<Hospital>() {
                    public int compare(Hospital o1, Hospital o2) {
                        return o1.getBed_availability().compareTo(o2.getBed_availability());
                    }
                });
                Collections.reverse(hospitalList);
                break;
            case "travel_dist" :
                Collections.sort(hospitalList, new Comparator<Hospital>() {
                    public int compare(Hospital o1, Hospital o2) {
                        return o1.getGeodesic_distance().compareTo(o2.getGeodesic_distance());
                    }
                });
                break;
            case "comfortness" :
                Collections.sort(hospitalList, new Comparator<Hospital>() {
                    public int compare(Hospital o1, Hospital o2) {
                        return o1.getComfortness_factor().compareTo(o2.getComfortness_factor());
                    }
                });
                Collections.reverse(hospitalList);
                break;
        }
        RenderList(hospitalList);
    }

    private void ListFilterBy(String FilterBy){
        List<Hospital> hospitalList = new ArrayList<>();
        switch (FilterBy){
            case "10km" :
                for(int i=0; i<hospitalListMain.size(); i++){
                    Hospital mobj = hospitalListMain.get(i);
                    if(mobj.getGeodesic_distance() <= 10){
                        hospitalList.add(mobj);
                    }
                }
                break;
            case "25km" :
                for(int i=0; i<hospitalListMain.size(); i++){
                    Hospital mobj = hospitalListMain.get(i);
                    if(mobj.getGeodesic_distance() <= 25){
                        hospitalList.add(mobj);
                    }
                }
                break;
            case "all" :
                hospitalList = hospitalListMain;
                break;
        }
        RenderList(hospitalList);
    }

    private  void RenderList( List<Hospital> hospitalList){
        loadingDialog.showDialog( "");
        mAdapter = new HospitalsAdapter(hospitalList, MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        loadingDialog.dismissDialog();
        ShowNoData(hospitalList.size());
    }

    private void CheckFunctionCall(){
        if(!isCalled){
            fetchHospitalAsync(latitude, longitude);
            isCalled = true;
        }
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }*/

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(50000);
        mLocationRequest.setFastestInterval(50000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //For Data Fetch
    private void fetchHospitalAsync( final double usr_lat, final  double usr_long) {
        loadingDialog.showDialog( "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_HOSPITALS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        loadingDialog.dismissDialog();
                        try {
                            //converting response to json object
                            //JSONObject obj = new JSONObject(ServerResponse);
                            JSONArray jsonArray = new JSONArray(ServerResponse);
                                hospitalListMain.clear();
                                if (jsonArray.length() > 0) {
                                    for (int i = 0, size = jsonArray.length(); i < size; i++) {
                                        JSONObject mobj = jsonArray.getJSONObject(i);
                                        Hospital hospital = new Hospital();
                                        hospital.setName(mobj.getString("name"));
                                        hospital.setAddress(mobj.getString("address"));
                                        hospital.setLatitude(mobj.getString("latitude"));
                                        hospital.setLongitude(mobj.getString("longitude"));
                                        hospital.setCountry_code(mobj.getString("country_code"));
                                        hospital.setContact_no(mobj.getString("contact"));
                                        hospital.setPhone_area_code(mobj.getString("phone_area_code"));
                                        hospital.setPin_code(mobj.getString("pincode"));
                                        hospital.setTotal_beds(mobj.getInt("total_beds"));
                                        hospital.setRemaining_beds(mobj.getInt("remaining_beds"));
                                        hospital.setBed_availability(mobj.getInt("bed_availability"));
                                        hospital.setComfortness_factor(mobj.getDouble("comfortness"));
                                        hospital.setGeodesic_distance(mobj.getDouble("geodesic_distance"));
                                        if(mobj.has("logo") && !mobj.isNull("logo")){
                                            hospital.setImg_path(mobj.getString("logo"));
                                        }
                                        else{
                                            hospital.setImg_path("");
                                        }
                                        hospitalListMain.add(hospital);
                                    }

                                    ListFilterBy("all");
                                    swipeContainer.setRefreshing(false);
                            }
                            else {

                            }
                        }
                        catch (Exception e)
                        {
                            //Toast.makeText(getApplicationContext(), "Error In parsing", Toast.LENGTH_SHORT).show();
                            //e.printStackTrace();
                            new IonAlert(MainActivity.this, IonAlert.WARNING_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Error occurred while accessing data. Please try again.")
                                    .setConfirmText("Refresh")
                                    .setConfirmClickListener(new IonAlert.ClickListener() {
                                        @Override
                                        public void onClick(IonAlert sDialog) {
                                            sDialog.dismissWithAnimation();
                                            fetchHospitalAsync(latitude, longitude);
                                        }
                                    })
                                    .show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // Showing error message if something goes wrong.

                        Toast.makeText(getApplicationContext(), "Error::"+volleyError.toString(), Toast.LENGTH_SHORT).show();

                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            //This indicates that the reuest has either time out or there is no connection

                            //Toast.makeText(getActivity(), "Connection is too slow... kindly wait we are trying to reconnect...", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof AuthFailureError) {
                            //Error indicating that there was an Authentication Failure while performing the request
                            // Toast.makeText(getActivity(), "Kindly wait we are trying to reconnect...", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            //  Toast.makeText(getActivity(), "Kindly wait we are trying to reconnect...", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            //   Toast.makeText(getActivity(), "Kindly wait we are trying to reconnect...", Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            //  Toast.makeText(getActivity(), "Kindly wait we are trying to reconnect...", Toast.LENGTH_SHORT).show();

                        }
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                // Adding All values to Params.
                params.put("longitude", String.valueOf(usr_long));
                params.put("latitude", String.valueOf(usr_lat));
                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //add max retries so that it cant give timeout error
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //Toast.makeText(getApplicationContext(),"test clicked", Toast.LENGTH_LONG).show();
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void  OnCardButtonClick(boolean call, String data){
        if(call ){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+data));
            startActivity(intent);
        }
        else {
            Uri mapUri = Uri.parse(data);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        //Place current location marker
        longitude = location.getLatitude();
         latitude = location.getLongitude();
        CheckFunctionCall();
        //Toast.makeText(this, latitude+":::"+longitude, Toast.LENGTH_SHORT).show();

    }
}