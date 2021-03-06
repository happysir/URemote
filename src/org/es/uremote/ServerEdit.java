package org.es.uremote;

import static org.es.uremote.utils.IntentKeys.ACTION_EDIT_SERVER;
import static org.es.uremote.utils.IntentKeys.EXTRA_SERVER_DATA;
import static org.es.uremote.utils.IntentKeys.EXTRA_SERVER_ID;

import org.es.uremote.objects.ServerBuilder;
import org.es.uremote.objects.ServerInfo;
import org.es.uremote.objects.ServerInfo.ConnectionType;
import org.es.uremote.utils.IntentKeys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Cyril Leroux
 *
 */
public class ServerEdit extends Activity {

	private static final String TAG = "EditServer";
	private ServerBuilder mServerBuilder;

	private EditText mServerName;
	private EditText mLocalHost1;
	private EditText mLocalHost2;
	private EditText mLocalHost3;
	private EditText mLocalHost4;
	private EditText mLocalPort;

	private EditText mBroadcast1;
	private EditText mBroadcast2;
	private EditText mBroadcast3;
	private EditText mBroadcast4;

	private EditText mRemoteHost1;
	private EditText mRemoteHost2;
	private EditText mRemoteHost3;
	private EditText mRemoteHost4;
	private EditText mRemotePort;

	private EditText mMacAddress1;
	private EditText mMacAddress2;
	private EditText mMacAddress3;
	private EditText mMacAddress4;
	private EditText mMacAddress5;
	private EditText mMacAddress6;

