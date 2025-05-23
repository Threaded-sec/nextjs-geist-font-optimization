package com.example.moviewatcher.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviewatcher.R;
import com.example.moviewatcher.models.Episode;

import java.util.ArrayList;
import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

    private List<Episode> episodes;
    private final OnEpisodeClickListener listener;

    public interface OnEpisodeClickListener {
        void onEpisodeClick(Episode episode);
    }

    public EpisodeAdapter(OnEpisodeClickListener listener) {
        this.listener = listener;
        this.episodes = new ArrayList<>();
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_episode, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        Episode episode = episodes.get(position);
        holder.bind(episode);
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    class EpisodeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView episodeThumbnail;
        private final TextView episodeTitle;
        private final TextView episodeNumber;
        private final TextView episodeDuration;
        private final TextView creditsCost;
        private final View watchedIndicator;

        EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            episodeThumbnail = itemView.findViewById(R.id.episodeThumbnail);
            episodeTitle = itemView.findViewById(R.id.episodeTitle);
            episodeNumber = itemView.findViewById(R.id.episodeNumber);
            episodeDuration = itemView.findViewById(R.id.episodeDuration);
            creditsCost = itemView.findViewById(R.id.creditsCost);
            watchedIndicator = itemView.findViewById(R.id.watchedIndicator);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEpisodeClick(episodes.get(position));
                }
            });
        }

        void bind(Episode episode) {
            episodeTitle.setText(episode.getTitle());
            episodeNumber.setText(itemView.getContext()
                    .getString(R.string.episode, episode.getEpisodeNumber()));
            episodeDuration.setText(itemView.getContext()
                    .getString(R.string.duration, episode.getDuration()));
            creditsCost.setText(itemView.getContext()
                    .getString(R.string.credits_cost, episode.getCreditCost()));
            
            watchedIndicator.setVisibility(episode.isWatched() ? View.VISIBLE : View.GONE);

            // Load thumbnail using Glide
            Glide.with(itemView.getContext())
                    .load(episode.getThumbnailUrl())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_episode)
                    .error(R.drawable.error_episode)
                    .into(episodeThumbnail);
        }
    }
}
