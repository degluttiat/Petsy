package app.petsy;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private ArrayList<PetModel> petsData;

    public MyAdapter(ArrayList<PetModel> PetModels) {
        petsData = PetModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PetModel petModel = petsData.get(position);
        holder.mImageView.setImageResource(petModel.getImgResId());
        holder.locationTextView.setText(petModel.getLocation());
        holder.postDate.setText((petModel.getDate()));
    }

    @Override
    public int getItemCount() {
        return petsData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView locationTextView;
        private TextView phoneNumberTextView;
        private TextView postDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.pet_picture);
            locationTextView = itemView.findViewById(R.id.location);
            postDate = itemView.findViewById(R.id.textViewPostDate);

        }
    }
}






