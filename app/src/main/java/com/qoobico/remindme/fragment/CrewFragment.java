package com.qoobico.remindme.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.qoobico.remindme.R;
import com.qoobico.remindme.activity.MainActivity;
import com.qoobico.remindme.activity.ViewContactActivity;
import com.qoobico.remindme.adapter.FlightCrewAdapter;
import com.qoobico.remindme.adapter.UsersAdapter;
import com.qoobico.remindme.app.EndPoints;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.helper.SimpleDividerItemDecoration;
import com.qoobico.remindme.model.FlightCrew;
import com.qoobico.remindme.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final int LAYOUT = R.layout.crew_layout;
    private View view;
    private String TAG = "cucle";
    private String crewId;
    private String UserId = MyApplication.getInstance().getPrefManager().getUser().getId();
    FloatingActionsMenu ReadNessMenu;
    FloatingActionButton ReadNessOk;
    FloatingActionButton ReadNessCancel;

    private static String today;
    String Readiness;
    private String Ok = "1";
    private String Canc = "3";
    private String DataCreate = "Дата назначения ";
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<User> FlightCrewArrayList;
    private FlightCrewAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView nameCrew, dataCreate;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        Log.d(TAG, "onCreateView");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_crews);
        nameCrew = (TextView) view.findViewById(R.id.number_crew);
        dataCreate = (TextView) view.findViewById(R.id.date_create_crew);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_crew);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.material_blue_100,
                R.color.material_blue_300,
                R.color.material_blue_500,
                R.color.material_blue_700);
        FlightCrewArrayList = new ArrayList<>();
        mAdapter = new FlightCrewAdapter(getActivity(), FlightCrewArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        ReadNessMenu = (FloatingActionsMenu) view.findViewById(R.id.clickon);
        ReadNessOk = (FloatingActionButton) view.findViewById(R.id.ok_readness);
        ReadNessCancel = (FloatingActionButton) view.findViewById(R.id.cancel_readness);
        ReadNessMenu.setVisibility(View.INVISIBLE);


        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated");
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                fetchFlightCrew(Readiness);

                ReadNessOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String endPoint = EndPoints.READINESS.replace("_ID_", UserId);
                        Log.e(TAG, "endPoint: " + endPoint);
                        StringRequest strRead = new StringRequest(Request.Method.PUT,
                                endPoint, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG, "response: " + response);

                                try {
                                    JSONObject obj = new JSONObject(response);

                                    // check for error flag
                                    if (obj.getBoolean("error") == false) {

                                    } else {
                                        // login error - simply toast the message
                                        Toast.makeText(getContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    Log.e(TAG, "json parsing error: " + e.getMessage());
                                    Toast.makeText(getContext(), R.string.no_data, Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                NetworkResponse networkResponse = error.networkResponse;
                                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                                Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() {
                                Log.d(TAG, "Отправка данных");
                                Map<String, String> params = new HashMap<>();
                                params.put("readiness", Ok);

                                Log.e(TAG, "params: " + params.toString());
                                return params;
                            }
                        };

                        //Adding request to request queue
                        MyApplication.getInstance().addToRequestQueue(strRead);
                        ReadNessOk.setVisibility(View.INVISIBLE);
                    }


                });

                ReadNessCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
                        final EditText input = new EditText(getContext());
                        a_builder.setMessage(R.string.alertMessege)
                                .setCancelable(false)
                                .setView(input)
                                .setPositiveButton(R.string.alertOk,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getContext(), "Заявка отправлена ", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton(R.string.alertCancel,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }) ;
                        AlertDialog alert = a_builder.create();
                        alert.setTitle(R.string.alertTitle);
                        alert.show();

                    }
                });

            }
        });

        recyclerView.addOnItemTouchListener(new UsersAdapter.RecyclerTouchListener(getContext(), recyclerView, new UsersAdapter.UserClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                User user = FlightCrewArrayList.get(position);
                Intent intent = new Intent(getActivity(), ViewContactActivity.class);
                intent.putExtra("user_id", user.getId());
                intent.putExtra("name", user.getName());
                intent.putExtra("email", user.getEmail());
                intent.putExtra("phone", user.getPhone());
                intent.putExtra("user_image", user.getImageUser());
                intent.putExtra("position_name", user.getPosition());
                intent.putExtra("created_at", user.getCreate());
                intent.putExtra("birthday", user.getBirthday());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    private void initFloating(){
        ReadNessMenu.setVisibility(View.VISIBLE);
       if(Readiness!=null) {
           if (Readiness.equals(Ok)) {
               ReadNessCancel.setVisibility(View.VISIBLE);
               ReadNessOk.setVisibility(View.INVISIBLE);
           } else if(Readiness.equals(Canc)) {
               ReadNessOk.setVisibility(View.VISIBLE);
               ReadNessCancel.setVisibility(View.INVISIBLE);
           }
       } else { }
   }

    private String fetchFlightCrew(String Readeness) {
        swipeRefreshLayout.setRefreshing(true);
        crewId = MyApplication.getInstance().getPrefManager().getCodeUser().getCodeId();
        String endPoint = EndPoints.CREW.replace("_ID_", crewId);
        Log.e(TAG, "endPoint: " + endPoint);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obju = new JSONObject(response);

                    // check for error flag
                    if (obju.getBoolean("error") == false) {
                        JSONObject crewObj = obju.getJSONObject("flight_crew");
                        String commentId = crewObj.getString("code_id");
                        String commentText = crewObj.getString("crew_name");
                        String commentcreate = crewObj.getString("created_at");

                        FlightCrew fl = new FlightCrew();
                        fl.setId(commentId);
                        fl.setCodeValue(commentText);
                        fl.setCreate(commentcreate);
                        nameCrew.setText(fl.getCodeValue());
                        dataCreate.setText(DataCreate +getTimeStamp(fl.getCreate()));
                        JSONArray UsersArray = obju.getJSONArray("user");
                        for (int i = 0; i < UsersArray.length(); i++) {
                            JSONObject UsersObj = (JSONObject) UsersArray.get(i);
                            User us = new User();
                            us.setId(UsersObj.getString("user_id"));
                            us.setName(UsersObj.getString("name"));
                            us.setEmail(UsersObj.getString("email"));
                            us.setPhone(UsersObj.getString("phone"));
                            us.setImageUser(UsersObj.getString("user_image"));
                            us.setPosition(UsersObj.getString("position_name"));
                            us.setReadiness(UsersObj.getString("readiness"));
                            us.setCreate(UsersObj.getString("created_at"));
                            us.setBirthday(UsersObj.getString("birthday"));
//                            Проверяем готовность пользователя
                            if(us.getId().equals(UserId)){
                                Readiness = (us.getReadiness());

                               }
                            FlightCrewArrayList.add(us);
                        }

                        RelativeLayout cinfo = (RelativeLayout) view.findViewById(R.id.inf);
                        cinfo.setBackgroundResource(R.color.white);

                    } else {

                        Toast.makeText(getContext(), "error правда" + obju.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), R.string.no_data, Toast.LENGTH_LONG).show();

                }

                mAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "initFloating");
                initFloating();
                // subscribing to all chat room topics

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
        return Readiness;
    }

    @Override
    public void onRefresh() {
        FlightCrewArrayList.clear();
//        MainActivity mainAct = new MainActivity();
//        mainAct.UserCode();
        fetchFlightCrew(Readiness);
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

}
