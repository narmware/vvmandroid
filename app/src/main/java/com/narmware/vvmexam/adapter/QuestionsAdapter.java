package com.narmware.vvmexam.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.activity.DemoActivity;
import com.narmware.vvmexam.pojo.Questions;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.MyViewHolder> implements RealmChangeListener {

    Context context;
    RealmResults<Questions> mData;
    Realm realm;

    public QuestionsAdapter(Context context, RealmResults<Questions> mData) {
        this.context = context;
        this.mData = mData;
        realm=Realm.getInstance(context);

        mData.addChangeListener(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_questions, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Questions question=mData.get(position);

        byte[] decodedString = Base64.decode(question.getQname(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.mImgQuestion.setImageBitmap(decodedByte);

        holder.mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==mData.size()-1) {
                    Toast.makeText(context, "No questions", Toast.LENGTH_SHORT).show();
                }
                else {
                    DemoActivity.mRecyclerView.smoothScrollToPosition(position + 1);
                }
            }
        });

        holder.mBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(position==0) {
                    Toast.makeText(context, "No questions", Toast.LENGTH_SHORT).show();
                }
                else{
                    DemoActivity.mRecyclerView.smoothScrollToPosition(position - 1);
                }
            }
        });

        holder.mBtnOptA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              setAnswer(position,"A");
            }
        });

        holder.mBtnOptB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAnswer(position,"B");
            }
        });

        holder.mBtnOptC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAnswer(position,"C");
            }
        });

        holder.mBtnOptD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAnswer(position,"D");

            }
        });

        holder.mTxtSelectedOpt.setText(question.getAnswer());
    }

    public void setAnswer(int position,String answer)
    {
        realm.beginTransaction();
        mData.get(position).setAnswer(answer);
        realm.commitTransaction();
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

        ImageView mImgQuestion;
        Button mBtnNext,mBtnPrevious;
        Button mBtnOptA,mBtnOptB,mBtnOptC,mBtnOptD;
        TextView mTxtSelectedOpt;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImgQuestion=itemView.findViewById(R.id.img_question);
            mBtnNext=itemView.findViewById(R.id.btn_next);
            mBtnPrevious=itemView.findViewById(R.id.btn_previous);
            mTxtSelectedOpt=itemView.findViewById(R.id.selected_opt);

            mBtnOptA=itemView.findViewById(R.id.btn_opt_a);
            mBtnOptB=itemView.findViewById(R.id.btn_opt_b);
            mBtnOptC=itemView.findViewById(R.id.btn_opt_c);
            mBtnOptD=itemView.findViewById(R.id.btn_opt_d);

        }
    }
}
