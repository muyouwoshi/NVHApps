/**
 * Òì²½Ð´ÎÄ¼þ
 */
package com.example.mobileacquisition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WriteRunnable implements Runnable {
	private String tempName;
	private boolean isWriting=false;
	private ArrayList<byte[]> write_data;
	public void getTempName(String tempName) {
		this.tempName = tempName;
	}
	public void ifWriting(boolean isWriting){
		this.isWriting=isWriting;
	}
	public void getWrite_data( ArrayList<byte[]> write_data){
		this.write_data=write_data;
	}
	@Override
	public void run() {
		try {
			FileOutputStream fos = null;
			File file = null;
			try {
				file = new File(tempName);
				if (file.exists()) {
					file.delete();
				}
				fos = new FileOutputStream(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			while (isWriting || write_data.size() > 0) {
				byte[] buffer = null;
				synchronized (write_data) {
					if (write_data.size() > 0) {
						buffer = write_data.get(0);
						write_data.remove(0);
					}
				}
				try {
					if (buffer != null) {
						fos.write(buffer);
						fos.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fos.close();

		} catch (Throwable t) {

		}
	}
}
