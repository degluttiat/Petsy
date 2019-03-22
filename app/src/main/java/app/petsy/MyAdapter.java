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
    private ArrayList<PetModel> petsData = new ArrayList<>();

    public MyAdapter() {
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
        //holder.mImageView.setImageResource(petModel.getImgUrl());
        holder.cityTextView.setText(petModel.getCity());
        holder.postDate.setText((petModel.getAddress()));
    }

    @Override
    public int getItemCount() {
        return petsData.size();
    }

    public void add(PetModel pm) {
        petsData.add(pm);
        notifyItemInserted(petsData.size() - 1);
    }

    public void clearCollection(){
        petsData.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView cityTextView;
        private TextView contactsTextView;
        private TextView postDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.pet_picture);
            cityTextView = itemView.findViewById(R.id.city);
            postDate = itemView.findViewById(R.id.textViewPostDate);

        }
    }
}






