package com.exoTut.exotut3;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class libExoTut {
	static public String writeXml(List<View> widgets){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "widgets");
			serializer.attribute("", "number", String.valueOf(widgets.size()));
			for (View widget: widgets){
				serializer.startTag("", "widget");
				serializer.attribute("", "id", String.valueOf(widget.getId()));
				if(widget instanceof EditText){
					EditText et = (EditText) widget;
					serializer.text(et.getText().toString());
				}else if(widget instanceof RadioButton){
					RadioButton rb = (RadioButton) widget;
					serializer.attribute("", "isSelected", (rb.isChecked())?"true":"false");
				}else if(widget instanceof Spinner){
					Spinner sp = (Spinner) widget;
					@SuppressWarnings("unchecked")
					ArrayAdapter<String> spa = (ArrayAdapter<String>) sp.getAdapter();
					final int selected = sp.getSelectedItemPosition();
					for(int i=0 ; i < spa.getCount() ; i++){
						serializer.startTag("", "child");
						serializer.attribute("", "selected",(i == selected)?"true":"false");
						serializer.text(spa.getItem(i));
						serializer.endTag("", "child");
					}
				}
				serializer.endTag("", "widget");
			}
			serializer.endTag("", "widgets");
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	public static int readXml(String file, List<View> widgets, Activity parent)
	{
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder        db  = dbf.newDocumentBuilder();
			InputSource            is  = new InputSource();

			is.setCharacterStream(new StringReader(file));

			Document doc   = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("widget");

			if(nodes.getLength() != widgets.size())return -1;

			// iterate the employees
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);

				View widget = widgets.get(i);
				if(!String.valueOf(widget.getId()).equals(element.getAttribute("id")))return -1;
				if(widget instanceof EditText){
					EditText et = (EditText) widget;
					et.setText(getCharacterDataFromElement(element));
				}else if(widget instanceof RadioButton){
					RadioButton rb = (RadioButton) widget;
					String valBool = element.getAttribute("isSelected");
					if(valBool.equals("true")){
						rb.setChecked(true);
					}else if(valBool.equals("false")){
						rb.setChecked(false);
					}
				}else if(widget instanceof Spinner){
					Spinner sp = (Spinner) widget;

					List<String> elements = new ArrayList<String>();

					int indexSelected = 0;
					NodeList nodesSpin = doc.getElementsByTagName("child");
					for (int j = 0; j < nodesSpin.getLength(); j++) {
						Element elementSpin = (Element) nodesSpin.item(j);
						elements.add(getCharacterDataFromElement(elementSpin));
						if(elementSpin.getAttribute("selected").equals("true")){
							indexSelected = j;
						}
					}

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent, android.R.layout.simple_spinner_item, elements);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					sp.setAdapter(adapter);
					sp.setSelection(indexSelected);

				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}
}
