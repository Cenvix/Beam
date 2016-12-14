package mediumph.beam.pop;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.nxp.nfclib.classic.IMFClassic;
import com.nxp.nfclib.classic.IMFClassicEV1;
import com.nxp.nfclib.exceptions.CloneDetectedException;
import com.nxp.nfclib.exceptions.ReaderException;
import com.nxp.nfclib.ultralight.IUltralight;
import com.nxp.nfclib.ultralight.IUltralightC;
import com.nxp.nfcliblite.NxpNfcLibLite;
import com.nxp.nfcliblite.Nxpnfcliblitecallback;

import org.w3c.dom.Text;

import mediumph.beam.NFC.UltraLightCAPI;
import mediumph.beam.R;

public class pop_loading extends AppCompatActivity
{
    TextView txtGreeting, txtLoad;





    private String loadMode;
    private int currLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_loading);
        txtGreeting = (TextView) findViewById(R.id.txt_greetTemplate);
        txtLoad = (TextView) findViewById(R.id.txt_loadHolder);

        this.loadMode = "check";

        currLoad = getIntent().getIntExtra("load", 0);

        if(getIntent().getIntExtra("mode", 0) == 0)
        {

            this.loadMode = "check";
            txtGreeting.setText("Tap card now to CHECK the balance of your card!");
            /*
            txtGreeting.setText("Hi! Your Current Load is:");
            txtLoad.setText("P "+ getIntent().getIntExtra("load", 0));
            */
        }
        if(getIntent().getIntExtra("mode", 0) == 1)
        {

            this.loadMode = "load";
            txtGreeting.setText("Tap card now to LOAD your card!");
            txtLoad.setText("\nYou are about to load \nP "+ getIntent().getIntExtra("load", 0));
            /*
            txtGreeting.setText("Hi! Thanks For Loading!\r\nYour Current Load is:");
            txtLoad.setText("P "+ getIntent().getIntExtra("load", 0));
            */
        }



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));



        //NFC
        initializeNFC();
    }

    // FOR NFC
    private NfcAdapter nfcAdapter;
    private static final String TAG = "NFC";
    private NxpNfcLibLite libInstance = null;
    private IUltralight mifareUL;
    private IMFClassic classic;
    private IUltralightC mifareULC;

    public void initializeNFC(){
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(this.nfcAdapter!=null&&nfcAdapter.isNdefPushEnabled()){
            Toast.makeText(this,"Ready to tap Card!",Toast.LENGTH_LONG).show();
        }
        else
        {


            Toast.makeText(this,"NFC not Working",Toast.LENGTH_LONG).show();
            finish();
        }


        libInstance = NxpNfcLibLite.getInstance();
        // Call registerActivity function before using other functions of the library.
        libInstance.registerActivity(this);
    }

    public void showMessage(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onResume() {
        super.onResume();


        this.libInstance.startForeGroundDispatch();
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.libInstance.stopForeGroundDispatch();
    }

    @Override
    protected void onNewIntent(final Intent intent) {


        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        showMessage("Card Detected! Don't Remove");
        System.out.println("CHECK CHECK LOL");
        //this.outputText.setText(tag.toString());
        try {
            libInstance.filterIntent(intent, callback);
            if(mifareUL ==null){

            }
        } catch (CloneDetectedException e) {
            e.printStackTrace();
            showMessage("FAIL");
        }



    }

    private Nxpnfcliblitecallback callback = new Nxpnfcliblitecallback() {


        @Override
        public void onNewTagDetected(Tag tag) {
            Log.d(TAG, "-------------- onNewTagDetected ------");
        }

        @Override
        public void onClassicEV1CardDetected(IMFClassicEV1 imfClassicEV1) {

            showMessage("Wrong Card");
            return;
        }


        @Override
        public void onUltraLightCardDetected(final IUltralight objUlCard) {
// TODO Auto-generated method stub


            showMessage("Wrong Card");
            /*
            Log.i(TAG,"UltraLight Card Detected" );
            try {
                objUlCard.getReader().connect();
                mifareUL = objUlCard;

                ultraLightLogic();

                objUlCard.getReader().close();
            } catch (ReaderException e) {
                e.printStackTrace();
                System.out.println("Fail");
            }

            */
            return;
        }

        @Override
        public void onClassicCardDetected(final IMFClassic objMFCCard) {

            classic = objMFCCard;
				/* Insert your logic here by commenting the function call below. */

            /*
            try {
                classic.getReader().connect();
                classicCardLogic();
            } catch (Throwable t) {
                showMessage("Unknown Error Tap Again!");
            }

            */
            showMessage("Wrong Card");
        }

        @Override
        public void onUltraLightCCardDetected(final IUltralightC ulC){
// TODO Auto-generated method stub



            Log.i(TAG,"UltraLightC Card Detected" );
            try {
                ulC.getReader().connect();
                mifareULC = ulC;

                ultraLightCLogic();
                ulC.getReader().close();
            } catch (ReaderException e) {
                e.printStackTrace();
                System.out.println("Fail");
            }

        }

    };

    /***********************************************
     * ULTRALIGHTC
     ***********************************************/

    public void ultraLightCLogic() {
        UltraLightCAPI api = new UltraLightCAPI(this,mifareULC);



        if(this.loadMode =="load"){
            //api.authenticateMifareULC();
            //api.formatULC();
            //api.unlockMifareULC();
            //api.updateKeyMifareULC("AAAAAAAAAAAAAAAA");
            String first = api.getMoney();

            api.addMoney(this.currLoad);

            String last = api.getMoney();
            txtGreeting.setText("P "+this.currLoad+" LOADED TO YOUR CARD!\r");
            txtLoad.setText("\n Previous: P "+first+"\n Current: P "+ last);
            //TODO Joseph Function
            this.loadMode="done";
        }
        else if(this.loadMode =="check"){
            txtGreeting.setText("Hi! Your Current Load is:");
            txtLoad.setText("P "+ api.getMoney());
            this.loadMode="done";
        }
        else{
            txtGreeting.setText("Scan Failed!\nPress Back and Try Again");
            txtLoad.setText("");
        }
        showMessage("Tag Complete! Remove Tag");
    }



}
