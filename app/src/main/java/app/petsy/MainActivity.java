package app.petsy;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ListFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String DEDEDE_COLOR = "#DEDEDE";
    private List<CityModel> citiesList = new ArrayList();
    private ViewPager vpPager;
    private MyPagerAdapter adapterViewPager;
    private Button found;
    private Button lost;
    private Button add;
    private View foundButtonLine;
    private View lostButtonLine;
    private View addButtonLine;
    private ImageButton clearButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setViewPager();

        changeFragmentListenerAndBtnBehavior();

        setButtons();

        setDrawerAndNavigation(toolbar);

        getData();

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
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void setSearchingView() {
        AutoCompleteTextView tv = findViewById(R.id.autoCompleteTextView);
        final List<String> cities = new ArrayList<>();
        for (CityModel city : citiesList) {
            cities.add(city.getHe());
            cities.add(city.getRu());
            cities.add(city.getEn());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.select_dialog_item,
                cities);
        tv.setThreshold(1);//will start working from first character
        tv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        tv.setBackgroundColor(Color.WHITE);

        tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        ListFragment listFragment = new ListFragment();
        if (id == R.id.nav_found) {
            // Handle the camera action
            vpPager.setCurrentItem(0);
        } else if (id == R.id.nav_lost) {
            vpPager.setCurrentItem(1);
        } else if (id == R.id.nav_add) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_share) {

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
                Intent myIntent = new Intent(MainActivity.this, AddPetActivity.class);
                startActivity(myIntent);
                addButtonSelectedBehavior();
                break;
            case R.id.clearButton:
//                tv.clearListSelection();
                System.out.println("clicked");
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
}
