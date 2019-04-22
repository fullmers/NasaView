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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.FavoritesViewHolder> {

    private final LayoutInflater inflater;
    private List<ApodEntity> favorites;
    private final Context context;
    private FavoriteSelectionListener favoriteSelectionListener;

    public interface FavoriteSelectionListener {
        void onFavoriteSelected(ApodEntity apod, ImageView image);
    }

    public FavoritesListAdapter(Context context, FavoriteSelectionListener favoriteSelectionListener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.favoriteSelectionListener = favoriteSelectionListener;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = inflater.inflate(R.layout.favorites_item,parent,false);
        return new FavoritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        if (favorites != null ) {
            ApodEntity current = favorites.get(position);
            holder.titleTextView.setText(current.getTitle());
            holder.descPreviewTextView.setText(current.getExplanation());

            Picasso.get()
                    .load(current.getUrl())
                    .placeholder(context.getResources().getDrawable(R.drawable.default_apod))
                    .into(holder.imagePreviewImageView);
        } else {
            holder.titleTextView.setText(context.getString(R.string.no_favorites));
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

    public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.preview_image) ImageView imagePreviewImageView;
        @BindView(R.id.title_text) TextView titleTextView;
        @BindView(R.id.desc_text) TextView descPreviewTextView;


        private FavoritesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            favoriteSelectionListener.onFavoriteSelected(favorites.get(getAdapterPosition()), imagePreviewImageView);
        }
    }

}
