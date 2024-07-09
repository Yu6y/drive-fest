package com.example.drivefest;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.drivefest.adapter.ClickListener;
import com.example.drivefest.adapter.EventListAdapter;
import com.example.drivefest.adapter.FollowClickListener;
import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.viewmodel.HomeViewModel;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    private EventListAdapter eventListAdapter;
    private RecyclerView favList;
    private HomeViewModel homeVM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        homeVM = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        favList = view.findViewById(R.id.fav_event_list);
        favList.setHasFixedSize(true);
        favList.setLayoutManager(new GridLayoutManager(getContext(), 1));

        eventListAdapter = new EventListAdapter(getContext(), new ArrayList<>(), new ClickListener() {
            @Override
            public void onClick(String id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("event_id", (Parcelable) homeVM.getEventById(id));
                EventDescFragment fragment = new EventDescFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                Fragment currFragment = fragmentManager.findFragmentByTag("favoriteFragment");
                getParentFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, fragment, "descFragment")
                        .hide(currFragment)
                        .addToBackStack("favDesc")
                        .commit();
            }
        }, new FollowClickListener() {
            @Override
            public void onBtnClick(EventShort event, Button button, TextView text) {
                if(button.getText().equals("Obserwujesz")){
                    homeVM.unFollowFavEvent(event);
                    /*button.setText("Obserwuj");
                    Drawable newIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite);
                    button.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
                    button.setBackgroundResource(R.drawable.custom_button);
                    button.setTextColor(ContextCompat.getColor(getContext(), R.color.black));*/
                    /*int count = event.getFollowersCount() - 1;
                    text.setText(String.valueOf(count));*/
                    eventListAdapter.notifyDataSetChanged();
                }

            }
        });
        favList.setAdapter(eventListAdapter);

        homeVM.getFavEventShortLiveData().observe(getViewLifecycleOwner(), favEventShorts -> {
            //homeVM.setFollowedEvents();
            eventListAdapter.updateData(favEventShorts);
            Log.e("fav coutn", String.valueOf(favEventShorts.size()));
            eventListAdapter.notifyDataSetChanged();
        });


        return view;
    }
}