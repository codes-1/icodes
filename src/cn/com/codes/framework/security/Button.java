package cn.com.codes.framework.security;

import java.util.ArrayList;
import java.util.List;

import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.security.Button;


public class Button {

	private String name ;
	private String icon;
	private Boolean share ;
	private String id ;
	private int seq ;
	private String url ;
	public Button(){
		
	}
	
	public Button(String url){
		this.url = url ;
	}
	
	public Button(String id,String name,String icon,Boolean share,int seq,String url){
		this.id = id ;
		this.name = name ;
		this.icon = icon ;
		this.share = share ;
		this.seq = seq ;
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public boolean isShare() {
		return share;
	}
	public void setShare(boolean share) {
		this.share = share;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String toString(){
		
		return "{\nid="+id +"\n name=" +name +"\n icon="+icon+"\n share=" +share+"\n seq="+seq +"}\n" ;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
	

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Button))
			return false;
		Button castOther = (Button) other;
		return ( this.url != null
				&& castOther.url != null && this.url.trim().equals(
				castOther.url.trim()));
	}
	
	public int hashCode() {
		int result = 17;
		result = 37 * result + (getUrl() == null ? 0 : getUrl().hashCode());
		return result;
	}
	
	public static void main(String[] args){
		
		List<Button> buttonlist = new ArrayList<Button>();
		buttonlist.add(new Button("测试"));
		System.out.println(buttonlist.contains(new Button("测试")));
		buttonlist.remove(new Button("测试"));
		System.out.println(buttonlist.contains(new Button("测试")));
		List<ListObject> test = new ArrayList<ListObject>();
		test.add(new ListObject("5"));
		test.add(new ListObject("6"));
		test.add(new ListObject("7"));
		test.remove(new ListObject("6"));
		System.out.println(test.size());
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}
