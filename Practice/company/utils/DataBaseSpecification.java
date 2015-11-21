package com.sutisoft.roe.util;

import org.apache.commons.lang.xwork.StringUtils;

import com.sutisoft.roe.setup.AppConstants;

public enum DataBaseSpecification {
	IMPORT("Import"),ANALYSIS("Analysis"),VOICE_PROPOSAL("Voice Proposal"),DATA_PROPOSAL("Data Proposal"),
	NETWORK("OPEN,4G,3G,NONE"),YES_NO("YES,NO"),INTERNATIONAL_VOICE("Canada,Mexico,None,Other"),
	RATEPLAN_TYPE_TEXT("UNLIMITED,LIMITED,NONE"),VOICE_DATA("OPEN,VOICE,DATA");
	public String specification;
	private DataBaseSpecification(final String specification)
	{
		this.specification=specification;
	}
	public String getSpecification() {
		return this.specification;
	}
	/**
	 * @author Sairam Rajulapati
	 * Rate Plan Types
	 */
	public enum RatePlanTypes{
		NONE(1,"none",AppConstants.NONE),PRIMARY_RATE_PLAN_LIBRARY(2,"prpl",AppConstants.PRIMARY_RATE_PLAN_LIBRARY),DATA_FEATURE_RATE_LIBRARY(3,"dfrl",AppConstants.DATA_FEATURE_RATE_LIBRARY),TEXT_RATE_PLAN_LIBRARY(4,"trpl",AppConstants.TEXT_RATE_PLAN_LIBRARY),RATE_PLAN_TYPE(5,"rptype",AppConstants.RATE_PLAN_TYPE);
		private int typeId;
		private String type;
		private String description;
		private RatePlanTypes(int typeId, String type, String description) {
			this.typeId = typeId;
			this.type = type;
			this.description = description;
		}
		public int getTypeId() {
			return typeId;
		}
		public String getType() {
			return type;
		}
		
		public String getDescription() {
			return description;
		}
		
/**
 * 	This is method is used for getting rate paln id based on rate plan name.	
 * @param ratePlan
 * @return ratePlan Id based on @param ratePlan.
 */
		public synchronized static int getTargetRatePlanID(String ratePlanType){
			for(RatePlanTypes planType : RatePlanTypes.values())
			{
				if(StringUtils.equalsIgnoreCase(ratePlanType, planType.getType()) || StringUtils.equalsIgnoreCase(ratePlanType, planType.getDescription() )){
					return planType.getTypeId();
				}
			}
			// Return None, if ratePlan is not existed in existed palns.
			return RatePlanTypes.NONE.getTypeId();
		}
		
		public synchronized static RatePlanTypes getTargetRatePlan(String ratePlanType){
			for(RatePlanTypes planType : RatePlanTypes.values())
			{
				if(StringUtils.equalsIgnoreCase(ratePlanType, planType.getType()) || StringUtils.equalsIgnoreCase(ratePlanType, planType.getDescription() )){
					return planType;
				}
			}
			// Return None, if ratePlan is not existed in existed palns.
			return RatePlanTypes.NONE;
		}
	}
}
