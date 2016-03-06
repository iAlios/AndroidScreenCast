package com.greatline.alio.android.screencast.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.android.ddmlib.IDevice;
import com.greatline.alio.android.screencast.api.AndroidDevice;
import com.greatline.alio.android.screencast.api.injector.ConstEvtKey;
import com.greatline.alio.android.screencast.api.injector.ConstEvtMotion;
import com.greatline.alio.android.screencast.api.injector.Injector;
import com.greatline.alio.android.screencast.api.injector.ScreenCaptureThread.ScreenCaptureListener;
import com.greatline.alio.android.screencast.ui.explorer.JFrameExplorer;
import com.greatline.alio.android.screencast.util.KeyCodeUtil;

@SuppressWarnings("serial")
public class JFrameMain extends JFrame {

	private JScrollPane jScrollPane;
	private IDevice device;
	private Injector injector;
	private Dimension oldImageDimension = null;
	private JPanelScreen jPanelScreen = new JPanelScreen();
	private JToolBar jToolBar = new JToolBar();
	private JToolBar jToolBarHardkeys = new JToolBar();

	private JToggleButton jtbRecord = new JToggleButton("Record");
	private JButton jbOpenUrl = new JButton("Open Url");
	private JButton jbExplorer = new JButton("Explore");
	private JButton jbScreenCast = new JButton("ScreenCast");
	private JButton jbKbPower = new JButton("Power");
	private JButton jbKbHome = new JButton("Home");
	private JButton jbKbMenu = new JButton("Menu");
	private JButton jbKbBack = new JButton("Back");
	private JButton jbKbSearch = new JButton("Search");
	private JButton jbKbPhoneOn = new JButton("Call");
	private JButton jbKbPhoneOff = new JButton("End call");
	private JButton jbPaint = new JButton("Paint");

	private JButton jbKbLeft = new JButton("LSwipe");
	private JButton jbKbRight = new JButton("RSwipe");

	private JButton jbKbUp = new JButton("USwipe");
	private JButton jbKbDown = new JButton("DSwipe");
	
	private JButton jbKbRotate = new JButton("Rotate");

	private JPaintPanel jPaintPanel;

	private static final String MAINPANEL = "Main Panel";
	private static final String PAINTPANEL = "Paint Panel";

