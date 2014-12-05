package it.mondovich.simpletorch;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity {
    Camera camera;
    Toast t;
    ImageView lampadina;
    boolean FlashOn=false;
    Camera.Parameters p;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Testiamo la presenza o meno del flash
        if( this.getPackageManager().hasSystemFeature( PackageManager.FEATURE_CAMERA_FLASH )){
            t=new Toast(this);
            t.makeText(this, getString(R.string.flash_supported), Toast.LENGTH_LONG).show();
        }
        else{
            t=new Toast(this);
            t.makeText(this, getString(R.string.flash_unsupported), Toast.LENGTH_LONG).show();
        }
        camera=Camera.open();

        lampadina=(ImageView)findViewById(R.id.imageView);
        lampadina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = camera.getParameters();

                if(!FlashOn){
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(p);
                    FlashOn=true;
                }
                else{
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(p);
                    FlashOn=false;
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (camera != null) {
            camera.release();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
