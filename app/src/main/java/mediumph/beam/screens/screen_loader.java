package mediumph.beam.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.FrameLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import mediumph.beam.R;
import mediumph.beam.pop.pop_loading;

public class screen_loader extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected Button[] btn_denoms = new Button[8];//0 for 20, 1 for 50, 2 for 100, 3 for 200, 4 for 500, 5 for 1000, 6 for refresh, 7 for loading or scanning
    protected int nTotal_load = 0;
    protected int nProcessed_load = 0;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("EventName/Loader/Transactions");
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scr_loader);
        FrameLayout LL = (FrameLayout) findViewById(R.id.loaderFrame);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        int i = getResources().getDisplayMetrics().heightPixels/12;
        System.out.println(getResources().getDisplayMetrics().heightPixels);

        this.btn_denoms[0] = (Button) findViewById(R.id.btn_20);
        this.btn_denoms[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fnc_updateAmount(20);
            }
        });
        this.btn_denoms[0].setHeight(i);

        this.btn_denoms[1] = (Button) findViewById(R.id.btn_50);
        this.btn_denoms[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fnc_updateAmount(50);
            }
        });
        this.btn_denoms[1].setHeight(i);

        this.btn_denoms[2] = (Button) findViewById(R.id.btn_100);
        this.btn_denoms[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fnc_updateAmount(100);
            }
        });
        this.btn_denoms[2].setHeight(i);

        this.btn_denoms[3] = (Button) findViewById(R.id.btn_200);
        this.btn_denoms[3].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fnc_updateAmount(200);
            }
        });
        this.btn_denoms[3].setHeight(i);

        this.btn_denoms[4] = (Button) findViewById(R.id.btn_500);
        this.btn_denoms[4].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fnc_updateAmount(500);
            }
        });
        this.btn_denoms[4].setHeight(i);

        this.btn_denoms[5] = (Button) findViewById(R.id.btn_1000);
        this.btn_denoms[5].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fnc_updateAmount(1000);
            }
        });
        this.btn_denoms[5].setHeight(i);

        this.btn_denoms[6] = (Button) findViewById(R.id.btn_clearLoad);
        this.btn_denoms[6].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fnc_resetAmount();
            }
        });

        this.btn_denoms[7] = (Button) findViewById(R.id.btn_loadUp);
        this.btn_denoms[7].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fnc_loadUp();
            }
        });
        this.btn_denoms[7].setHeight(i);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    protected void fnc_updateAmount(int nAmount) {
        this.nTotal_load = this.nTotal_load + nAmount;
        this.btn_denoms[7].setText("P" + nTotal_load);
    }

    protected void fnc_resetAmount() {
        this.btn_denoms[7].setText("Check Load");
        nTotal_load = 0;
    }

    protected void fnc_loadUp() {
        //todo dito start ng pag kuha ng shit from the card
        if (nTotal_load > 0) {
            nProcessed_load = nProcessed_load + nTotal_load;

            Map<String, String> pushData = new HashMap<String, String>();
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss").format(Calendar.getInstance().getTime());
            pushData.put("Time", "" + timeStamp);
            pushData.put("Buyer ID", "0001");//todo dummy data, kunin nalang from the card yung ID
            pushData.put("Amount", "" + nTotal_load);
            mConditionRef.push().setValue(pushData);

            //process adding load before popping up
            Intent intent = new Intent(screen_loader.this, pop_loading.class);
            intent.putExtra("mode", 1);
            intent.putExtra("load", this.nTotal_load);
            startActivity(intent);
            //then reset current load no?
            fnc_resetAmount();
        } else {
            Intent intent = new Intent(screen_loader.this, pop_loading.class);
            intent.putExtra("mode", 0);//do not replace
            intent.putExtra("load", this.nTotal_load);//todo you can replace this nalang to the reading shit for load
            startActivity(intent);
        }
    }


    //todo do not edit past here oks na to
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
        getMenuInflater().inflate(R.menu.screen_loader, menu);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        AlertDialog.Builder errorAlert = new AlertDialog.Builder(this);

        if (id == R.id.nav_loader) {
            errorAlert.setMessage("Already in Loading Screen");
            errorAlert.create().show();
        } else if (id == R.id.nav_merchant) {
            if (getIntent().getStringExtra("access").equals("0")) {
                Intent intent = new Intent(this, screen_merchant.class);
                String extra = getIntent().getStringExtra("access");
                intent.putExtra("access", extra);
                startActivity(intent);
            } else {
                errorAlert.setMessage("You Do Not Have Access to the Merchant Screen");
                errorAlert.create().show();
            }
        } else if (id == R.id.nav_viewProfile) {

        } else if (id == R.id.nav_loadProfile) {

        } else if (id == R.id.nav_register) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "screen_loader Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://mediumph.beam/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "screen_loader Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://mediumph.beam/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
