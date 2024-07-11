package com.example.drivefest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.drivefest.adapter.ExpandableListAdapter;
import com.example.drivefest.data.ExpandableListDataItems;
import com.example.drivefest.viewmodel.HomeViewModel;

import java.util.HashMap;
import java.util.List;

public class AddWorkshopFragment extends Fragment {
    private HomeViewModel homeVM;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private EditText name, date, location, address, desc;
    private TextView pic;
    private Button picBtn, acceptBtn;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_add_workshop, container, false);

        expandableListView = view.findViewById(R.id.add_workshop_expandableListView);
        expandableListAdapter = new ExpandableListAdapter(getContext(), ExpandableListDataItems.getWorkshopData());
        expandableListView.setAdapter(expandableListAdapter);

        name = view.findViewById(R.id.add_workshop_name);
        location = view.findViewById(R.id.add_workshop_location);
        address = view.findViewById(R.id.add_workshop_address);
        desc = view.findViewById(R.id.add_workshop_desc);
        pic = view.findViewById(R.id.add_workshop_pic);
        picBtn = view.findViewById(R.id.add_workshop_pic_btn);
        acceptBtn = view.findViewById(R.id.add_workshop_accept);

        homeVM = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        picBtn.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= 22)
                checkPermission();
            else
                openGallery();
            if(imageUri != null) {
                pic.setTextColor(getResources().getColor(R.color.black));
                pic.setText(String.valueOf(imageUri));
            }
        });

        acceptBtn.setOnClickListener(v -> {
            HashMap<String, List<String>> checked = expandableListAdapter.getCheckedBoxes();
            Log.e("tags checL", String.valueOf(checked.get("Tagi").size()));
            if(name.getText().toString().trim().isEmpty())
                name.setError("Uzupełnij pole!");
            else if(location.getText().toString().trim().isEmpty())
                location.setError("Uzupełnij pole!");
            else if(address.getText().toString().trim().isEmpty())
                address.setError("Uzupełnij pole!");
            else if(pic.getText().toString().isEmpty()) {
                pic.setText("Wybierz zdjęcie!");
                pic.setTextColor(getResources().getColor(R.color.red));
            }
            else if(desc.getText().toString().trim().isEmpty())
                desc.setError("Uzupełnij pole!");
            else if(checked.get("Województwo").isEmpty() || checked.get("Województwo").size() > 1)
                Toast.makeText(getContext(), "Zaznacz jedno województwo!", Toast.LENGTH_SHORT).show();
            else if(checked.get("Tagi").isEmpty())
                Toast.makeText(getContext(), "Zaznacz przynajmniej jeden Tag", Toast.LENGTH_SHORT).show();
            else {
                Bitmap resizedBitmap = homeVM.resizeImage(imageUri,  getActivity());

                Uri resizedImageUri = homeVM.getImageUri(getContext(), resizedBitmap);

                homeVM.pushWorkshopPhoto(resizedImageUri.toString());
                homeVM.getWorkshopPhotoName().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if(s.isEmpty()) {
                            Toast.makeText(getContext(), "Couldn't have added picture!", Toast.LENGTH_SHORT).show();
                            homeVM.getWorkshopPhotoName().removeObserver(this);
                        }
                        homeVM.pushWorkshopShort(name.getText().toString(), location.getText().toString(), checked);
                        homeVM.getWorkshopId().observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if(s.equals("fail")){
                                    Toast.makeText(getContext(), "Couldn't have added workshop!", Toast.LENGTH_SHORT).show();
                                    homeVM.getWorkshopId().removeObserver(this);
                                }

                                homeVM.pushWorkshopDesc(name.getText().toString(), location.getText().toString(),
                                        address.getText().toString(), desc.getText().toString(), checked);
                                homeVM.getAddWorkshopResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
                                    @Override
                                    public void onChanged(String s) {
                                        if(s.equals("fail")){
                                            Toast.makeText(getContext(), "Couldn't have added description", Toast.LENGTH_SHORT).show();
                                            homeVM.getAddWorkshopResponse().removeObserver(this);
                                        }
                                        else{
                                            Toast.makeText(getContext(), "Workshop added successfully!", Toast.LENGTH_SHORT).show();
                                            close();
                                        }

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            ((HomeActivity) activity).setupDrawerToggle(true);

            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setNavigationOnClickListener(v -> close());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ((HomeActivity) activity).setupDrawerToggle(false);
        }
    }

    public void close() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("addWorkshopFragment");
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commit();
            fragmentManager.popBackStack();
        }
    }
    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED){
            if(shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES)){
                Toast.makeText(getContext(), "Accept required permission", Toast.LENGTH_SHORT).show();
            }else{
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
            }
        }
        else
            openGallery();
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1    ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == AppCompatActivity.RESULT_OK && requestCode == 1 && data != null){
            imageUri = data.getData();
            pic.setText(imageUri.toString());
        }
    }
}