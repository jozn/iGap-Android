package com.iGap.adapter;

import java.io.File;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.iGap.R;
import com.iGap.adapter.RecycleAdapterExplorer.ViewHolder;
import com.iGap.interfaces.OnItemClickListener;


/**
 * 
 * adapter for use in activity explorer that show user file manger
 *
 */

public class RecycleAdapterExplorer extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<StructListItem> item;
    private ViewHolder                viewholder;
    private OnItemClickListener       onItemClickListener;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView  txtTitle;
        public ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.e_sub_textView1);
            imageView = (ImageView) itemView.findViewById(R.id.e_sub_imageView1);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = ViewHolder.super.getPosition();
                    onItemClickListener.onItemClick(view, position);
                }
            });
        }

    }


    public RecycleAdapterExplorer(ArrayList<StructListItem> items, OnItemClickListener onItemClickListener) {
        item = items;
        this.onItemClickListener = onItemClickListener;

    }


    @Override
    public int getItemCount() {
        return item.size();
    }


    public void onBindViewHolder(ViewHolder holder, int position) {

        StructListItem rowItem = item.get(position);
        holder.txtTitle.setText(rowItem.name);
        holder.imageView.setImageResource(rowItem.image);

        if (rowItem.image == R.drawable.j_pic) {
            holder.imageView.setTag(rowItem.path);
            new LoadImage(holder.imageView).execute();
        }
        else if (rowItem.image == R.drawable.j_video) {
            holder.imageView.setTag(rowItem.path);
            new getVideoThumbnail(holder.imageView).execute();
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_layout_explorer, parent, false);
        viewholder = new ViewHolder(v);
        return viewholder;
    }


    /**
     * 
     * return Thumbnail bitmap from file path image
     *
     */

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private ImageView imv;
        private String    path;


        public LoadImage(ImageView imageView) {
            imv = imageView;
            path = imageView.getTag().toString();
        }


        @Override
        protected Bitmap doInBackground(Object... params) {

            Bitmap bitmap = null;
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 8;
            File file = new File(path);

            if (file.exists())
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);

            return bitmap;
        }


        @Override
        protected void onPostExecute(Bitmap result) {

            if ( !imv.getTag().toString().equals(path)) {
                return;
            }
            if (result != null && imv != null) {
                imv.setImageBitmap(result);
            }
        }

    }


    /**
     * 
     * return Thumbnail bitmap from file path video
     *
     */

    class getVideoThumbnail extends AsyncTask<Object, Void, Bitmap> {

        private ImageView imv;
        private String    path;


        public getVideoThumbnail(ImageView imageView) {
            imv = imageView;
            path = imageView.getTag().toString();
        }


        @Override
        protected Bitmap doInBackground(Object... params) {

            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
            return bMap;
        }


        @Override
        protected void onPostExecute(Bitmap result) {

            if ( !imv.getTag().toString().equals(path)) {
                return;
            }
            if (result != null && imv != null) {
                imv.setImageBitmap(result);
            }
        }
    }

}