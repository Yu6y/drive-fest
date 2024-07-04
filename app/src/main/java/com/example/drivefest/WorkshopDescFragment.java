package com.example.drivefest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.drivefest.data.model.Workshop;
import com.example.drivefest.data.model.WorkshopDesc;
import com.example.drivefest.viewmodel.EventDescViewModel;
import com.example.drivefest.viewmodel.HomeViewModel;

public class WorkshopDescFragment extends Fragment {
    private EventDescViewModel eventVM;
    private TextView title, text, address, tags, rating;
    private ImageView image;
    private Workshop workshop;
    private HomeViewModel homeVM;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workshop_desc, container, false);

        homeVM = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        workshop = getArguments().getParcelable("workshop_id");
        title = view.findViewById(R.id.workshopDesc_title);
        text = view.findViewById(R.id.workshopDesc_text);
        address = view.findViewById(R.id.workshopDesc_address);
        tags = view.findViewById(R.id.workshopDesc_tags);
        rating = view.findViewById(R.id.workshopDesc_rating);
        image = view.findViewById(R.id.workshopDesc_image);

        homeVM.updateWorkshopDesc(workshop);

        homeVM.getWorkshopDesc().observe(getViewLifecycleOwner(), new Observer<WorkshopDesc>() {
            @Override
            public void onChanged(WorkshopDesc workshopDesc) {
                if (workshopDesc != null) {
                    title.setText(workshopDesc.getName());
                    text.setText(workshopDesc.getDescription());
                    address.setText(workshopDesc.getLocation() + ", " +
                            workshopDesc.getAddress());
                    String tag = "";
                    String[] tagsArray = workshopDesc.getTags();
                    for (int i = 0; i < tagsArray.length; i++) {
                        tag += '#' + tagsArray[i];
                        if (i != tagsArray.length - 1)
                            tag += ", ";
                    }
                    tags.setText(tag);
                    rating.setText("Ocena: " + workshopDesc.getRating());

                    Glide
                            .with(getContext())
                            .load(workshopDesc.getImage())
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .placeholder(R.drawable.ic_cloud_download)
                            .error(R.drawable.ic_error)
                            .into(image);
                }
            }
        });
        return view;
    }

       /* @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity != null && activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

                Toolbar toolbar = activity.findViewById(R.id.toolbar);
                if (toolbar != null) {
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getActivity().onBackPressed();
                        }
                    });
                }

                DrawerLayout drawerLayout = activity.findViewById(R.id.home_drawer);
                if (drawerLayout != null) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();

            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity != null && activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                activity.getSupportActionBar().setDisplayShowHomeEnabled(false);

                DrawerLayout drawerLayout = activity.findViewById(R.id.home_drawer);
                if (drawerLayout != null) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }
        }*/

    }