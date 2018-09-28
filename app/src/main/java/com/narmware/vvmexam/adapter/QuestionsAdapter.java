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
        holder.mTxtSelectedOpt.setText(question.getAnswer());
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
        TextView mTxtSelectedOpt;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImgQuestion=itemView.findViewById(R.id.img_question);
            mTxtSelectedOpt=itemView.findViewById(R.id.selected_opt);

        }
    }
}
