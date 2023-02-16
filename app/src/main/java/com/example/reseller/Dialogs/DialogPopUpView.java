package com.example.reseller.Dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.example.reseller.R;


public class DialogPopUpView extends DialogFragment implements OnClickListener {
    public interface OnInputListener {
        void sendInput(int minPrice, int maxPrice, String category, String sorting, boolean pressed, String search);
    }

    public OnInputListener mInputListener;
    final String LOG_TAG = "myLogs";
    private Button confirm, clearAll;
    private EditText MaxPrice, MinPrice, SearchField;
    private String categoryText, sortingText;
    private int MinPriceText, MaxPriceText;
    private boolean pressed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.pop_view_sort, container);
        try {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
        final Spinner spinnersort = rootview.findViewById(R.id.spinner_sorting);

        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.Sorting, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnersort.setAdapter(adapter2);

        final Spinner spinner = rootview.findViewById(R.id.spinner_category);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        MaxPrice = rootview.findViewById(R.id.maxprice);
        MinPrice = rootview.findViewById(R.id.minprice);
        SearchField = rootview.findViewById(R.id.search_dialog);
        clearAll = rootview.findViewById(R.id.clearSorting);
        confirm = rootview.findViewById(R.id.confirmbtn);
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String min = MinPrice.getText().toString();
                if (!min.isEmpty()) {
                    MinPriceText = Integer.valueOf(MinPrice.getText().toString());
                }
                String max = MaxPrice.getText().toString();
                if (!max.isEmpty()) {
                    MaxPriceText = Integer.valueOf(MaxPrice.getText().toString());
                }
                categoryText = spinner.getSelectedItem().toString();
                pressed = true;
                sortingText = spinnersort.getSelectedItem().toString();
                mInputListener = (DialogPopUpView.OnInputListener) getTargetFragment();

                mInputListener.sendInput(MinPriceText, MaxPriceText, categoryText, sortingText, pressed, SearchField.getText().toString());
                pressed = false;
//                GlobalStorageFragment.filterDialog(MinPriceText, MaxPriceText, categoryText, sortingText, SearchField.getText().toString());
                dismiss();
            }
        });
        clearAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MinPrice.setText("");
                MaxPrice.setText("");
                SearchField.setText("");
                spinner.setAdapter(adapter);
                spinnersort.setAdapter(adapter2);
                mInputListener = (DialogPopUpView.OnInputListener) getTargetFragment();
                mInputListener.sendInput(0, 0, "Category", "Sorting", false, "");
                dismiss();
            }
        });
        return rootview;
    }


    public void onClick(View v) {


    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);


    }


}