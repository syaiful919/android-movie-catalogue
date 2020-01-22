package com.syaiful.moviecatalogue.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.syaiful.moviecatalogue.R;
import com.syaiful.moviecatalogue.model.Movie;

import java.util.ArrayList;

public class RvFavAdapter extends RecyclerView.Adapter<RvFavAdapter.ListViewHolder> {
    private ArrayList<Movie> listMovie = new ArrayList<>();

    public RvFavAdapter() {

    }

    public ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<Movie> listMovie) {
        if (listMovie.size() > 0) {
            this.listMovie.clear();
        }
        this.listMovie.addAll(listMovie);

        notifyDataSetChanged();
    }

    public void addItem(Movie movie) {
        this.listMovie.add(movie);
        notifyItemInserted(listMovie.size() - 1);
    }

    public void removeItem(int position) {
        this.listMovie.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listMovie.size());
    }

    @NonNull
    @Override
    public RvFavAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new RvFavAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RvFavAdapter.ListViewHolder holder, final int position) {
        Movie movie = listMovie.get(position);

        holder.txtName.setText(movie.getName());
        holder.txtDescription.setText(movie.getDescription());

        Glide.with(holder.itemView.getContext())
                .load(listMovie.get(position).getPoster())
                .apply(new RequestOptions().override(100, 150))
                .into(holder.imgPoster);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listMovie.get(position), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView txtName, txtDescription;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDescription = itemView.findViewById(R.id.txt_description);
            imgPoster = itemView.findViewById(R.id.img_poster);
        }

    }

    private RvFavAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(RvFavAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie movie, int position);
    }
}

