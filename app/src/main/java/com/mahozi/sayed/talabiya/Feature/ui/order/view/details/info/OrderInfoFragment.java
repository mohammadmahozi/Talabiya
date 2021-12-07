package com.mahozi.sayed.talabiya.Feature.ui.order.view.details.info;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mahozi.sayed.talabiya.Feature.ui.order.OrderViewModel;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.create.DatePickerPopUp;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.create.TimePickerPopUp;
import com.mahozi.sayed.talabiya.Feature.ui.person.PersonActivity;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.order.OrderEntity;
import com.mahozi.sayed.talabiya.Storage.order.SubOrderEntity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderInfoFragment extends Fragment {


    private EditText dateEditText;
    private EditText timeEditText;

    private EditText totalEditText;
    private EditText payerEditText;
    private EditText statusEditText;

    private CustomEditText noteEditText;

    private CheckBox statusCheckBox;

    private ImageButton receiptImageButton;

    private OrderViewModel orderViewModel;

    private String currentPath;

    OrderEntity currentOrder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_order_info, container, false);

        orderViewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);

        currentOrder = orderViewModel.getCurrentOrder();



        dateEditText = view.findViewById(R.id.fragment_order_details_date);
        dateEditText.setText(currentOrder.date);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerPopUp datePick = new DatePickerPopUp(new DatePickerPopUp.OnDateReceived() {
                    @Override
                    public void getDate(String date) {
                        dateEditText.setText(date);

                        currentOrder.date = date;
                        orderViewModel.updateOrder(currentOrder);
                    }
                });

                datePick.show(getActivity().getSupportFragmentManager(), "DatePicker");

            }
        });



        timeEditText = view.findViewById(R.id.fragment_order_details_time);
        timeEditText.setText(currentOrder.time);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerPopUp timePick = new TimePickerPopUp(new TimePickerPopUp.OnTimeReceived() {
                    @Override
                    public void getTime(String time) {
                        timeEditText.setText(time);
                        currentOrder.time = time;
                        orderViewModel.updateOrder(currentOrder);
                    }
                });

                timePick.show(getActivity().getSupportFragmentManager(), "TimePicker");

            }
        });




        totalEditText = view.findViewById(R.id.fragment_order_details_total);
        totalEditText.setText(String.valueOf(currentOrder.total));



        payerEditText = view.findViewById(R.id.fragment_order_details_payer);
        payerEditText.setText(currentOrder.payer);
        payerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPersonFragment();

            }
        });



        statusEditText = view.findViewById(R.id.fragment_order_details_status);
        statusEditText.setText(currentOrder.clearance_date == null? getResources().getString(R.string.not_payed): currentOrder.clearance_date);

        statusCheckBox = view.findViewById(R.id.fragment_order_details_status_check_box);
        statusCheckBox.setChecked(currentOrder.status);

        statusCheckBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    onStatusCheckBoxClick();

                    return true;
                }
                return false;
            }
        });




        noteEditText = view.findViewById(R.id.fragment_order_details_note);
        noteEditText.setText(currentOrder.note);
        noteEditText.setCustomEditTextInterface(new CustomEditText.CustomEditTextInterface() {
            @Override
            public void onBackButtonPressed() {
                currentOrder.note = noteEditText.getText().toString().trim();
                orderViewModel.updateOrder(currentOrder);
                Toast.makeText(getContext(), R.string.note_updated, Toast.LENGTH_LONG).show();
                noteEditText.clearFocus();
            }
        });

        noteEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE){

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    currentOrder.note = noteEditText.getText().toString().trim();
                    orderViewModel.updateOrder(currentOrder);
                    Toast.makeText(getContext(), R.string.note_updated, Toast.LENGTH_LONG).show();
                    noteEditText.clearFocus();


                    return true;
                }
                return false;
            }
        });



        receiptImageButton = view.findViewById(R.id.receipt_image_button);
        if (currentOrder.receiptPath != null){

            int height = getResources().getDimensionPixelSize(R.dimen.thumbnail_height);
            int width = getResources().getDimensionPixelSize(R.dimen.thumbnail_width);

            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(currentOrder.receiptPath), width, height);

            receiptImageButton.setImageBitmap(thumbImage);

        }
        receiptImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (currentOrder.receiptPath == null){

                    currentPath = dispatchTakePictureIntent();
                }

                else {

                    showImageInGallery();
                }


            }
        });

        receiptImageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onReceiptImageButtonLongClick();


                return false;
            }
        });




        return view;

    }



    private void startPersonFragment(){

        Intent intent = new Intent(getActivity(), PersonActivity.class);
        intent.putExtra("getPerson", true);
        startActivityForResult(intent, 1);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                String personName = data.getStringExtra("personName");

                currentOrder.payer = personName;
                orderViewModel.updateOrder(currentOrder);

                payerEditText.setText(personName);

            }

            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == REQUEST_TAKE_PHOTO ) {
            if (resultCode == Activity.RESULT_OK){


                if (currentOrder.receiptPath != null)
                    new File(currentOrder.receiptPath).delete();

                currentOrder.receiptPath = currentPath;
                //update the path in the database if the picture is taken
                orderViewModel.updateOrder(currentOrder);

                int height = getResources().getDimensionPixelSize(R.dimen.thumbnail_height);
                int width = getResources().getDimensionPixelSize(R.dimen.thumbnail_width);

                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(currentPath), width, height);


                receiptImageButton.setImageBitmap(thumbImage);
            }

            if (resultCode == Activity.RESULT_CANCELED) {

                if (currentOrder.receiptPath == null){

                    new File(currentPath).delete();

                }


            }


        }
    }

    private void onStatusCheckBoxClick(){
        //if it was checked and got unchecked. the condition happens after the click and the chnage
        if (statusCheckBox.isChecked()){


            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.confirm).setMessage(R.string.status_to_not_payed)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            currentOrder.status = false;
                            currentOrder.clearance_date = getResources().getString(R.string.not_payed);
                            orderViewModel.updateOrder(currentOrder);


                            statusEditText.setText(getResources().getString(R.string.not_payed));
                            statusCheckBox.setChecked(false);



                        }})
                    .setNegativeButton(android.R.string.no, null).show();




        }

        else {

            DatePickerPopUp datePick = new DatePickerPopUp(new DatePickerPopUp.OnDateReceived() {
                @Override
                public void getDate(String date) {

                    currentOrder.status = true;
                    currentOrder.clearance_date = date;
                    orderViewModel.updateOrder(currentOrder);

                    statusCheckBox.setChecked(true);
                    statusEditText.setText(date);



                }
            });

            datePick.show(getActivity().getSupportFragmentManager(), "DatePicker");


        }
    }




    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */".jpg",         /* suffix */storageDir      /* directory */);

        // Save a file: path for use with ACTION_VIEW intents
        //currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 2;

    private String dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            try {
                photoFile = createImageFile();
            }

            catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(), "com.mahozi.sayed.talabiya.FileProvider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                return photoFile.getAbsolutePath();

            }

        }

        return "";
    }


    private void showImageInGallery(){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        File f = new File(currentOrder.receiptPath);
        Uri uri = FileProvider.getUriForFile(getContext(), "com.mahozi.sayed.talabiya.FileProvider", f);

        intent.setDataAndType(uri, "image/*");

        startActivity(intent);
    }



    private void onReceiptImageButtonLongClick(){

        if (currentOrder.receiptPath != null){

            showContextMenuForImage();

        }
    }

    private void showContextMenuForImage(){

        final CharSequence[] items = {getResources().getString(R.string.edit), getResources().getString(R.string.delete)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (item == 0){
                    currentPath = dispatchTakePictureIntent();
                }

                else if (item == 1){

                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.confirm).setMessage(R.string.delete_receipt_picture)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    receiptImageButton.setImageResource(R.mipmap.attach_icon);
                                    new File(currentOrder.receiptPath).delete();


                                    currentOrder.receiptPath = null;
                                    orderViewModel.updateOrder(currentOrder);




                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }

            }
        });
        builder.show();
    }


}
