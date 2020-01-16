package com.indicosmic.www.mypolicynow.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import com.indicosmic.www.mypolicynow.break_in_manager.inprogressInspection.InprogressInspectionDetails;
import com.indicosmic.www.mypolicynow.model.InprogressInspectionModel;


import java.util.ArrayList;

/**
 * Created by Ind3 on 16-01-18.
 */

public class InprogressInspectionAdapter extends RecyclerView.Adapter<InprogressInspectionAdapter.InprogressInspectionViewHolder> {

    Context mCtx;
    private ArrayList<InprogressInspectionModel> mInprogressinspectionArrayList;
    private ArrayList<InprogressInspectionModel> mInprogressinspectionFilteredList;
    SwipeRefreshLayout refreshLayout;
    int lastPosition = -1;

    public InprogressInspectionAdapter(Context mCtx, ArrayList<InprogressInspectionModel> mInprogressinspectionArrayList, SwipeRefreshLayout refreshLayout) {
        this.mCtx = mCtx;
        this.mInprogressinspectionArrayList = mInprogressinspectionArrayList;
        this.mInprogressinspectionFilteredList = mInprogressinspectionArrayList;
        this.refreshLayout = refreshLayout;
    }


    @Override
    public InprogressInspectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inprogress_inspection_menu, viewGroup, false);
        return new InprogressInspectionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final InprogressInspectionViewHolder viewHolder, final int position) {


        InprogressInspectionModel dataObject = mInprogressinspectionArrayList.get(position);
        viewHolder.bind(dataObject);

        viewHolder.proposal_no.setText(mInprogressinspectionFilteredList.get(position).getProposal_no().toUpperCase());
        viewHolder.register_no.setText(mInprogressinspectionFilteredList.get(position).getRegistration_no().toUpperCase());
        viewHolder.sub_inspec_date.setText(mInprogressinspectionFilteredList.get(position).getSub_inspec_date().toUpperCase());
        viewHolder.ic_name.setText(mInprogressinspectionFilteredList.get(position).getIc_name().toUpperCase());
        Glide.with(mCtx).load(R.drawable.car).into(viewHolder.vehicle_thumbnail);

        Animation animation = AnimationUtils.loadAnimation(mCtx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        viewHolder.itemView.startAnimation(animation);
        lastPosition = position;

        viewHolder.recycleritem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mCtx, InprogressInspectionDetails.class);
                i.putExtra("proposal_list_id", mInprogressinspectionFilteredList.get(position).getId());
                i.putExtra("proposal_no", mInprogressinspectionFilteredList.get(position).getProposal_no());
                i.putExtra("registration_no", mInprogressinspectionFilteredList.get(position).getRegistration_no());
                i.putExtra("ic_name", mInprogressinspectionFilteredList.get(position).getIc_name());
                mCtx.startActivity(i);
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(InprogressInspectionViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mInprogressinspectionFilteredList = mInprogressinspectionArrayList;
                    //mFilteredList = mArrayList;
                } else {

                    ArrayList<InprogressInspectionModel> filteredList = new ArrayList<>();

                    for (InprogressInspectionModel inprogressInspectionmod : mInprogressinspectionArrayList) {

                        if (inprogressInspectionmod.getProposal_no().toLowerCase().contains(charString) || inprogressInspectionmod.getRegistration_no().toLowerCase().contains(charString) || inprogressInspectionmod.getIc_name().toLowerCase().contains(charString)) {

                            filteredList.add(inprogressInspectionmod);
                        }
                    }

                    mInprogressinspectionFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mInprogressinspectionFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mInprogressinspectionFilteredList = (ArrayList<InprogressInspectionModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mInprogressinspectionFilteredList.size();
    }


    class InprogressInspectionViewHolder extends RecyclerView.ViewHolder {

        //customer vehicle info
        TextView proposal_list_id, proposal_no, register_no, ic_name, txtPending, sub_inspec_date;
        ImageView vehicle_thumbnail;
        RelativeLayout recycleritem;
        private ObjectAnimator anim;

        public InprogressInspectionViewHolder(View itemView) {
            super(itemView);
            recycleritem = (RelativeLayout) itemView.findViewById(R.id.recycleritem);
            proposal_list_id = (TextView) itemView.findViewById(R.id.proposal_list_id);
            proposal_no = (TextView) itemView.findViewById(R.id.proposal_no);
            register_no = (TextView) itemView.findViewById(R.id.register_no);
            ic_name = (TextView) itemView.findViewById(R.id.ic_name);
            sub_inspec_date = (TextView) itemView.findViewById(R.id.sub_inspec_date);
            txtPending = (TextView) itemView.findViewById(R.id.txtpending);
            vehicle_thumbnail = (ImageView) itemView.findViewById(R.id.vehicle_thumbnail);

            anim = ObjectAnimator.ofFloat(txtPending, "alpha", 0.0f, 1.0f);
            anim.setRepeatMode(ValueAnimator.RESTART);
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(500);

        }

        public void bind(InprogressInspectionModel dataObject){
            anim.start();
        }

    }
}