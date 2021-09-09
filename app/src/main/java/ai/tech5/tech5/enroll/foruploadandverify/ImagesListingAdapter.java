package ai.tech5.tech5.enroll.foruploadandverify;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.util.List;

import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.utils.GlideApp;
import ai.tech5.tech5.enroll.utils.GlideRequests;


/**
 * @author naidu
 */
public class ImagesListingAdapter extends RecyclerView.Adapter<ImagesListingAdapter.ViewHolder> {

    private GlideRequests request;




    private List<String> images;
    private Context context;

    public ImagesListingAdapter(List<String> movies) {
        this.images = movies;
    }

    public String getItem(int position) {
        return images.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View rootView = LayoutInflater.from(context).inflate(R.layout.gallery_image_row, parent, false);
        request = GlideApp.with(context);


        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        File file = new File(images.get(position));

        if (file != null && file.exists()) {

//            request
//                    .load(Uri.fromFile(new File(images.get(position))))
//                    .placeholder(R.drawable.app_icon)
//                    .into(holder.thumbnail);

            holder.thumbnail.setImageURI(Uri.fromFile(new File(images.get(position))));

            String fileName = file.getName();
            if(fileName.contains(".bmp")){
                fileName=fileName.replace(".bmp", "");
            }else if(fileName.contains(".png")){
                fileName=fileName.replace(".png", "");

            }

            holder.txtImageName.setText(fileName);

        } else {
            holder.thumbnail.setImageBitmap(null);
            holder.txtImageName.setText("");
        }


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView txtImageName;


        public ViewHolder(View root) {
            super(root);
            thumbnail = root.findViewById(R.id.thumbnail);
            txtImageName = root.findViewById(R.id.name);

        }
    }
}
