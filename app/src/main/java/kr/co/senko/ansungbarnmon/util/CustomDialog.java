package kr.co.senko.ansungbarnmon.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import kr.co.senko.ansungbarnmon.R;

public class CustomDialog extends Dialog {
    public interface DialogResponseListener {
        void getResponse(boolean response, String number);
    }

    private DialogResponseListener dialogResponseListener = null;

    public CustomDialog(@NonNull Context context, DialogResponseListener dialogResponseListener) {
        super(context);
        this.dialogResponseListener = dialogResponseListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_phone);
        Button btnCancel = findViewById(R.id.btnPhoneInputCancel);
        btnCancel.setTag("cancel");
        btnCancel.setOnClickListener(onClickListener);

        Button btnConfirm = findViewById(R.id.btnPhoneInputConfirm);
        btnConfirm.setTag("confirm");
        btnConfirm.setOnClickListener(onClickListener);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        layoutParams.dimAmount = 0.8f;
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        layoutParams.height = displayMetrics.heightPixels;
        layoutParams.height = (int) (displayMetrics.heightPixels * 0.3);
        layoutParams.width = (int) (displayMetrics.widthPixels * 0.9);
        getWindow().setAttributes(layoutParams);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);

    }

    private final View.OnClickListener onClickListener = view -> {
        switch (view.getTag().toString()) {
            case "cancel":
                dismiss();
                dialogResponseListener.getResponse(false, "");
                break;
            case "confirm":
                String number = ((EditText)findViewById(R.id.eTxtPhoneNumber)).getText().toString();
                number = "010"+number;
                dismiss();
                dialogResponseListener.getResponse(true, number);
                break;
            default:
                break;
        }
    };
}
