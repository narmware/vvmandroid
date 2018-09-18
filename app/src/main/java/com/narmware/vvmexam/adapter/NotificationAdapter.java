package com.narmware.vvmexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.activity.WebViewActivity;
import com.narmware.vvmexam.pojo.NotificationItems;
import com.narmware.vvmexam.support.Constants;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private ArrayList<NotificationItems> mData;

    public NotificationAdapter(Context mContext, ArrayList<NotificationItems> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle,mDesc, mDate;
        CircleImageView mImgNotCatType;
        NotificationItems mItem;

        public MsgViewHolder(View itemView) {
            super(itemView);
            mTitle=itemView.findViewById(R.id.txt_title);
            mDesc=itemView.findViewById(R.id.txt_desc);
            mDate=itemView.findViewById(R.id.txt_date);
            mImgNotCatType=itemView.findViewById(R.id.cat_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, mItem.getNot_title(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle,mDesc, mDate;
        ImageView mImage;
        CircleImageView mImgNotCatType;
        NotificationItems mItem;

        public ImgViewHolder(View itemView) {
            super(itemView);
            mTitle=itemView.findViewById(R.id.txt_title);
            mDesc=itemView.findViewById(R.id.txt_desc);
            mDate=itemView.findViewById(R.id.txt_date);
            mImage=itemView.findViewById(R.id.img_noti);
            mImgNotCatType=itemView.findViewById(R.id.cat_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, mItem.getNot_title(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class LinkViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle,mDesc, mDate;
        ImageButton mImgBtn;
        CircleImageView mImgNotCatType;
        NotificationItems mItem;

        public LinkViewHolder(View itemView) {
            super(itemView);
            mTitle=itemView.findViewById(R.id.txt_title);
            mDesc=itemView.findViewById(R.id.txt_desc);
            mDate=itemView.findViewById(R.id.txt_date);
            mImgBtn=itemView.findViewById(R.id.img_btn_link);
            mImgNotCatType=itemView.findViewById(R.id.cat_img);
            mImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(Constants.LINK,mItem.getNot_link());
                    mContext.startActivity(intent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, mItem.getNot_title(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        Log.e("Adapter size",mData.size()+"");

        if(mData.get(position).getNot_type().equals("message"))
        {
            return 0;
        }
        else if(mData.get(position).getNot_type().equals("image"))
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            //msg type
            case 0:
                return new MsgViewHolder(LayoutInflater.from(mContext).inflate(R.layout.notification_msg_type, parent, false));

                //image type
            case 1:
                return new ImgViewHolder(LayoutInflater.from(mContext).inflate(R.layout.notification_image_type, parent, false));

                //link type
            case 2:
                return new LinkViewHolder(LayoutInflater.from(mContext).inflate(R.layout.notification_link_type, parent, false));

        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
                MsgViewHolder msgViewHolder = (MsgViewHolder)holder;
                msgViewHolder.mTitle.setText(mData.get(position).getNot_title());
                msgViewHolder.mDesc.setText(mData.get(position).getNot_msg());
                msgViewHolder.mDate.setText(mData.get(position).getNot_date());
                Picasso.with(mContext)
                        .load(mData.get(position).getNot_cat_img())
                        .fit()
                        .into(msgViewHolder.mImgNotCatType);
                msgViewHolder.mItem=mData.get(position);
                break;

            case 1:
                ImgViewHolder imgViewHolder = (ImgViewHolder)holder;
                imgViewHolder.mTitle.setText(mData.get(position).getNot_title());
                imgViewHolder.mDesc.setText(mData.get(position).getNot_msg());
                imgViewHolder.mDate.setText(mData.get(position).getNot_date());
                Picasso.with(mContext)
                        .load(mData.get(position).getNot_img())
                        .fit()
                        .into(imgViewHolder.mImage);
                imgViewHolder.mItem=mData.get(position);

                Picasso.with(mContext)
                        .load(mData.get(position).getNot_cat_img())
                        .fit()
                        .into(imgViewHolder.mImgNotCatType);
                break;

            case 2:
                LinkViewHolder linkViewHolder = (LinkViewHolder)holder;
                linkViewHolder.mTitle.setText(mData.get(position).getNot_title());
                linkViewHolder.mDesc.setText(mData.get(position).getNot_msg());
                linkViewHolder.mDate.setText(mData.get(position).getNot_date());
                Picasso.with(mContext)
                        .load(mData.get(position).getNot_cat_img())
                        .fit()
                        .into(linkViewHolder.mImgNotCatType);

                linkViewHolder.mItem=mData.get(position);
                break;
        }
    }
}