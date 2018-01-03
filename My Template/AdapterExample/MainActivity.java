package com.example.omarsaad.testingapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    ListView lstServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstServices = (ListView)findViewById(R.id.lstserver);

        ArrayList<Service> services = new ArrayList<Service>();

        final Service service1 = new Service();
        service1.ServiceName = "Service1";
        service1.IPAddress = "192.168.1.1";
        service1.Protocol = "protocol1";
        service1.Port = "port1";
        service1.Status = true;
        services.add(service1);

        Service service2 = new Service();
        service2.ServiceName = "Service2";
        service2.IPAddress = "192.168.1.2";
        service2.Protocol = "protocol2";
        service2.Port = "port2";
        service2.Status = false ;
        services.add(service2);

        Service service3 = new Service();
        service3.ServiceName = "Service3";
        service3.IPAddress = "192.168.1.3";
        service3.Protocol = "protocol3";
        service3.Port = "port3";
        service3.Status = true;
        services.add(service3);

        Service service4 = new Service();
        service4.ServiceName = "Service4";
        service4.IPAddress = "192.168.1.4";
        service4.Protocol = "protocol4";
        service4.Port = "port4";
        service4.Status = false ;
        services.add(service4);

        Service service5 = new Service();
        service5.ServiceName = "Service5";
        service5.IPAddress = "192.168.1.5";
        service5.Protocol = "protocol5";
        service5.Port = "port5";
        service5.Status = true;
        services.add(service5);

        Service service6 = new Service();
        service6.ServiceName = "Service6";
        service6.IPAddress = "192.168.1.6";
        service6.Protocol = "protocol6";
        service6.Port = "port6";
        service6.Status = false;
        services.add(service6);

        Service service7 = new Service();
        service7.ServiceName = "Service7";
        service7.IPAddress = "192.168.1.7";
        service7.Protocol = "protocol7";
        service7.Port = "port7";
        service7.Status = true;
        services.add(service7);

        final ServerListAdapter adapt = new ServerListAdapter(this, R.layout.lstserveritem, services);

        lstServices.setAdapter(adapt);

        Button test = (Button)findViewById(R.id.testing);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service1.Status = false;
                adapt.notifyDataSetChanged();
            }
        });

    }
}
