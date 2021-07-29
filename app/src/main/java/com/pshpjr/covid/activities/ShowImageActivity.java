package com.pshpjr.covid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.pshpjr.covid.R;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class ShowImageActivity extends FragmentActivity {

    private ViewPager2 viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        ArrayList<Integer> data = new ArrayList<>();

        Intent i = getIntent();
        int type = i.getIntExtra("type",-1);

        switch (type){
            case 0:
                data.add(R.drawable.jaga0);
                data.add(R.drawable.jaga1);
                data.add(R.drawable.jaga2);
                data.add(R.drawable.jaga3);
                data.add(R.drawable.jaga4);
                break;
            case 1:
                data.add(R.drawable.do1);
                data.add(R.drawable.do2);
                data.add(R.drawable.do3);
                data.add(R.drawable.do4);
                data.add(R.drawable.do5);
                break;
            default:
                break;
        }


        ViewPager2 viewPager = findViewById(R.id.viewpager);
        ViewpagerAdapter adapter = new ViewpagerAdapter(data);
        viewPager.setAdapter(adapter);

        CircleIndicator3 ci = findViewById(R.id.indicator);
        ci.setViewPager(viewPager);


    }



    public class ViewpagerAdapter extends RecyclerView.Adapter<ViewpagerAdapter.ViewHolder>{

        private ArrayList<Integer> data;

        public ViewpagerAdapter(ArrayList<Integer> Items) {
            this.data = Items;
        }

        @NonNull
        @Override
        public ViewpagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_image_page, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewpagerAdapter.ViewHolder holder, int position) {
            holder.image.setImageResource(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.imageView);
            }
        }

    }
}
