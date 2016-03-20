package jacob.uk.com.usbcheck;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import jacob.uk.com.usbcheck.events.BatteryChangeEvent;
import jacob.uk.com.usbcheck.services.CommandService;

public class MainActivity extends AppCompatActivity {

    public String device = android.os.Build.DEVICE;
    private CommandService commandService = new CommandService();
    private int detecting;
    private int line;
    private int tick;
    private int cross;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        detecting = getResources().getIdentifier("jacob.uk.com.usbcheck:drawable/detecting", null, null);
        line = getResources().getIdentifier("jacob.uk.com.usbcheck:drawable/line", null, null);
        tick = getResources().getIdentifier("jacob.uk.com.usbcheck:drawable/tick", null, null);
        cross = getResources().getIdentifier("jacob.uk.com.usbcheck:drawable/cross", null, null);

        BatteryChangeEvent batteryChangeEvent = new BatteryChangeEvent();
        batteryChangeEvent.init(MainActivity.this);
    }

    public void anglerCheck() {
        try {
            String result = commandService.executeCommand("cat /sys/class/typec/typec_device/current_detect");

            if (result.equals("0") || result.equals("1")) {
                compliantCable();
            } else {
                nonCompliantCable();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bullheadCheck() {
        try {
            String result = commandService.executeCommand("cat /sys/bus/i2c/drivers/fusb301/*/fclientcur");

            if (Integer.parseInt(result) <= 1500) {
                compliantCable();
            } else {
                nonCompliantCable();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void nonCompliantCable() {
        final ImageView status = (ImageView) findViewById(R.id.status);
        final TextView statusText = (TextView) findViewById(R.id.status_text);
        final TextView moreDetails = (TextView) findViewById(R.id.more_details);

        status.setImageResource(cross);
        statusText.setText("Cable is dangerous, cease use immediately.");
        moreDetails.setText("This cable is not USB-C compliant, it is drawing too much power from the source and a potential fire hazard. \n\nThis cable may harm your device.\n\nDevice: ");
        moreDetails.append(device);
    }

    private void compliantCable() {
        final ImageView status = (ImageView) findViewById(R.id.status);
        final TextView statusText = (TextView) findViewById(R.id.status_text);
        final TextView moreDetails = (TextView) findViewById(R.id.more_details);

        status.setImageResource(tick);
        statusText.setText("Cable is safe to use.");
        moreDetails.setText("This cable is USB-C compliant and drawing under 3As of power from the source.\n\nNot guaranteed to be 100% accurate, always apply caution when using electrical outlets.\n\nDevice: ");
        moreDetails.append(device);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.github) {
            Uri uriUrl = Uri.parse("https://github.com/imjacobclark/usbcheck");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
            return true;
        }else if(id == R.id.jacob){
            Uri uriUrl = Uri.parse("https://www.jacob.uk.com");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
