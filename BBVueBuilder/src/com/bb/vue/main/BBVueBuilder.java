package com.bb.vue.main;

import com.bb.vue.form.MainForm;

public class BBVueBuilder {

	public static String version = "200610";
	public static String title = "BBVueBuilder";
	public static MainForm mainForm = null;
	
	public static void main(String[] args) {
		mainForm = new MainForm(title, version, args);
	}
}