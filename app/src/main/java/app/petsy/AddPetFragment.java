package app.petsy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddPetFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    static final int REQUEST_CODE_FROM_CAMERA = 2;
    private int REQUEST_CODE_FROM_GALLERY = 1;
    private String currentPhotoPath;
    private File photoFileForCamera;
    private ImageView imageView;
    private AutoCompleteTextView searchingView;

    public AddPetFragment() {
        // Required empty public constructor
    }

    public static AddPetFragment newInstance() {
        return new AddPetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.imgPreview);
        Button button = view.findViewById(R.id.btnaddPhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTakePhotoIntent();
            }
        });

        Button button2 = view.findViewById(R.id.btnUploadPhoto);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTakePhotoFromGalleryIntent();
            }
        });
        searchingView = view.findViewById(R.id.city);
        setAutoComplete();
    }

    private void runTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFileForCamera = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFileForCamera != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(), "app.petsy", photoFileForCamera);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_FROM_CAMERA);
            }
        }
    }

    private void runTakePhotoFromGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_FROM_GALLERY);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    void setAutoComplete() {
        final List<String> cities = new ArrayList<>();
        final List<CityModel> citiesList = mListener.getCitiesList();
        for (CityModel city : citiesList) {
            cities.add(city.getHe());
            cities.add(city.getRu());
            cities.add(city.getEn());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.select_dialog_item,
                cities);
        searchingView.setThreshold(1);//will start working from first character
        searchingView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        searchingView.setBackgroundColor(Color.WHITE);

        searchingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position);
                String chosenCityID = mListener.getChosenCityID(item);
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FROM_CAMERA && resultCode == RESULT_OK) {
/*            PetModel petModel = new PetModel();
            petModel.setCity("Petah Tikva");
            DataProvider.addPet(petModel, photoFileForCamera, DataProvider.FOUND_PET);*/
            onImageReceivedFromCamera();
        }

        if (requestCode == REQUEST_CODE_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            onImageReceivedFromGallery(data);
        }

    }

    private void onImageReceivedFromCamera() {

        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    private void onImageReceivedFromGallery(Intent data) {
        Uri uri = data.getData();

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        List<CityModel> getCitiesList();

        String getChosenCityID(String cityName);
    }
}
