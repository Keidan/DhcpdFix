package ork.kei.android.phone.root.dhcpcdfix;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 *******************************************************************************
 * @file DhcpdFix.java
 * @author Keidan
 * @date 27/08/2015
 *
 * Term of the license in in the file license.txt.
 *
 *******************************************************************************
 */
public class DhcpdFix extends Activity {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
  }

  public void actionFix(View v) {
    if (RootTools.isAccessGiven()) {
      final Command command = new Command(0, "cd /data/misc/dhcp", "rm -f dhcpcd-wlan0.lease dhcpcd-wlan0.pid");
      try {
        RootTools.getShell(true).add(command);
        displayMessage(R.string.success);
      } catch (final IOException e) {
        displayMessage(e.getMessage());
      } catch (final TimeoutException e) {
        displayMessage(e.getMessage());
      } catch (final RootDeniedException e) {
        displayMessage(e.getMessage());
      }
    } else
      displayMessage(R.string.no_root);
  }
  
  private void displayMessage(final String msg) {
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
  }

  private void displayMessage(final int msg) {
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
  }
}