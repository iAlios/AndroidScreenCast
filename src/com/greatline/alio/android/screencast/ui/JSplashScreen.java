package com.greatline.alio.android.screencast.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

@SuppressWarnings("serial")
public class JSplashScreen extends JWindow {
	JLabel label = new JLabel(
			"Great Line AndroidCast Loading...",
			new ImageIcon(
					JFrameMain.class
							.getResource("/com/greatline/alio/android/screencast/resource/icon.png")),
			(int) JLabel.CENTER_ALIGNMENT);

	public JSplashScreen() {
		setLayout(new BorderLayout());
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(label, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
	}

	public void setText(String text) {
		// label.setText(text);
		// pack();
		// setLocationRelativeTo(null);
	}

}
