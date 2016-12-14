package mediumph.beam.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import mediumph.beam.R;
import mediumph.beam.pop.pop_eventDeets;
import mediumph.beam.pop.pop_merchantDeets;

public class screen_homeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    Button[] buttonContent = new Button[8];// 0 loader, 1 merchant, 2 eventProfile, 3merchantProfile, 4 active function, 5 active device, 6 register, 7 sync

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scr_homescreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //buttons initialization here
        this.buttonContent[0] = (Button) findViewById(R.id.btn_homeLoader);
        this.buttonContent[1] = (Button) findViewById(R.id.btn_homeMerchant);
        this.buttonContent[2] = (Button) findViewById(R.id.btn_homeEventProfile);
        this.buttonContent[3] = (Button) findViewById(R.id.btn_homeMerchantProfile);
        this.buttonContent[4] = (Button) findViewById(R.id.btn_homeActiveFunctions);
        this.buttonContent[5] = (Button) findViewById(R.id.btn_homeActiveDevice);
        this.buttonContent[6] = (Button) findViewById(R.id.btn_homeRegistration);
        this.buttonContent[7] = (Button) findViewById(R.id.btn_homeSync);

        //onlicks here
        this.buttonContent[0].setOnClickListener(new View.OnClickListener()
        { public void onClick(View v){ gotoScreen(0);}});

        this.buttonContent[1].setOnClickListener(new View.OnClickListener()
        { public void onClick(View v){ gotoScreen(1);}});

        this.buttonContent[2].setOnClickListener(new View.OnClickListener()
        { public void onClick(View v){ gotoScreen(2);}});

        this.buttonContent[3].setOnClickListener(new View.OnClickListener()
        { public void onClick(View v){ gotoScreen(3);}});

        this.buttonContent[4].setOnClickListener(new View.OnClickListener()
        { public void onClick(View v){ gotoScreen(4);}});

        this.buttonContent[5].setOnClickListener(new View.OnClickListener()
        { public void onClick(View v){ gotoScreen(5);}});

        this.buttonContent[6].setOnClickListener(new View.OnClickListener()
        { public void onClick(View v){ gotoScreen(6);}});

        this.buttonContent[7].setOnClickListener(new View.OnClickListener()
        { public void onClick(View v){ gotoScreen(7);}});

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    void gotoScreen(int i)
    {
        if(i == 0)
        {
            Intent intent = new Intent(this, screen_loader.class);
            String extra = getIntent().getStringExtra("access");
            intent.putExtra("access", extra);
            startActivity(intent);
        }
        if(i == 1)
        {
            Intent intent = new Intent(this, screen_merchant.class);
            String extra = getIntent().getStringExtra("access");
            intent.putExtra("access", extra);
            startActivity(intent);
        }
        if(i == 2)
        {
            Intent intent = new Intent(screen_homeScreen.this, pop_eventDeets.class);
            startActivity(intent);
        }
        if(i == 3)
        {
            Intent intent = new Intent(screen_homeScreen.this,  pop_merchantDeets.class);
            startActivity(intent);
        }
        if(i == 4)
        {

        }
        if(i == 5)
        {

        }
    }



    //Todo do not edit past here lol kasi d ko alam kung ano ginagawa ng rest here lol
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.screen_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_loader)
        {
            Intent intent = new Intent(this, screen_loader.class);
            String extra = getIntent().getStringExtra("access");
            intent.putExtra("access", extra);
            startActivity(intent);
        }
        else if (id == R.id.nav_merchant)
        {
            Intent intent = new Intent(this, screen_merchant.class);
            String extra = getIntent().getStringExtra("access");
            intent.putExtra("access", extra);
            startActivity(intent);
        }
        else if (id == R.id.nav_viewProfile)
        {
            Intent intent = new Intent(screen_homeScreen.this, pop_eventDeets.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_loadProfile)
        {
            Intent intent = new Intent(screen_homeScreen.this, pop_merchantDeets.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_register)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
