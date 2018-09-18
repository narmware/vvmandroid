package com.narmware.vvmexam.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmexam.R;
import com.narmware.vvmexam.activity.LoginActivity;
import com.narmware.vvmexam.adapter.NotificationAdapter;
import com.narmware.vvmexam.pojo.NotificationItems;
import com.narmware.vvmexam.pojo.NotificationResponse;
import com.narmware.vvmexam.support.EndPoints;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ArrayList<NotificationItems> notificationItems;
    @BindView(R.id.notification_recycler) RecyclerView mRecyclerView;
    NotificationAdapter mAdapter;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int temp_id;
    public static boolean loading = true;
    public RequestQueue mVolleyRequest;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        mVolleyRequest = Volley.newRequestQueue(getContext());
        ButterKnife.bind(this,view);

        setNotificationAdapter();
        GetNotifications("1","0");
    }

    public void setNotificationAdapter() {
        notificationItems = new ArrayList<>();

        SnapHelper snapHelper = new LinearSnapHelper();

        mAdapter = new NotificationAdapter(getContext(), notificationItems);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);

        //pagination
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    int size=notificationItems.size();
                    Log.v("...", ""+visibleItemCount+"  "+linearLayoutManager.findLastVisibleItemPosition()+"   "+size);

                    int current_id= Integer.parseInt(notificationItems.get(notificationItems.size()-1).getNot_id());

                    if(temp_id > current_id)
                    {
                        loading=true;
                    }
                    if (loading) {

                        if (linearLayoutManager.findLastVisibleItemPosition() == notificationItems.size() - 1) {
                            //if(pastVisiblesItems == dharamshalaItems.size()-3 ){
                            loading = false;
                            HashMap<String, String> param = new HashMap();
                            String pos = notificationItems.get(notificationItems.size() - 1).getNot_id();
                            temp_id = Integer.parseInt(pos);
                          //call service here
                            GetNotifications("0",pos);

                            Log.v("...", "Last Item Wow !" + notificationItems.size()+"  "+pos);
                            //Do pagination.. i.e. fetch new data
                        }

                    }
                }

            }
        });

        mAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void GetNotifications(String isFirst,String not_id) {
      /*  final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Getting Categories...");
        dialog.setCancelable(false);
        dialog.show();*/
      //http://letzlearn.in/vision/getNotifications.php?isfirst=1&not_id
        String url= EndPoints.GET_NOTIFICATIONS+"?isfirst="+isFirst+"&not_id="+not_id;

        Log.e("Noti url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            Log.e("Noti Json_string",response.toString());
                            Gson gson = new Gson();

                            NotificationResponse dataResponse=gson.fromJson(response.toString(),NotificationResponse.class);
                            NotificationItems[] mlist=dataResponse.getResult();

                            for(NotificationItems item:mlist){
                                notificationItems.add(item);
                            }
                            mAdapter.notifyDataSetChanged();

                        } catch (Exception e) {

                            e.printStackTrace();
                            //dialog.dismiss();
                        }
                        //dialog.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Test Error");
                        //dialog.dismiss();
                    }
                }
        );
        mVolleyRequest.add(obreq);
    }


}
