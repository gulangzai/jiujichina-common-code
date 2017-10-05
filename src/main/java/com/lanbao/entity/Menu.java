package com.lanbao.entity;

import java.util.ArrayList;
import java.util.List; 
/**
 * 
* 绫诲悕绉帮細Menu.java
* 绫绘弿杩帮細 
* @author lanbao
* 浣滆�呭崟浣嶏細 
* 鑱旂郴鏂瑰紡锛�
* 鍒涘缓鏃堕棿锛�2014骞�6鏈�28鏃�
* @version 1.0
 */
public class Menu {
	
	 
	public String f_menu_parent;
	public String f_menu_name;
	public String f_menu_url;
	public String f_menu_icon;
	public int f_menu_serial;
	public String f_status; 
	
	
	private String MENU_ID;
	private String MENU_NAME;
	private String MENU_URL;
	private String PARENT_ID;
	private String MENU_ORDER;
	private String MENU_ICON;
	private String MENU_TYPE;
	private String target;
	private String f_menu_id;
	
	private Menu parentMenu;
	private List<Menu> subMenu;
	
	private boolean hasMenu = false;
	
	List<Menu> tbMenus = new ArrayList<Menu>();
	
	public String getMENU_ID() {
		return MENU_ID;
	}
	public void setMENU_ID(String mENU_ID) {
		MENU_ID = mENU_ID;
	}
	public String getMENU_NAME() {
		return MENU_NAME;
	}
	public void setMENU_NAME(String mENU_NAME) {
		MENU_NAME = mENU_NAME;
	}
	public String getMENU_URL() {
		return MENU_URL;
	}
	public void setMENU_URL(String mENU_URL) {
		MENU_URL = mENU_URL;
	}
	public String getPARENT_ID() {
		return PARENT_ID;
	}
	public void setPARENT_ID(String pARENT_ID) {
		PARENT_ID = pARENT_ID;
	}
	public String getMENU_ORDER() {
		return MENU_ORDER;
	}
	public void setMENU_ORDER(String mENU_ORDER) {
		MENU_ORDER = mENU_ORDER;
	}
	public Menu getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}
	public List<Menu> getSubMenu() {
		return subMenu;
	}
	public void setSubMenu(List<Menu> subMenu) {
		this.subMenu = subMenu;
	}
	public boolean isHasMenu() {
		return hasMenu;
	}
	public void setHasMenu(boolean hasMenu) {
		this.hasMenu = hasMenu;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getMENU_ICON() {
		return MENU_ICON;
	}
	public void setMENU_ICON(String mENU_ICON) {
		MENU_ICON = mENU_ICON;
	}
	public String getMENU_TYPE() {
		return MENU_TYPE;
	}
	public void setMENU_TYPE(String mENU_TYPE) {
		MENU_TYPE = mENU_TYPE;
	}
	public String getF_menu_id() {
		return f_menu_id;
	}
	public void setF_menu_id(String f_menu_id) {
		this.f_menu_id = f_menu_id;
	}
	public String getF_menu_parent() {
		return f_menu_parent;
	}
	public void setF_menu_parent(String f_menu_parent) {
		this.f_menu_parent = f_menu_parent;
	}
	public String getF_menu_name() {
		return f_menu_name;
	}
	public void setF_menu_name(String f_menu_name) {
		this.f_menu_name = f_menu_name;
	}
	public String getF_menu_url() {
		return f_menu_url;
	}
	public void setF_menu_url(String f_menu_url) {
		this.f_menu_url = f_menu_url;
	}
	public String getF_menu_icon() {
		return f_menu_icon;
	}
	public void setF_menu_icon(String f_menu_icon) {
		this.f_menu_icon = f_menu_icon;
	}
	public int getF_menu_serial() {
		return f_menu_serial;
	}
	public void setF_menu_serial(int f_menu_serial) {
		this.f_menu_serial = f_menu_serial;
	}
	public String getF_status() {
		return f_status;
	}
	public void setF_status(String f_status) {
		this.f_status = f_status;
	}
	public List<Menu> getTbMenus() {
		return tbMenus;
	}
	public void setTbMenus(List<Menu> tbMenus) {
		this.tbMenus = tbMenus;
	}
	
	
}
