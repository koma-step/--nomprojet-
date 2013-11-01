package com.exoTut.exotut3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private EditText    m_field1     = null;
	private EditText    m_field2     = null;
	private RadioButton m_button1    = null;
	private RadioButton m_button2    = null;
	private Spinner     m_spinner    = null;
	private List<View>  m_listWidget = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		m_field1  = (EditText)    findViewById(R.id.editText1);
		m_field2  = (EditText)    findViewById(R.id.editText2);
		m_button1 = (RadioButton) findViewById(R.id.radioButton1);
		m_button2 = (RadioButton) findViewById(R.id.radioButton2);
		m_spinner = (Spinner)     findViewById(R.id.spinner1);
		
		List<String> elements = new ArrayList<String>();
		elements.add("toto");
		elements.add("tutu");
		elements.add("titi");
	         
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, elements);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    m_spinner.setAdapter(adapter);
		
		m_listWidget = new ArrayList<View>();
		m_listWidget.add(m_field1);
		m_listWidget.add(m_field2);
		m_listWidget.add(m_button1);
		m_listWidget.add(m_button2);
		m_listWidget.add(m_spinner);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected (MenuItem item)
	{
	  switch(item.getItemId())
	  {
	      case R.id.save:
	      {
	          String str = libExoTut.writeXml(m_listWidget);
	          writeOnPhone("exoTut3.txt", str);
	          return true;
	      }
	      case R.id.load:
	      {
	      	  String str = readOnPhone("exoTut3.txt");
	      	  if(libExoTut.readXml(str, m_listWidget, this) == -1){
	      		 Toast.makeText(getApplicationContext(), "sauvegarde corompu", Toast.LENGTH_SHORT).show();
	      	  }
		      return true;
	      }
	  }
	  return super.onOptionsItemSelected(item);
	}
	
	public void writeOnPhone(String fileName, String data)
	{
		FileOutputStream output = null;   

		try {
			output = openFileOutput(fileName, MODE_PRIVATE);
			output.write(data.getBytes());
			if(output != null)
				output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String readOnPhone(String fileName)
	{
		FileInputStream input = null;
		StringBuilder str = new StringBuilder();
		int car;
		try {
			input = openFileInput(fileName);
			while((car = input.read()) != -1){
				str.append(Character.toChars(car));
			}
			if(input != null)
				input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str.toString();
	}
}
