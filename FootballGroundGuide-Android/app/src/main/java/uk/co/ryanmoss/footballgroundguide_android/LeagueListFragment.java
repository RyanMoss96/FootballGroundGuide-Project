package uk.co.ryanmoss.footballgroundguide_android;

import android.app.Activity;
import android.content.Context;
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


public class LeagueListFragment extends Fragment {

    private String LEAGUE_URL = "http://46.101.2.231/FootballGroundGuide/get_league_list.php";
    private static final String TAG = "LeagueList";
    private String result;
    ListView leagueListView;
    FragmentController leagueController;
    public LeagueListFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_league_list, container, false);

        result  = this.getArguments().getString("country");


        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        leagueListView = (ListView) getView().findViewById(R.id.leagueListView);
        getLeagues(result);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            leagueController = (FragmentController) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FragmentController");
        }
    }

    private void setListContent(ArrayList<String> listContent){

        if(getActivity() != null) {
            ArrayAdapter<String> leagueAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, listContent);
            leagueListView.setAdapter(leagueAdapter);

            leagueListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    String selectedLeague = leagueListView.getItemAtPosition(position).toString().trim();
                    Log.d(TAG, selectedLeague);
                    leagueController.ground(selectedLeague);
                }
            });
        }
    }

    public void getLeagues(String country) {

        JSONObject js = new JSONObject();
        try{
            js.put("country", country);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST,LEAGUE_URL, js,
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

        }


    }


    private ArrayList createArrayList(JSONObject response)
    {
        ArrayList<String> responseList = new ArrayList<>();
        try{
            JSONArray listArray = response.getJSONArray("leagues");
            for(int i = 0; i < listArray.length();i++)
            {
                JSONObject row = listArray.getJSONObject(i);

                String name = row.getString("league_name");

                responseList.add(name);
                Log.d(TAG, name);
            }
        } catch (JSONException e)
        {

        }


        return responseList;
    }
}
