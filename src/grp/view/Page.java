package grp.view;

import java.awt.FlowLayout;
import java.util.stream.Collectors;

import javax.swing.JScrollPane;

import grp.component.Choice;
import grp.component.FilePicker;
import grp.component.Sequence;
import grp.component.Section;
import grp.component.TextField;
import grp.util.Form;

@SuppressWarnings("serial")
public class Page extends Section {
	
	private MainView frame;
	private JScrollPane scrollPane;
	public Form form;
	
	public Page(MainView frame) {
		this.frame = frame;
		
		setPadding(5);
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setViewportBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);
		
		Section view = new Section();
		view.setLayout(new FlowLayout());
		view.setPadding(5,0,5,0);
		scrollPane.setViewportView(view);
		
		makeForm();
	}
	
	public void makeForm() {
		form = new Form();
		
		form.addElement("Key", new TextField());
		form.addElement("Input file", new FilePicker());
		form.addElement("Ouput file", new FilePicker(true));
		form.addElement("Output bits", new Sequence());
		form.addElement("Mode", new Choice("Encrypt:e;Decrypt:d"));
		
		form.placeIn((Section) scrollPane.getViewport().getView());
	}
	
	public void load() {
		clear();
		
//		Application.controller.whenReady(() -> {
//			form.placeIn((Section) scrollPane.getViewport().getView());
//			
//			Support.setTimeout(100, () -> {
//				toggleLoading();
//			});
//		});
	}
	
	public void clear() {
		((Section)scrollPane.getViewport().getView()).removeAll();
		form.clear();
		frame.refresh();
	}
	
	public String print() {
		
		return "<html>" + form.getWithNames().entrySet().stream()
				.map(e -> "<b>" + e.getKey().replaceFirst("\\*$", "") + ":</b> " + e.getValue())
				.collect(Collectors.joining("<br>"));
	}
}
