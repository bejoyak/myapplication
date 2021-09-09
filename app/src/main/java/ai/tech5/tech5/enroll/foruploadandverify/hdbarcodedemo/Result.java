package ai.tech5.tech5.enroll.foruploadandverify.hdbarcodedemo;

import android.util.Log;


import java.util.LinkedHashMap;
import java.util.List;

import ai.tech5.tech5.enroll.foruploadandverify.tlvdecoder.ITLVRecord;

public class Result {
    public static LinkedHashMap<String, String> demographics;
    public static byte[] compressedImage;
    public static byte[] decompressedImage;
    public static byte[] faceTemplate;
    public static byte[] capturedFaceBytes;
    public static LinkedHashMap<Integer, byte[]> fingerprints;
    public static  String expiryDate;

    public static void reset() {
        demographics = null;
        compressedImage = null;
        faceTemplate = null;
        capturedFaceBytes = null;
        fingerprints = null;
        decompressedImage = null;
        expiryDate=null;
    }


    public static void setResultsFromTlvRecords(List<ITLVRecord> recordList) {

        Result.fingerprints = new LinkedHashMap<>();

        for (ITLVRecord record : recordList) {

            Log.d("TAG", "type " + record.getType());


            if (record.getType() == ITLVRecord.IDEncodeFieldType.FaceTemplate) {
                Result.faceTemplate = record.getData();
            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.Demog) {


//                Demographics demo = new Gson().fromJson(new String(record.getData()));
//
//                Result.demographics

                try {
                    String demogsStrings = new String(record.getData());
                    String values[] = demogsStrings.split(",");
//                    String keys[] = {"Name", "Company", "Country", "Designation", "Email", "Gender", "Marital Status", "Blood Group", "Date Of Issue", "Cryptograph Validity","Aadhar","Age","Address","Vaccinedose","Doctorname","Description"};
//                    String keys[] = {"Name",  "Gender",  "Blood Group", "Date Of Issue", "Valid upto","Aadhar/ID No","Age","Vaccine Dose","Doctor Name","Vaccine Name","State","District","DateOfAppointment","CenterName","CenterID","Pincode"};
//                    String keys[] = {"PatientID",  "Issuedate",  "Name", "Email", "Date Of Birth","Country","Gender","Blood Group","Kit","Vaccinebox","Cartridge","Injector"};
                    String keys[] = {"Patient ID",  "Issue Date",  "Name", "Email", "Date Of Birth","Country","Gender","Blood Group","Kit ID","Kit Details","Kit Package Date","VaccineBox ID","VaccineBox Details","VaccineBox Package Date","Cartridge ID","Cartridge Package Date","Cartridge Expiry Date","Cartridge Manufacturer",
                            "Injector ID","Injector Device Packaged By"};
//space

                   Result.demographics = new LinkedHashMap<String, String>();
                    for (int i = 0; i < values.length; i++) {
                       Result.demographics.put(keys[i], values[i]);
                    }
                } catch (Exception e) {

                }



            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.FaceCompressedImage) {
                Result.compressedImage = record.getData();

            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.FingerTemplateR1) {

              Result.fingerprints.put(1, record.getData());

            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.FingerTemplateR2) {

                Result.fingerprints.put(2, record.getData());

            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.FingerTemplateR3) {

                Result.fingerprints.put(3, record.getData());

            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.FingerTemplateR4) {

               Result.fingerprints.put(4, record.getData());

            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.FingerTemplateR5) {

              Result.fingerprints.put(5, record.getData());

            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.FingerTemplateL1) {

               Result.fingerprints.put(6, record.getData());

            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.FingerTemplateL2) {

             Result.fingerprints.put(7, record.getData());

            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.FingerTemplateL3) {

              Result.fingerprints.put(8, record.getData());

            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.FingerTemplateL4) {

                Result.fingerprints.put(9, record.getData());

            } else if (record.getType() == ITLVRecord.IDEncodeFieldType.FingerTemplateL4) {

             Result.fingerprints.put(10, record.getData());

            }

//            else if (record.getType() == ITLVRecord.IDEncodeFieldType.Expiry_Date) {
////
////                Result.expiryDate= new String(record.getData());
////
////            }


        }
    }
}
