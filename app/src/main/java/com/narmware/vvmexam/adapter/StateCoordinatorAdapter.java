package com.narmware.vvmexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.activity.DemoActivity;
import com.narmware.vvmexam.pojo.Questions;
import com.narmware.vvmexam.pojo.StateCoordDetails;
import com.narmware.vvmexam.support.Constants;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class StateCoordinatorAdapter extends RecyclerView.Adapter<StateCoordinatorAdapter.MyViewHolder> implements RealmChangeListener{

    Context context;
    RealmResults<StateCoordDetails> mData;
    Realm realm;

    public StateCoordinatorAdapter(Context context, RealmResults<StateCoordDetails> mData) {
        this.context = context;
        this.mData = mData;

        realm=Realm.getInstance(context);
        mData.addChangeListener(this);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_coordinator_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        StateCoordDetails details=mData.get(position);

        holder.mTxtName.setText(details.getName());
        Picasso.with(context)
                .load(details.getProfile_img())
                .placeholder(R.drawable.vvm_logo_old)
                .into(holder.mImgProf);

        holder.mItem=details;
    }


    @Override
    public int getItemCount() {
        Log.e("Coordinator name",mData.size()+"");

        return mData.size();
    }

    @Override
    public void onChange() {
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mTxtName;
        ImageButton mBtnCall;
        CircleImageView mImgProf;

        StateCoordDetails mItem;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTxtName=itemView.findViewById(R.id.txt_name);
            mBtnCall=itemView.findViewById(R.id.btn_call);
            mImgProf=itemView.findViewById(R.id.img_coord);

            mBtnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = mItem.getMobile();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    context.startActivity(intent);
                }
            });
        }
    }
}
