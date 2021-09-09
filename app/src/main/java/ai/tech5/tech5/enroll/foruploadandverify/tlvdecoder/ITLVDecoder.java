package ai.tech5.tech5.enroll.foruploadandverify.tlvdecoder;

import java.util.List;

public interface ITLVDecoder {
    List<ITLVRecord> decode(byte[] tlvEncodedData) throws TLVDecodeException, IllegalArgumentException;
}
