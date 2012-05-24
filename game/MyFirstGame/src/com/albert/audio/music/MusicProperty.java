package com.albert.audio.music;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;

import com.albert.IProperty;
import com.albert.audio.music.exception.MusicLoadPropertyException;

public class MusicProperty implements IProperty {
	private int id;
	private float leftVolume;
	private float rightVolume;
	private String url;
	private boolean isMute;
	
	private String file;
	private Context mContext;
	
	
	public MusicProperty(Context mContext) {
		super();
		this.mContext = mContext;
	}


	@Override
	public void saveProperty() {
		// TODO Auto-generated method stub
		Properties p = new Properties();
		p.put("id", id);
		p.put("leftVolume", leftVolume);
		p.put("rightVolume", rightVolume);
		p.put("url", url);
		p.put("isMute", isMute);
		try
		{
			FileOutputStream stream = mContext.openFileOutput(file, Context.MODE_WORLD_WRITEABLE);
			p.store(stream, "");
		}
		catch (FileNotFoundException e)
		{
			throw new MusicLoadPropertyException(e.getMessage());
		}
		catch (IOException e)
		{
			throw new MusicLoadPropertyException(e.getMessage());
		}
	}


	@Override
	public void readProperty() {
		// TODO Auto-generated method stub
		Properties properties = new Properties();
		try
		{
			FileInputStream stream = mContext.openFileInput(file);
			properties.load(stream);
			this.id = Integer.parseInt(properties.get("id").toString());
			this.leftVolume = Float.parseFloat(properties.get("leftVolume").toString());
			this.rightVolume = Float.parseFloat(properties.get("rightVolume").toString());
			this.url = properties.get("url").toString();
			this.isMute = Boolean.parseBoolean(properties.get("isMute").toString());
		}catch (FileNotFoundException e)
		{
			throw new MusicLoadPropertyException(e.getMessage());
		}
		catch (IOException e)
		{
			throw new MusicLoadPropertyException(e.getMessage());
		}
		catch (NumberFormatException e){
			throw new MusicLoadPropertyException(e.getMessage());
		}
	}


	@Override
	public void setStoreFile(String file) {
		// TODO Auto-generated method stub
		this.file = file;
	}

}
