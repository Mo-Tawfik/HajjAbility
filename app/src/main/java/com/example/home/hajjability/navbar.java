package com.example.home.hajjability;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class navbar {
    private DrawerLayout mDrawerLayout;


    public void create(final DrawerLayout v, final Context context)//DrawerLayout dr, NavigationView nv)
    {
        //mDrawerLayout = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        mDrawerLayout = v;
        final ArrayList<MenuItem> cat = new ArrayList<MenuItem>();
        // mDrawerLayout.bringToFront();

        final NavigationView navigationView = mDrawerLayout.findViewById(R.id.nav_view);
        final View headerView = navigationView.getHeaderView(0);
        final Menu menu = navigationView.getMenu();
//        cat.add(menu.findItem(R.id.chair));
//        cat.add(menu.findItem(R.id.volunteer));
//        cat.add(menu.findItem(R.id.food));
//        cat.add(menu.findItem(R.id.clothes));
//        cat.add(menu.findItem(R.id.clothes));


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);


                        if (menuItem.getItemId() == R.id.chair) {
                            navigationView.invalidate();
                            Intent intent = new Intent(context, chair.class);
                            context.startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.volunteer) {
                            navigationView.invalidate();
                            Intent intent = new Intent(context, volunteer.class);
                            context.startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.live) {
                            navigationView.invalidate();
                            Intent intent = new Intent(context, live.class);
                            context.startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.manasek) {
                            navigationView.invalidate();
                            Intent intent = new Intent(context, volunteer.class);
                            context.startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.guide) {
                            navigationView.invalidate();
                            Intent intent = new Intent(context, guide.class);
                            context.startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.speech) {
                            navigationView.invalidate();
                            Intent intent = new Intent(context, speech.class);
                            context.startActivity(intent);
                        }
                        // TextView x = (TextView) headerView.findViewById(R.id.textView7);
                        // x.setText("How can we help you?");

return true;
                    }
                });
                }












}
