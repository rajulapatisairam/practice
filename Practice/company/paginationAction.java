public String ratePlanAnslysisData() {
		LOGGER.info(LoggerUtils.methodStartingMessage());
			PaginationUtils<RatePlanAnalysis> paginationUtils = null;
			String currentPage  = SessionUtils.getRequestParameter("currentPage");
			Integer startIndex  = StringUtils.isEmpty(currentPage)? 0 : Integer.parseInt(currentPage);
			Integer numberOfRecordsToDisplay = 10;
			RatePlanAnalysis  ratePlanAnalysis = new RatePlanAnalysis();
			String carrierId = SessionUtils.getRequestParameter(CARRIER_ID);
			if (!StringUtils.isEmpty(carrierId)) {
					ratePlanAnalysis.setCarrierId(Integer.parseInt(carrierId));
			}
			ratePlanAnalysis.setCompanyId(SessionUtils.getCompanyId());
			paginationUtils = new PaginationUtils<RatePlanAnalysis>(ratePlanAnalysis);
			Map<String,Object> resultsMap =  new HashMap<String,Object>();
			//resultsMap.put("Order", "ratePlanAnalysisId");
			paginationUtils.getPaginationData(resultsMap, startIndex, numberOfRecordsToDisplay);
		LOGGER.info(LoggerUtils.methodEndingMessage());
		return "rpanalysisPagination";
	}
