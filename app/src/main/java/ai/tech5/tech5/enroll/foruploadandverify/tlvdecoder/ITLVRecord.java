package ai.tech5.tech5.enroll.foruploadandverify.tlvdecoder;

public interface ITLVRecord {
    enum IDEncodeFieldType
    {
        None(0),

        // Face related
        Faces__First (1),
        FaceImage(2),
        FaceTemplate(3),
        FaceCompressedImage(4),
        Faces__Last(5),

        // Fingers related
        Fingers__First(50),
        RightSlap(51),
        FingerImageR1(52),
        FingerTemplateR1(53),
        FingerImageR2(54),
        FingerTemplateR2(55),
        FingerImageR3(56),
        FingerTemplateR3(57),
        FingerImageR4(58),
        FingerTemplateR4(59),
        FingerImageR5(60),
        FingerTemplateR5(61),
        LeftSlap(62),
        FingerImageL1(63),
        FingerTemplateL1(64),
        FingerImageL2(65),
        FingerTemplateL2(66),
        FingerImageL3(67),
        FingerTemplateL3(68),
        FingerImageL4(69),
        FingerTemplateL4(70),
        FingerImageL5(71),
        FingerTemplateL5(72),
        Fingers__Last(73),

        // Eyes related
        Eyes__First(100),
        IrisImageR(101),
        IrisTemplateR(102),
        IrisImageL(103),
        IrisTemplateL(104),
        Eyes__Last(105),

        // Voice related
        Voice__First(150),
        VoiceSample(151),
        VoiceTemplate(152),
        Voice__Last(153),

        // Others
        Extra__First(1000),
        Extra(1001),
        Demog(1002),
        DigitalCertificate(1003),
        BinaryBlob(1004),
        CryptographId(1005),
        Extra__Last(1006);

        public final int _val;

        IDEncodeFieldType(int val)
        {
            _val = val;
        }

        public static IDEncodeFieldType fromInteger(int x) {
            IDEncodeFieldType[] values = IDEncodeFieldType.values();
            for (IDEncodeFieldType v : values ) {
                if(v._val == x){
                    return v;
                }
            }
            return null;
        }
    }

    IDEncodeFieldType getType();
    byte[] getData();
}
