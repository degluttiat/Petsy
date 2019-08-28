package app.petsy;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import app.petsy.model.PetModel;

public class DataProvider {

    public static final String LOST_PET ="lostPets";
    public static final String FOUND_PET ="foundPets";
    public static final String HOMELESS = "homeless";


    public static void addPet(PetModel petModel, final Uri uri, String collectionName) {
        FirebaseFirestore.getInstance().collection(collectionName)
                .add(petModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("zzz", "DocumentSnapshot written with ID: " + documentReference.getId());
                        if (uri != null) {
                            uploadImage(documentReference.getId(), uri);
                        }
                    }
                });
    }


    public static void addPet(PetModel petModel, String collectionName) {
        FirebaseFirestore.getInstance().collection(collectionName)
                .add(petModel);
    }

    private static void uploadImage(String id, Uri uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("photos");

        StorageReference fileRef = storageRef.child(id);
        UploadTask uploadTask = fileRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("qqq", "Fail to upload image, " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("qqq", "uploaded image");
            }
        });
    }

    public static String uploadImage(Uri uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("photos");

        String id = String.valueOf(System.currentTimeMillis());
        StorageReference fileRef = storageRef.child(id);
        UploadTask uploadTask = fileRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("qqq", "Fail to upload image, " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("qqq", "uploaded image");
            }
        });
        return id;
    }
}
