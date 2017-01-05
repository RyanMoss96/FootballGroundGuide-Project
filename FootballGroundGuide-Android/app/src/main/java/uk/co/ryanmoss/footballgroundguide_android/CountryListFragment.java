package uk.co.ryanmoss.footballgroundguide_android;

import android.app.Activity;
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


public class CountryListFragment extends Fragment {



    //private String COUNTRY_URL = "http://46.101.2.231/FootballGroundGuide/get_country_list.php";
    private String COUNTRY_URL = "http://178.62.121.73/countries";
    private static final String TAG = "CountryFragment";
    ListView groundListView;
    FragmentController leagueController;


    public CountryListFragment() {
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
        return inflater.inflate(R.layout.fragment_country_list, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        groundListView = (ListView) getView().findViewById(R.id.FullGroundListView);
        getCountries();
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


    public void setListContent(ArrayList<String> listContent)
    {

        if(getActivity() != null)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, listContent);
            groundListView.setAdapter(adapter);

            groundListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {



                    String selectedCountry = groundListView.getItemAtPosition(position).toString().trim();
                    Log.d(TAG, selectedCountry);
                    leagueController.league(selectedCountry);


                }
            });
        }




    }

    public void getCountries() {

        JSONObject js = new JSONObject();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,COUNTRY_URL, js,
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


    }


    private ArrayList createArrayList(JSONObject response)
    {
        ArrayList<String> responseList = new ArrayList<>();
        try{
            JSONArray listArray = response.getJSONArray("country");
            for(int i = 0; i < listArray.length();i++)
            {
                JSONObject row = listArray.getJSONObject(i);
                int id = row.getInt("country_id");
                String name = row.getString("country_name");

                responseList.add(name);
                Log.d(TAG, name);
            }
        } catch (JSONException e)
        {

        }


        return responseList;
    }
}
