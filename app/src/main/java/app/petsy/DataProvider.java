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
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class DataProvider {

    public static final String LOST_PET ="lostPets";
    public static final String FOUND_PET ="foundPets";


    public static void addPet(PetModel petModel, final File file, String collectionName) {
        FirebaseFirestore.getInstance().collection(collectionName)
                .add(petModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("zzz", "DocumentSnapshot written with ID: " + documentReference.getId());
                        if (file != null) {
                            uploadImage(documentReference.getId(), file);
                        }
                    }
                });
    }

    private static void uploadImage(String id, File file) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("photos");

        Uri uri = Uri.fromFile(file);
        StorageReference fileRef = storageRef.child(id);
        UploadTask uploadTask = fileRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("qqq", "Fail to upload image");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("qqq", "uploaded image");
            }
        });
    }


}
