package tech.kapardi.c_19beds.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import tech.kapardi.c_19beds.MainActivity;
import tech.kapardi.c_19beds.R;
import tech.kapardi.c_19beds.models.Hospital;

public class HospitalsAdapter extends RecyclerView.Adapter<HospitalsAdapter.MyViewHolder> {

    List<Hospital> hospitalList;
    Context context;

    public HospitalsAdapter(List<Hospital> incidentList, Context context) {
        this.hospitalList = incidentList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hospital_item_layout, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Hospital hospital = hospitalList.get(position);

        final String contact = hospital.getPhone_area_code()+" "+hospital.getContact_no();
        String address = hospital.getAddress()+","+hospital.getPin_code();
        String bed_avail = String.valueOf(hospital.getBed_availability()) + " %";
        String distance = String.valueOf(hospital.getGeodesic_distance()) + " KM";
        holder.hp_name.setText(hospital.getName());
        holder.hp_phone.setText(contact);
        holder.hp_address.setText(address);
        holder.hp_total.setText(String.valueOf(hospital.getTotal_beds()));
        holder.hp_remaining.setText(String.valueOf(hospital.getRemaining_beds()));
        holder.hp_bed_availability.setText(bed_avail);
        holder.hp_distance.setText(distance);
        //holder.hp_confortness.setText(String.valueOf(hospital.getComfortness_factor()));

        if(!hospital.getImg_path().equals("")){
            Glide.with(context).load(hospital.getImg_path()).dontAnimate().into(holder.hp_image);
        }
        /*if(hospital.getBed_availability() > 50){
            holder.hp_remaining.setTextColor(Color.GREEN);
        }
        else if(hospital.getBed_availability() <= 50 && hospital.getBed_availability() > 25){
            holder.hp_remaining.setTextColor(Color.YELLOW);
        }
        else{
            holder.hp_remaining.setTextColor(Color.RED);
        }*/

        /*holder.incident_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, IncidentDetailsActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(intent);
            }
        });*/

        holder.btn_call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {
                    ((MainActivity)context).OnCardButtonClick(true, contact);
                }
            }
        });

        holder.btn_navigate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String data = "geo:0,0?q="+hospital.getLongitude()+","+hospital.getLatitude()+"("+hospital.getName()+")";
                if (context instanceof MainActivity) {
                    ((MainActivity)context).OnCardButtonClick(false, data);
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return hospitalList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        hospitalList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Hospital> list) {
        hospitalList.addAll(list);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hp_name, hp_total, hp_remaining, hp_phone, hp_address, hp_distance, hp_bed_availability, hp_confortness;
        CardView item_card;
        ImageView hp_image;
        ConstraintLayout btn_navigate_layout, btn_call_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            hp_name = itemView.findViewById(R.id.hp_name);
            hp_total = itemView.findViewById(R.id.hp_total);
            hp_remaining = itemView.findViewById(R.id.hp_remaining);
            hp_phone = itemView.findViewById(R.id.hp_phone);
            hp_address = itemView.findViewById(R.id.hp_address);
            btn_navigate_layout = itemView.findViewById(R.id.btn_navigate_layout);
            btn_call_layout = itemView.findViewById(R.id.btn_call_layout);
            hp_image = itemView.findViewById(R.id.hp_image);
            item_card = itemView.findViewById(R.id.item_card);
            hp_distance = itemView.findViewById(R.id.hp_distance);
            hp_bed_availability = itemView.findViewById(R.id.hp_bed_availability);
            //hp_confortness = itemView.findViewById(R.id.hp_confortness);

        }
    }
}