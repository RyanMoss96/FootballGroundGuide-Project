package uk.co.ryanmoss.footballgroundguide_android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GroundListFragment extends Fragment {

    private String GROUND_URL = "http://46.101.2.231/FootballGroundGuide/get_ground_list.php";
    private static final String TAG = "GroundList";
    private String result;
    ListView groundListView;
    public GroundListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ground_list, container, false);




        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // Check whether we're recreating a previously destroyed instance

        result  = this.getArguments().getString("league");


        groundListView = (ListView) getView().findViewById(R.id.groundListView);
        Log.d(TAG, result);
        getGrounds(result);
    }

    private void setListContent(ArrayList<String> listContent){

        if(getActivity() != null) {
            ArrayAdapter<String> groundAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, listContent);
            groundListView.setAdapter(groundAdapter);

            groundListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String selectedGround = groundListView.getItemAtPosition(position).toString().trim();

                    Intent openGroundIntent = new Intent(getActivity(), GroundProfileActivity.class);
                    openGroundIntent.putExtra("ground", selectedGround);
                    startActivity(openGroundIntent);
                    Log.d(TAG, selectedGround);


                }
            });
        }
    }

    public void getGrounds(String league) {

        JSONObject js = new JSONObject();
        try{
            js.put("league", league);
            Log.d(TAG, league);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST,GROUND_URL, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            ArrayList<String> responseList;
                            responseList =  createArrayList(response);
                            setListContent(responseList);

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());


                }
            });

            VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(jsonObjReq);
        } catch (JSONException e)
        {
            Log.e(TAG, e.toString());
        }


    }


    private ArrayList createArrayList(JSONObject response)
    {
        ArrayList<String> responseList = new ArrayList<>();
        try{
            JSONArray listArray = response.getJSONArray("grounds");
            for(int i = 0; i < listArray.length();i++)
            {
                JSONObject row = listArray.getJSONObject(i);

                String name = row.getString("team_name");

                responseList.add(name);
                Log.d(TAG, name);
            }
        } catch (JSONException e)
        {

        }


        return responseList;
    }



}
