package com.narmware.vvmexam.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.support.SharedPreferencesHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonalInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.spinner_state) Spinner mSpinnState;
    @BindView(R.id.spinner_city) Spinner mSpinnCity;
    @BindView(R.id.spinner_gender) Spinner mSpinnGender;

    public static EditText mEdtName;

    ArrayAdapter arrayAdapterState;
    ArrayList<String> mStatesList;

    ArrayAdapter arrayAdapterCities;
    ArrayList<String> mCitiesList;

    ArrayAdapter arrayAdapterGender;
    ArrayList<String> mGenderList;

    private OnFragmentInteractionListener mListener;

    public static String mState,mCity,mGender;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalInfoFragment newInstance(String param1, String param2) {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
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
        View view= inflater.inflate(R.layout.fragment_personal_info, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
        mEdtName=view.findViewById(R.id.edt_name);

        if(SharedPreferencesHelper.getUserName(getContext())!=null)
        {
            mEdtName.setText(SharedPreferencesHelper.getUserName(getContext()));
        }
        mStatesList=new ArrayList<>();
        mStatesList.add("Maharashtra");
        mStatesList.add("Telangana");

        mCitiesList=new ArrayList<>();
        mCitiesList.add("Pune");
        mCitiesList.add("Mumbai");
        mCitiesList.add("Nashik");
        mCitiesList.add("Nagpur");

        mGenderList=new ArrayList<>();
        mGenderList.add("Male");
        mGenderList.add("Female");
        mGenderList.add("Other");

        arrayAdapterState=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,mStatesList);
        mSpinnState.setAdapter(arrayAdapterState);
        if(SharedPreferencesHelper.getPreffExamState(getContext())!=null)
        {
            mSpinnState.setSelection(mStatesList.indexOf(SharedPreferencesHelper.getPreffExamState(getContext())));
            arrayAdapterState.notifyDataSetChanged();
        }

        arrayAdapterGender=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,mGenderList);
        mSpinnGender.setAdapter(arrayAdapterGender);
        if(SharedPreferencesHelper.getUserGender(getContext())!=null)
        {
            mSpinnGender.setSelection(mGenderList.indexOf(SharedPreferencesHelper.getUserGender(getContext())));
            arrayAdapterGender.notifyDataSetChanged();
        }

        arrayAdapterCities=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,mCitiesList);
        mSpinnCity.setAdapter(arrayAdapterCities);
        if(SharedPreferencesHelper.getPreffExamCity(getContext())!=null)
        {
            mSpinnCity.setSelection(mCitiesList.indexOf(SharedPreferencesHelper.getPreffExamCity(getContext())));
            arrayAdapterCities.notifyDataSetChanged();
        }

        mSpinnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mState=mStatesList.get(i);
                SharedPreferencesHelper.setPreffExamState(mState,getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mGender=mGenderList.get(i);
                SharedPreferencesHelper.setUserGender(mGender,getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCity=mCitiesList.get(i);
                SharedPreferencesHelper.setPreffExamCity(mCity,getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
