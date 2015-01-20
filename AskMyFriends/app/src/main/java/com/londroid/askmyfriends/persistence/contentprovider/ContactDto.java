package com.londroid.askmyfriends.persistence.contentprovider;

import android.graphics.Bitmap;


public class ContactDto {
	
	private String contactName;

	private Bitmap contactThumbnail;

	private String phoneNumber;
	

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Bitmap getContactThumbnail() {
		return contactThumbnail;
	}

	public void setContactPhoto(Bitmap contactThumbnail) {
		this.contactThumbnail = contactThumbnail;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
