package tech.kapardi.c_19beds.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

import tech.kapardi.c_19beds.R;

public class LoadingDialog {
    Activity activity;
    Dialog dialog;
    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(String msg){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_dialog_layout);

        TextView text = (TextView) dialog.findViewById(R.id.loading_text);
        if(msg.equals("")){
            text.setVisibility(View.GONE);
        }
        else {
            text.setText(msg);
        }
        SpinKitView loadingDialog;
        loadingDialog =  dialog.findViewById(R.id.spin_kit);

        /*Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        dialog.show();

    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
