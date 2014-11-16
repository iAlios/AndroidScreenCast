package com.greatline.alio.android.screencast.test;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class CardLayoutDemo implements ActionListener {
	JPanel cards; // a panel that uses CardLayout
	final static String[] strings = { "Component 1",
			"Component 2 is so long-winded it makes the container wide",
			"Component 3" };

	private static JComponent createComponent(String s) {
		JLabel l = new JLabel(s);
		l.setBorder(BorderFactory
				.createMatteBorder(5, 5, 5, 5, Color.DARK_GRAY));
		l.setHorizontalAlignment(JLabel.CENTER);
		return l;
	}

	public void addCardsToPane(Container pane) {
		JRadioButton[] rb = new JRadioButton[strings.length];
		ButtonGroup group = new ButtonGroup();
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));

		for (int i = 0; i < strings.length; i++) {
			rb[i] = new JRadioButton("Show component #" + (i + 1));
			rb[i].setActionCommand(String.valueOf(i));
			rb[i].addActionListener(this);
			group.add(rb[i]);
			buttons.add(rb[i]);
		}
		rb[0].setSelected(true);

		// Create the panel that contains the "cards".
		cards = new JPanel(new CardLayout());
		for (int i = 0; i < strings.length; i++) {
			cards.add(createComponent(strings[i]), String.valueOf(i));
		}

		pane.add(buttons, BorderLayout.NORTH);
		pane.add(cards, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent evt) {
		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, (String) evt.getActionCommand());
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		CardLayoutDemo demo = new CardLayoutDemo();
		demo.addCardsToPane(frame.getContentPane());

		frame.pack();
		frame.setVisible(true);
	}
}