	private EditText mConnectionTimeout;
	private EditText mReadTimeout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_edit);

		mServerName		= (EditText)findViewById(R.id.server_name);

		mLocalHost1	= (EditText)findViewById(R.id.local_host1);
		mLocalHost2	= (EditText)findViewById(R.id.local_host2);
		mLocalHost3	= (EditText)findViewById(R.id.local_host3);
		mLocalHost4	= (EditText)findViewById(R.id.local_host4);
		mLocalPort	= (EditText)findViewById(R.id.local_port);

		mBroadcast1	= (EditText)findViewById(R.id.broadcast1);
		mBroadcast2	= (EditText)findViewById(R.id.broadcast2);
		mBroadcast3	= (EditText)findViewById(R.id.broadcast3);
		mBroadcast4	= (EditText)findViewById(R.id.broadcast4);

		mRemoteHost1	= (EditText)findViewById(R.id.remote_host1);
		mRemoteHost2	= (EditText)findViewById(R.id.remote_host2);
		mRemoteHost3	= (EditText)findViewById(R.id.remote_host3);
		mRemoteHost4	= (EditText)findViewById(R.id.remote_host4);
		mRemotePort		= (EditText)findViewById(R.id.remote_port);

		mMacAddress1	= (EditText)findViewById(R.id.mac_address1);
		mMacAddress2	= (EditText)findViewById(R.id.mac_address2);
		mMacAddress3	= (EditText)findViewById(R.id.mac_address3);
		mMacAddress4	= (EditText)findViewById(R.id.mac_address4);
		mMacAddress5	= (EditText)findViewById(R.id.mac_address5);
		mMacAddress6	= (EditText)findViewById(R.id.mac_address6);

		mConnectionTimeout	= (EditText)findViewById(R.id.connection_timeout);
		mReadTimeout		= (EditText)findViewById(R.id.read_timeout);

		if (ACTION_EDIT_SERVER.equals(getIntent().getAction())) {
			loadServer(getIntent());
		}
	}

	private void loadServer(Intent data) {
		ServerInfo server = (ServerInfo) getIntent().getParcelableExtra(EXTRA_SERVER_DATA);
		if (server == null) {
			finishActivity(Activity.RESULT_CANCELED);
		}

		loadSimpleData(server);
		splitLocalHost(server);
		splitBroadcast(server);
		splitRemoteHost(server);
		splitMacAddress(server);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_server, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.done:
			mServerBuilder = new ServerBuilder();

			mServerBuilder.setName(getName());
			mServerBuilder.setLocalHost(getLocalHost());
			mServerBuilder.setLocalPort(getLocalPort());
			mServerBuilder.setBroadcast(getBroadcast());
			mServerBuilder.setRemoteHost(getRemoteHost());
			mServerBuilder.setRemotePort(getRemotePort());
			mServerBuilder.setMacAddress(getMacAddress());
			mServerBuilder.setConnectionTimeout(getConnectionTimeout());
			mServerBuilder.setReadTimout(getReadTimeout());
			mServerBuilder.setConnectioType(getConnectionType());

			try {
				ServerInfo server = mServerBuilder.build();
				Intent data = new Intent();
				data.putExtra(EXTRA_SERVER_DATA, server);
				data.putExtra(EXTRA_SERVER_ID, getIntent().getIntExtra(EXTRA_SERVER_ID, -1));
				setResult(RESULT_OK, data);
				finish();

			} catch (Exception e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, e.getMessage());
				}
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			return true;


			//			case R.id.cancel:
			//				setResult(RESULT_CANCELED);
			//				finish();
			//				return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void loadSimpleData(final ServerInfo server) {
		mServerName.setText(server.getName().trim());

		mConnectionTimeout.setText(String.valueOf(server.getConnectionTimeout()));
		mReadTimeout.setText(String.valueOf(server.getReadTimeout()));
	}

	private void splitLocalHost(final ServerInfo server) {
		String parts[] = server.getLocalHost().split("\\.");
		mLocalHost1.setText(parts[0]);
		mLocalHost2.setText(parts[1]);
		mLocalHost3.setText(parts[2]);
		mLocalHost4.setText(parts[3]);

		mLocalPort.setText(String.valueOf(server.getLocalPort()));
	}

	private void splitBroadcast(final ServerInfo server) {
		String parts[] = server.getBroadcast().split("\\.");
		mBroadcast1.setText(parts[0]);
		mBroadcast2.setText(parts[1]);
		mBroadcast3.setText(parts[2]);
		mBroadcast4.setText(parts[3]);

		mLocalPort.setText(String.valueOf(server.getLocalPort()));
	}

	private void splitRemoteHost(final ServerInfo server) {
		String parts[] = server.getRemoteHost().split("\\.");
		mRemoteHost1.setText(parts[0]);
		mRemoteHost2.setText(parts[1]);
		mRemoteHost3.setText(parts[2]);
		mRemoteHost4.setText(parts[3]);

		mRemotePort.setText(String.valueOf(server.getRemotePort()));
	}

	private void splitMacAddress(final ServerInfo server) {
		String parts[] = server.getMacAddress().split("-");
		mMacAddress1.setText(parts[0]);
		mMacAddress2.setText(parts[1]);
		mMacAddress3.setText(parts[2]);
		mMacAddress4.setText(parts[3]);
		mMacAddress5.setText(parts[4]);
		mMacAddress6.setText(parts[5]);
	}

	private String getName() {
		return mServerName.getEditableText().toString();
	}

	private String getLocalHost() {

		StringBuilder sb = new StringBuilder();
		sb.append(mLocalHost1.getEditableText().toString());
		sb.append(".");
		sb.append(mLocalHost2.getEditableText().toString());
		sb.append(".");
		sb.append(mLocalHost3.getEditableText().toString());
		sb.append(".");
		sb.append(mLocalHost4.getEditableText().toString());
		return sb.toString();
	}

	private String getBroadcast() {

		StringBuilder sb = new StringBuilder();
		sb.append(mBroadcast1.getEditableText().toString());
		sb.append(".");
		sb.append(mBroadcast2.getEditableText().toString());
		sb.append(".");
		sb.append(mBroadcast3.getEditableText().toString());
		sb.append(".");
		sb.append(mBroadcast4.getEditableText().toString());
		return sb.toString();
	}

	private int getLocalPort() {
		return Integer.valueOf(mLocalPort.getEditableText().toString());
	}

	private String getRemoteHost() {

		StringBuilder sb = new StringBuilder();
		sb.append(mRemoteHost1.getEditableText().toString());
		sb.append(".");
		sb.append(mRemoteHost2.getEditableText().toString());
		sb.append(".");
		sb.append(mRemoteHost3.getEditableText().toString());
		sb.append(".");
		sb.append(mRemoteHost4.getEditableText().toString());
		return sb.toString();
	}

	private int getRemotePort() {
		return Integer.valueOf(mRemotePort.getEditableText().toString());
	}

	private String getMacAddress() {

		StringBuilder sb = new StringBuilder();
		sb.append(mMacAddress1.getEditableText().toString());
		sb.append("-");
		sb.append(mMacAddress2.getEditableText().toString());
		sb.append("-");
		sb.append(mMacAddress3.getEditableText().toString());
		sb.append("-");
		sb.append(mMacAddress4.getEditableText().toString());
		sb.append("-");
		sb.append(mMacAddress5.getEditableText().toString());
		sb.append("-");
		sb.append(mMacAddress6.getEditableText().toString());
		return sb.toString();
	}

	private int getConnectionTimeout() {
		return Integer.valueOf(mConnectionTimeout.getEditableText().toString());
	}

	private int getReadTimeout() {
		return Integer.valueOf(mReadTimeout.getEditableText().toString());
	}

	private ConnectionType getConnectionType() {
		// TODO load from IHM
		return ConnectionType.LOCAL;
	}
}
