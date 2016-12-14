package mediumph.beam.NFC;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nxp.nfclib.exceptions.SmartCardException;
import com.nxp.nfclib.ultralight.IUltralightC;
import com.nxp.nfclib.utils.NxpLogUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

/**
 * Created by Vincent on 06/07/2016.
 */
public class UltraLightCAPI {

    private static String pass = "AAAAAAAAAAAAAAAA";
    private static String TAG = "ULC-API";
    private AppCompatActivity parentScreen;
    private IUltralightC mifareULC;

    public UltraLightCAPI(AppCompatActivity parent, IUltralightC mifareULC)
    {
        this.parentScreen = parent;
        this.mifareULC = mifareULC;
    }

    public void showMessage(String s){
        Toast.makeText(parentScreen,s,Toast.LENGTH_LONG).show();
    }


    /*****************************************************************
     *
     * BASIC METHODS
     *
     *****************************************************************/

    /**
     * formatULC() formats the ULC
     * @return
     */

    public boolean formatULC() {


        boolean res = false;
        try {
            NxpLogUtils.d(TAG, "testULformat, start");
            mifareULC.getReader().setTimeout(500);
            mifareULC.format();
            res = true;

        } catch (SmartCardException e) {
            showMessage("Format: " + res);
            e.printStackTrace();
        }
        NxpLogUtils.d(TAG, "testULformat, result is " + res);
        NxpLogUtils.d(TAG, "testULformat, End");

        return res;
    }

    /**
     * lockMifareULC()
     * Locks the ULC  using config3desMemaccess(int pageNo, byte access)
     *
     * pageNo dictates which pages are locked, starting from pageNo to the end
     * e.g. 0x04 locks page 4 - 47
     *
     * access dictates the type of access to those pages
     * 0x00 locks read and write while 0x01 locks wrtie only
     */

