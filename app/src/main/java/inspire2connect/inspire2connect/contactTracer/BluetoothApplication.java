package inspire2connect.inspire2connect.contactTracer;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;


import inspire2connect.inspire2connect.contactTracer.bluetooth.BluetoothService;

public class BluetoothApplication extends Application {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        this.startBluetoothService();
    }

    private void startBluetoothService() {
        Intent intent = new Intent(this, BluetoothService.class);
        startService(intent);
    }




}
