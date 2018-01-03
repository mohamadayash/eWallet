package com.example.omarsaad.testingapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar Saad on 1/6/2016.
 */
public class ServerListAdapter extends ArrayAdapter<Service> {

    Context context;
    int resource;
    ArrayList<Service> serveices;


    public ServerListAdapter(Context context, int resource, ArrayList<Service> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        serveices = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout serviceView;

        Service service = getItem(position);

        if (convertView == null) {
            serviceView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, serviceView, true);
        } else {
            serviceView = (LinearLayout) convertView;
        }

        TextView ServiceName = (TextView)serviceView.findViewById(R.id.servicename);
        ServiceName.setText(service.ServiceName);
        TextView IPAddress = (TextView)serviceView.findViewById(R.id.ipadddress);
        IPAddress.setText(service.IPAddress);
        TextView Protocol = (TextView)serviceView.findViewById(R.id.protocol);
        Protocol.setText(service.Protocol);
        TextView Port = (TextView)serviceView.findViewById(R.id.port);
        Port.setText(service.Port);

        ImageView imgStatus = (ImageView)serviceView.findViewById(R.id.status);

        if(service.Status==true)
        {
            imgStatus.setBackgroundResource(R.drawable.greencircle);
        }
        else
        {
            imgStatus.setBackgroundResource(R.drawable.redcircle);
        }

        return serviceView;
    }
}