    public void lockMifareULC()
    {

        try{
            mifareULC.config3desMemaccess(0x04,(byte)0x00);
            NxpLogUtils.d(TAG, "Mifare Locked");
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void unlockMifareULC()
    {

        try{
            mifareULC.config3desMemaccess(0x2A,(byte)0x00);
            NxpLogUtils.d(TAG, "Mifare unLocked");
        }catch(IOException e){
            e.printStackTrace();
        }

    }


    /**
     * Sets a key using the String s for he card. Has to be 16 bytes or 16 chars
     * @param s
     */
    public boolean updateKeyMifareULC( String s)
    {
        boolean res = false;
        byte[] pass = s.getBytes(Charset.forName("UTF-8"));
        try{
            mifareULC.program3DESkey(pass);
            NxpLogUtils.d(TAG, "Mifare Pass Approved");
            res = true;
        }catch(IOException e){
            e.printStackTrace();
        }

        return res;

    }

    /**
     * Allows access for the card using the password set in this class (this.pass)
     * Access is only allowed for 1 intent
     */
    public boolean authenticateMifareULC()
    {
        boolean res = false;

        byte[] pass = this.pass.getBytes(Charset.forName("UTF-8"));
        try{
            mifareULC.authenticate(pass);
            NxpLogUtils.d(TAG, "Mifare Authenticated");
            res = true;
        }catch(IOException e){
            e.printStackTrace();
            res = false;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            res = false;
        } catch (SmartCardException e) {
            e.printStackTrace();
            res =false;
        }
        return res;

    }

    /**
     * readMifareULC() reads all pages
     * @return
     */

    public String readMifareULC()
    {
        String output = "null";
        boolean res = false;

        try{
            NxpLogUtils.d(TAG, "Mifare Read, start");
            output = new String(mifareULC.readAll(), Charset.forName("UTF-8"));
            res = true;

        }catch(IOException e){
            e.printStackTrace();

        }
        //showMessage("Read MifareULC: "  + res);
        return output;
    }

    /**
     * reads 4 pages from int page BUT
     * returns only int page contents
     * @param page
     * @return
     */
    public String readPageMifareULC(int page)
    {
        String output = "null";
        boolean res = false;

        try{
            NxpLogUtils.d(TAG, "Mifare Read, start");
            output = new String(mifareULC.read(page), Charset.forName("UTF-8"));
            if(output.length()>=4)
            output = output.substring(0,4);
            else output = null;
            res = true;

        }catch(IOException e){
            e.printStackTrace();

        }
        //showMessage("Read MifareULC: "  + res);
        return output;
    }

    public String readFourPagesMifareULC(int page)
    {
        String output = "null";
        boolean res = false;

        try{
            NxpLogUtils.d(TAG, "Mifare Read, start");
            output = new String(mifareULC.read(page), Charset.forName("UTF-8"));
            res = true;

        }catch(IOException e){
            e.printStackTrace();

        }
        //showMessage("Read MifareULC: "  + res);
        return output;
    }

    /**
     * Writes on the card starting from page 4 to a pages depending on the length of the string
     * NOTE: A single Pages can only be written if string is 4 bytes or 4 chars long.
     *
     * String gets cut up into 4 chars
     * e.g. "Ryanwell Manibo!"
     *  page 1 = "Ryan"; page 2 = "well"; page 3 = " Man"; page 4 = "ibo!"
     *
     *  If the String is not divisible by 4
     *  e.g. "Joseph"
     *  the string will be cut up like this
     *  page 1 = "Jose"; page 2  = "ph__"
     *  with an under score '_' taking the place of the missing chars;
     * @param s
     */

    public void writeMifareULC(String s){

        while(s.length()%4!=0)
            s += "_";

        //byte[] data = s.getBytes(Charset.forName("UTF-8"));

        try{
            NxpLogUtils.d(TAG, "Mifare Write, start");
            showMessage("Writing start");
            for(int i= 0;i<s.length()/4;i++)
                mifareULC.write(i+4, s.substring(i*4,i*4+4).getBytes(Charset.forName("UTF-8")));


            showMessage("Write Success");
        }catch(IOException e){
            e.printStackTrace();
            showMessage("Write Failed");
        }

    }

    /**
     * Same as the original write but for only 1 page.
     * if the string is too long for 4 chars the method cuts the string into the first 4 chars
     *
     * if it's shorter than 4 it still does the under scoring of the string
     *
     * @param page
     * @param s
     */

    public boolean writePageMifareULC(int page, String s){

        boolean res = false;

        String out = s;
        if(s.length()>4)
        {
            out = s.substring(0,5);
        }

        while(out.length()%4!=0)
            out += "_";

        //byte[] data = s.getBytes(Charset.forName("UTF-8"));

        try{
            NxpLogUtils.d(TAG, "Mifare Write, start");

            mifareULC.write(page, out.getBytes(Charset.forName("UTF-8")));

            res = true;
        }catch(IOException e){
            e.printStackTrace();
            res = false;
        }

        return res;
    }


    /***************************
     * GETTERS AND SETTER!!!!
     *
     * THESE getters and setters are made for the card
     * and based on the event ticketting app
    ***************************/

    //Getters

    public String getCardAccess(){
        String out = readPageMifareULC(4).substring(0,1);

        if(out.equals("null"))
            showMessage(out);

        return out;
    }

    public String getCardNotes(){
        String out = readPageMifareULC(4).substring(1,2);

        if(out.equals("null"))
            showMessage(out);
        return out;
    }

    public String getUniqueID(){
        String out = this.readPageMifareULC(4).substring(2,4) + this.readPageMifareULC(5);
        if(out.contains("null"))
            showMessage("null");
        return out;
    }

    public String getMoney(){
        String out = this.readFourPagesMifareULC(6);
        out = out.substring(0,8);

        out = Integer.parseInt(out) +"";

        if(out.contains("null"))
            showMessage("null");
        return out;
    }

    public String getOrderQueue(){

        String curr = this.readFourPagesMifareULC(8);
        String out = "";


        for(int i = 12; i<40 && !curr.contains("null"); i++)
        {
            out += curr;
            curr = this.readFourPagesMifareULC(i);
            i+=3;
        }

        for(int i = 0; i<4;i++)
        {
            String substring = curr.substring(i*4, i*4+4);
            if(!substring.equals("null"))
            out+=substring;
            else {
                break;
            }
        }




        return out;
    }


    /***************************
     *SETTERS
     * TODO Add boolean checkers for write
     **************************/

    public void setAccess(String s){
        String temp = this.readPageMifareULC(4);
        s = s + temp.substring(1,4);

        writePageMifareULC(4,s);
    }


    public void setNotes(String s){
        String temp = this.readPageMifareULC(4);
        s =temp.substring(0,1)+ s + temp.substring(2,4);

        writePageMifareULC(4,s);
    }


    public void setUniqueID(String s){
        String  one = s.substring(0,2),
                two = s.substring(2,6);

        String temp = this.readPageMifareULC(4);
        one =temp.substring(0,2)+ one;

        writePageMifareULC(4, one);
        writePageMifareULC(5,two);
    }

    public boolean setMoney(String s){
        boolean res=false;

        while(s.length()<8)
            s= "0"+s;

        System.out.println("Money: "+s);
        res = this.writePageMifareULC(6,s.substring(0,4));

        res = this.writePageMifareULC(7,s.substring(4,8));

        return res;
    }

    public boolean addMoney(int load){
        String moneyString = getMoney();
        int out = Integer.parseInt(moneyString) + load;

        boolean res = false;

        if(out<=99999999&&out>=0){
            if(setMoney(out+"")){
                res = true;
            }
        }


        return res;
    }





}
