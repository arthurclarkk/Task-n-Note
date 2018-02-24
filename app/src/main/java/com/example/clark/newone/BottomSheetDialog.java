package com.example.clark.newone;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

/**
 * Created by clark on 23.02.2018.
 */

public class BottomSheetDialog extends BottomSheetDialogFragment {

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.bottomsheet, null);
        dialog.setContentView(contentView);
    }

}
