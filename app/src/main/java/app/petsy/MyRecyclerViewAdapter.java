package app.petsy;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private ArrayList<PetModel> petsData = new ArrayList<>();
    private final ListFragment.OnFragmentInteractionListener mListener;

    public MyRecyclerViewAdapter(ListFragment.OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        PetModel petModel = petsData.get(position);
        holder.petModel = petModel;
        //holder.mImageView.setImageResource(petModel.getImgId());
        holder.cityTextView.setText(petModel.getCity());
        holder.postDate.setText((petModel.getAddress()));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        Log.d("ZAQ", "ID 2:" + petModel.getImgId());
        final StorageReference storageRef = storage.getReference().child("photos").child(petModel.getImgId());
        Log.d("ZAQ", "storageRef:" + storageRef.getPath());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.mImageView.getContext())
                        .load(uri.toString())
                        .error(R.mipmap.ic_launcher)
                        .into(holder.mImageView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petsData.size();
    }

    public void add(PetModel pm) {
        petsData.add(pm);
        notifyItemInserted(petsData.size() - 1);
    }

    public void clearCollection() {
        petsData.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView cityTextView;
        private TextView contactsTextView;
        private TextView postDate;
        private PetModel petModel;
        private ListFragment.OnFragmentInteractionListener mListener;

        public ViewHolder(@NonNull View itemView, ListFragment.OnFragmentInteractionListener mListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = itemView.findViewById(R.id.pet_picture);
            cityTextView = itemView.findViewById(R.id.city);
            postDate = itemView.findViewById(R.id.textViewPostDate);
            this.mListener = mListener;
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                mListener.onItemClicked(petModel);
            }

        }
    }
}






