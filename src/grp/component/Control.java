package grp.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import grp.util.Support;

@SuppressWarnings("serial")
public abstract class Control extends JPanel {
	
	static Font defaultFont = new Font(null, Font.BOLD, 12);
	static Dimension defaultSize = new Dimension(0, 29);
	private JLabel unitLabel = new JLabel();
	private Component separator = createSeparator(6);
	
	private String id;
	
	Control() {
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	static Component createSeparator() {
		return createSeparator(5);
	}
	
	static Component createSeparator(int length) {
		return Box.createRigidArea(new Dimension(length, 0));
	}
	
	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public abstract String getValue();
	
	public abstract void setValue(String value);
	
	public boolean isEmpty() {
		return getValue().trim().isEmpty();
	}
	
	private String oldValue;
	
	public void addChangeListener(Runnable runnable) {
		oldValue = getValue();
		Support.setInterval(500, s -> {
			if(!getValue().equals(oldValue)) {
				runnable.run();
				oldValue = getValue();
			}
		});
	}
	
	protected void toggleUnit() {
		if(!unitLabel.getText().isEmpty()) {
			add(separator);
			add(unitLabel);
		} else {
			remove(separator);
			remove(unitLabel);
		}
	}
	
	public void setUnit(String unit) {
		if(unit == null) return;
		unitLabel.setText(unit);
		toggleUnit();
	}
}
