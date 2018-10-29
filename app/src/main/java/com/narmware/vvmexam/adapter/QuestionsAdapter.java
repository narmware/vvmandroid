package com.narmware.vvmexam.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
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
import com.narmware.vvmexam.support.SharedPreferencesHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        String language=SharedPreferencesHelper.getPreffExamLanguages(context);
        Log.e("Exam lanuguage",language);

        String key= SharedPreferencesHelper.getInvKey(context);
        Bitmap bitmap=null;

        if(key.equals("6") || key.equals("7") || key.equals("8")) {

            if(language.equals("English")) {
                bitmap = getImage(question.getEngj());
            }

            if(language.equals("Marathi")) {
                bitmap = getImage(question.getMarj());
            }
        }


        if(key.equals("9") || key.equals("10") || key.equals("11")) {


            if(language.equals("English")) {
                bitmap = getImage(question.getEngs());
            }
            if(language.equals("Marathi")) {
                bitmap = getImage(question.getMars());
            }
            }
        holder.mImgQuestion.setImageBitmap(bitmap);

    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Bitmap getImage(String path)
    {
        BufferedReader reader = null;
        Bitmap decodedByte=null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(path)));

            // do reading, usually loop until end of file reading
            String mLine;
            StringBuilder lineBuilder=new StringBuilder();

            while ((mLine = reader.readLine()) != null) {
                lineBuilder.append(mLine);
            }
            Log.e("Text file", String.valueOf(lineBuilder));
            byte[] decodedString = Base64.decode(lineBuilder.toString(), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return decodedByte;
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
        TextView mTxtSelectedOpt,mTxtQue;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImgQuestion=itemView.findViewById(R.id.img_question);
            mTxtSelectedOpt=itemView.findViewById(R.id.selected_opt);
            mTxtQue=itemView.findViewById(R.id.txt_question);

        }
    }
}
