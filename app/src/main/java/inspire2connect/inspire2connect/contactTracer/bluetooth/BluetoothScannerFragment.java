package inspire2connect.inspire2connect.contactTracer.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.contactTracer.base.BaseListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class BluetoothScannerFragment extends BaseListFragment {

    private static final String TAG = BluetoothScannerFragment.class.getSimpleName();

    // TODO: Scanning Period is set here. Change the same
    private static final long SCAN_PERIOD = 20000;

    private BluetoothAdapter bluetoothAdapter;

    private BluetoothLeScanner bluetoothScanner;

    private ScanCallback bluetoothScanCallback;

    private BluetoothScanResultAdapter bluetoothScanResultAdapter;

    private Handler handler;

    private boolean isCurrentlyScanning = false;
    /**
     * Must be called after object creation by MainActivity.
     *
     * @param btAdapter the local BluetoothAdapter
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setBluetoothAdapter(BluetoothAdapter btAdapter) {
        this.bluetoothAdapter = btAdapter;
        bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        setRetainInstance(true);

        bluetoothScanResultAdapter = new BluetoothScanResultAdapter(getActivity().getApplicationContext(),
                LayoutInflater.from(getActivity()));
        handler = new Handler();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        setListAdapter(bluetoothScanResultAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setDivider(null);
        getListView().setDividerHeight(0);

        setEmptyText(getString(R.string.empty_list));

        // Trigger refresh on app's 1st load
        startScanning();

    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startScanning() {
        if(isCurrentlyScanning) return;
        bluetoothScanCallback = new SampleScanCallback();
        isCurrentlyScanning = true;
        try {
            bluetoothScanner.startScan(buildScanFilters(), buildScanSettings(), bluetoothScanCallback);
            String toastText = getString(R.string.scan_start_toast) + " "
                    + TimeUnit.SECONDS.convert(SCAN_PERIOD, TimeUnit.MILLISECONDS) + " "
                    + getString(R.string.seconds);
            // Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Log.d( " Scanning did not start", e.getMessage());
        }
        handler.removeCallbacksAndMessages(null);
        Logd(TAG, "Starting Scanning again");
        // Will stop the scanning after a set time.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isCurrentlyScanning) {
                    Logd(TAG,"Start scanning again");
                    stopScanning();
                    startScanning();
                }
            }
        },SCAN_PERIOD);
    }



    public void stopScanning() {
        Logd(TAG, "Stopping Scanning");
        isCurrentlyScanning = false;
        // Stop the scan, wipe the callback.
        bluetoothScanner.stopScan(bluetoothScanCallback);
        bluetoothScanCallback = null;

        // Even if no new results, update 'last seen' times.
        bluetoothScanResultAdapter.notifyDataSetChanged();
    }

    /**
     * Return a List of {@link ScanFilter} objects to filter by Service UUID.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();

        ScanFilter.Builder builder = new ScanFilter.Builder();
        // Comment out the below line to see all BLE devices around you
        builder.setServiceUuid(Constants.Service_UUID);
        scanFilters.add(builder.build());

        return scanFilters;
    }

    /**
     * Return a {@link ScanSettings} object set to use low power (to preserve battery life).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ScanSettings buildScanSettings() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        // TO DO - check different scan modes and devices
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        // builder.setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES);
        // builder.setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE);
        return builder.build();
    }

    /**
     * Custom ScanCallback object - adds to adapter on success, displays error on failure.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class SampleScanCallback extends ScanCallback {

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);

            for (ScanResult result : results) {
                bluetoothScanResultAdapter.add(result);
            }
            bluetoothScanResultAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            bluetoothScanResultAdapter.add(result);
            bluetoothScanResultAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Toast.makeText(getActivity(), "Scan failed with error: " + errorCode, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
