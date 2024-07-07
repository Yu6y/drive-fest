package com.example.drivefest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.drivefest.data.model.Workshop;
import com.example.drivefest.data.model.WorkshopDesc;
import com.example.drivefest.viewmodel.EventDescViewModel;
import com.example.drivefest.viewmodel.HomeViewModel;

public class WorkshopDescFragment extends Fragment {
    private EventDescViewModel eventVM;
    private TextView title, text, address, tags, rating, ratingCount;
    private ImageView image;
    private Workshop workshop;
    private HomeViewModel homeVM;
    private AppCompatRatingBar ratingBar;

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
        ratingCount = view.findViewById(R.id.workshopDesc_rating_count);
        image = view.findViewById(R.id.workshopDesc_image);
        ratingBar = view.findViewById(R.id.eventDesc_btn_rate);

        /*ratingBar.setRating(0);
        ratingBar.setEnabled(true);*/

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
                    ratingCount.setText("Ocen: " + workshopDesc.getRatingCount());


                    if(workshopDesc.isRated()){
                        Log.e("rating bar", "kurwa jego mac");
                        ratingBar.setRating(workshopDesc.getRate());
                        Log.e("no ja pierdole", String.valueOf(workshopDesc.getRate()));
                        ratingBar.setEnabled(false);
                    }
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

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getContext(), String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();

                ratingBar.setEnabled(false);
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
        Fragment fragment = fragmentManager.findFragmentByTag("workshopDesc");
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .hide(fragment)
                    .commit();
            fragmentManager.popBackStack();
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("on", "detach");
        homeVM.clearWorkshopDesc();
    }
}