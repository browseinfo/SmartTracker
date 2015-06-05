package com.app.smarttracker.entity;

public class POEntry {
	long id;
	long poNumber;
	String productName;
	String suplierName;
	double quantity;
	String paymentTerms;
	String ETD;
	String ATD;
	String ETA;
	String ATA;
	String departurePortName;
	String arrivalPortName;
	boolean docSent;

	String sailingDate;
	String docTrackNumber;
	String docSentDate;
	String docRecvDate;
	String customClearanceDate;
	String factoryArrivalDate;
	
	String lastPortName;
	String destinationPortDate;
	String timestamp;
	String customerRemarks;
	String supplierRemarks;
	boolean isDeleted;
	int editCount;
	boolean isSynced;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(long poNumber) {
		this.poNumber = poNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getETD() {
		return ETD;
	}

	public void setETD(String eTD) {
		ETD = eTD;
	}

	public String getATD() {
		return ATD;
	}

	public void setATD(String aTD) {
		ATD = aTD;
	}

	public String getETA() {
		return ETA;
	}

	public void setETA(String eTA) {
		ETA = eTA;
	}

	public String getATA() {
		return ATA;
	}

	public void setATA(String aTA) {
		ATA = aTA;
	}

	public boolean isDocSent() {
//		if(docSent)
//			return 1;
//		else
//			return 0;
		return docSent;
	}

	public void setDocSent(boolean docSent) {
		this.docSent = docSent;
	}

	public String getDocTrackNumber() {
		return docTrackNumber;
	}

	public void setDocTrackNumber(String docTrackNumber) {
		this.docTrackNumber = docTrackNumber;
	}

	public String getSailingDate() {
		return sailingDate;
	}

	public void setSailingDate(String sailingDate) {
		this.sailingDate = sailingDate;
	}

	public String getLastPortName() {
		return lastPortName;
	}

	public void setLastPortName(String lastPortName) {
		this.lastPortName = lastPortName;
	}

	public String getDestinationPortDate() {
		return destinationPortDate;
	}

	public void setDestinationPortDate(String destinationPortDate) {
		this.destinationPortDate = destinationPortDate;
	}

	public String getCustomerRemarks() {
		return customerRemarks;
	}

	public void setCustomerRemarks(String customerRemarks) {
		this.customerRemarks = customerRemarks;
	}

	public String getSupplierRemarks() {
		return supplierRemarks;
	}

	public void setSupplierRemarks(String supplierRemarks) {
		this.supplierRemarks = supplierRemarks;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getEditCount() {
		return editCount;
	}

	public void setEditCount(int editNo) {
		this.editCount = editNo;
	}

	public boolean isSynced() {
		return isSynced;
	}

	public void setSynced(boolean isSynced) {
		this.isSynced = isSynced;
	}
	
	public String getSuplierName() {
		return suplierName;
	}

	public void setSuplierName(String suplierName) {
		this.suplierName = suplierName;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getDeparturePortName() {
		return departurePortName;
	}

	public void setDeparturePortName(String departurePortName) {
		this.departurePortName = departurePortName;
	}

	public String getArrivalPortName() {
		return arrivalPortName;
	}

	public void setArrivalPortName(String arrivalPortName) {
		this.arrivalPortName = arrivalPortName;
	}

	public String getDocSentDate() {
		return docSentDate;
	}

	public void setDocSentDate(String docSentDate) {
		this.docSentDate = docSentDate;
	}

	public String getDocRecvDate() {
		return docRecvDate;
	}

	public void setDocRecvDate(String docRecvDate) {
		this.docRecvDate = docRecvDate;
	}

	public String getCustomClearanceDate() {
		return customClearanceDate;
	}

	public void setCustomClearanceDate(String customClearanceDate) {
		this.customClearanceDate = customClearanceDate;
	}

	public String getFactoryArrivalDate() {
		return factoryArrivalDate;
	}

	public void setFactoryArrivalDate(String factoryArrivalDate) {
		this.factoryArrivalDate = factoryArrivalDate;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


}
