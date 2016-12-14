package mediumph.beam.screens;

import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mediumph.beam.R;
import mediumph.beam.buttons.CustomerOrderListButtons;
import mediumph.beam.buttons.HorizontalMerchantButtons;
import mediumph.beam.buttons.VerticalMerchantButtons;

public class screen_merchant extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //Database
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String sMerchName;//dummy data pero dito mo na i-save yung merchant shit
    DatabaseReference mConditionRef;//initiate mo nalang to siguro sa oncreate :))

    //mechant stuff
    //funcs_Merchants[] merch_Stuff = new funcs_Merchants[99];
    ArrayList<HorizontalMerchantButtons> bHorizontal = new ArrayList<>();
    ArrayList<VerticalMerchantButtons> bVertical = new ArrayList<>();
    ArrayList<CustomerOrderListButtons> bCustomer = new ArrayList<>();
    ArrayList<LinearLayout> customerOrderLayoutH = new ArrayList<>();
    ArrayList<LinearLayout> bHorizontalVertLay = new ArrayList<>();

    //customers stuff
    //funcs_Customers[] customer_Stuff = new funcs_Customers[99];

    //layouts
    LinearLayout lay_Horizontal;
    LinearLayout lay_Vertical;
    LinearLayout lay_customer;

    Button btn_Order;

    private DatabaseReference mRootRef2 = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scr_merchant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //initialize all here
        sMerchName = getIntent().getStringExtra("merchant");
        mConditionRef = mRootRef.child("EventName/Merchants/Transactions/"+sMerchName);

        btn_Order = (Button) findViewById(R.id.btn_placeOrder);
        btn_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                while(i < bCustomer.size())
                {
                    fnc_PurchaseRecord(i);
                    i++;
                }

            }
        });

        //placing order button
        /*final Button btn_PlaceOrder = (Button) findViewById(R.id.btn_placeOrder);
        btn_PlaceOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                fnc_PurchaseRecord();
            }
        });*/

        //TabHost tabhosting = (TabHost) findViewById(R.id.tabHost);

        TabHost tabHosting = (TabHost) findViewById(R.id.tabHost);
        tabHosting.setup();

        //tab 1
        TabHost.TabSpec spec = tabHosting.newTabSpec("Grid View");
        spec.setContent(R.id.sv_horizontal);
        spec.setIndicator("Grid View");
        tabHosting.addTab(spec);


        int screenY = getResources().getDisplayMetrics().heightPixels;
        int screenX = getResources().getDisplayMetrics().widthPixels;

        FrameLayout lay_top = (FrameLayout)findViewById(R.id.topFrame);
        ViewGroup.LayoutParams paramsTop = lay_top.getLayoutParams();
        paramsTop.height = (screenY/10) *3;
        paramsTop.width = screenX;
        lay_top.setLayoutParams(paramsTop);


        lay_Horizontal = (LinearLayout) findViewById(R.id.sv_horizontal);
        lay_Vertical = (LinearLayout) findViewById(R.id.sv_vertical);
        lay_customer = (LinearLayout) findViewById(R.id.customerLayout);


        ViewGroup.LayoutParams params = lay_customer.getLayoutParams();
        params.height = (screenY/10);
        params.width = screenX;
        lay_customer.setLayoutParams(params);
        //LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(screenX,screenY/2);
        //lay_customer.setLayoutParams(LP);

        //tab 2
        spec = tabHosting.newTabSpec("List View");
        spec.setContent(R.id.sv_vertical);
        spec.setIndicator("List View");
        tabHosting.addTab(spec);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        DatabaseReference mConditionRef = mRootRef2.child("EventName/Merchants/Menu/" + sMerchName);


        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterator<DataSnapshot> a = dataSnapshot.getChildren().iterator();
                int i = Integer.parseInt(dataSnapshot.getChildrenCount()+"");
                fnc_initializeButtons(a,i);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
    void fnc_initializeButtons(Context context)
    {
        int i = 0;
        merch_Stuff[i] = new funcs_Merchants();
        merch_Stuff[i].InitiateVals("jollibee", i, context);
        customer_Stuff[i].InitiateVals(merch_Stuff[i].sName, merch_Stuff[i].sCode, merch_Stuff[i].nPrice);
        while(merch_Stuff[i].sEnd.equals("no"))
        {
            i++;
            merch_Stuff[i].InitiateVals("jollibee", i, context);

            if(merch_Stuff[i].sEnd.equals("no"))
            {
                customer_Stuff[i].InitiateVals(merch_Stuff[i].sName, merch_Stuff[i].sCode, merch_Stuff[i].nPrice);//todo continue here
            }

            lay_Horizontal.addView(merch_Stuff[i].bHorizontal);
            i++;
        }

    }
     */


    public void fnc_initializeButtons(Iterator<DataSnapshot> items, int count)
    {

        System.out.println("INITIALIZE");
        int i = 0;
        //boolean cond = true;
        LinearLayout LL = null;
        while(items.hasNext())
        {
            HorizontalMerchantButtons h = new HorizontalMerchantButtons(this, i, sMerchName);//DUMMY
            VerticalMerchantButtons v = new VerticalMerchantButtons(this, i, sMerchName);//DUMMY

            h.setOnClickListener(this);
            v.setOnClickListener(this);

            DataSnapshot snap = items.next();
                System.out.println(i+" ADDING BUTTON");
                h.initBtn(snap);
                this.bHorizontal.add(h);
                if (i%2 ==0)
                {
                    LL = new LinearLayout(this);
                    LL.setOrientation(LinearLayout.VERTICAL);
                }
                LL.addView(h);
                if(i%2 == 0)
                this.lay_Horizontal.addView(LL);
                v.initBtn(snap);
                this.bVertical.add(v);
                this.lay_Vertical.addView(v);



            i++;
        }
        System.out.println("END");

        for(int j = 0; j<count; j++)
        {
            CustomerOrderListButtons c = new CustomerOrderListButtons(this,j,bHorizontal.get(j).getsName(),bHorizontal.get(j).getsCode(),bHorizontal.get(j).getnPrice());
            c.setOnClickListener(this);
            this.bCustomer.add(c);
            LinearLayout l = new LinearLayout(this);
            l.addView(c.getTxtDisplay());
            l.addView(c);
            this.customerOrderLayoutH.add(l);


        }
    }

    protected void fnc_PurchaseRecord(int indexNum)//eto yung may date time format
    {
        if(bCustomer.get(indexNum).getnAmount() > 0)
        {
            mConditionRef = mRootRef.child("EventName/Merchants/Transactions/" + sMerchName);
            Map<String, String> pushData = new HashMap<String, String>();
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss").format(Calendar.getInstance().getTime());
            pushData.put("time", "" + timeStamp);
            pushData.put("buyer ID", "0001");//dummy data palitan mo nalang, naka mark as "dummy" yung mga kelangan mong palitan
            pushData.put("Merchant ID", sMerchName);//dummy right side yung kelangan
            pushData.put("Order Code", bCustomer.get(indexNum).getsCode());//dummy
            pushData.put("Amount", "" + bCustomer.get(indexNum).getnAmount());
            pushData.put("Order Price", "" + bCustomer.get(indexNum).getnOrderTotalPrice());//dummy
            mConditionRef.push().setValue(pushData);
            //edited from here
            bCustomer.get(indexNum).setnAmount(0);
            lay_customer.removeView(customerOrderLayoutH.get(indexNum));
        }

        //buyerStoreOrder[TotalOrderCount] = i; todo eto yung ilalagay na siguro sa DB?
        //txtv_SampleOrder.setText("your order is:"+merchantProduct[i]+" for "+"php"+merchantProductPrice[i]+" "+timeStamp);
        //SampleSave = merchantProduct[i]+";"+merchantProductPrice[i]+";"+timeStamp;
        //fnc_saveToLocal();
        //TotalOrderCount++;
    }

    //todo don't edit beyond dis or something unless kung alam mo ginagawa mo :)

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(!drawer.isDrawerOpen(GravityCompat.START))
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.screen_merchant, menu);
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

        AlertDialog.Builder errorAlert = new AlertDialog.Builder(this);

        if (id == R.id.nav_loader)
        {
            if(getIntent().getStringExtra("access").equals("0"))
            {
                Intent intent = new Intent(this, screen_loader.class);
                String extra = getIntent().getStringExtra("access");
                intent.putExtra("access", extra);
                startActivity(intent);
            }
            else
            {
                errorAlert.setMessage("You Do Not Have Access to the Loader Screen");
                errorAlert.create().show();
            }
        }
        else if (id == R.id.nav_merchant)
        {
            errorAlert.setMessage("Already in Merchant Screen");
            errorAlert.create().show();
        }
        else if (id == R.id.nav_viewProfile)
        {

        }
        else if (id == R.id.nav_loadProfile)
        {

        }
        else if (id == R.id.nav_register)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public ArrayList<HorizontalMerchantButtons> getbHorizontal() {
        return bHorizontal;
    }

    public void setbHorizontal(ArrayList<HorizontalMerchantButtons> bHorizontal) {
        this.bHorizontal = bHorizontal;
    }

    public ArrayList<VerticalMerchantButtons> getbVertical() {
        return bVertical;
    }

    public void setbVertical(ArrayList<VerticalMerchantButtons> bVertical) {
        this.bVertical = bVertical;
    }

    @Override
    public void onClick(View v) {

        for(int i=0; i<bHorizontal.size();i++){
            if(v.equals(bHorizontal.get(i))){
                System.out.println(bHorizontal.get(i).getsName()+" H");//CHANGE THIS CODE TO ACTUALL ADDING
                //use bHorizontal.get(i). getsName() or getnPrice or what ever
                bCustomer.get(i).setnAmount(bCustomer.get(i).getnAmount()+1);
                if (bCustomer.get(i).getnAmount() == 1)
                    lay_customer.addView(customerOrderLayoutH.get(i));
            }
        }

        for(int i=0; i<bVertical.size();i++){
            if(v.equals(bVertical.get(i))){
                System.out.println(bVertical.get(i).getsName()+" V");//CHANGE THIS CODE TO ACTUALL ADDING
                //use bVertical.get(i). getsName() or getnPrice or what ever
                bCustomer.get(i).setnAmount(bCustomer.get(i).getnAmount()+1);
                if (bCustomer.get(i).getnAmount() == 1)
                    lay_customer.addView(customerOrderLayoutH.get(i));
            }
        }

        for(int i=0; i<bCustomer.size();i++){
            if(v.equals(bCustomer.get(i))){
                System.out.println(bCustomer.get(i).getsName()+" V");//CHANGE THIS CODE TO ACTUALL ADDING
                //use bVertical.get(i). getsName() or getnPrice or what ever
                bCustomer.get(i).setnAmount(bCustomer.get(i).getnAmount()-1);
                if (bCustomer.get(i).getnAmount() == 0)
                    lay_customer.removeView(customerOrderLayoutH.get(i));
            }
        }

        for(int i = 0, j=0; i<bCustomer.size(); i++)
        {
            if (bCustomer.get(i).getnAmount() > 0)
            {
                j++;
            }
            if (j > 0)
            {
                btn_Order.setText("Check Out");
            }
            else if(j == 0)
            {
                btn_Order.setText("Please Place Order");
            }
        }



    }
}