	public JFrameMain(IDevice device) throws IOException {
		this.device = device;
		initialize();
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {

					public boolean dispatchKeyEvent(KeyEvent e) {
						if (!JFrameMain.this.isActive())
							return false;
						if (injector == null)
							return false;
						if (e.getID() == KeyEvent.KEY_PRESSED) {
							int code = KeyCodeUtil.getKeyCode(e);
							injector.injectKeycode(ConstEvtKey.ACTION_DOWN,
									code);
						}
						//if (e.getID() == KeyEvent.KEY_RELEASED) {
						//	int code = KeyCodeUtil.getKeyCode(e);
						//	injector.injectKeycode(ConstEvtKey.ACTION_UP, code);
						//}
						return false;
					}
				});
	}

	public void initialize() throws IOException {
		jToolBar.setFocusable(false);
		jbExplorer.setFocusable(false);
		jbScreenCast.setFocusable(false);
		jtbRecord.setFocusable(false);
		jbOpenUrl.setFocusable(false);
		jbPaint.setFocusable(false);

		jbKbHome.setFocusable(false);
		jbKbPower.setFocusable(false);
		jbKbMenu.setFocusable(false);
		jbKbBack.setFocusable(false);
		jbKbSearch.setFocusable(false);
		jbKbPhoneOn.setFocusable(false);
		jbKbPhoneOff.setFocusable(false);

		jbKbLeft.setFocusable(false);
		jbKbRight.setFocusable(false);

		jbKbUp.setFocusable(false);
		jbKbDown.setFocusable(false);
		jbKbRotate.setFocusable(false);

		jbKbLeft.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				injector.injectSwap(10, 100, 400, 100);
			}

		});
		
		jbKbRotate.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				injector.screencapture.toogleOrientation();
			}
			
		});
		jbKbRight.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				injector.injectSwap(400, 100, 10, 100);
			}

		});
		jbKbUp.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				injector.injectSwap(100, jPanelScreen.getHeight() / 3, 100,
						jPanelScreen.getHeight() * 2 / 3);
			}

		});
		jbKbDown.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				injector.injectSwap(100, jPanelScreen.getHeight() * 2 / 3, 100,
						jPanelScreen.getHeight() / 3);
			}

		});
		jbKbPower.addActionListener(new KbClickActionListener(
				ConstEvtKey.KEYCODE_POWER));
		jbKbHome.addActionListener(new KbClickActionListener(
				ConstEvtKey.KEYCODE_HOME));
		jbKbMenu.addActionListener(new KbClickActionListener(
				ConstEvtKey.KEYCODE_MENU));
		jbKbBack.addActionListener(new KbClickActionListener(
				ConstEvtKey.KEYCODE_BACK));
		jbKbSearch.addActionListener(new KbClickActionListener(
				ConstEvtKey.KEYCODE_SEARCH));
		jbKbPhoneOn.addActionListener(new KbClickActionListener(
				ConstEvtKey.KEYCODE_CALL));
		jbKbPhoneOff.addActionListener(new KbClickActionListener(
				ConstEvtKey.KEYCODE_ENDCALL));

		jToolBarHardkeys.add(jbKbPower);
		jToolBarHardkeys.add(jbKbHome);
		jToolBarHardkeys.add(jbKbMenu);
		jToolBarHardkeys.add(jbKbBack);
		jToolBarHardkeys.add(jbKbSearch);
		jToolBarHardkeys.add(jbKbPhoneOn);
		jToolBarHardkeys.add(jbKbPhoneOff);
		jToolBarHardkeys.add(jbKbLeft);
		jToolBarHardkeys.add(jbKbRight);
		jToolBarHardkeys.add(jbKbUp);
		jToolBarHardkeys.add(jbKbDown);
		jToolBarHardkeys.add(jbKbRotate);

		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						getClass()
								.getResource(
										"/com/greatline/alio/android/screencast/resource/android.png")));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(jToolBar, BorderLayout.NORTH);
		add(jToolBarHardkeys, BorderLayout.SOUTH);
		JPanel cards = new JPanel();
		cards.setLayout(new CardLayout());
		jScrollPane = new JScrollPane(cards);

		jPaintPanel = new JPaintPanel();
		cards.add(jPanelScreen, MAINPANEL);
		cards.add(jPaintPanel, PAINTPANEL);
		jScrollPane.setPreferredSize(new Dimension(100, 100));
		add(jScrollPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);

		MouseAdapter mouseAdapter = new MouseAdapter() {

			Point mLastMouseEvent = null;

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (injector == null)
					return;
				Point p2 = jPanelScreen.getRawPoint(arg0.getPoint());
				injector.injectTap(p2.x, p2.y);
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				if (injector == null)
					return;
				try {
					Point p2 = jPanelScreen.getRawPoint(arg0.getPoint());
					injector.injectMouse(ConstEvtMotion.ACTION_MOVE, p2.x, p2.y);

					injector.injectSwap(mLastMouseEvent.x, mLastMouseEvent.y,
							p2.x, p2.y);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				if (injector == null)
					return;
				try {
					Point p2 = jPanelScreen.getRawPoint(arg0.getPoint());
					injector.injectMouse(ConstEvtMotion.ACTION_DOWN, p2.x, p2.y);
					mLastMouseEvent = p2;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (injector == null)
					return;
				try {
					if (arg0.getButton() == MouseEvent.BUTTON3) {
						injector.screencapture.toogleOrientation();
						arg0.consume();
						return;
					}
					Point p2 = jPanelScreen.getRawPoint(arg0.getPoint());
					injector.injectMouse(ConstEvtMotion.ACTION_UP, p2.x, p2.y);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if (injector == null)
					return;
				try {
					injector.injectTrackball(arg0.getWheelRotation() < 0 ? -1f
							: 1f);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};

		jPanelScreen.addMouseMotionListener(mouseAdapter);
		jPanelScreen.addMouseListener(mouseAdapter);
		jPanelScreen.addMouseWheelListener(mouseAdapter);

		jtbRecord.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (jtbRecord.isSelected()) {
					startRecording();
				} else {
					stopRecording();
				}
			}

		});
		jToolBar.add(jtbRecord);

		jbPaint.addActionListener(new ActionListener() {

			private JPanel cards;

			public ActionListener setCardLayout(JPanel cards) {
				this.cards = cards;
				return this;
			}

			public void actionPerformed(ActionEvent arg0) {
				CardLayout cardLayout = (CardLayout) cards.getLayout();
				if (jbPaint.getText().equals("Paint")) {
					cardLayout.show(cards, PAINTPANEL);
					jbPaint.setText("Cast");
				} else {
					cardLayout.show(cards, MAINPANEL);
					jbPaint.setText("Paint");
				}
			}
		}.setCardLayout(cards));
		jToolBar.add(jbPaint);

		jbExplorer.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JFrameExplorer jf = new JFrameExplorer(device);
				jf.setIconImage(getIconImage());
				jf.setVisible(true);
			}
		});
		jToolBar.add(jbExplorer);

		jbOpenUrl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JDialogUrl jdUrl = new JDialogUrl();
				jdUrl.setVisible(true);
				if (!jdUrl.result)
					return;
				String url = jdUrl.jtfUrl.getText();
				new AndroidDevice(device).openUrl(url);
			}
		});
		jToolBar.add(jbOpenUrl);

		jbScreenCast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jFileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Image(png,jpeg,jpg)", "png", "jpg");
				jFileChooser.setFileFilter(filter);
				int returnVal = jFileChooser.showSaveDialog(JFrameMain.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					injector.screencapture.saveImage(jFileChooser
							.getSelectedFile());
				}
			}
		});
		jToolBar.add(jbScreenCast);

	}

	public void setInjector(Injector injector) {
		this.injector = injector;
		injector.screencapture.setListener(new ScreenCaptureListener() {

			public void handleNewImage(Dimension size, BufferedImage image,
					boolean landscape) {
				if (oldImageDimension == null
						|| !size.equals(oldImageDimension)) {
					jScrollPane.setPreferredSize(size);
					JFrameMain.this.pack();
					oldImageDimension = size;
				}
				jPanelScreen.handleNewImage(size, image, landscape);
			}
		});
	}

	private void startRecording() {
		JFileChooser jFileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Video file", "mov");
		jFileChooser.setFileFilter(filter);
		int returnVal = jFileChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			injector.screencapture.startRecording(jFileChooser
					.getSelectedFile());
		}
	}

	private void stopRecording() {
		injector.screencapture.stopRecording();
	}

	public class KbActionListener implements ActionListener {

		int key;

		public KbActionListener(int key) {
			this.key = key;
		}

		public void actionPerformed(ActionEvent e) {
			if (injector == null)
				return;
			injector.injectKeycode(ConstEvtKey.ACTION_DOWN, key);
			injector.injectKeycode(ConstEvtKey.ACTION_UP, key);
		}

	}

	public class KbClickActionListener implements ActionListener {

		int key;

		public KbClickActionListener(int key) {
			this.key = key;
		}

		public void actionPerformed(ActionEvent e) {
			if (injector == null)
				return;
			injector.injectKeycode(ConstEvtKey.ACTION_DOWN, key);
		}

	}

}
