package ai.tech5.tech5.models;
//package com.ptbli.core.orm;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;

//@Entity
//@Table(name = "EKYC_DEMOGRAPHICS")
public class EkycDemographics {

    @SerializedName("face")
    public String face;


    //    @Column(name = "NAMA_LGKP")
    @SerializedName("NAME")
    public String name;

    //    @JsonAdapter(value = DemographicsDateOfBirthAdapter.class)
//    @Column(name = "TGL_LHR")
    @SerializedName("DOB")
    public String dateOfBirth;


    //    @Column(name = "JENIS_KLMIN")
    @SerializedName("GENDER")
    public String gender;

    //    @Column(name = "GOL_DRH")
    @SerializedName("BLOOD_GROUP")
    public String bloodGroup;

    //    @Column(name = "EMAILID")
    @SerializedName("EMAILID")
    public String EMAILID;

    @SerializedName("COUNTRY")
    public String COUNTRY;



    //    @Column(name = "EMAILID")
    @SerializedName("DIGITAL_ID")
    public String DIGITAL_ID;

//    @Id
////    @Column(name = "TRANSACTION_ID")
//    private String transactionId;

////    @Column(name = "CUSTOMERID")
//    @SerializedName("CUSTOMERID")
//    public String customerId;

////    @Column(name = "NIK")
//    @SerializedName("NIK")
//    public BigDecimal NIK;

////    @Column(name = "NAMA_PROP")
//    @SerializedName("NAMA_PROP")
//    public String NAMA_PROP;
//
////    @Column(name = "NAMA_KAB")
//    @SerializedName("NAMA_KAB")
//    public String NAMA_KAB;
//
////    @Column(name = "NAMA_KEC")
//    @SerializedName("NAMA_KEC")
//    public String kec;
//
////    @Column(name = "NAMA_KEL")
//    @SerializedName("NAMA_KEL")
//    public String kel;

////    @Column(name = "TMPT_LHR")
//    @SerializedName("TMPT_LHR")
//    public String placeOfBirth;

////    @Column(name = "AGAMA")
//    @SerializedName("AGAMA")
//    public String religion;

////    @Column(name = "JENIS_PKRJN")
//    @SerializedName("JENIS_PKRJN")
//    public String occupation;

////    @Column(name = "NO_RT")
//    @SerializedName("NO_RT")
//    public int rt;
//
////    @Column(name = "NO_RW")
//    @SerializedName("NO_RW")
//    public int rw;
//
////    @Column(name = "ALAMAT")
//    @SerializedName("ALAMAT")
//    public String address;

////    @Column(name = "STAT_KWN")
//    @SerializedName("STAT_KWN")
//    public String maritalStatus;
//
////    @Column(name = "NO_PROP")
//    @SerializedName("NO_PROP")
//    public int noProp;
//
////    @Column(name = "NO_KAB")
//    @SerializedName("NO_KAB")
//    public int noKab;
//
////    @Column(name = "NAMA_LGKP_AYAH")
//    @SerializedName("NAMA_LGKP_AYAH")
//    public String fatherName;
//
////    @Column(name = "NAMA_LGKP_IBU")
//    @SerializedName("NAMA_LGKP_IBU")
//    public String motherName;
//
////    @Column(name = "NO_KK")
//    @SerializedName("NO_KK")
//    public String noKK;
//
////    @JsonAdapter(value = DemographicsDateTimeAdapter.class)
////    @Column(name = "CREATED")
//    @SerializedName("CREATED")
//    public Date createdDate;
//
////    @JsonAdapter(value = DemographicsDateTimeAdapter.class)
////    @Column(name = "LAST_UPDATED")
//    @SerializedName("LAST_UPDATED")
//    public Date lastUpdated;
//
////    @Column(name = "NO_KEC")
//    @SerializedName("NO_KEC")
//    public int NO_KEC;
//
////    @Column(name = "NO_KEL")
//    @SerializedName("NO_KEL")
//    public int NO_KEL;
//
////    @Column(name = "NO_PASPOR")
//    @SerializedName("NO_PASPOR")
//    public String NO_PASPOR;
//
////    @Column(name = "NIK_IBU")
//    @SerializedName("NIK_IBU")
//    public BigDecimal NIK_IBU;
//
////    @Column(name = "NIK_AYAH")
//    @SerializedName("NIK_AYAH")
//    public BigDecimal NIK_AYAH;
//
////    @Column(name = "PHONENUMBER")
//    @SerializedName("PHONENUMBER")
//    public String PHONENUMBER;

////    @Column(name = "EKTP_STATUS")
//    @SerializedName("EKTP_STATUS")
//    public String EKTP_STATUS;
//
////    @Column(name = "PDDK_AKH")
//    @SerializedName("PDDK_AKH")
//    public String PDDK_AKH;

//    public BigDecimal getNIK_IBU() {
//        return NIK_IBU;
//    }
//
//    public void setNIK_IBU(BigDecimal nIK_IBU) {
//        NIK_IBU = nIK_IBU;
//    }
//
//    public BigDecimal getNIK_AYAH() {
//        return NIK_AYAH;
//    }
//
//    public void setNIK_AYAH(BigDecimal nIK_AYAH) {
//        NIK_AYAH = nIK_AYAH;
//    }
//
//    public String getPDDK_AKH() {
//        return PDDK_AKH;
//    }
//
//    public void setPDDK_AKH(String pDDK_AKH) {
//        PDDK_AKH = pDDK_AKH;
//    }

//    public String getDigitalID() {
//        return digitalID;
//    }
//
//    public void setDigitalID(String digitalID) {
//        this.digitalID = digitalID;
//    }
//
////    @Column(name = "DIGITAL_ID")
//    private String digitalID;
//
//
//    public String getTransactionId() {
//        return transactionId;
//    }
//
//    public void setTransactionId(String transactionId) {
//        this.transactionId = transactionId;
//    }
//
//    public String getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(String customerId) {
//        this.customerId = customerId;
//    }
//
//    public BigDecimal getNIK() {
//        return NIK;
//    }
//
//    public void setNIK(BigDecimal nIK) {
//        NIK = nIK;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getNAMA_PROP() {
//        return NAMA_PROP;
//    }
//
//    public void setNAMA_PROP(String nAMA_PROP) {
//        NAMA_PROP = nAMA_PROP;
//    }
//
//    public String getNAMA_KAB() {
//        return NAMA_KAB;
//    }
//
//    public void setNAMA_KAB(String nAMA_KAB) {
//        NAMA_KAB = nAMA_KAB;
//    }
//
//    public String getKec() {
//        return kec;
//    }
//
//    public void setKec(String kec) {
//        this.kec = kec;
//    }
//
//    public String getKel() {
//        return kel;
//    }
//
//    public void setKel(String kel) {
//        this.kel = kel;
//    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

//    public String getPlaceOfBirth() {
//        return placeOfBirth;
//    }
//
//    public void setPlaceOfBirth(String placeOfBirth) {
//        this.placeOfBirth = placeOfBirth;
//    }
//
//    public String getReligion() {
//        return religion;
//    }
//
//    public void setReligion(String religion) {
//        this.religion = religion;
//    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

//    public String getOccupation() {
//        return occupation;
//    }
//
//    public void setOccupation(String occupation) {
//        this.occupation = occupation;
//    }
//
//    public int getRt() {
//        return rt;
//    }
//
//    public void setRt(int rt) {
//        this.rt = rt;
//    }
//
//    public int getRw() {
//        return rw;
//    }
//
//    public void setRw(int rw) {
//        this.rw = rw;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

//    public String getMaritalStatus() {
//        return maritalStatus;
//    }
//
//    public void setMaritalStatus(String maritalStatus) {
//        this.maritalStatus = maritalStatus;
//    }
//
//    public int getNoProp() {
//        return noProp;
//    }
//
//    public void setNoProp(int noProp) {
//        this.noProp = noProp;
//    }
//
//    public int getNoKab() {
//        return noKab;
//    }
//
//    public void setNoKab(int noKab) {
//        this.noKab = noKab;
//    }
//
//    public String getFatherName() {
//        return fatherName;
//    }
//
//    public void setFatherName(String fatherName) {
//        this.fatherName = fatherName;
//    }
//
//    public String getMotherName() {
//        return motherName;
//    }
//
//    public void setMotherName(String motherName) {
//        this.motherName = motherName;
//    }
//
//    public String getNoKK() {
//        return noKK;
//    }
//
//    public void setNoKK(String noKK) {
//        this.noKK = noKK;
//    }
//
//    public Date getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(Date createdDate) {
//        this.createdDate = createdDate;
//    }
//
//    public Date getLastUpdated() {
//        return lastUpdated;
//    }
//
//    public void setLastUpdated(Date lastUpdated) {
//        this.lastUpdated = lastUpdated;
//    }
//
//    public int getNO_KEC() {
//        return NO_KEC;
//    }
//
//    public void setNO_KEC(int nO_KEC) {
//        NO_KEC = nO_KEC;
//    }
//
//    public int getNO_KEL() {
//        return NO_KEL;
//    }
//
//    public void setNO_KEL(int nO_KEL) {
//        NO_KEL = nO_KEL;
//    }
//
//    public String getNO_PASPOR() {
//        return NO_PASPOR;
//    }
//
//    public void setNO_PASPOR(String nO_PASPOR) {
//        NO_PASPOR = nO_PASPOR;
//    }
//
//    public String getPHONENUMBER() {
//        return PHONENUMBER;
//    }
//
//    public void setPHONENUMBER(String pHONENUMBER) {
//        PHONENUMBER = pHONENUMBER;
//    }

    public String getEMAILID() {
        return EMAILID;
    }

    public void setEMAILID(String eMAILID) {
        EMAILID = eMAILID;
    }

//    public String getEKTP_STATUS() {
//        return EKTP_STATUS;
//    }
//
//    public void setEKTP_STATUS(String eKTP_STATUS) {
//        EKTP_STATUS = eKTP_STATUS;
//    }

}

