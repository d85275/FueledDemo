package com.e.fueleddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import android.view.View;
import android.widget.TextView;

import com.e.fueleddemo.dependency_injection.component.AppComponent;
import com.e.fueleddemo.dependency_injection.component.DaggerAppComponent;
import com.e.fueleddemo.dependency_injection.modules.DatabaseModule;
import com.e.fueleddemo.restaurant_model.Venue;
import com.e.fueleddemo.viewmodel.MyViewModel;
import com.e.fueleddemo.viewmodel.MyViewModelFactory;


import java.util.ArrayList;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvRestaurants;
    private RestaurantAdapter restaurantAdapter;

    private MyViewModel viewModel;

    // the visible venues
    private ArrayList<Venue> alVenues = new ArrayList<>();
    private ArrayList<Venue> alHide = new ArrayList<>();

    @Inject
    MyViewModelFactory viewModelFactory;

    ProgressDialog pd;

    private TextView tvMode;
    private TextView tvTitle;

    private static final int MODE_USUAL_LIST = 0;
    private static final int MODE_HIDE_LIST = 1;

    private int btMode = MODE_HIDE_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
        dagger();
        setViewModel();
    }

    private void findViews() {
        rvRestaurants = findViewById(R.id.rvRestaurants);
        tvMode = findViewById(R.id.tvMode);
        tvTitle = findViewById(R.id.tvTitle);

        // show the progress dialog before we get our first data
        pd = new ProgressDialog(this);
        pd.setTitle(R.string.loading);
        pd.show();
    }

    private void setListeners() {
        tvMode.setOnClickListener(this);

        // set swipe action listener
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(rvRestaurants);
    }

    private void dagger() {
        // field injection
        //DaggerAppComponent.create().inject(this);
        AppComponent appComponent = DaggerAppComponent
                .builder()
                .databaseModule(new DatabaseModule(this))
                .build();
        appComponent.inject(this);
    }

    private void setViewModel() {
        // get view model
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyViewModel.class);

        // set observer for data and error messages
        viewModel.getRestaurants().observe(MainActivity.this, resultObserver);
        viewModel.getErrorMessage().observe(MainActivity.this, errorObserver);
    }

    Observer<ArrayList<Venue>> resultObserver = new Observer<ArrayList<Venue>>() {
        @Override
        public void onChanged(ArrayList<Venue> alVenue) {
            // dismiss the progress dialog
            if (pd.isShowing()) {
                pd.dismiss();
            }
            alVenues = alVenue;
            alHide = viewModel.getHide();
            if (restaurantAdapter == null) {
                restaurantAdapter = new RestaurantAdapter(alVenues, MainActivity.this, viewModel);
                setRestaurantAdapter(restaurantAdapter);
            }
        }
    };


    Observer<Integer> errorObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer stringId) {
            AlertDialog alError = new AlertDialog.Builder(MainActivity.this).create();
            alError.setView(getCustomView(getResources().getString(stringId)));

            alError.setButton(AlertDialog.BUTTON_POSITIVE, getText(R.string.bt_retry), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // try to update the data if the user wants to retry
                    viewModel.getRestaurants();
                }
            });
            alError.setButton(AlertDialog.BUTTON_NEGATIVE, getText(R.string.bt_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // do nothing if cancel button is pressed, just dismiss the progress dialog if necessary
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
            });
            alError.show();
        }
    };


    private View getCustomView(String s) {
        TextView errorView = new TextView(MainActivity.this);
        errorView.setText(s);
        errorView.setTextSize(14);
        errorView.setHeight(400);
        errorView.setGravity(Gravity.CENTER);
        errorView.setTextColor(Color.DKGRAY);
        return errorView;
    }

    private void setRestaurantAdapter(RestaurantAdapter adapter) {
        rvRestaurants.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvRestaurants.setLayoutManager(mLayoutManager);
        rvRestaurants.setAdapter(adapter);
    }

    //
    ItemTouchHelper.SimpleCallback touchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            // swipe to delete a restaurant from the list
            if (btMode == MODE_HIDE_LIST) {
                hideRestaurant(viewHolder);
            } else {
                restoreRestaurant(viewHolder);
            }
        }
    };

    private void hideRestaurant(RecyclerView.ViewHolder viewHolder) {
        Venue venue = alVenues.get(viewHolder.getAdapterPosition());
        alVenues.remove(venue);
        alHide.add(venue);
        restaurantAdapter.notifyDataSetChanged();

        // add the hidden restaurant to the database
        viewModel.addHide(venue);
    }

    private void restoreRestaurant(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        Venue venue = alHide.get(position);
        alVenues.add(venue);
        alHide.remove(venue);
        restaurantAdapter.notifyDataSetChanged();

        // delete the hidden restaurant to the database
        viewModel.deleteHide(position);
    }

    @Override
    public void onClick(View view) {
        if (btMode == MODE_HIDE_LIST) {
            btMode = MODE_USUAL_LIST;
            restaurantAdapter.setAlVenues(alHide, btMode);
            tvMode.setText(getResources().getString(R.string.nearby));
            tvTitle.setText(getResources().getString(R.string.hidden_restaurants));
        } else {
            btMode = MODE_HIDE_LIST;
            restaurantAdapter.setAlVenues(alVenues, btMode);
            tvMode.setText(getResources().getString(R.string.hidden));
            tvTitle.setText(getResources().getString(R.string.nearby_restaurants));
        }
    }
}
