package online.postboard.android.data.model;

import java.io.Serializable;
import java.util.List;


public class UserProfile implements Serializable {
	private String email;
	private List<AvatarsItem> avatars;

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setAvatars(List<AvatarsItem> avatars){
		this.avatars = avatars;
	}

	public List<AvatarsItem> getAvatars(){
		return avatars;
	}

	@Override
	public String toString(){
		return
				"UserProfile{" +
						"email = '" + email + '\'' +
						",avatars = '" + avatars + '\'' +
						"}";
	}

	public UserProfile() {
	}

}