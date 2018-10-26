package com.narmware.vvmexam.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.activity.DemoActivity;
import com.narmware.vvmexam.pojo.QuestionSequenceType;
import com.narmware.vvmexam.pojo.Questions;
import com.narmware.vvmexam.support.Constants;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class QNumAdapter extends RecyclerView.Adapter<QNumAdapter.MyViewHolder> implements RealmChangeListener{

    Context context;
    RealmResults<Questions> mData;
    Realm realm;

    public QNumAdapter(Context context, RealmResults<Questions> mData) {
        this.context = context;
        this.mData = mData;

        realm=Realm.getInstance(context);
        mData.addChangeListener(this);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_qnum, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Questions question=mData.get(position);
        holder.mTxtQnum.setText(question.getQid());

        if(question.getqAnswertype()==Constants.VIEWED)
        {
            holder.mTxtQnum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.custom_round_viewd));
        }
        if(question.getqAnswertype()==Constants.ATTEMPTED)
        {
            holder.mTxtQnum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.custom_round_attempted));
        }
        if(question.getqAnswertype()==Constants.NOT_VIEWED)
        {
            holder.mTxtQnum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.custom_round_notviewed));
        }
       /* if(question.getqAnswertype()==Constants.REVIEW)
        {
            holder.mTxtQnum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.custom_round_marked));
        }*/
        holder.mItem=question;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onChange() {
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mTxtQnum;
        Questions mItem;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTxtQnum=itemView.findViewById(R.id.txt_qnum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,mItem.getQid(), Toast.LENGTH_SHORT).show();
                    DemoActivity.dialog.dismiss();
                    DemoActivity.mRecyclerView.smoothScrollToPosition(Integer.parseInt(mItem.getQid())-1);
                }
            });
        }
    }
}
