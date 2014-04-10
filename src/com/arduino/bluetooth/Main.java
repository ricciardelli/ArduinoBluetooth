package com.arduino.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class Main extends Activity {

	public static final String ARDUINO_DEVICE = "Arduino";
	public static final String SERIAL_PORT_SERVICE_ID = "00001101-0000-1000-8000-00805f9b34fb";
	public static final String TAG = "ArduinoBluetooth";

	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private BluetoothDevice mBluetoothDevice = null;
	private BluetoothSocket mBluetoothSocket = null;

	private OutputStream mOutputStream;
	private InputStream mInputStream;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		 * TODO Do this!
		 * 1. Open connection 
		 * 2. Send data 
		 * 3. Receive data 
		 * 4. Close connection
		 */

		if (mBluetoothAdapter == null) {
			printError(R.string.error_01);
		}

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetooth, 0);
		}

		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				if (device.getName().equals(ARDUINO_DEVICE)) {
					mBluetoothDevice = device;
					break;
				}
			}
		}

		openConnection();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Opens the BT connection.
	 */
	private void openConnection() {
		UUID uuid = UUID.fromString(SERIAL_PORT_SERVICE_ID);
		try {
			mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
		} catch (IOException e) {
			e.printStackTrace();
			printError(R.string.error_02);
		}
		try {
			mBluetoothSocket.connect();
		} catch (IOException e) {
			e.printStackTrace();
			printError(R.string.error_03);
		}
		try {
			mOutputStream = mBluetoothSocket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			printError(R.string.error_04);
		}
		try {
			mInputStream = mBluetoothSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			printError(R.string.error_05);
		}
	}

	/**
	 * Prints the error message based on the resource parameter.
	 * 
	 * @param resId
	 *            The resource identifier.
	 */
	private void printError(int resId) {
		// TODO if (toastNotification.isEnabled())
		Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
		Log.e(TAG, getString(resId));
	}

}
