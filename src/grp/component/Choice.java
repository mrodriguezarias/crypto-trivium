package grp.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import grp.util.Support;

@SuppressWarnings("serial")
public class Choice extends Control {

	private Map<String, String> model;
	private ComboBox comboBox;
	private RadioGroup radioGroup;
	private boolean comboMode;
	
	public Choice(String model) {
		this(Support.dictFromString(model));
	}
	
	public Choice(List<String> model) {
		this(model.stream().collect(Collectors.toMap(String::new, String::new)));
	}
	
	public Choice(Map<String, String> model) {
		
		this.model = model;
		renderControl();
	}
	
	public boolean isComboMode() {
		return comboMode || model.size() > 5;
	}
	
	private void renderControl() {
	
		removeAll();
		if(isComboMode()) {
			comboBox = new ComboBox(new ArrayList<>(model.keySet()));
			add(comboBox);
		} else {
			radioGroup = new RadioGroup(new ArrayList<>(model.keySet()));
			add(radioGroup);
		}
	}
	
	public void setValue(String value) {
		
		if(value == null) return;
		model.keySet().stream().filter(k -> model.get(k).equals(value)).findAny().ifPresent(key -> {
			if(isComboMode()) {
				comboBox.setSelectedItem(key);
			} else {
				radioGroup.setSelectedItem(key);
			}
		});
	}
	
	public String getValue() {
		if(isComboMode()) {
			return model.get(comboBox.getSelectedItem());
		} else {
			return model.get(radioGroup.getSelectedItem());
		}
	}
}
