package org.es.uremote;

import static android.view.HapticFeedbackConstants.VIRTUAL_KEY;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * The dashboard class that leads everywhere in the application.
 *
 * @author Cyril Leroux
 *
 */
public class Home extends Activity implements OnClickListener {

	// The request codes of ActivityForResults
	private static final int RC_ENABLE_BT	= 0;
	private static final int RC_ENABLE_WIFI	= 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// Set up a click listener for each button
		((ImageButton) findViewById(R.id.btnLights)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.btnTv)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.btnRobot)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.btnMediaCenter)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.btnHifi)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.btnTools)).setOnClickListener(this);
	}

	@Override
	public void onClick(View _view) {
		_view.performHapticFeedback(VIRTUAL_KEY);

		switch (_view.getId()) {
		case R.id.btnLights:
			Toast.makeText(Home.this, getString(R.string.msg_light_control_not_available), Toast.LENGTH_SHORT).show();
			break;

		case R.id.btnTv:
			startActivity(new Intent(getApplicationContext(), TvDialer.class));
			break;

		case R.id.btnRobot:
			startRobotControl();
			break;

		case R.id.btnMediaCenter:
			startServerTabHost();
			break;

		case R.id.btnHifi:
			Toast.makeText(Home.this, getString(R.string.msg_hifi_control_not_available), Toast.LENGTH_SHORT).show();
			break;

		case R.id.btnTools:
			startActivity(new Intent(getApplicationContext(), AppSettings.class));
			//Toast.makeText(Home.this, getString(R.string.msg_settings_control_not_available), Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	private void startRobotControl () {
		// Si le Bluetooth n'est pas activ�, demander � l'activer
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, RC_ENABLE_BT);
		} else {
			startActivity(new Intent(getApplicationContext(), RobotControl.class));
		}
	}

	private void startServerTabHost() {
		// Si le Wifi n'est pas activ�, demander son activation.
		final WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if (!wifiMgr.isWifiEnabled()) {
			Intent enableIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
			startActivityForResult(enableIntent, RC_ENABLE_WIFI);
		} else {
			startActivity(new Intent(getApplicationContext(), ServerControl.class));
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {

		// Retour de la requ�te d'activation du Bluetooth
		case RC_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
				// Lancement de l'activit� de contr�le des robots si le bluetooth est activ�
				startActivity(new Intent(getApplicationContext(), RobotControl.class));

			} else { // Si le Bluetooth n'est pas activ� on n'ouvre pas l'activit�
			}
			break;

			// Retour de la requ�te d'activation du wifi
		case RC_ENABLE_WIFI:
			startActivity(new Intent(getApplicationContext(), ServerControl.class));
			// TODO
			//			final WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
			//			// Si l'utilisateur a activ� le Wifi, on ouvre l'activit�
			//			if (wifiMgr.isWifiEnabled()) {
			//				startActivity(new Intent(getApplicationContext(), ServerTabHost.class));
			//			}
			break;
		}
	}
}