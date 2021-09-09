package ai.tech5.tech5.enroll.foruploadandverify.tlvdecoder;

public class TLVRecordExample implements ITLVRecord {
    private IDEncodeFieldType type;
    private byte[] data;

    TLVRecordExample(IDEncodeFieldType typeIn, byte[] rawData) {
        type = typeIn;
        data = rawData;
    }

    @Override
    public IDEncodeFieldType getType() {
        return type;
    }

    @Override
    public byte[] getData() {
        return data;
    }
}