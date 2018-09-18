package com.narmware.vvmexam.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.narmware.vvmexam.adapter.DistrictAdapter;
import com.narmware.vvmexam.adapter.StateAdapter;
import com.narmware.vvmexam.dialogs.TnCDialogFragment;
import com.narmware.vvmexam.pojo.City;
import com.narmware.vvmexam.pojo.CityResponse;
import com.narmware.vvmexam.pojo.District;
import com.narmware.vvmexam.pojo.DistrictResponse;
import com.narmware.vvmexam.pojo.StatesResponse;
import com.narmware.vvmexam.pojo.States;
import com.narmware.vvmexam.support.Constants;
import com.narmware.vvmexam.support.EndPoints;
import com.narmware.vvmexam.support.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectLocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectLocationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RequestQueue mVolleyRequest;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.spinner_state) Spinner mSpinnState;
    @BindView(R.id.spinner_city) Spinner mSpinnCity;
    @BindView(R.id.spinner_district) Spinner mSpinndistrict;
    @BindView(R.id.check_tnc) CheckBox mCheckTnc;
    @BindView(R.id.txt_terms) TextView mTxtTermsConditions;

    StateAdapter arrayAdapterState;
    ArrayList<States> mStatesList;

    CityAdapter arrayAdapterCities;
    ArrayList<City> mCitiesList;

    DistrictAdapter arrayAdapterDistrict;
    ArrayList<District> mDistsList;

    public static String mState,mCity,mDistrict;
    public static String mState_id,mCity_id,mDistrict_id;

    private OnFragmentInteractionListener mListener;
    DialogFragment dialogFragment;

    public SelectLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectLocationFragment newInstance(String param1, String param2) {
        SelectLocationFragment fragment = new SelectLocationFragment();
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
        View view= inflater.inflate(R.layout.fragment_select_location, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
        mVolleyRequest = Volley.newRequestQueue(getContext());

        mStatesList=new ArrayList<>();
        mCitiesList=new ArrayList<>();
        mDistsList=new ArrayList<>();

        arrayAdapterState=new StateAdapter(mStatesList,getContext());
        mSpinnState.setAdapter(arrayAdapterState);
        getStates();

        arrayAdapterDistrict=new DistrictAdapter(mDistsList,getContext());
        mSpinndistrict.setAdapter(arrayAdapterDistrict);

        arrayAdapterCities=new CityAdapter(mCitiesList,getContext());
        mSpinnCity.setAdapter(arrayAdapterCities);

        mSpinnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mState=mStatesList.get(i).getState_name();
                mState_id=mStatesList.get(i).getState_id();
                SharedPreferencesHelper.setUserState(mState,getContext());
                getDistrict(mStatesList.get(i).getState_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinndistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mDistrict=mDistsList.get(i).getDistrict_name();
                mDistrict_id=mDistsList.get(i).getDistrict_id();
                SharedPreferencesHelper.setUserDistrict(mDistrict,getContext());
                getCities(mDistsList.get(i).getDistrict_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCity=mCitiesList.get(i).getCity_name();
                mCity_id=mCitiesList.get(i).getCity_id();
                SharedPreferencesHelper.setUserCity(mCity,getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if(SharedPreferencesHelper.getIsTcAccpted(getContext())==true)
        {
            mCheckTnc.setChecked(true);
        }
        mCheckTnc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true)
                {
                    SharedPreferencesHelper.setIsTcAccpted(true,getContext());
                }
                if(b==false)
                {
                    SharedPreferencesHelper.setIsTcAccpted(false,getContext());
                }
            }
        });

        mTxtTermsConditions.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
            dialogFragment = TnCDialogFragment.newInstance();
            //Bundle args = new Bundle();
            //args.putString("name",name);
            //newFragment.setArguments(args);

            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

            dialogFragment.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_right));
            dialogFragment.setCancelable(false);
            dialogFragment.show(fragmentTransaction, "dialog");

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

                        Gson gson=new Gson();
                        StatesResponse dataResponse=gson.fromJson(response,StatesResponse.class);
                        States[] array=dataResponse.getResult();

                        mStatesList.clear();
                        for(States item:array)
                        {
                            Log.e("State name",item.getState_name());
                            mStatesList.add(item);
                            if(SharedPreferencesHelper.getUserState(getContext()).equals(item.getState_name()))
                            {
                                mSpinnState.setSelection(mStatesList.indexOf(item));
                            }
                        }
                        arrayAdapterState.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE ERR","That didn't work!");
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

    private void getDistrict(final String state_id) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("RESPONSE",response);

                        Gson gson=new Gson();
                        DistrictResponse dataResponse=gson.fromJson(response,DistrictResponse.class);
                        District[] array=dataResponse.getResult();

                        mDistsList.clear();
                        for(District item:array)
                        {
                            mDistsList.add(item);
                            if(SharedPreferencesHelper.getUserDistrict(getContext()).equals(item.getDistrict_name()))
                            {
                                mSpinndistrict.setSelection(mDistsList.indexOf(item));
                            }
                        }
                        arrayAdapterDistrict.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE ERR","That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", Constants.DISTRICT);
                params.put(Constants.STATE_ID, state_id);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

    private void getCities(final String dist_id) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("RESPONSE",response);

                        Gson gson=new Gson();
                        CityResponse dataResponse=gson.fromJson(response,CityResponse.class);
                        City[] array=dataResponse.getResult();

                        mCitiesList.clear();
                        for(City item:array)
                        {
                            mCitiesList.add(item);
                            if(SharedPreferencesHelper.getUserCity(getContext()).equals(item.getCity_name()))
                            {
                                mSpinnCity.setSelection(mCitiesList.indexOf(item));
                            }
                        }
                        arrayAdapterCities.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE ERR","That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", Constants.CITY);
                params.put(Constants.DIST_ID, dist_id);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }


}
