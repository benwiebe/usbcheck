package jacob.uk.com.usbcheck.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.ImageView;
import android.widget.TextView;

import jacob.uk.com.usbcheck.MainActivity;
import jacob.uk.com.usbcheck.R;

public class BatteryChangeEvent {
    public String device = android.os.Build.DEVICE;

    public void init(final MainActivity mainActivity){
        final int detecting = mainActivity.getResources().getIdentifier("jacob.uk.com.usbcheck:drawable/detecting", null, null);
        final int line = mainActivity.getResources().getIdentifier("jacob.uk.com.usbcheck:drawable/line", null, null);

        final ImageView status = (ImageView) mainActivity.findViewById(R.id.status);
        final TextView statusText = (TextView) mainActivity.findViewById(R.id.status_text);
        final TextView moreDetails = (TextView) mainActivity.findViewById(R.id.more_details);

        status.setImageResource(detecting);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

                if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                    statusText.setText("Connect to a computer, not an AC outlet.");
                    status.setImageResource(detecting);
                    moreDetails.setText("");
                } else {
                    if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {

                        if(device.equals("angler")) {
                            mainActivity.anglerCheck();
                        }else if(device.equals("bullhead")){
                            mainActivity.bullheadCheck();
                        }else{
                            status.setImageResource(line);
                            statusText.setText("Unsupported device.");
                            moreDetails.setText("Only Nexus 6P and Nexus 5X are currently supported.");
                        }

                    } else if (plugged == 0) {
                        statusText.setText("Please connect to a computer.");
                        status.setImageResource(detecting);
                        moreDetails.setText("");
                    } else {
                        statusText.setText("Unable to detect device state.");
                        moreDetails.setText("");
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        mainActivity.registerReceiver(receiver, filter);
    }
}
