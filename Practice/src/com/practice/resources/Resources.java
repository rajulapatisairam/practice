package com.practice.resources;

public class Resources {
	private static final Resources RESOURCES=new Resources();
	public static final Developer DEVELOPER=RESOURCES.new Developer();
	public static final Project PROJECT=RESOURCES.new Project();
	private Resources() {
		super();
		// TODO Auto-generated constructor stub
	}
public class Project{
	private String name;
	private String javaVersion;
	private String apacheVersion;
	private Project() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJavaVersion() {
		return javaVersion;
	}
	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}
	public String getApacheVersion() {
		return apacheVersion;
	}
	public void setApacheVersion(String apacheVersion) {
		this.apacheVersion = apacheVersion;
	}
	
}
	
	public class Developer{
		private String name;
		private String job;
		private String contact;
		
		
		private Developer() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getJob() {
			return job;
		}
		public void setJob(String job) {
			this.job = job;
		}
		public String getContact() {
			return contact;
		}
		public void setContact(String contact) {
			this.contact = contact;
		}
		
	}

}
