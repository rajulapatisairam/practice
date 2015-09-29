package com.sutisoft.roe.setup.date;
import com.sutisoft.roe.setup.date.Pattern;

/**
 * 
 * @author sairamr
 * _ represents /
 * 0 represents :
 * o represents .
 * _s_ represents space
 * _k_ reresents ,
 * if none of the above are specified then defult one is /
 */
public enum DatePatterns implements Pattern{
	EEEE_s_MMM_s_dd_s_HH0mm0ss_s_z_s_yyyy("EEEE MMM dd HH:mm:ss z yyyy"),
	MMM_dd_yyyy_s_hhOmm_s_a("MMM_dd_yyyy hh:mm a"),
	MMddyyyy("MM/dd/yyyy"),ddmyyyy("dd/MM/yyyy"),yyyyMMdd("yyyy/MM/dd"),
	yyyyddMM("yyyy/dd/MM"),ddMMyyyy("dd/MM/yyyy"),
	yyyy0MM0dd("yyyy:MM:dd"),yyyy0dd0MM("yyyy:dd:MM"),
	dd0MM0yyyy("dd:MM:yyyy"),MM0dd0yyyy("MM:dd:yyyy"),
	yyyyoMModd("yyyy.MM.dd"),yyyyoddoMM("yyyy.dd.MM"),
	ddoMMoyyyy("dd.MM.yyyy"),MModdoyyyy("MM.dd.yyyy"),
	yyyy_MM_dd("yyyy-MM-dd"),dd_MMM_yyyy("dd-MMM-yyyy"),
	yyyy_dd_MM("yyyy-dd-MM"),dd_MM_yyyy("dd-MM-yyyy"),
	MM_dd_yyyy("MM-dd-yyyy"),MMM_dd_yyyy("MMM-dd-yyyy"),
	ddMMMYYYY("dd MMM yyyy"),
	yyyy_MM_ddTHH0mm0ssZ("yyyy-MM-dd'T'HH:mm:ss'Z'"),
	yyyy0MM0ddTHH0mm0ssZ("yyyy-MM-dd'T'HH:mm:ssZ"),
	yyyy_MM_dd_T_HH0mm0ss("yyyy-MM-dd'T'HH:mm:ss"),
	yyyy_MM_ddTHH0mm0ssoSSSZ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
	yyyy_MM_dd_s_HH0mm0ss("yyyy-MM-dd HH:mm:ss"), 
	Mddyy("M/dd/yy"),MMM_s_dd_k_s_yyyy("MMM dd, yyyy"),MMM0dd0yyyy("MMM:dd:yyyy"),
	MMddyyyyTHH0mm0ssoSSSZ("MM/dd/yyyy'T'HH:mm:ss.SSS'Z'"), 
	MMddyyyyTHH0mm0ssoSSS("MM/dd/yyyy'T'HH:mm:ss.SSS"), 
	MMddyyyyTHH0mm0ssZ("MM/dd/yyyy'T'HH:mm:ssZ"),
	MMddyyyyTHH0mm0ss("MM/dd/yyyy'T'HH:mm:ss"),
	MMddyyyy_s_HH0mm0ss("MM/dd/yyyy HH:mm:ss"),MMMddyyyy("MMM/dd/yyyy"),
	yyyy0MM0dd_s_HH0mm0ss("yyyy:MM:dd HH:mm:ss"),
	HH0mm0ss("HH:mm:ss"),YEAR("yyyy"),HOUR("HH"),SECONDS("ss");

	private String pattern;
	private DatePatterns(final String pattern)
	{
		this.pattern=pattern;
	}
	@Override
	public String getPattern(){
		return this.pattern;
	}
	
	}
