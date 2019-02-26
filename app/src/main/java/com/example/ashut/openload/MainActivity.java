package com.example.ashut.openload;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Objects;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity
        implements
        LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        HistoryFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        DescriptionFragment.OnFragmentInteractionListener,
        ResultFragment.OnFragmentInteractionListener,
        LoginRegisterFragment.onFragmentInteraction,
        RecyclerViewClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final long MOVE_DEFAULT_TIME = 1000;
    private static final long FADE_DEFAULT_TIME = 300;
    private Stack<Fragment> stack;
    private Unbinder unbinder;

    private FragmentManager mFragmentManager;
    private static String API_KEY = "6LeUcJAUAAAAANvvh0IBNGGkVhzPomiUs1fay8x7";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        stack = new Stack<>();

        unbinder=ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.threelines);

        setSupportActionBar(toolbar);
        //For initial loading of the application
        if (savedInstanceState == null) {
            SearchFragment searchFragment = new SearchFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, searchFragment).commit();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            openHome();
        } else if (id == R.id.nav_login) {
            openLoginRegister();
        } else if (id == R.id.nav_profile) {
            openProfile();
        } else if (id == R.id.nav_history) {
            openHistory();
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


    public void openHome() {
        SearchFragment searchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, searchFragment)
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

                .addToBackStack(null)
                .commit();


    }

    void openProfile() {
        ProfileFragment profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, profileFragment)
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

                .addToBackStack("SearchF")
                .commit();

    }


    @Override
    public void openLogin() {
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container, loginFragment)
                .addToBackStack("LoginF")
                .commit();
    }

    @Override
    public void openRegister() {
        RegisterFragment registerFragment = new RegisterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, registerFragment)
                .addToBackStack(null)
                .commit();
    }

    void openHistory() {
        HistoryFragment historyFragment = new HistoryFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, historyFragment)
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

                .addToBackStack("SearchF")
                .commit();

    }


    void openLoginRegister() {
        LoginRegisterFragment fragment = new LoginRegisterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .addToBackStack("SearchF")
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onclick(int position, ImageView imageView) {

        DescriptionFragment fragment = new DescriptionFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .addToBackStack("SearchF")
                .commit();

    }
//
//    @Override
//    public void openDescription() {

//    }
//
//    @Override
//    public void openResult() {
//        ResultFragment fragment = new ResultFragment();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, fragment)
//                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
//                .addToBackStack("SearchF")
//                .commit();
//    }

    @Override
    public void onAnimateItemClick(int adapterPosition, Movies moviesList, ImageView ivMoviehead, String transtitionName) {
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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
