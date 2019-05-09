package com.example.plantgrower;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.materialize.util.UIUtils;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.mikhaellopez.circularimageview.CircularImageView;


public class MainActivity extends AppCompatActivity {

    private AccountHeader headerResult = null;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;
    private Toolbar toolbar;
    private Bundle msavedInstanceState;
    private CircularFillableLoaders circularFillableLoaders;


    @Override
    protected void onCreate(Bundle msavedInstanceState) {
        super.onCreate(msavedInstanceState);
        setContentView(R.layout.activity_sample_dark_toolbar);
        setUpToolbar();

        //Remove line to test RTL support
        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);




        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName("Roberto el jalapeño").withIcon("https://s3.amazonaws.com/imagestuffs/aji.png");

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile)
                .withSavedInstance(msavedInstanceState)
                .build();

        setCrossfadeDrawerLayout();

        circularFillableLoaders = findViewById(R.id.circularFillableLoaders);

        circularFillableLoaders.setProgress(80);
        circularFillableLoaders.setBorderWidth(5 * getResources().getDisplayMetrics().density);
        circularFillableLoaders.setAmplitudeRatio((float) 10 / 1000);

    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setCrossfadeDrawerLayout() {

        //items
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("humedad").withIcon(FontAwesome.Icon.faw_seedling).withIdentifier(1);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("riego").withIcon(FontAwesome.Icon.faw_tint).withIdentifier(2);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("tres").withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName("cuatro").withIcon(FontAwesome.Icon.faw_eye).withIdentifier(4);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withName("cinco").withDescription("A more complex sample").withIcon(MaterialDesignIconic.Icon.gmi_adb).withIdentifier(5);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withName("seis").withIcon(MaterialDesignIconic.Icon.gmi_car).withIdentifier(6);
        SecondaryDrawerItem item7 = new SecondaryDrawerItem().withName("siete").withIcon(FontAwesome.Icon.faw_github).withIdentifier(7).withSelectable(false);


        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDrawerLayout(R.layout.crossfade_material_drawer)
                .withHasStableIds(true)
                .withDrawerWidthDp(72)
                .withGenerateMiniDrawer(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        item6,
                        new DividerDrawerItem(),
                        item7
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 7) {
                            new LibsBuilder()
                                    .withFields(R.string.class.getFields())
                                    .withActivityStyle(Libs.ActivityStyle.DARK)
                                    .start(MainActivity.this);
                        } else {
                            if (drawerItem instanceof Nameable) {
                                Toast.makeText(MainActivity.this, ((Nameable) drawerItem).getName().getText(MainActivity.this), Toast.LENGTH_SHORT).show();
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(msavedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //get out our drawerLyout
        crossfadeDrawerLayout = (CrossfadeDrawerLayout) result.getDrawerLayout();

        //define maxDrawerWidth
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)
        MiniDrawer miniResult = result.getMiniDrawer();
        //build the view for the MiniDrawer
        View view = miniResult.build(this);
        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });

        //hook to the crossfade event
        crossfadeDrawerLayout.withCrossfadeListener(new CrossfadeDrawerLayout.CrossfadeListener() {
            @Override
            public void onCrossfade(View containerView, float currentSlidePercentage, int slideOffset) {
                //Log.e("CrossfadeDrawerLayout", "crossfade: " + currentSlidePercentage + " - " + slideOffset);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


}
