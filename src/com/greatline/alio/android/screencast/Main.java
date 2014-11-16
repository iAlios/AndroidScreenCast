package com.greatline.alio.android.screencast;

import java.io.IOException;


import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.greatline.alio.android.screencast.api.injector.Injector;
import com.greatline.alio.android.screencast.app.SwingApplication;
import com.greatline.alio.android.screencast.ui.JDialogDeviceList;
import com.greatline.alio.android.screencast.ui.JFrameMain;
import com.greatline.alio.android.screencast.ui.JSplashScreen;

public class Main extends SwingApplication {

	JFrameMain jFrameMain;
	Injector injector;
	IDevice device;
	
	public Main(boolean nativeLook) throws IOException {
		super(nativeLook);
		JSplashScreen jSplashScreen = new JSplashScreen();
		try {
			initialize(jSplashScreen);
		} finally {
			jSplashScreen.setVisible(false);
			jSplashScreen = null;
		}
	}
	
	private void initialize(JSplashScreen jSplashScreen) throws IOException {
		jSplashScreen.setVisible(true);
		
		AndroidDebugBridge bridge = AndroidDebugBridge.createBridge();
		waitDeviceList(bridge);

		IDevice devices[] = bridge.getDevices();
		// Let the user choose the device
		if(devices.length == 1) {
			device = devices[0];
		} else {
			JDialogDeviceList jDialogDeviceList = new JDialogDeviceList(devices);
			jDialogDeviceList.setVisible(true);
			device = jDialogDeviceList.getDevice();
		}
		if(device == null) {
			System.exit(0);
			return;
		}
		
		// Start showing the device screen
		jFrameMain = new JFrameMain(device);
		jFrameMain.setTitle(""+device);
		jFrameMain.setVisible(true);
		
		// Starting injector
//		jSplashScreen.setText("Starting input injector...");
//		jSplashScreen.setVisible(true);

		injector = new Injector(device);
		injector.start();
		jFrameMain.setInjector(injector);
		jSplashScreen.setVisible(false);	
	}

	
	private void waitDeviceList(AndroidDebugBridge bridge) {
		int count = 0;
		while (bridge.hasInitialDeviceList() == false) {
			try {
				Thread.sleep(100);
				count++;
			} catch (InterruptedException e) {
				// pass
			}
			// let's not wait > 10 sec.
			if (count > 300) {
				throw new RuntimeException("Timeout getting device list!");
			}
		}
	}
	
	protected void close() {
		System.out.println("cleaning up...");
		if(injector != null)
			injector.close();
	
		if(device != null) {
			synchronized (device) {
				AndroidDebugBridge.terminate();
			}
		}
		System.out.println("cleanup done, exiting...");
		super.close();
	}

	public static void main(String args[]) throws IOException {
		boolean nativeLook = args.length == 0 || !args[0].equalsIgnoreCase("nonativelook");
		new Main(nativeLook);
	}

}
