package com.example.locationmanagerhooker;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.locationmanagerhooker.dynamicproxy.Main;
import com.example.locationmanagerhooker.test.Car;
import com.example.locationmanagerhooker.test.Person;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * https://juejin.im/post/5daacc1f6fb9a04de6513b08
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Main.main();
                change();
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        HookHelper.hookLocationManager(locationManager);
        initLocation(locationManager);
    }

    private void initLocation(LocationManager locationManager) {
        String provider = LocationManager.GPS_PROVIDER;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 1001);
                return;
            }
        }


        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        }
//        Location location = locationManager.getLastKnownLocation(provider);
//        if (location != null) {
//            showLocation(location);
//        }

        locationManager.requestLocationUpdates(provider, 5000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                showLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        System.out.println();
    }

    private void showLocation(Location location) {
        String currentPosition = "latitude is " + location.getLatitude() + "\n"
            + "longitude is " + location.getLongitude();
        Log.i("MainApplication", currentPosition);
    }

    //测试，修改一个类中的变量为另外一个
    private void change() {
        Person person = new Person("name1", new Car("benz"));

        try {
            Class<?> personClass = Class.forName("com.example.locationmanagerhooker.test.Person");

            Car anotherCar = new Car("bmw");

            Field field2 = personClass.getDeclaredField("car");
            field2.setAccessible(true);
            field2.set(person, anotherCar);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("change test person'car: " + person.getCar());
    }
}