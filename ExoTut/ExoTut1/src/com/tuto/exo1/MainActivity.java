package com.tuto.exo1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

public class MainActivity extends Activity{
	
	private Spinner liste = null;
	private File mFile = null;
	private TextView mfield1 = null;
	private TextView mfield2 = null;
	private CheckBox check1 = null;
	private CheckBox check2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/files/exotut1.txt");
        
        mfield1 = (TextView) findViewById(R.id.champ_saisie_1);
        mfield2 = (TextView) findViewById(R.id.champ_saisie_2);
        
        check1 = (CheckBox) findViewById(R.id.checkBox1);
        check2 = (CheckBox) findViewById(R.id.checkBox2);
        
        liste = (Spinner) findViewById(R.id.spinner1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public HashMap<String, String> majMap()
    {
    	HashMap<String, String> rep = new HashMap<String, String>();
    	
    	FileInputStream input = null;
		DataInputStream dataIO = null;
		try {
			input = new FileInputStream(mFile);
			dataIO = new DataInputStream(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
		boolean eof = false;
		while(!eof)
		{
			String str = "";
			try
			{
				str = dataIO.readLine();
			}catch (FileNotFoundException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		        e.printStackTrace();
		    }catch (IOException e) {
		        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		    }
			
			if(str == null){
				eof = true;
			}
			
			String key = "", value = "";
			int j = 0;
			boolean atvalue = false;
			for(int i=0 ; !eof && (i + j) < str.length(); i++)
			{
				if(str.charAt(i+j) == '='){
					j = i;
					i = 1;
					atvalue = true;
				}
				if(str.charAt(i+j) !=' '){
					if(atvalue){
						value += str.charAt(i+j);
					}else{
						key += str.charAt(i+j);
					}
				}
			}
			rep.put(key, value);
		}
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return rep;
    	
    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.action_load:
		{
			HashMap<String, String> map =majMap();
			mfield1.setText(map.get("fieldtext1"));
			mfield2.setText(map.get("fieldtext2"));
			
			check1.setChecked(map.get("checkbox1").equals("true"));
			check2.setChecked(map.get("checkbox2").equals("true"));
			
			ArrayList<String> exemple = new ArrayList<String>(); 
			String[] tmp = map.get("list").split(",");
			
			for(String s : tmp){
				exemple.add(s);
			}
	        
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exemple);
	        
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        liste.setAdapter(adapter);
			
			return true;
		}
		case R.id.action_save:
		{
			BufferedWriter writer = null;
			try {
				mFile.createNewFile();
				writer = new BufferedWriter(new OutputStreamWriter(
				          new FileOutputStream(mFile), "utf-8"));
		        writer.write(String.valueOf("fieldtext1 = " + mfield1.getText() + "\n").toString());
		        writer.write(String.valueOf("fieldtext2 = " + mfield2.getText() + "\n").toString());
		        writer.write(String.valueOf("checkbox1 = " + check1.isChecked() + "\n").toString());
		        writer.write(String.valueOf("checkbox2 = " + check2.isChecked() + "\n").toString());
		        writer.write(String.valueOf("list = toto,tutu,titi\n").toString());
		          
		        writer.close();
		        } catch (FileNotFoundException e) {
		        	Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		        } catch (IOException e) {
		        	Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		        }
			return false;
		}
		}
		return false;
	}
    
}
