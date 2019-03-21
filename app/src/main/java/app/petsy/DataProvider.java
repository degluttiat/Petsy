package app.petsy;

import com.google.firebase.firestore.FirebaseFirestore;

public class DataProvider {

    public static void addLost(PetModel petModel) {
        FirebaseFirestore.getInstance().collection("lostPets").add(petModel);
    }

    public static void addFound(PetModel petModel) {
        FirebaseFirestore.getInstance().collection("foundPets").add(petModel);
    }

}
