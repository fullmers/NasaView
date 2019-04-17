package com.amiculous.nasaview.ui.favorites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.FavoritesViewHolder> {


    private final LayoutInflater inflater;
    private List<ApodEntity> favorites;
    private Context context;

    FavoritesListAdapter(Context context) {inflater = LayoutInflater.from(context);
    this.context = context;}

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = inflater.inflate(R.layout.favorites_item,parent,false);
        return new FavoritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {
        if (favorites != null ) {
            ApodEntity current = favorites.get(position);
            holder.titleTextView.setText(current.getTitle());
            holder.descPreviewTextView.setText(current.getExplanation());

            Picasso.get()
                    .load(current.getUrl())
                    .placeholder(context.getResources().getDrawable(R.drawable.default_apod))
                    .into(holder.imagePreviewImageView);
        } else {
            holder.titleTextView.setText("No favorites saved yet");
        }
    }

    void setFavorites(List<ApodEntity> favorites) {
        this.favorites = favorites;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (favorites != null) {
            return favorites.size();
        } else return 0;
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imagePreviewImageView;
        private final TextView titleTextView;
        private final TextView descPreviewTextView;

        private FavoritesViewHolder(View itemView) {
            super(itemView);
            imagePreviewImageView = itemView.findViewById(R.id.preview_image);
            titleTextView = itemView.findViewById(R.id.title_text);
            descPreviewTextView = itemView.findViewById(R.id.desc_text);

        }
    }

}
