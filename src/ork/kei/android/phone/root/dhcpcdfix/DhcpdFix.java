package ork.kei.android.phone.root.dhcpcdfix;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

/**
 *******************************************************************************
 * @file DhcpdFix.java
 * @author Keidan
 * @date 27/08/2015
 *
 *       Term of the license in in the file license.txt.
 *
 *******************************************************************************
 */
public class DhcpdFix extends Activity {
  private final static String DIR      = "/data/misc/dhcp/";
  private final static String FPID     = "dhcpcd-wlan0.pid";
  private final static String FLEASE   = "dhcpcd-wlan0.lease";
  private TextView            tvStatus = null;
  private Button              btFix    = null;

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    final TextView tvRoot = (TextView) findViewById(R.id.rootStatusTV);
    tvStatus = (TextView) findViewById(R.id.fixStatusTV);
    btFix = (Button)findViewById(R.id.fixBT);
    
    if (RootTools.isAccessGiven()) {
      tvStatus.setVisibility(View.VISIBLE);
      updatTv(tvRoot, R.string.rootStatusOK, true);
      if (!RootTools.exists(DIR + FPID) && !RootTools.exists(DIR + FLEASE)) {
        updatTv(tvStatus, R.string.fixStatusOK, true);
        buttonEnable(btFix, false);
      } else {
        updatTv(tvStatus, R.string.fixStatusNOK, false);
        buttonEnable(btFix, true);
      }
    } else {
      buttonEnable(btFix, false);
      tvStatus.setVisibility(View.GONE);
      updatTv(tvRoot, R.string.rootStatusNOK, false);
    }
  }

  public void actionFix(final View v) {
    if (RootTools.isAccessGiven()) {
      final Command command = new Command(0, "cd " + DIR, "rm -f " + FPID + " "
          + FLEASE);
      try {
        RootTools.getShell(true).add(command);
        if (!RootTools.exists(DIR + FPID) && !RootTools.exists(DIR + FLEASE)) {
          updatTv(tvStatus, R.string.fixStatusOK, true);
          displayMessage(R.string.success);
          buttonEnable(btFix, false);
        } else {
          displayMessage(R.string.removeFailed);
        }
      } catch (final IOException e) {
        displayMessage(e.getMessage());
      } catch (final TimeoutException e) {
        displayMessage(e.getMessage());
      } catch (final RootDeniedException e) {
        displayMessage(e.getMessage());
      }
    } else
      displayMessage(R.string.noRoot);
  }
  
  private void buttonEnable(Button bt, final boolean en) {
    bt.setEnabled(en);
    bt.setClickable(en);
  }

  /* Update a TextView */
  private void updatTv(final TextView tv, final int txt, final boolean ok) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        tv.setText(txt);
        tv.setCompoundDrawablesWithIntrinsicBounds(
            !ok ? R.drawable.red_flat_sing_ko : R.drawable.green_flat_sing_ok,
            0, 0, 0);
      }
    });
  }

  /* Display Toast messages */
  private void displayMessage(final String msg) {
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
  }

  private void displayMessage(final int msg) {
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
  }
}