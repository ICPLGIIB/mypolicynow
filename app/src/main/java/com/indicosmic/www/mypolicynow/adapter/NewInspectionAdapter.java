package com.indicosmic.www.mypolicynow.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.break_in_manager.newInspection.NewInspectionDetails;
import com.indicosmic.www.mypolicynow.model.NewInspectionModel;

import java.util.ArrayList;

/**
 * Created by Ind3 on 16-12-17.
 */

public class NewInspectionAdapter extends RecyclerView.Adapter<NewInspectionAdapter.NewInspectionViewHolder> {

    Context mCtx;
    private ArrayList<NewInspectionModel> mNewinspectionArrayList;
    private ArrayList<NewInspectionModel> mNewinspectionFilteredList;
    SwipeRefreshLayout refreshLayout;
    int lastPosition = -1;


    public NewInspectionAdapter(Context mCtx, ArrayList<NewInspectionModel> mNewinspectionArrayList, SwipeRefreshLayout refreshLayout) {
        this.mCtx = mCtx;
        this.mNewinspectionArrayList = mNewinspectionArrayList;
        this.mNewinspectionFilteredList = mNewinspectionArrayList;
        this.refreshLayout = refreshLayout;
    }


    @Override
    public NewInspectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_inspection_menu, viewGroup, false);
        return new NewInspectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewInspectionViewHolder viewHolder, final int position) {

        viewHolder.proposal_no.setText(mNewinspectionFilteredList.get(position).getProposal_no().toUpperCase());
        viewHolder.register_no.setText(mNewinspectionFilteredList.get(position).getRegistration_no().toUpperCase());
        viewHolder.ic_name.setText(mNewinspectionFilteredList.get(position).getIc_name().toUpperCase());
        Glide.with(mCtx).load(R.drawable.car).into(viewHolder.vehicle_thumbnail);

        Animation animation = AnimationUtils.loadAnimation(mCtx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        viewHolder.itemView.startAnimation(animation);
        lastPosition = position;

        viewHolder.recycleritem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mCtx, NewInspectionDetails.class);
                i.putExtra("proposal_list_id", mNewinspectionFilteredList.get(position).getId());
                i.putExtra("proposal_no", mNewinspectionFilteredList.get(position).getProposal_no());
                i.putExtra("registration_no", mNewinspectionFilteredList.get(position).getRegistration_no());
                i.putExtra("ic_name", mNewinspectionFilteredList.get(position).getIc_name());
                mCtx.startActivity(i);
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(NewInspectionViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mNewinspectionFilteredList = mNewinspectionArrayList;
                    //mFilteredList = mArrayList;
                } else {

                    ArrayList<NewInspectionModel> filteredList = new ArrayList<>();

                    for (NewInspectionModel newInspectionMode : mNewinspectionArrayList) {

                        if (newInspectionMode.getProposal_no().toLowerCase().contains(charString) || newInspectionMode.getRegistration_no().toLowerCase().contains(charString) || newInspectionMode.getIc_name().toLowerCase().contains(charString)) {

                            filteredList.add(newInspectionMode);
                        }
                    }

                    mNewinspectionFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mNewinspectionFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mNewinspectionArrayList = (ArrayList<NewInspectionModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mNewinspectionFilteredList.size();
    }


    public class NewInspectionViewHolder extends RecyclerView.ViewHolder {

        //customer vehicle info
        TextView proposal_list_id, proposal_no, register_no, ic_name;
        ImageView vehicle_thumbnail;
        RelativeLayout recycleritem;

        public NewInspectionViewHolder(final View itemView) {
            super(itemView);
            recycleritem = (RelativeLayout) itemView.findViewById(R.id.recycleritem);
            proposal_list_id = (TextView) itemView.findViewById(R.id.proposal_list_id);
            proposal_no = (TextView) itemView.findViewById(R.id.proposal_no);
            register_no = (TextView) itemView.findViewById(R.id.register_no);
            ic_name = (TextView) itemView.findViewById(R.id.ic_name);
            vehicle_thumbnail = (ImageView) itemView.findViewById(R.id.vehicle_thumbnail);


        }

    }
}