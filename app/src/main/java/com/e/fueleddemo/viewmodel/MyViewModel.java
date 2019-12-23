package com.e.fueleddemo.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.bumptech.glide.Glide;
import com.e.fueleddemo.R;
import com.e.fueleddemo.database.HideRestaurant;
import com.e.fueleddemo.remote.JavaScriptUtils;
import com.e.fueleddemo.repository.DataRepository;
import com.e.fueleddemo.restaurant_model.Icon;
import com.e.fueleddemo.restaurant_model.SearchResult;
import com.e.fueleddemo.restaurant_model.Venue;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MyViewModel extends ViewModel {

    private MutableLiveData<Integer> _errorMessage = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Venue>> _venues = new MutableLiveData<>();

    private DataRepository repository;
    private List<HideRestaurant> alDbHideRestaurants = new ArrayList<>();

    @Inject
    public MyViewModel(DataRepository repository) {
        this.repository = repository;

    }


    public LiveData<ArrayList<Venue>> getRestaurants() {
        loadHide();
        return _venues;
    }

    public LiveData<Integer> getErrorMessage() {
        return _errorMessage;
    }

    private void loadHide() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(repository.getHide()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<HideRestaurant>>() {
                    @Override
                    public void onSuccess(List<HideRestaurant> list) {
                        alDbHideRestaurants = list;
                        loadVenues();
                    }

                    @Override
                    public void onError(Throwable e) {
                        _errorMessage.setValue(R.string.error);

                    }
                }));
    }

    private void loadVenues() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(repository.venueSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SearchResult>() {
                    @Override
                    public void onSuccess(SearchResult result) {
                        _venues.setValue(getVisibleRestaurants(result.getResponse().getVenues()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isSameCause(e, UnknownHostException.class)) {
                            // the error for possibly no network connection
                            _errorMessage.setValue(R.string.no_network);
                        } else {
                            // other error messages
                            _errorMessage.setValue(R.string.error);
                        }

                    }
                }));
    }

    private boolean isSameCause(Throwable e, Class<? extends Throwable> cl) {
        return cl.isInstance(e) || e.getCause() != null && isSameCause(e.getCause(), cl);
    }

    private ArrayList<Venue> getVisibleRestaurants(ArrayList<Venue> alVenues) {
        ArrayList<Integer> idx = new ArrayList<>();
        int i = 0;
        while (i < alDbHideRestaurants.size()) {
            for (int j = 0; j < alVenues.size(); j++) {
                String address1 = alDbHideRestaurants.get(i).venue.getLocation().toString();
                String address2 = alVenues.get(j).getLocation().toString();
                if (address1.equals(address2)) {
                    alVenues.remove(j);
                    idx.add(j);
                    break;
                }
            }
            i++;
        }
        return alVenues;
    }

    public ArrayList<Venue> getHide() {
        ArrayList<Venue> alHide = new ArrayList<>();
        for (int i = 0; i < alDbHideRestaurants.size(); i++) {
            Venue venue = alDbHideRestaurants.get(i).venue;
            alHide.add(venue);
        }
        return alHide;
    }

    // add the hidden restaurant to the database
    public void addHide(final Venue venue) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HideRestaurant hideRestaurant = new HideRestaurant();
                hideRestaurant.venue = venue;
                repository.addHide(hideRestaurant);
                alDbHideRestaurants.add(hideRestaurant);

            }
        }).start();
    }

    // delete the hidden restaurant to the database
    public void deleteHide(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("123", "delete: " + position);
                repository.deleteHide(alDbHideRestaurants.get(position));
                alDbHideRestaurants.remove(position);
            }
        }).start();

    }

    // for adapter
    public String getAddress(Venue venue, Context ctx) {
        String address = venue.getLocation().getAddress();
        String postCode = venue.getLocation().getPostalCode();
        return String.format(ctx.getResources().getString(R.string.address), address, postCode);
    }


    public String getIconUrl(Venue venue) {
        Icon icon = venue.getCategories().get(0).getIcon();
        return icon.getPrefix() + "64" + icon.getSuffix();
    }


    public AlertDialog getGoDialog(final Venue venue, Context ctx) {
        AlertDialog adGo = new AlertDialog.Builder(ctx).create();
        adGo.setView(getCustomView(venue, ctx));
        adGo.setButton(AlertDialog.BUTTON_POSITIVE, ctx.getText(R.string.bt_go), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                navigation(venue, ctx);
            }
        });
        adGo.setButton(AlertDialog.BUTTON_NEGATIVE, ctx.getText(R.string.bt_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return adGo;
    }


    // use Google maps to navigate from the office to the restaurant
    private void navigation(Venue venue, Context ctx) {
        String departure = "Fueled | London, Liverpool Street, London";
        String destination = venue.getLocation().getLat() + "," + venue.getLocation().getLng() + "(" + venue.getName() + ")";
        String uri = "http://maps.google.com/maps?saddr=" + departure + "&daddr=" + destination;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(ctx.getPackageManager()) != null) {
            ctx.startActivity(intent);
        } else {
            Toast.makeText(ctx, ctx.getText(R.string.navigate_error), Toast.LENGTH_SHORT).show();
        }
    }

    private View getCustomView(Venue venue, Context ctx) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.show_location, null);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        ImageView ivLogo = view.findViewById(R.id.ivLogo);
        WebView wvLocation = view.findViewById(R.id.wvLocation);
        setWebView(wvLocation, venue.getLocation().getLat(), venue.getLocation().getLng(), ctx);
        tvName.setText(venue.getName());
        tvAddress.setText(getAddress(venue, ctx));
        Glide.with(ctx).load(getIconUrl(venue)).into(ivLogo);
        return view;
    }

    private void setWebView(WebView wvMap, double lat, double lng, Context ctx) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        wvMap.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (height * 0.5)));
        wvMap.getSettings().setJavaScriptEnabled(true);
        wvMap.setWebChromeClient(getWebListener(ctx));
        wvMap.addJavascriptInterface(new JavaScriptUtils(lat, lng), "Android");
        wvMap.loadUrl("file:///android_asset/showLocation.html");
    }

    private static WebChromeClient getWebListener(Context ctx) {
        return new WebChromeClient() {
            ProgressDialog pd;

            public void onProgressChanged(WebView webView, int progress) {
                if (progress < 100) {
                    if (pd == null) {
                        pd = new ProgressDialog(ctx);
                        pd.show();
                    }
                } else if (progress == 100 && pd.isShowing()) {
                    Log.e("1", "done");
                    pd.dismiss();
                }
            }
        };
    }

    public boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
