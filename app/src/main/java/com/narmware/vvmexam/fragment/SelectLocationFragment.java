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

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.activity.RegisterActivity;
import com.narmware.vvmexam.dialogs.TnCDialogFragment;
import com.narmware.vvmexam.support.SharedPreferencesHelper;

import java.util.ArrayList;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.spinner_state) Spinner mSpinnState;
    @BindView(R.id.spinner_city) Spinner mSpinnCity;
    @BindView(R.id.spinner_district) Spinner mSpinndistrict;
    @BindView(R.id.check_tnc) CheckBox mCheckTnc;
    @BindView(R.id.txt_terms) TextView mTxtTermsConditions;

    ArrayAdapter arrayAdapterState;
    ArrayList<String> mStatesList;

    ArrayAdapter arrayAdapterCities;
    ArrayList<String> mCitiesList;

    ArrayAdapter arrayAdapterDistrict;
    ArrayList<String> mDistsList;

    public static String mState,mCity,mDistrict;

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

        mStatesList=new ArrayList<>();
        mStatesList.add("Maharashtra");
        mStatesList.add("Telangana");

        mCitiesList=new ArrayList<>();
        mCitiesList.add("Maharashtra");
        mCitiesList.add("Telangana");

        mDistsList=new ArrayList<>();
        mDistsList.add("Maharashtra");
        mDistsList.add("Telangana");

        arrayAdapterState=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,mStatesList);
        mSpinnState.setAdapter(arrayAdapterState);
        if(SharedPreferencesHelper.getUserState(getContext())!=null)
        {
            mSpinnState.setSelection(mStatesList.indexOf(SharedPreferencesHelper.getUserState(getContext())));
            arrayAdapterState.notifyDataSetChanged();
        }

        arrayAdapterDistrict=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,mDistsList);
        mSpinndistrict.setAdapter(arrayAdapterDistrict);
        if(SharedPreferencesHelper.getUserDistrict(getContext())!=null)
        {
            mSpinndistrict.setSelection(mDistsList.indexOf(SharedPreferencesHelper.getUserDistrict(getContext())));
            arrayAdapterDistrict.notifyDataSetChanged();
        }

        arrayAdapterCities=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,mCitiesList);
        mSpinnCity.setAdapter(arrayAdapterCities);
        if(SharedPreferencesHelper.getUserCity(getContext())!=null)
        {
            mSpinnCity.setSelection(mCitiesList.indexOf(SharedPreferencesHelper.getUserCity(getContext())));
            arrayAdapterCities.notifyDataSetChanged();
        }

        mSpinnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mState=mStatesList.get(i);
                SharedPreferencesHelper.setUserState(mState,getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinndistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mDistrict=mDistsList.get(i);
                SharedPreferencesHelper.setUserDistrict(mDistrict,getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCity=mCitiesList.get(i);
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
}
