package app.petsy;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements ListFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AddPetFragment.OnFragmentInteractionListener {

    public static final String DEDEDE_COLOR = "#DEDEDE";
    private final List<CityModel> citiesList = new ArrayList();
    private ViewPager vpPager;
    private MyViewPagerAdapter adapterViewPager;
    private Button found;
    private Button lost;
    private Button add;
    private View foundButtonLine;
    private View lostButtonLine;
    private View addButtonLine;
    private ImageButton clearButton;
    private AutoCompleteTextView searchingView;
    private ImageView petImage;
    private TextView petCityPopUp;
    private TextView addressPopup;
    private TextView descriptionPopup;
    private TextView contactsPopup;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchingView = findViewById(R.id.autoCompleteTextView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setViewPager();

        changeFragmentListenerAndBtnBehavior();

        setButtons();

        setDrawerAndNavigation(toolbar);

        getData();

    }

    public void onButtonShowPopupWindowClick(View view, PetModel petModel) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference().child("photos").child(petModel.getImgId());
        Log.d("ZAQ", "storageRef:" + storageRef.getPath());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MainActivity.this)
                        .load(uri.toString())
                        .placeholder(R.drawable.photo_not_found)
                        .error(R.mipmap.ic_launcher)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(petImage);
            }
        });

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
        View popupView = inflater.inflate(R.layout.popup_window, rootView, false);
        petImage = popupView.findViewById(R.id.petImage);
        petCityPopUp = popupView.findViewById(R.id.popupCity);
        petCityPopUp.setText(getCityById(petModel.getCity()));
        addressPopup = popupView.findViewById(R.id.addressPopup);
        descriptionPopup = popupView.findViewById(R.id.descriptionPopup);
        contactsPopup = popupView.findViewById(R.id.contactsPopup);
        addressPopup.setText(getString(R.string.addressConst) + " " + petModel.getAddress());
        descriptionPopup.setText(getString(R.string.descriptionConst) + " " + petModel.getDescription());
        contactsPopup.setText(getString(R.string.contactsConst) + " " + petModel.getContacts());

        // create the popup window
        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }


    private void changeFragmentListenerAndBtnBehavior() {
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        foundButtonSelectedBehavior();
                        break;
                    case 1:
                        lostButtonSelectedBehavior();
                        break;
                    case 2:
                        addButtonSelectedBehavior();
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setButtons() {
        found = findViewById(R.id.btnFound);
        found.setOnClickListener(this);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        foundButtonLine = findViewById(R.id.foundBtnLine);
        lost = findViewById(R.id.btnLost);
        lost.setOnClickListener(this);
        lostButtonLine = findViewById(R.id.lostBtnLine);
        add = findViewById(R.id.btnAdd);
        add.setOnClickListener(this);
        addButtonLine = findViewById(R.id.addBtnLine);
        clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(this);
    }

    private void setViewPager() {
        vpPager = findViewById(R.id.vpPager);
        adapterViewPager = new MyViewPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void setSearchingView() {
        final List<String> cities = new ArrayList<>();
        for (CityModel city : citiesList) {
            cities.add(city.getHe());
            cities.add(city.getRu());
            cities.add(city.getEn());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.select_dialog_item,
                cities);
        searchingView.setThreshold(1);//will start working from first character
        searchingView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        searchingView.setBackgroundColor(Color.WHITE);

        searchingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position);
                String cityId = null;
                for (CityModel cityModel : citiesList) {
                    if (cityModel.getRu().equals(item) || cityModel.getHe().equals(item) || cityModel.getEn().equals(item)) {
                        cityId = cityModel.getId();
                    }
                }
                ListFragment listFragment0 = (ListFragment) adapterViewPager.instantiateItem(vpPager, 0);
                ListFragment listFragment1 = (ListFragment) adapterViewPager.instantiateItem(vpPager, 1);
                listFragment0.getData(cityId);
                listFragment1.getData(cityId);
            }
        });
    }

    private void setDrawerAndNavigation(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_found) {
            // Handle the camera action
            vpPager.setCurrentItem(0);
        } else if (id == R.id.nav_lost) {
            vpPager.setCurrentItem(1);
        } else if (id == R.id.nav_add) {
            vpPager.setCurrentItem(2);
        } else if (id == R.id.nav_about) {
            Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Petsy");
            String message = getString(R.string.let_me_recommend_you) + "\n\nhttps://play.google.com/store/apps/details?id=app.petsy";

            i.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(i, getString(R.string.choose_one)));
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getCitiesData() {

    }

    private void getData() {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("cities");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                assert snapshots != null;
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            CityModel cm = dc.getDocument().toObject(CityModel.class);
                            cm.setId(dc.getDocument().getId());
                            citiesList.add(cm);
                            setSearchingView();
                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:
                            break;
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFound:
                vpPager.setCurrentItem(0);
                foundButtonSelectedBehavior();
                break;
            case R.id.btnLost:
                vpPager.setCurrentItem(1);
                lostButtonSelectedBehavior();
                break;
            case R.id.btnAdd:
                vpPager.setCurrentItem(2);
                addButtonSelectedBehavior();
                break;
            case R.id.fab:
                vpPager.setCurrentItem(2);
            case R.id.clearButton:
                searchingView.setText("");
                ListFragment listFragment0 = (ListFragment) adapterViewPager.instantiateItem(vpPager, 0);
                ListFragment listFragment1 = (ListFragment) adapterViewPager.instantiateItem(vpPager, 1);
                listFragment0.getData(null);
                listFragment1.getData(null);

                break;
        }
    }

    private void addButtonSelectedBehavior() {
        add.setTextColor(Color.WHITE);
        addButtonLine.setVisibility(View.VISIBLE);
        lost.setTextColor(Color.parseColor(DEDEDE_COLOR));
        lostButtonLine.setVisibility(View.INVISIBLE);
        found.setTextColor(Color.parseColor(DEDEDE_COLOR));
        foundButtonLine.setVisibility(View.INVISIBLE);
    }

    private void lostButtonSelectedBehavior() {
        lost.setTextColor(Color.WHITE);
        lostButtonLine.setVisibility(View.VISIBLE);
        found.setTextColor(Color.parseColor(DEDEDE_COLOR));
        foundButtonLine.setVisibility(View.INVISIBLE);
        add.setTextColor(Color.parseColor(DEDEDE_COLOR));
        addButtonLine.setVisibility(View.INVISIBLE);
    }

    private void foundButtonSelectedBehavior() {
        found.setTextColor(Color.WHITE);
        foundButtonLine.setVisibility(View.VISIBLE);
        lost.setTextColor(Color.parseColor(DEDEDE_COLOR));
        lostButtonLine.setVisibility(View.INVISIBLE);
        add.setTextColor(Color.parseColor(DEDEDE_COLOR));
        addButtonLine.setVisibility(View.INVISIBLE);
    }

    @Override
    public List<CityModel> getCitiesList() {
        return citiesList;
    }

    @Override
    public String getChosenCityID(String cityName) {
        for (CityModel cityModel : citiesList) {
            if (cityModel.getRu().equals(cityName) || cityModel.getHe().equals(cityName) || cityModel.getEn().equals(cityName)) {
                return cityModel.getId();
            }
        }
        return null;
    }

    @Override
    public String getCityById(String id) {
        for (CityModel cityModel : citiesList) {
            if (cityModel.getId().equals(id)) {
                String lang = Locale.getDefault().getLanguage();
                if (lang.equals("ru")) {
                    return cityModel.ru;
                } else if (lang.equals("iw")) {
                    return cityModel.he;
                } else {
                    return cityModel.en;
                }
            }
        }
        return "Unknown";
    }

    @Override
    public void onItemClicked(PetModel petModel) {
        onButtonShowPopupWindowClick(foundButtonLine, petModel);

    }
}
