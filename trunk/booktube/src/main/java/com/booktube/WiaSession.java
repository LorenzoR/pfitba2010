package com.booktube;

import java.util.Locale;

import org.apache.wicket.request.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;

import com.booktube.model.User;

public class WiaSession extends WebSession {

	private static final long serialVersionUID = -9144400391708437742L;
	private User user;
	
	public static WiaSession get() {
		return (WiaSession) Session.get();
	}

	

	public WiaSession(Request request) {
		super(request);
		setLocale(Locale.getDefault());
	}

	public synchronized User getLoggedInUser() {
		return user;
	}

	public synchronized boolean isAuthenticated() {
		return (user != null);
	}

	public synchronized void logInUser(User user) {
		this.user = user;
		dirty();
	}
	
	public synchronized void logOutUser() {
		this.user = null;
	}
}