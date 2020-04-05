package inspire2connect.inspire2connect.contactTracer.bluetooth;

import android.bluetooth.le.AdvertiseCallback;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import inspire2connect.inspire2connect.R;

import inspire2connect.inspire2connect.contactTracer.base.BaseFragment;


public class BluetoothAdvertisingFragment extends BaseFragment implements View.OnClickListener {


    private Switch advertiseSwitch;

    private BroadcastReceiver advertiseFailureReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        advertiseFailureReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                int errorCode = intent.getIntExtra(BluetoothAdvertiserService.ADVERTISE_FAIL_EXTRA_CODE, -1);

                advertiseSwitch.setChecked(false);

                String errorMessage = getString(R.string.start_error_prefix);
                switch (errorCode) {
                    case AdvertiseCallback.ADVERTISE_FAILED_ALREADY_STARTED:
                        errorMessage += " " + getString(R.string.start_error_already_started);
                        break;
                    case AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE:
                        errorMessage += " " + getString(R.string.start_error_too_large);
                        break;
                    case AdvertiseCallback.ADVERTISE_FAILED_FEATURE_UNSUPPORTED:
                        errorMessage += " " + getString(R.string.start_error_unsupported);
                        break;
                    case AdvertiseCallback.ADVERTISE_FAILED_INTERNAL_ERROR:
                        errorMessage += " " + getString(R.string.start_error_internal);
                        break;
                    case AdvertiseCallback.ADVERTISE_FAILED_TOO_MANY_ADVERTISERS:
                        errorMessage += " " + getString(R.string.start_error_too_many);
                        break;
                    case BluetoothAdvertiserService.ADVERTISE_TIME_OUT:
                        errorMessage = " " + getString(R.string.advertising_timedout);
                        break;
                    default:
                        errorMessage += " " + getString(R.string.start_error_unknown);
                }

                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ca_fragment_advertiser, container, false);

        advertiseSwitch = view.findViewById(R.id.advertise_switch);
        advertiseSwitch.setOnClickListener(this);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (BluetoothAdvertiserService.running) {
            advertiseSwitch.setChecked(true);
        } else {
            advertiseSwitch.setChecked(false);
        }

        IntentFilter failureFilter = new IntentFilter(BluetoothAdvertiserService.ADVERTISE_FAILED);
        getActivity().registerReceiver(advertiseFailureReceiver, failureFilter);

    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(advertiseFailureReceiver);
    }

    private static Intent getServiceIntent(Context c) {
        return new Intent(c, BluetoothAdvertiserService.class);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.advertise_switch:
                // Is the toggle on?
                boolean on = ((Switch) v).isChecked();

                if (on) {
                    startAdvertising();
                } else {
                    stopAdvertising();
                }
                break;

        }
    }


    private void startAdvertising() {
        Context c = getActivity();
        c.startService(getServiceIntent(c));
    }


    private void stopAdvertising() {
        Context c = getActivity();
        c.stopService(getServiceIntent(c));
        advertiseSwitch.setChecked(false);
    }

}
