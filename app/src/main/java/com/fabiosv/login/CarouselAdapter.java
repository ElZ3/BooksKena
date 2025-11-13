package com.fabiosv.login;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public enum Type { IMAGE, VIDEO }

    public static class CarouselItem {
        public final Type type;
        public final int imageRes;   // para imágenes
        public final Uri videoUri;   // para video

        private CarouselItem(Type t, int img, Uri vid) { type = t; imageRes = img; videoUri = vid; }
        public static CarouselItem image(int res) { return new CarouselItem(Type.IMAGE, res, null); }
        public static CarouselItem video(Uri uri) { return new CarouselItem(Type.VIDEO, 0, uri); }
    }

    private final List<CarouselItem> items;
    private final LayoutInflater inflater;

    public CarouselAdapter(Context ctx, List<CarouselItem> items) {
        this.items = items;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type == Type.VIDEO ? 1 : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = inflater.inflate(R.layout.item_carousel_video, parent, false);
            return new VideoVH(v);
        } else {
            View v = inflater.inflate(R.layout.item_carousel_image, parent, false);
            return new ImageVH(v);
        }
    }

    @Override public int getItemCount() { return items.size(); }

    static class ImageVH extends RecyclerView.ViewHolder {
        ImageView img;
        ImageVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.carouselImage);
        }
    }

    static class VideoVH extends RecyclerView.ViewHolder {
        VideoView video;
        VideoVH(@NonNull View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.carouselVideo);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        CarouselItem it = items.get(position);

        if (getItemViewType(position) == 1) {
            VideoVH vh = (VideoVH) h;
            vh.video.setVideoURI(it.videoUri);
            vh.video.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                mp.setVideoScalingMode(android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                vh.video.start();
            });
        } else {
            ImageVH ih = (ImageVH) h;
            ih.img.setImageResource(it.imageRes);
            ih.img.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // respeta proporción, sin cortes
            ih.img.setAdjustViewBounds(true);
            ih.img.setBackgroundColor(android.graphics.Color.WHITE);
        }
    }

}
