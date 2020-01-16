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
import com.indicosmic.www.mypolicynow.break_in_manager.referbackInspection.ReferBackInspectionDetails;
import com.indicosmic.www.mypolicynow.model.ReferbackModel;

import java.util.ArrayList;

/**
 * Created by Ind3 on 16-01-18.
 */

public class ReferBackAdapter extends RecyclerView.Adapter<ReferBackAdapter.ReferBackViewHolder> {

    Context mCtx;
    private ArrayList<ReferbackModel> mReferBackinspectionArrayList;
    private ArrayList<ReferbackModel> mReferBackinspectionFilteredList;
    SwipeRefreshLayout refreshLayout;
    int lastPosition = -1;

    public ReferBackAdapter(Context mCtx, ArrayList<ReferbackModel> mReferBackinspectionArrayList, SwipeRefreshLayout refreshLayout) {
        this.mCtx = mCtx;
        this.mReferBackinspectionArrayList = mReferBackinspectionArrayList;
        this.mReferBackinspectionFilteredList = mReferBackinspectionArrayList;
        this.refreshLayout = refreshLayout;
    }



    @Override
    public ReferBackViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.referback_inspection_menu, viewGroup, false);
        return new ReferBackViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ReferBackViewHolder viewHolder, final int position) {

        viewHolder.proposal_no.setText(mReferBackinspectionFilteredList.get(position).getProposal_no().toUpperCase());
        viewHolder.register_no.setText(mReferBackinspectionFilteredList.get(position).getRegistration_no().toUpperCase());
        viewHolder.ic_name.setText(mReferBackinspectionFilteredList.get(position).getIc_name().toUpperCase());
        Glide.with(mCtx).load(R.drawable.car).into(viewHolder.vehicle_thumbnail);

        Animation animation = AnimationUtils.loadAnimation(mCtx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        viewHolder.itemView.startAnimation(animation);
        lastPosition = position;

        viewHolder.recycleritem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mCtx, ReferBackInspectionDetails.class);
                i.putExtra("proposal_list_id", mReferBackinspectionFilteredList.get(position).getId());
                i.putExtra("proposal_no", mReferBackinspectionFilteredList.get(position).getProposal_no());
                i.putExtra("registration_no", mReferBackinspectionFilteredList.get(position).getRegistration_no());
                i.putExtra("ic_name", mReferBackinspectionFilteredList.get(position).getIc_name());
                mCtx.startActivity(i);
                //  Toast.makeText(mCtx, ""+mInprogressinspectionFilteredList.get(position).getProposal_no(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onViewDetachedFromWindow(ReferBackViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mReferBackinspectionFilteredList = mReferBackinspectionArrayList;
                    //  mFilteredList = mArrayList;
                } else {

                    ArrayList<ReferbackModel> filteredList = new ArrayList<>();

                    for (ReferbackModel referBackInspectionmod : mReferBackinspectionFilteredList) {

                        if (referBackInspectionmod.getProposal_no().toLowerCase().contains(charString) || referBackInspectionmod.getRegistration_no().toLowerCase().contains(charString) || referBackInspectionmod.getIc_name().toLowerCase().contains(charString)) {

                            filteredList.add(referBackInspectionmod);
                        }
                    }

                    mReferBackinspectionFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mReferBackinspectionFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mReferBackinspectionFilteredList = (ArrayList<ReferbackModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mReferBackinspectionFilteredList.size();
    }



    class ReferBackViewHolder extends RecyclerView.ViewHolder {

        //customer vehicle info
        TextView proposal_list_id,proposal_no,register_no,ic_name;
        ImageView vehicle_thumbnail;
        RelativeLayout recycleritem;

        public ReferBackViewHolder(View itemView) {
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