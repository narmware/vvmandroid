package com.narmware.vvmexam.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.narmware.vvmexam.R;

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

    ArrayAdapter arrayAdapterState;
    ArrayList<String> mStates;

    ArrayAdapter arrayAdapterCities;
    ArrayList<String> mCities;

    ArrayAdapter arrayAdapterDistrict;
    ArrayList<String> mDists;

    private OnFragmentInteractionListener mListener;

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

        mStates=new ArrayList<>();
        mStates.add("Maharashtra");
        mStates.add("Telangana");

        mCities=new ArrayList<>();
        mCities.add("Maharashtra");
        mCities.add("Telangana");

        mDists=new ArrayList<>();
        mDists.add("Maharashtra");
        mDists.add("Telangana");

        arrayAdapterState=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,mStates);
        mSpinnState.setAdapter(arrayAdapterState);

        arrayAdapterDistrict=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,mDists);
        mSpinndistrict.setAdapter(arrayAdapterDistrict);

        arrayAdapterCities=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,mCities);
        mSpinnCity.setAdapter(arrayAdapterCities);
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
