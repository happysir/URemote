package org.es.uremote.computer;


import static android.app.Activity.RESULT_OK;
import static android.view.HapticFeedbackConstants.VIRTUAL_KEY;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import org.es.uremote.R;
import org.es.uremote.network.AsyncMessageMgr;
import org.es.uremote.utils.IntentKeys;
import org.es.uremote.utils.Message;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Cyril Leroux
 * 
 * Classe permettant de se connecter et d'envoyer des commandes � un serveur distant via une AsyncTask.
 *
 */
public class FragDashboard extends Fragment implements OnClickListener {
	// Liste des RequestCodes pour les ActivityForResults
	private static final int RC_APP_LAUNCHER	= 0;
	private static final int STATE_KO	= 0;
	private static final int STATE_OK	= 1;
	private static final int STATE_CONNECTING	= 2;
	private ImageButton mCmdMute;

	private TextView mTvServerState;
	private ProgressBar mPbConnection;

	/** 
	 * Cette fonction est appel�e lors de la cr�ation de l'activit�
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.server_dashboard_frag, container, false);

		mTvServerState = (TextView) view.findViewById(R.id.tvServerState);
		mPbConnection = (ProgressBar) view.findViewById(R.id.pbConnection);

		((Button) view.findViewById(R.id.cmdTest)).setOnClickListener(this);
		((Button) view.findViewById(R.id.cmdKillServer)).setOnClickListener(this);
		((Button) view.findViewById(R.id.cmdSwitch)).setOnClickListener(this);
		((Button) view.findViewById(R.id.cmdGomStretch)).setOnClickListener(this);
		((Button) view.findViewById(R.id.btnAppLauncher)).setOnClickListener(this);

		((ImageButton) view.findViewById(R.id.cmdPrevious)).setOnClickListener(this);
		((ImageButton) view.findViewById(R.id.cmdPlayPause)).setOnClickListener(this);
		((ImageButton) view.findViewById(R.id.cmdStop)).setOnClickListener(this);
		((ImageButton) view.findViewById(R.id.cmdNext)).setOnClickListener(this);
		mCmdMute = (ImageButton) view.findViewById(R.id.cmdMute);
		mCmdMute.setOnClickListener(this);
		
		((TextView) view.findViewById(R.id.tvServerInfos)).setText(MessageMgr.getServerInfos());

		return view;
	}

	/**
	 * Prise en comptes des �v�nements onClick 
	 */
	@Override
	public void onClick(View _view) {
		_view.performHapticFeedback(VIRTUAL_KEY);

		switch (_view.getId()) {

		case R.id.cmdKillServer :
			askToKillServer();
			break;
		case R.id.cmdTest :
			sendAsyncMessage(Message.TEST_COMMAND);
			break;
		case R.id.cmdSwitch :
			sendAsyncMessage(Message.MONITOR_SWITCH_WINDOW);
			break;
		case R.id.btnAppLauncher :
			startActivityForResult(new Intent(getActivity().getApplicationContext(), AppLauncher.class), RC_APP_LAUNCHER);
			break;
		case R.id.cmdGomStretch :
			sendAsyncMessage(Message.GOM_PLAYER_STRETCH);
			break;

		case R.id.cmdPrevious :
			sendAsyncMessage(Message.MEDIA_PREVIOUS);
			break;
		case R.id.cmdPlayPause :
			sendAsyncMessage(Message.MEDIA_PLAY_PAUSE);
			break;
		case R.id.cmdStop :
			sendAsyncMessage(Message.MEDIA_STOP);
			break;
		case R.id.cmdNext :
			sendAsyncMessage(Message.MEDIA_NEXT);
			break;
		case R.id.cmdMute :
			sendAsyncMessage(Message.VOLUME_MUTE);
			break;
		default:
			break;
		}
	}
	
	/** 
	 * Gestion des actions en fonction du code de retour renvoy� apr�s un StartActivityForResult.
	 * 
	 * @param _requestCode Code d'identification de l'activit� appel�e.
	 * @param _resultCode Code de retour de l'activit� (RESULT_OK/RESULT_CANCEL).
	 * @param _data Les donn�es renvoy�es par l'application.
	 */
	@Override
	public void onActivityResult(int _requestCode, int _resultCode, Intent _data) {	// R�sultat de l'activit� Application Launcher
		if (_requestCode == RC_APP_LAUNCHER && _resultCode == RESULT_OK) {
			final String message = _data.getStringExtra(IntentKeys.APPLICATION_MESSAGE);
			sendAsyncMessage(message);
		} 
	}
	
	/** Demande une confirmation � l'utilisateur avant de tuer le serveur */
	private void askToKillServer() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(android.R.drawable.ic_menu_more);
		builder.setMessage(R.string.confirm_kill_server);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// Envoi du message si l'utilisateur confirme
				sendAsyncMessage(Message.KILL_SERVER);
			}
		});

		builder.setNegativeButton(android.R.string.cancel, null);
		builder.show();
	}

	/**
	 * Fonction de mise � jour de l'interface utilisateur
	 * @param _state L'�tat � mettre � jour (OK, KO, CONNECTING)
	 */
	private void updateConnectionState(int _state) {
		int drawableResId;
		int messageResId;
		int visibility;
		
		switch (_state) {
		case STATE_OK:
			drawableResId = android.R.drawable.presence_online; 
			messageResId = R.string.msg_command_succeeded;
			visibility = INVISIBLE;
			break;
			
		case STATE_CONNECTING:
			drawableResId = android.R.drawable.presence_away; 
			messageResId = R.string.msg_command_running;
			visibility = VISIBLE;
			break;

		default: // KO
			drawableResId = android.R.drawable.presence_offline; 
			messageResId = R.string.msg_command_failed;
			visibility = INVISIBLE;
			break;
		}
		final Drawable imgLeft = getResources().getDrawable(drawableResId);
		imgLeft.setBounds(0, 0, 24, 24);
		mTvServerState.setCompoundDrawables(imgLeft, null, null, null);
		mTvServerState.setText(messageResId);
		mPbConnection.setVisibility(visibility);
	}

	/**
	 * Cette fonction initialise le composant g�rant l'envoi des messages 
	 * puis envoie le message pass� en param�tre.
	 * @param _message Le message � envoyer.
	 */
	private void sendAsyncMessage(String _message) {
		if (MessageMgr.availablePermits() > 0) { 
			new MessageMgr().execute(_message);
		} else {
			Toast.makeText(getActivity().getApplicationContext(), "No more permit available !", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Classe asynchrone de gestion d'envoi des messages au serveur
	 * @author cyril.leroux
	 */
	private class MessageMgr extends AsyncMessageMgr {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			updateConnectionState(STATE_CONNECTING);
		}

		@Override
		protected void onPostExecute(String _serverReply) {
			super.onPostExecute(_serverReply);

			if (_serverReply != null && !_serverReply.isEmpty()) {
				Toast.makeText(getActivity().getApplicationContext(), _serverReply, Toast.LENGTH_SHORT).show();

				if (_serverReply.equals(Message.REPLY_VOLUME_MUTED)) {
					mCmdMute.setImageResource(R.drawable.volume_muted);
					
				} else if (_serverReply.equals(Message.REPLY_VOLUME_ON)) {
					mCmdMute.setImageResource(R.drawable.volume_on);
					
				}

				updateConnectionState(STATE_OK);
			} else {
				updateConnectionState(STATE_KO);
			}
		}
	}
}
