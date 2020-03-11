package com.example.weather;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class SliderAdapter extends PagerAdapter {

    String humidity, light, rain, temp;//-----------------------------

    Context context;
    LayoutInflater layoutInflater;

    //-------------------
    private DatabaseReference reff;
    //-------------------
    int nightTime = 1800;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.main_layout, container, false);


        final TextView slideHeading0;
        final ImageView slideImageView0;

        switch (position) {
            case 0:
                view = layoutInflater.inflate(R.layout.main_layout, container, false);
                slideImageView0 = view.findViewById(R.id.image);
                slideHeading0 = view.findViewById(R.id.prospect);


                final TextView slideDescription0 = view.findViewById(R.id.temperature);

                reff = FirebaseDatabase.getInstance().getReference().child("values");
                final View finalView = view;
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        temp = dataSnapshot.child("tempVal").getValue().toString();

                        slideDescription0.setText(temp);

                        humidity = dataSnapshot.child("humidityVal").getValue().toString();
                        light = dataSnapshot.child("lightVal").getValue().toString();
                        rain = dataSnapshot.child("rainVal").getValue().toString();

                        //---------------------------------------------
//                        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm");
//                        Date date = new Date(System.currentTimeMillis());
////                        System.out.println(formatter.format(date));
//                        String currTime = formatter.format(date);

                        Date date = new Date();
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
                        System.out.println(t);
                        //---------------------------------------------

                        finalView.setBackgroundColor(Color.parseColor("#F4F4F4"));
                        if (Integer.parseInt(rain) < 1024 && t < nightTime) {
                            slideImageView0.setImageResource(R.drawable.rainy);
                            slideHeading0.setText("RAINY");
                        } else if (Integer.parseInt(temp) < 25 && Integer.parseInt(light) == 0 && t < nightTime) {
                            slideImageView0.setImageResource(R.drawable.partly);
                            slideHeading0.setText("PARTLY CLOUDY");
                        } else if (Integer.parseInt(light) == 1 && t < nightTime) {//Integer.parseInt(temp) < 25 &&
                            slideImageView0.setImageResource(R.drawable.cloudy);
                            slideHeading0.setText("CLOUDY");
                        } else if (Integer.parseInt(rain) >= 1024 && t < nightTime) {
                            slideImageView0.setImageResource(R.drawable.sunny);
                            slideHeading0.setText("SUNNY");

                        } else if (Integer.parseInt(rain) >= 1024 && t >= nightTime) {
                            slideImageView0.setImageResource(R.drawable.night);
                            slideHeading0.setText("NIGHT");

                            finalView.setBackgroundColor(Color.parseColor("#233651"));
                        } else if (Integer.parseInt(rain) < 1024 && t >= nightTime) {
                            slideImageView0.setImageResource(R.drawable.rainy);
                            slideHeading0.setText("NIGHT");

                            finalView.setBackgroundColor(Color.parseColor("#233651"));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                slideHeading0.setText("SUNNY");
//                slideDescription0.setText("20");
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.auxiliary_layout, container, false);

                ImageView slideImageBackground = view.findViewById(R.id.foreground);

                ImageView slideHumidity = view.findViewById(R.id.image_humidity);
                ImageView slideLight = view.findViewById(R.id.image_light);
                ImageView slideRain = view.findViewById(R.id.image_rain);

                TextView slideHeading1 = view.findViewById(R.id.details);
                final TextView dataHumidity = view.findViewById(R.id.text_humidityData);
                final TextView dataLight = view.findViewById(R.id.text_lightData);
                final TextView dataRain = view.findViewById(R.id.text_rainData);

                slideImageBackground.setImageResource(R.drawable.foreground);

                slideHumidity.setImageResource(R.drawable.humidity);
                slideLight.setImageResource(R.drawable.light);
                slideRain.setImageResource(R.drawable.rain);

                reff = FirebaseDatabase.getInstance().getReference().child("values");
                final View finalView1 = view;
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        humidity = dataSnapshot.child("humidityVal").getValue().toString();
                        light = dataSnapshot.child("lightVal").getValue().toString();
                        rain = dataSnapshot.child("rainVal").getValue().toString();

//                        if (Integer.parseInt(humidity) )
                        dataHumidity.setText(humidity + "%");

                        if (Integer.parseInt(light) == 0)
                            dataLight.setText("clear day");
                        else dataLight.setText("overcast");

                        if (Integer.parseInt(rain) < 1024) {
                            dataRain.setText("raining");
                        } else
                            dataRain.setText("not raining");

                        Date date = new Date();
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
                        System.out.println(t);

                        if (t >= nightTime)
                            finalView1.setBackgroundColor(Color.parseColor("#233651"));
                        else
                            finalView1.setBackgroundColor(Color.parseColor("#F4F4F4"));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                slideHeading1.setText("DETAILS");

                break;
            default:
                break;
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

}
