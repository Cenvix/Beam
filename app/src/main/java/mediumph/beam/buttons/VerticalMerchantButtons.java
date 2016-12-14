package mediumph.beam.buttons;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by josephgenio on 15/08/16.
 */
public class VerticalMerchantButtons extends Button
{

    private double nPrice;
    private int index;
    private String sCode, sName, sEnd;
    private String merchName;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    public VerticalMerchantButtons(Context context, int index, String merchName) {
        super(context);
        this.index = index;
        this.merchName = merchName;


        this.setHeight(100);
        this.setWidth(900);
        //this.setText(sCode);
        this.setBackgroundColor(Color.parseColor("#00838F"));
        this.setId(index+99);
        // Horizontal Properties
        // such

    }

    public void initBtn(DataSnapshot snap)
    {
        DatabaseReference mConditionRef = mRootRef.child("EventName/Merchants/Menu/" + merchName + "/Item" + index + "/Code");
        //FIREBASE SHIT
        //Returns true if not end
        Iterator<DataSnapshot> out = snap.getChildren().iterator();

        sCode = out.next().getValue().toString();
        sName = out.next().getValue().toString();
        nPrice = Double.parseDouble(out.next().getValue().toString());



        this.setText(sName);
    }


    //For isExist
    public String outExist;

    public boolean isExist(int index, String merchName)
    {

        outExist = null;
        DatabaseReference mConditionRef = mRootRef.child("EventName/Merchants/Menu/" + merchName + "/Item" + index+"/Code");


        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                outExist = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //System.out.println("IS EXIST is still??: "+ outExist);


        if(outExist!=null)
            return true;
        else
            return false;
    }

    public String getsEnd() {
        return sEnd;
    }

    public void setsEnd(String sEnd) {
        this.sEnd = sEnd;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public double getnPrice() {
        return nPrice;
    }

    public void setnPrice(double nPrice) {
        this.nPrice = nPrice;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }
}
