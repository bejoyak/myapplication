package ai.tech5.tech5.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ai.tech5.tech5.R;

public class GifProgressDialog {

    private Context activity;
    private Dialog dialog;

    //..we need the context else we can not create the dialog so get context in constructor
    public GifProgressDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(int drawableId,String title) {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
//        dialog.setContentView(R.layout.dialogue);
        dialog.setContentView(R.layout.loaddialogue);

        //...initialize the imageView form infalted layout 
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        TextView titile=dialog.findViewById(R.id.dialoguetitle_text);
        if (title!=""){
            titile.setText(title);
        }else {
            titile.setVisibility(View.GONE);
        }

        dialog.setTitle(titile.toString());

        Glide.with(activity).asGif().load(drawableId).into(gifImageView);
        //...finaly show it
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done 
    public void hideDialog() {

        if(dialog!=null&&dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}