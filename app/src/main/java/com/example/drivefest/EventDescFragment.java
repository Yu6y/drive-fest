    package com.example.drivefest;

    import android.graphics.drawable.Drawable;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.activity.OnBackPressedCallback;
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;
    import androidx.core.content.ContextCompat;
    import androidx.core.view.GravityCompat;
    import androidx.drawerlayout.widget.DrawerLayout;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentManager;
    import androidx.lifecycle.Observer;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.bumptech.glide.load.engine.DiskCacheStrategy;
    import com.example.drivefest.adapter.CommentListAdapter;
    import com.example.drivefest.data.model.Comment;
    import com.example.drivefest.data.model.Event;
    import com.example.drivefest.data.model.EventShort;
    import com.example.drivefest.viewmodel.EventDescViewModel;
    import com.example.drivefest.viewmodel.HomeViewModel;

    import java.util.ArrayList;
    import java.util.List;

    public class EventDescFragment extends Fragment {

        private EventDescViewModel eventVM;
        private HomeViewModel homeVM;
        private TextView title, text, city, date, tags, followers;
        private ImageView image, userPhoto;
        private Button btnFollow, btnPostComment;
        private EditText comment;
        private RecyclerView recyclerView;
        private CommentListAdapter commentListAdapter;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_event_desc, container, false);

            eventVM = new ViewModelProvider(this).get(EventDescViewModel.class);
            homeVM = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

            title = view.findViewById(R.id.eventDesc_title);
            text = view.findViewById(R.id.eventDesc_text);
            city = view.findViewById(R.id.eventDesc_city);
            date = view.findViewById(R.id.eventDesc_date);
            tags = view.findViewById(R.id.eventDesc_tags);
            followers = view.findViewById(R.id.eventDesc_followers);
            image = view.findViewById(R.id.eventDesc_image);
            btnFollow = view.findViewById(R.id.eventDesc_btn_follow);
            btnPostComment = view.findViewById(R.id.commentBtn);
            comment = view.findViewById(R.id.commentEditText);
            userPhoto = view.findViewById(R.id.eventDesc_usr_pic);
            recyclerView = view.findViewById(R.id.comment_list);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

            eventVM.updateEventDesc((EventShort) getArguments().getParcelable("event_id"));
            eventVM.fetchCommentsList((EventShort) (getArguments().getParcelable("event_id")));

            commentListAdapter = new CommentListAdapter(getContext(), new ArrayList<>());
            recyclerView.setAdapter(commentListAdapter);
            eventVM.getCommentsList().observe(getViewLifecycleOwner(), new Observer<List<Comment>>() {
                @Override
                public void onChanged(List<Comment> comments) {
                    commentListAdapter.updateData(comments);
                }
            });
            eventVM.getEvent().observe(getViewLifecycleOwner(), new Observer<Event>() {
                @Override
                public void onChanged(Event event) {
                    if (event != null) {
                        title.setText(event.getName());
                        text.setText(event.getDescription());
                        city.setText(event.getLocation() + ", " + event.getAddress());
                        date.setText(event.getDate().toString());
                        Log.e("evenrtdsc", String.valueOf(event.getIsFollowed()));
                        if(event.getIsFollowed()){
                            setBtnFollow();
                        }
                        String tag = "";
                        String[] tagsArray = event.getTags();
                        for(int i = 0; i < tagsArray.length; i++){
                            tag += '#' + tagsArray[i];
                            if(i != tagsArray.length - 1)
                                tag += ", ";
                        }
                        tags.setText(tag);
                        followers.setText("Obserwujących: " + event.getFollowersCount());

                        Glide
                                .with(getContext())
                                .load(event.getImage())
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .placeholder(R.drawable.ic_cloud_download)
                                .error(R.drawable.ic_error)
                                .into(image);
                        Glide
                                .with(getContext())
                                .load(homeVM.getUserPic())
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .placeholder(R.drawable.ic_cloud_download)
                                .error(R.drawable.ic_error)
                                .into(userPhoto);
                    }
                }
            });

            btnFollow.setOnClickListener( v -> {
                    if (btnFollow.getText().equals("Obserwuj")) {
                        setBtnFollow();
                    } else if (btnFollow.getText().equals("Obserwujesz")) {
                        setBtnUnfollow();
                    }

            });

            btnPostComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!comment.getText().equals("")){
                        btnPostComment.setVisibility(View.INVISIBLE);
                        Comment commentPosted = new Comment(String.valueOf(comment.getText()), homeVM.getUserPic(), homeVM.getUserId(), homeVM.getUserDisplayName());
                        eventVM.postComment(commentPosted);
                        btnPostComment.setVisibility(View.VISIBLE);
                        eventVM.getCommentResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                                commentListAdapter.updateList(commentPosted);
                            }
                        });

                    }
                }
            });

                    requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                            close();
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
            Fragment fragment = fragmentManager.findFragmentByTag("descFragment");
            if (fragment != null) {
                fragmentManager.beginTransaction().hide(fragment).commit();
                fragmentManager.popBackStack();
            }
        }
        public void setBtnUnfollow(){
            btnFollow.setText("Obserwuj");
            Drawable newIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite);
            btnFollow.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
            btnFollow.setBackgroundResource(R.drawable.custom_button);
            btnFollow.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        public void setBtnFollow(){
            btnFollow.setText("Obserwujesz");
            Drawable newIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_white);
            btnFollow.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
            btnFollow.setBackgroundResource(R.drawable.button_pressed);
            btnFollow.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }
    }