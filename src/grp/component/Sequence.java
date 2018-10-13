package grp.component;

import java.util.Date;
import java.util.List;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import grp.util.Support;

@SuppressWarnings("serial")
public class Sequence extends Control {
	
	public static final int NUMBER = 0;
	public static final int TIME = 1;
	private static List<String> types = Support.listFromString("number, time");
	
	private int type;
	
	private JSpinner spinner = new JSpinner();
	private SpinnerModel model;
	
	public Sequence() {
		this(NUMBER);
	}
	
	public Sequence(String type) {
		this(Support.keyFromValue(types, type.toLowerCase(), 0));
	}
	
	public Sequence(int type) {
		
		this.type = type;
		switch(type) {
		case TIME:
			model = new SpinnerDateModel();
			spinner.setModel(model);
			spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm:ss.SSS"));
			setValue("00:00");
			break;
			
		case NUMBER:
		default:
			model = new SpinnerNumberModel();
			spinner.setModel(model);
			restrictNumericSpinner();
		}
		
		spinner.setPreferredSize(defaultSize);
		spinner.setFont(defaultFont);
		add(spinner);
	}
	
	private static class NumericFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (stringContainsOnlyDigits(string)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (stringContainsOnlyDigits(text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean stringContainsOnlyDigits(String text) {
            for (int i = 0; i < text.length(); i++) {
                if (!Character.isDigit(text.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }
	
	private void restrictNumericSpinner() {
		JSpinner.NumberEditor jsEditor = (JSpinner.NumberEditor) spinner.getEditor();
        final Document jsDoc = jsEditor.getTextField().getDocument();
        if (jsDoc instanceof PlainDocument) {
        	AbstractDocument doc = new PlainDocument() {
                private static final long serialVersionUID = 1L;

                @Override
                public void setDocumentFilter(DocumentFilter filter) {
                    if (filter instanceof NumericFilter) {
                        super.setDocumentFilter(filter);
                    }
                }
            };
            doc.setDocumentFilter(new NumericFilter());
            jsEditor.getTextField().setDocument(doc);
        }
	}

	public void setMinimum(Double min) {
		if(min == null) return;
		((SpinnerNumberModel) model).setMinimum(min);
	}
	
	public void setMaximum(Double max) {
		if(max == null) return;
		((SpinnerNumberModel) model).setMaximum(max);
	}
	
	public void setStep(Double step) {
		if(step == null) return;
		((SpinnerNumberModel) model).setStepSize(step);
	}
	
	public void setValue(String value) {
		if(value == null) return;
		switch(type) {
		case TIME:
			model.setValue(Support.dateFromString(value));
			break;
		case NUMBER:
		default:
			model.setValue(Double.parseDouble(value));
		}
	}
	
	public String getValue() {
		switch(type) {
		case TIME:
			Date date = (Date) model.getValue();
			return Support.stringFromDate(date, "HH:mm:ss.SSS");
		case NUMBER:
		default:
			SpinnerNumberModel numberModel = (SpinnerNumberModel) model;
			Object number = numberModel.getValue();
			if(numberModel.getStepSize() instanceof Integer) {
				number = new Integer(((Double) number).intValue());
			}
			return number.toString();
		}
	}
}
