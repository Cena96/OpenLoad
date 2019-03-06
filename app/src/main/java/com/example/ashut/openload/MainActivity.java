package com.example.ashut.openload;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashut.openload.models.Movie;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity
        implements
        LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        HistoryFragment.OnFragmentInteractionListenerHistory,
        SearchFragment.OnFragmentInteractionListener,
        DescriptionFragment.OnFragmentInteractionListener,
        ResultFragment.OnFragmentInteractionListener,
        LoginRegisterFragment.onFragmentInteraction,
        RecyclerViewHistoryClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final long MOVE_DEFAULT_TIME = 1000;
    private static final long FADE_DEFAULT_TIME = 300;
    private Stack<Fragment> stack;
    private Unbinder unbinder;

    //    private ApiService apiService

    private FragmentManager mFragmentManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @Nullable
    @BindView(R.id.nav_header_name)
    TextView user;
    @Nullable
    @BindView(R.id.nav_header_email)
    TextView email;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_log, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String userEmail = "";
        String userName = "";

        mFragmentManager = getSupportFragmentManager();
        stack = new Stack<>();

        unbinder = ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.threelines);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_btn_login) {
                    openFragment(new LoginRegisterFragment());
                    return true;
                } else {
                    user.setText(" ");
                    email.setText(" ");
                    openFragment(new SearchFragment());
                    return true;
                }
            }
        });

        //For initial loading of the application
        if (savedInstanceState == null) {
            openFragment(new SearchFragment());
        }

        //Getting data from the Shared Pref

        //To connect drawer with action bar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();

        userName = intent.getStringExtra("name");
        userEmail = intent.getStringExtra("email");

        //Modifying contents of Navigation Drawer Header
        updateNavHeader(userName, userEmail);

        //Setting Listener to the navigation view
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void updateNavHeader(String userName, String userEmail) {

        View header = navigationView.getHeaderView(0);

        user = header.findViewById(R.id.nav_header_name);
        user.setText(userName);
        email = header.findViewById(R.id.nav_header_email);
        email.setText(userEmail);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            openFragment(new SearchFragment());

        } else if (id == R.id.nav_profile) {
            openFragment(new ProfileFragment());

        } else if (id == R.id.nav_history) {
            openFragment(new HistoryFragment());

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            popFragment();
        }
    }

    //Clearing the fragment backstack
    private void popFragment() {
        if (!stack.empty()) {
            stack.pop();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("EXIT")
                    .setMessage("Are you sure you want to exit")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
        }
    }


    public void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("SearchF")
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onAnimateItemClick(int adapterPosition, Movie moviesList, ImageView ivMoviehead, String transtitionName) {
        if (isDestroyed()) {
            return;
        }
        HistoryFragment hfragment = (HistoryFragment) mFragmentManager.findFragmentById(R.id.container);
        Fragment dfragment = DescriptionFragment.newInstance();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fade exitfade = new Fade();
        exitfade.setDuration(FADE_DEFAULT_TIME);
        Objects.requireNonNull(hfragment).setExitTransition(exitfade);

        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
        enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
        dfragment.setSharedElementEnterTransition(enterTransitionSet);
        dfragment.startPostponedEnterTransition();

        Fade enterFade = new Fade();
        enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
        enterFade.setDuration(FADE_DEFAULT_TIME);
        dfragment.setEnterTransition(enterFade);

        View logo = ivMoviehead.findViewById(R.id.iv_movie_title);
        fragmentTransaction.addSharedElement(logo, logo.getTransitionName());
        fragmentTransaction.replace(R.id.container, dfragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void openResult(ArrayList<Movie> movies) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("movies", movies);

        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(bundle);
        openFragment(fragment);
    }

    @Override
    public void openDescription(Movie movie) {
        Bundle b = new Bundle();
        b.putParcelable("movie", movie);

        DescriptionFragment fragment = new DescriptionFragment();
        fragment.setArguments(b);

        openFragment(fragment);
    }

    @Override
    public void onItemClick(int position, ImageView imageView) {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}