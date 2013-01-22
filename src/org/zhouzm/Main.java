package org.zhouzm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.app.Activity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.Context;

public class Main extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    private void checkSimSN()
    {
        Log.d("zhouzm", "checkSimSN BEGIN");
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String simSN = telManager.getSimSerialNumber();
        Log.d("zhouzm", "simSN=" + simSN);

        String lastSimSN = null;
        String fileName = "SimSerialNumber.txt";
        try {
            byte[] bytes = new byte[1024];
            FileInputStream fileInput = this.openFileInput(fileName);
            int length = fileInput.read(bytes);
            fileInput.close();
            lastSimSN = new String(bytes, 0, length);

        } catch (Exception e) {
            Log.d("zhouzm", "failed to read " + fileName + ": " + e);
        }
        Log.d("zhouzm", "lastSimSN=" + lastSimSN);
        if (lastSimSN == null || !lastSimSN.equals(simSN)) {
            try {
                FileOutputStream fileOutput = this.openFileOutput(fileName, this.MODE_PRIVATE);
                fileOutput.write(simSN.getBytes());
                fileOutput.close();
                Log.d("zhouzm", "successful to write " + fileName);
            } catch (Exception e) {
                Log.d("zhouzm", "failed to write " + fileName + ": " + e);
            }
            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage("13400000000", null, "来自周志明的手机", null, null);
            Log.d("zhouzm", "SMS sent");
        }
        Log.d("zhouzm", "checkSimSN END");
    }

    public void onClickTest(View view)
    {
        checkSimSN();
    }
}
