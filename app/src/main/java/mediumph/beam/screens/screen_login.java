package mediumph.beam.screens;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import mediumph.beam.R;


public class screen_login extends AppCompatActivity {
    TextView input_user;
    TextView input_pass;
    Button btn_login;
    String access;
    String passwordComp;
    String sUname, sPword;

    private DatabaseReference mRootRef2 = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scr_login);

        this.input_user = (TextView) findViewById(R.id.login_user);
        this.input_pass = (TextView) findViewById(R.id.login_password);

        this.btn_login = (Button) findViewById(R.id.login_loginButt);
        this.btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fnc_login();
            }
        });
    }

    private void fnc_login()// TODO: HIHINGI NG INPUT FROM DB for comparison
    {
        AlertDialog.Builder errorAlert = new AlertDialog.Builder(this);


        errorAlert.setMessage("Missing Field");


        sUname = this.input_user.getText().toString();
        sPword = this.input_pass.getText().toString();

        DatabaseReference mConditionRef = mRootRef2.child("EventName/Access/" + sUname);

        if (!sUname.equals("") && !sPword.equals("")) {
            this.input_pass.setText("");
            this.input_user.setText("");
            mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    Iterator<DataSnapshot> a = dataSnapshot.getChildren().iterator();
                    int i = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                    fnc_CheckDB(a, i);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            /*Intent intent = new Intent(this, screen_homeScreen.class);
            intent.putExtra("mode", 0);
            startActivity(intent);*/
        } else {
            this.input_pass.setText("");
            errorAlert.create().show();
        }
    }

    void fnc_CheckDB(Iterator<DataSnapshot> items, int ChildCounter) {
        try {
            AlertDialog.Builder errorAlert = new AlertDialog.Builder(this);
            errorAlert.setMessage("Wrong Username or Password");
            access = items.next().getValue().toString();
            passwordComp = items.next().getValue().toString();
            if (sPword.equals(passwordComp)) {
                System.out.println("tama pass mo gags and access is " + access);
                if (access.equals("1")) {
                    //go to merchant screen
                    Intent intent = new Intent(this, screen_merchant.class);
                    intent.putExtra("merchant", sUname);
                    intent.putExtra("access", access);
                    startActivity(intent);
                } else if (access.equals("2")) {
                    //go to loader Screen
                    Intent intent = new Intent(this, screen_loader.class);
                    intent.putExtra("access", access);
                    startActivity(intent);
                } else {
                    //kung access is 0 then admin that can access loader and merchant
                    Intent intent = new Intent(this, screen_homeScreen.class);
                    intent.putExtra("access", access);
                    startActivity(intent);
                }
            } else {
                errorAlert.create().show();
            }
        } catch (Exception e) {
            AlertDialog.Builder errorAlert = new AlertDialog.Builder(this);
            errorAlert.setMessage("Wrong Username or Password.");
            errorAlert.create().show();
        }

    }

    @Override
    protected void onNewIntent(final Intent intent) {


    }

}