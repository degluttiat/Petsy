package app.petsy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.petsy.model.PetModel;


public class ListFragment extends Fragment implements EventListener<QuerySnapshot> {
    private static final String ARG_TYPE = "ARG_TYPE";

    private int type;

    private static int TYPE_FOUND = 0;
    private static int TYPE_LOST = 1;
    private static int TYPE_HOMELESS = 2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private CollectionReference collectionRef;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private String collectionName;
    private FirebaseFirestore fb;
    private ListenerRegistration registration;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(int type) {
        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost, container, false);
        recyclerView = view.findViewById(R.id.ms_Pictures);

        fb = FirebaseFirestore.getInstance();

        setCollectionRef();
        setRecyclerView();
        getData(null);

        // Inflate the layout for this fragment
        return view;
    }

    private void setCollectionRef() {
        collectionName = "";
        if (type == TYPE_FOUND) {
            collectionName = "foundPets";
        } else if (type == TYPE_LOST) {
            collectionName = "lostPets";
        } else if (type == TYPE_HOMELESS) {
            collectionName = "homeless";
        }

        collectionRef = FirebaseFirestore.getInstance().collection(collectionName);
    }

    public void getData(String cityId) {
        if (registration != null) {
            registration.remove();
        }
        if (cityId != null) {
            myRecyclerViewAdapter.clearCollection();
            registration = collectionRef
                    .whereEqualTo("city", cityId)
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener(MetadataChanges.INCLUDE, this);
        } else {
            myRecyclerViewAdapter.clearCollection();
            registration = collectionRef
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener(MetadataChanges.INCLUDE, this);
        }

    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(mListener);
        recyclerView.setAdapter(myRecyclerViewAdapter);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        String getCityById(String id);

        void onItemClicked(PetModel petModel);
    }


    @Override
    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
        if (e != null) {
            return;
        }
        assert queryDocumentSnapshots != null;
        ArrayList<String> petModels = new ArrayList<>();
        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:
                    PetModel pm = dc.getDocument().toObject(PetModel.class);
                    /*String id = dc.getDocument().getId();
                    pm.setImgId(id);*/
                    myRecyclerViewAdapter.add(pm);

                    Date timestampDate = pm.getTimestamp();
                    if (timestampDate == null) {
                        break;
                    }
                    long currentTime = Calendar.getInstance().getTime().getTime() / 1000;
                    long timestampNewTime = timestampDate.getTime() / 1000 + 60 * 60 * 24 * 30;
                    if (currentTime >= timestampNewTime) {
                        petModels.add(dc.getDocument().getId());
                    }
                    break;
                case MODIFIED:
                    break;
                case REMOVED:
                    break;
            }
        }
        Log.d("zzz", "petModels " + petModels.size());
        WriteBatch batch = fb.batch();
        for (String dc : petModels) {
            Log.d("zzz", "petModels " + dc);
            Log.d("zzz", "petModels " + collectionName);

            DocumentReference laRef = fb.collection(collectionName).document(dc);
            batch.delete(laRef);

        }
        batch.commit();
    }
}

