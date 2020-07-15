package com.example.bleexample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bleexample.R;
import com.example.bleexample.imlp.ItemClickListener;
import com.example.bleexample.model.DiscoveredBluetoothDevice;
import java.util.ArrayList;

public class ScannerAdapter extends RecyclerView.Adapter<ScannerAdapter.ItemHolder> {

    Context context;
    ArrayList<DiscoveredBluetoothDevice> devices;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ScannerAdapter(Context context, ArrayList<DiscoveredBluetoothDevice> devices, ItemClickListener itemClickListener) {
        this.context = context;
        this.devices = devices;
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        ItemHolder itemHolder = new ItemHolder(view);

        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        DiscoveredBluetoothDevice device = devices.get(position);

        if (device.getDevice().getName()== null){
            holder.device_name.setText("Unknown Device");
        }else {
            holder.device_name.setText(device.getDevice().getName());
        }

//        holder.device_name.setText(device.getDevice().getName());
        holder.device_address.setText(device.getDevice().getAddress());
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView device_name, device_address;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.device_name);
            device_address = itemView.findViewById(R.id.device_address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
