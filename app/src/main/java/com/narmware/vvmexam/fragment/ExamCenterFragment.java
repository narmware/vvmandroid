package com.narmware.vvmexam.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.adapter.StateCoordinatorAdapter;
import com.narmware.vvmexam.db.RealmController;
import com.narmware.vvmexam.pojo.Login;
import com.narmware.vvmexam.pojo.StateCoordDetails;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

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

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.edt_sch_state) EditText mEdtSchState;
    @BindView(R.id.edt_sch_district) EditText mEdtSchDistrict;
    @BindView(R.id.edt_sch_city) EditText mEdtSchCity;
    @BindView(R.id.edt_sch_name) EditText mEdtSchName;

    @BindView(R.id.linear_info) LinearLayout mLinearInfo;
    @BindView(R.id.txt_not_alloted_msg) TextView mTxtNotAllotedMsg;

    Realm realm;

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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init(view);
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
        realm= Realm.getInstance(getActivity());

        Login login= RealmController.with(getActivity()).getStudentDetails();

        if(login!=null) {
            if(login.getSch_city().equals("") && login.getSch_dist().equals("") && login.getSch_city().equals("") && login.getSch_name().equals(""))
            {
                mLinearInfo.setVisibility(View.GONE);
                mTxtNotAllotedMsg.setVisibility(View.VISIBLE);
            }
            else {
                mLinearInfo.setVisibility(View.VISIBLE);
                mTxtNotAllotedMsg.setVisibility(View.GONE);

                mEdtSchState.setText(login.getSch_state());
                mEdtSchDistrict.setText(login.getSch_dist());
                mEdtSchCity.setText(login.getSch_city());
                mEdtSchName.setText(login.getSch_name());
            }
        }
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
