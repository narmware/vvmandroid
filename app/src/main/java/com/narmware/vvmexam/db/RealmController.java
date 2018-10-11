package com.narmware.vvmexam.db;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.narmware.vvmexam.pojo.Login;
import com.narmware.vvmexam.pojo.QuestionSequenceType;
import com.narmware.vvmexam.pojo.Questions;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {
 
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getInstance(application);
    }
 
    public static RealmController with(Fragment fragment) {
 
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }
 
    public static RealmController with(Activity activity) {
 
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }
 
   /* public static RealmController with(Application application) {
 
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }
 */
    public static RealmController getInstance() {
 
        return instance;
    }
 
    public Realm getRealm() {
 
        return realm;
    }
 
    //Refresh the realm istance
    public void refresh() {
 
        realm.refresh();
    }
 
    //clear all objects from SchoolDetails.class
    public void clearAll() {
 
        realm.beginTransaction();
        realm.clear(Questions.class);
        realm.commitTransaction();
    }
 
    //find all objects in the SchoolDetails.class
    public RealmResults<Questions> getQuestions() {
 
        return realm.where(Questions.class).findAll();
    }

    public RealmResults<Questions> getQuestionsSetB() {

        return realm.where(Questions.class).findAllSorted("set_b_id");
    }

    public RealmResults<Questions> getQuestionsSetC() {

        return realm.where(Questions.class).findAllSorted("set_c_id");
    }

    public RealmResults<QuestionSequenceType> getQuestionSequence() {

        return realm.where(QuestionSequenceType.class).findAll();
    }

    public Login getStudentDetails() {

        return realm.where(Login.class).findFirst();
    }
    public void clearAllStudents() {

        realm.beginTransaction();
        realm.clear(Login.class);
        realm.commitTransaction();
    }
   /*

    public RealmResults<SchoolDetails> getPaidSchool() {

        return realm.where(SchoolDetails.class)
                .equalTo("payment_status", Constants.PAID)
                .findAll();
    }

    public RealmResults<SchoolDetails> getPaidSchool(boolean isContacted) {

        return realm.where(SchoolDetails.class)
                .equalTo("payment_status", Constants.PAID)
                .equalTo("isCalled",isContacted)
                .findAll();
    }
    public RealmResults<SchoolDetails> getUnpaidSchool(boolean isCalled) {

        return realm.where(SchoolDetails.class)
                .equalTo("payment_status", Constants.UNPAID)
                .equalTo("isCalled",isCalled)
                .findAll();
    }
 
    //query a single item with the given id
    public SchoolDetails getSchoolDetails(String id) {
 
        return realm.where(SchoolDetails.class).equalTo("id", id).findFirst();
    }

    //query example
    public RealmResults<SchoolDetails> queryedSchoolDetailss(String name) {
 
        return realm.where(SchoolDetails.class)
                .contains("state",name, RealmQuery.CASE_INSENSITIVE)
                .findAll();
 
    }
*/

}