package com.example.gear360.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.PatternMatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class WifiConnectionManager {

    public interface ConnectionCallback {
        void onConnected();
        void onFailed(String reason);
    }

    public static void connectToGear360(Context context, String password, ConnectionCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            WifiNetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                    .setSsidPattern(new PatternMatcher("Gear 360", PatternMatcher.PATTERN_PREFIX))
                    .setWpa2Passphrase(password)
                    .build();

            NetworkRequest request = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .setNetworkSpecifier(specifier)
                    .build();

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    connectivityManager.bindProcessToNetwork(network);
                    callback.onConnected();
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    callback.onFailed("User cancelled or camera not found.");
                }
            };

            connectivityManager.requestNetwork(request, networkCallback);

        } else {
            Toast.makeText(context, "Auto-connect requires Android 10 or higher.", Toast.LENGTH_SHORT).show();
            callback.onFailed("Unsupported Android version");
        }
    }
}