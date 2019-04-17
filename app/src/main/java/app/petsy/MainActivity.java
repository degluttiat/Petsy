package app.petsy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnFailureListener;
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

import app.petsy.model.CityModel;
import app.petsy.model.PetModel;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements ListFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AddPetFragment.OnFragmentInteractionListener {

    public static final String DEDEDE_COLOR = "#DEDEDE";
    private final List<CityModel> cityModelList = new ArrayList<>();
    private ViewPager viewPager;
    private MyViewPagerAdapter adapterViewPager;
    private Button btnFound;
    private Button btnLost;
    private Button btnHomeless;
    private View lineFound;
    private View lineLost;
    private View lineHomeless;
    private AutoCompleteTextView searchingView;
    private FloatingActionButton fab;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        appBarLayout = findViewById(R.id.appBar);

        searchingView = findViewById(R.id.autoCompleteTextView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setViewPager();

        changeFragmentListenerAndBtnBehavior();

        setButtons();

        setDrawerAndNavigation(toolbar);

        getData();

        //throw new IllegalStateException("Test");
    }

    private void changeFragmentListenerAndBtnBehavior() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                        homelessButtonSelectedBehavior();
                        break;
                    case 3:
                        fabButtonSelectedBehavior();
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setButtons() {
        btnFound = findViewById(R.id.btnFound);
        btnFound.setOnClickListener(this);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        lineFound = findViewById(R.id.foundBtnLine);
        btnLost = findViewById(R.id.btnLost);
        btnLost.setOnClickListener(this);
        lineLost = findViewById(R.id.lostBtnLine);
        btnHomeless = findViewById(R.id.btnHomeless);
        btnHomeless.setOnClickListener(this);
        lineHomeless = findViewById(R.id.homelessBtnLine);
        ImageButton clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(this);
    }

    private void setViewPager() {
        viewPager = findViewById(R.id.vpPager);
        viewPager.setOffscreenPageLimit(3);
        adapterViewPager = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void setSearchingView() {
        final List<String> cities = new ArrayList<>();
        for (CityModel city : cityModelList) {
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
                String cityName = adapter.getItem(position);
                String cityId = getChosenCityID(cityName);
                if (cityId == null) {
                    return;
                }
                ListFragment listFragment0 = (ListFragment) adapterViewPager.instantiateItem(viewPager, 0);
                ListFragment listFragment1 = (ListFragment) adapterViewPager.instantiateItem(viewPager, 1);
                ListFragment listFragment2 = (ListFragment) adapterViewPager.instantiateItem(viewPager, 2);
                listFragment0.getData(cityId);
                listFragment1.getData(cityId);
                listFragment2.getData(cityId);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_found) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_lost) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_homeless) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_add) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_about) {
            Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_pp) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cdn.rawgit.com/degluttiat/Petsy/master/privacy_policy.html"));
            startActivity(browserIntent);
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

    private void getData() {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("cities");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null || snapshots == null) {
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            CityModel cm = dc.getDocument().toObject(CityModel.class);
                            cm.setId(dc.getDocument().getId());
                            cityModelList.add(cm);
                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:
                            break;
                    }
                }
                setSearchingView();
                // Set searching view in AddPetFragment
                AddPetFragment fragment = (AddPetFragment) adapterViewPager.instantiateItem(viewPager, 3);
                fragment.setAutoComplete();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFound:
                viewPager.setCurrentItem(0);
                foundButtonSelectedBehavior();
                break;
            case R.id.btnLost:
                viewPager.setCurrentItem(1);
                lostButtonSelectedBehavior();
                break;
            case R.id.btnHomeless:
                viewPager.setCurrentItem(2);
                homelessButtonSelectedBehavior();
                break;
            case R.id.fab:
                viewPager.setCurrentItem(3);
                fabButtonSelectedBehavior();
            case R.id.clearButton:
                searchingView.setText("");
                ListFragment listFragment0 = (ListFragment) adapterViewPager.instantiateItem(viewPager, 0);
                ListFragment listFragment1 = (ListFragment) adapterViewPager.instantiateItem(viewPager, 1);
                ListFragment listFragment2 = (ListFragment) adapterViewPager.instantiateItem(viewPager, 2);
                listFragment0.getData(null);
                listFragment1.getData(null);
                listFragment2.getData(null);
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void fabButtonSelectedBehavior() {
        lineLost.setVisibility(View.INVISIBLE);
        lineFound.setVisibility(View.INVISIBLE);
        lineHomeless.setVisibility(View.INVISIBLE);
        btnLost.setTextColor(Color.parseColor(DEDEDE_COLOR));
        btnFound.setTextColor(Color.parseColor(DEDEDE_COLOR));
        btnHomeless.setTextColor(Color.parseColor(DEDEDE_COLOR));
        fab.setVisibility(View.INVISIBLE);
        appBarLayout.setExpanded(false, true);
    }

    @SuppressLint("RestrictedApi")
    private void homelessButtonSelectedBehavior() {
        btnHomeless.setTextColor(Color.WHITE);
        lineHomeless.setVisibility(View.VISIBLE);
        btnLost.setTextColor(Color.parseColor(DEDEDE_COLOR));
        lineLost.setVisibility(View.INVISIBLE);
        btnFound.setTextColor(Color.parseColor(DEDEDE_COLOR));
        lineFound.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        appBarLayout.setVisibility(View.VISIBLE);
        appBarLayout.setExpanded(true, true);
    }

    @SuppressLint("RestrictedApi")
    private void lostButtonSelectedBehavior() {
        btnLost.setTextColor(Color.WHITE);
        lineLost.setVisibility(View.VISIBLE);
        btnFound.setTextColor(Color.parseColor(DEDEDE_COLOR));
        lineFound.setVisibility(View.INVISIBLE);
        btnHomeless.setTextColor(Color.parseColor(DEDEDE_COLOR));
        lineHomeless.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        appBarLayout.setVisibility(View.VISIBLE);
        appBarLayout.setExpanded(true, true);
    }

    @SuppressLint("RestrictedApi")
    private void foundButtonSelectedBehavior() {
        btnFound.setTextColor(Color.WHITE);
        lineFound.setVisibility(View.VISIBLE);
        btnLost.setTextColor(Color.parseColor(DEDEDE_COLOR));
        lineLost.setVisibility(View.INVISIBLE);
        btnHomeless.setTextColor(Color.parseColor(DEDEDE_COLOR));
        lineHomeless.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        appBarLayout.setVisibility(View.VISIBLE);
        appBarLayout.setExpanded(true, true);
    }

    @Override
    public List<CityModel> getCityModelList() {
        return cityModelList;
    }

    @Override
    public String getChosenCityID(String cityName) {
        for (CityModel cityModel : cityModelList) {
            if (cityModel.getRu().equals(cityName) || cityModel.getHe().equals(cityName) || cityModel.getEn().equals(cityName)) {
                return cityModel.getId();
            }
        }
        return null;
    }

    @Override
    public String getCityById(String id) {
        for (CityModel cityModel : cityModelList) {
            if (cityModel.getId().equals(id)) {
                String lang = Locale.getDefault().getLanguage();
                switch (lang) {
                    case "ru":
                        return cityModel.getRu();
                    case "iw":
                        return cityModel.getHe();
                    default:
                        return cityModel.getEn();
                }
            }
        }
        return "Unknown";
    }

    @Override
    public void onItemClicked(PetModel petModel) {
        onButtonShowPopupWindowClick(petModel);
    }

    public void onButtonShowPopupWindowClick(PetModel petModel) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
        if (inflater == null) {
            return;
        }
        View popupView = inflater.inflate(R.layout.popup_window, rootView, false);
        ImageView petImage = popupView.findViewById(R.id.petImage);

        loadImage(petModel, petImage);

        TextView petCityPopUp = popupView.findViewById(R.id.popupCity);
        petCityPopUp.setText(getCityById(petModel.getCity()));
        TextView addressPopup = popupView.findViewById(R.id.addressPopup);
        TextView descriptionPopup = popupView.findViewById(R.id.descriptionPopup);
        TextView contactsPopup = popupView.findViewById(R.id.contactsPopup);
        addressPopup.setText(String.format("%s %s", getString(R.string.addressConst), petModel.getAddress()));
        descriptionPopup.setText(String.format("%s %s", getString(R.string.descriptionConst), petModel.getDescription()));
        contactsPopup.setText(String.format("%s %s", getString(R.string.contactsConst), petModel.getContacts()));

        // create the popup window
        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void loadImage(PetModel petModel, final ImageView petImage) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference photos = storage.getReference().child("photos");
        final StorageReference photoRef = photos.child(petModel.getImgId());
        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MainActivity.this)
                        .load(uri.toString())
                        .placeholder(R.drawable.photo_not_found)
                        .error(R.mipmap.ic_launcher)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(petImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                petImage.setImageResource(R.drawable.photo_not_found);
            }
        });
    }
}
