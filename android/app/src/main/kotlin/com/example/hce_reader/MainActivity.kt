package com.example.hce_reader

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import android.nfc.NfcAdapter;
import android.content.Intent;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.example.my_flutter_app/nfc";
    private static final String TAG = "NFCReader";
    private NfcAdapter nfcAdapter;

    @Override
    public void configureFlutterEngine(FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            if (call.method.equals("startNfc")) {
                                startNfc();
                            } else {
                                result.notImplemented();
                            }
                        }
                );
    }

    private void startNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) {
                Log.d(TAG, "NFC is not enabled.");
                return;
            }
            Intent intent = new Intent(this, getClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            nfcAdapter.enableForegroundDispatch(this, intent, null, null);
        } else {
            Log.d(TAG, "NFC is not available on this device.");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            IsoDep isoDep = IsoDep.get(tag);
            if (isoDep != null) {
                try {
                    isoDep.connect();
                    byte[] response = isoDep.transceive(new byte[]{0x00, (byte) 0xA4, 0x04, 0x00, 0x07, (byte) 0xD2, 0x76, 0x00, 0x00, 0x85, 0x01, 0x01, 0x00});
                    Log.d(TAG, "Received response: " + bytesToHex(response));
                } catch (Exception e) {
                    Log.e(TAG, "Error communicating with NFC card: " + e.getMessage());
                } finally {
                    try {
                        isoDep.close();
                    } catch (Exception e) {
                        Log.e(TAG, "Error closing IsoDep: " + e.getMessage());
                    }
                }
            }
        }
    }

    // Helper method to convert byte array to hex string
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
