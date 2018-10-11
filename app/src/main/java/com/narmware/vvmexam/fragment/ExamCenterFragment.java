package com.narmware.vvmexam.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmexam.R;
import com.narmware.vvmexam.adapter.CityAdapter;
import com.narmware.vvmexam.adapter.StateAdapter;
import com.narmware.vvmexam.pojo.City;
import com.narmware.vvmexam.pojo.CityResponse;
import com.narmware.vvmexam.pojo.States;
import com.narmware.vvmexam.pojo.StatesResponse;
import com.narmware.vvmexam.support.Constants;
import com.narmware.vvmexam.support.EndPoints;
import com.narmware.vvmexam.support.SharedPreferencesHelper;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExamCenterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExamCenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamCenterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.spinner_state) Spinner mSpinnState;
    @BindView(R.id.spinner_city) Spinner mSpinnCity;
   public static ImageView mImgProf;
    @BindView(R.id.btn_edit_prof_img) Button mBtnEditProf;
    @BindView(R.id.btn_camera) Button mBtnCamera;
    @BindView(R.id.rootview) RelativeLayout mRootView;

    RequestQueue mVolleyRequest;

    StateAdapter arrayAdapterState;
    ArrayList<States> mStatesList;

    CityAdapter arrayAdapterCities;
    ArrayList<City> mCitiesList;

    private OnFragmentInteractionListener mListener;

    public static String mState,mCity;
    public static String mState_id,mCity_id;

    public ExamCenterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExamCenterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExamCenterFragment newInstance(String param1, String param2) {
        ExamCenterFragment fragment = new ExamCenterFragment();
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
        View view= inflater.inflate(R.layout.fragment_exam_center, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
        mImgProf=view.findViewById(R.id.prof_image);
        mVolleyRequest = Volley.newRequestQueue(getContext());

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        mStatesList=new ArrayList<>();
        arrayAdapterState=new StateAdapter(mStatesList,getContext());
        mSpinnState.setAdapter(arrayAdapterState);
        getStates();

        mCitiesList=new ArrayList<>();

        arrayAdapterCities=new CityAdapter(mCitiesList,getContext());
        mSpinnCity.setAdapter(arrayAdapterCities);

        mSpinnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mState=mStatesList.get(i).getState_name();
                mState_id=mStatesList.get(i).getState_id();
                SharedPreferencesHelper.setPreffExamState(mState,getContext());
                getCities(mStatesList.get(i).getState_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCity=mCitiesList.get(i).getCity_name();
                mCity_id=mCitiesList.get(i).getExam_center_id();
                SharedPreferencesHelper.setPreffExamCity(mCity,getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBtnEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                getActivity().startActivityForResult(photoPickerIntent,Constants.GALLERY_REQUEST_CODE);
            }
        });

        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST_CODE);
            }
        });


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


    private void getStates() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("RESPONSE",response);

                        try {
                            Gson gson = new Gson();
                            StatesResponse dataResponse = gson.fromJson(response, StatesResponse.class);
                            States[] array = dataResponse.getResult();

                            mStatesList.clear();
                            for (States item : array) {
                                //Log.e("State name",item.getState_name());
                                mStatesList.add(item);
                                if(SharedPreferencesHelper.getPreffExamState(getContext())!=null) {
                                    if (SharedPreferencesHelper.getPreffExamState(getContext()).equals(item.getState_name())) {
                                        mSpinnState.setSelection(mStatesList.indexOf(item));
                                    }
                                }
                            }
                            arrayAdapterState.notifyDataSetChanged();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE ERR","That didn't work!");
                showNoConnectionDialog();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", "state");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

    private void getCities(final String state_id) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        //Log.e("RESPONSE CITY",response);

                        try{

                        Gson gson=new Gson();
                        CityResponse dataResponse=gson.fromJson(response,CityResponse.class);
                        City[] array=dataResponse.getResult();

                        mCitiesList.clear();
                        for(City item:array)
                        {
                            mCitiesList.add(item);
                            if(SharedPreferencesHelper.getPreffExamCity(getContext())!=null) {
                                if (SharedPreferencesHelper.getPreffExamCity(getContext()).equals(item.getCity_name())) {
                                    mSpinnCity.setSelection(mCitiesList.indexOf(item));
                                }
                            }
                        }
                        arrayAdapterCities.notifyDataSetChanged();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("RESPONSE ERR","That didn't work!");
                showNoConnectionDialog();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", Constants.EXAM_CITY);
                params.put(Constants.STATE_ID, state_id);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

    public void showNoConnectionDialog(){
        Snackbar.make(mRootView, getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT)
                /*.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),10);
                    }
                })*/
                //.setActionTextColor(Color.RED)
                .show();
    }
}
