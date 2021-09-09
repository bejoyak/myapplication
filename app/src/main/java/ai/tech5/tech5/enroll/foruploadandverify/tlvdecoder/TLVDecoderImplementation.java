package ai.tech5.tech5.enroll.foruploadandverify.tlvdecoder;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TLVDecoderImplementation implements ITLVDecoder {

    @Override
    public List<ITLVRecord> decode(byte[] tlvEncodedData) throws TLVDecodeException, IllegalArgumentException {
        if (tlvEncodedData == null || tlvEncodedData.length < 2) {
            throw new IllegalArgumentException("tlvEncodedData length can't be < 2");
        }

        ByteArrayInputStream btStream = new ByteArrayInputStream(tlvEncodedData);
        DataInputStream stream = new DataInputStream(btStream);

        List<ITLVRecord> result = new ArrayList<ITLVRecord>();

        try {
            byte openFirstByte = stream.readByte();
            byte openSecondByte = stream.readByte();
            boolean withoutExpTime = openFirstByte == 'P' && openSecondByte == 'K';

            int intFirstByte = openFirstByte & 0xFF;
            int intSecondByte = openSecondByte & 0x55;
            boolean withExpTime = intFirstByte == 0xFF && intSecondByte == 0x55;

            if (!withoutExpTime && !withExpTime) {
                throw new TLVDecodeException("Wrong first 2 bytes!");
            }

            if (withExpTime) {
                if (stream.available() < Integer.BYTES) {
                    throw new TLVDecodeException("Data corrupted!");
                }

                // we have unsigned int here
                long type = stream.readInt();

                if (stream.available() == 0) {
                    throw new TLVDecodeException("Expired!");
                }
            }

            int fullDataLength = 0;

            while (stream.available() > Short.BYTES * 2) {
                int type = stream.readShort();

                short recordDataLength = stream.readShort();
                if (stream.available() < recordDataLength) {
                    throw new TLVDecodeException("Data length corrupted!");
                }

                byte[] tmp = new byte[recordDataLength];
                stream.read(tmp, 0, recordDataLength);

                ITLVRecord.IDEncodeFieldType enumType = ITLVRecord.IDEncodeFieldType.fromInteger(type);
                if (enumType == null) {
                    throw new TLVDecodeException("Unknown TLV type!");
                }
                TLVRecordExample record = new TLVRecordExample(enumType, tmp);
                result.add(record);

                fullDataLength += Short.BYTES * 2 + recordDataLength;
            }

            // if data length is odd you need to check:
            // 1 - only 1 byte left;
            // 2 - this byte is 0.
            // if data length is even you need to check:
            // 1 - there is no data left;

            if (fullDataLength % 2 != 0) {
                if (stream.available() != 1 || stream.readByte() != 0x00) {
                    throw new TLVDecodeException("Wrong alignment!");
                }
            }
            else {
                if (stream.available() != 0) {
                    throw new TLVDecodeException("Wrong alignment!");
                }
            }

        } catch (IOException e) {
            throw new TLVDecodeException("Data corrupted!", e);
        }

        return result;
    }
}
