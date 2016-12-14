package mediumph.beam.buttons;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by josephgenio on 15/08/16.
 */
public class CustomerOrderListButtons extends Button
{
    private String sName, sCode;
    private double nOrderTotalPrice;
    private double nPrice;
    private int nAmount, index;
    private TextView txtDisplay;

    public CustomerOrderListButtons(Context context, int index, String sName, String sCode, double nPrice)
    {
        super(context);
        this.index = index;
        this.sName = sName;
        this.sCode = sCode;
        this.nPrice = nPrice;

        nAmount = 0;
        nOrderTotalPrice = 0;
        txtDisplay = new TextView(context);
        txtDisplay.setHeight(100);

        int i = getResources().getDisplayMetrics().widthPixels/2;

        txtDisplay.setWidth(i);
        txtDisplay.setText(sName + " x"+nAmount);
        txtDisplay.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.setHeight(100);
        this.setWidth(100);
        this.setText("X");
        this.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.setBackgroundColor(Color.parseColor("#fa4040"));
    }


    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public double getnOrderTotalPrice() {
        return nOrderTotalPrice;
    }

    public void setnOrderTotalPrice(double nOrderTotalPrice) {
        this.nOrderTotalPrice = nOrderTotalPrice;
    }

    public double getnPrice() {
        return nPrice;
    }

    public void setnPrice(double nPrice) {
        this.nPrice = nPrice;
    }

    public int getnAmount() {
        return nAmount;
    }

    public void setnAmount(int nAmount) {
        this.nAmount = nAmount;
        this.setnOrderTotalPrice(this.nPrice * this.nAmount);
        txtDisplay.setText(sName + " x"+nAmount);
    }

    public TextView getTxtDisplay() {
        return txtDisplay;
    }

    public void setTxtDisplay(TextView txtDisplay) {
        this.txtDisplay = txtDisplay;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
