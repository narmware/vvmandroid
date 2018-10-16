package com.narmware.vvmexam.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.narmware.vvmexam.R;
import com.narmware.vvmexam.activity.DemoActivity;
import com.narmware.vvmexam.activity.LoginActivity;
import com.narmware.vvmexam.db.RealmController;
import com.narmware.vvmexam.pojo.Login;
import com.narmware.vvmexam.support.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.edt_std_name) EditText mEdtStdName;
    @BindView(R.id.edt_std_date) EditText mEdtStdDate;
    @BindView(R.id.edt_std_aadhar) EditText mEdtStdAdhar;
    @BindView(R.id.edt_std_mobile) EditText mEdtStdMobile;
    @BindView(R.id.edt_std_email) EditText mEdtStdEmail;
    @BindView(R.id.edt_std_address) EditText mEdtStdAddress;
    @BindView(R.id.edt_std_pincode) EditText mEdtStdPincode;
    @BindView(R.id.edt_std_state) EditText mEdtStdState;
    @BindView(R.id.edt_std_district) EditText mEdtStdDistrict;
    @BindView(R.id.edt_std_city) EditText mEdtStdCity;

    public static ImageView mImgProf;
    @BindView(R.id.btn_edit_prof_img)
    Button mBtnEditProf;
    @BindView(R.id.btn_camera) Button mBtnCamera;

    private OnFragmentInteractionListener mListener;
    Realm realm;
    RequestQueue mVolleyRequest;

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentProfileFragment newInstance(String param1, String param2) {
        StudentProfileFragment fragment = new StudentProfileFragment();
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
        View view= inflater.inflate(R.layout.fragment_student_profile, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init(view);
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
        realm=Realm.getInstance(getActivity());
        mImgProf=view.findViewById(R.id.prof_image);
        mVolleyRequest = Volley.newRequestQueue(getContext());
        realm= Realm.getInstance(getActivity());

        Login login= RealmController.with(getActivity()).getStudentDetails();
        if(login!=null) {
            String img=login.getProfile_path();
            Log.e("Image",img+"  hello");
            if(!img.equals("")) {
                Picasso.with(getActivity())
                        .load(img)
                        .into(mImgProf);
            }
        }

        if(login!=null) {
            mEdtStdState.setText(login.getStudent_state());
            mEdtStdDistrict.setText(login.getStudent_dist());
            mEdtStdCity.setText(login.getStudent_city());
            mEdtStdPincode.setText(login.getStudent_pincode());
            mEdtStdAddress.setText(login.getStudent_address());
            mEdtStdName.setText(login.getStudent_name());
            mEdtStdDate.setText(login.getStudent_dob());
            mEdtStdAdhar.setText(login.getStudent_aadhar());
            mEdtStdMobile.setText(login.getStudent_mobile());
            mEdtStdEmail.setText(login.getStudent_email());
        }

        mBtnEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                getActivity().startActivityForResult(photoPickerIntent, Constants.GALLERY_REQUEST_CODE);
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
}
