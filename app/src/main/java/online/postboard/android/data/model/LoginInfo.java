package online.postboard.android.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class LoginInfo implements Serializable {
	private String providerID;
	private String providerKey;

	public void setProviderID(String providerID){
		this.providerID = providerID;
	}

	public String getProviderID(){
		return providerID;
	}

	public void setProviderKey(String providerKey){
		this.providerKey = providerKey;
	}

	public String getProviderKey(){
		return providerKey;
	}

	@Override
	public String toString(){
		return
				"LoginInfo{" +
						"providerID = '" + providerID + '\'' +
						",providerKey = '" + providerKey + '\'' +
						"}";
	}

}