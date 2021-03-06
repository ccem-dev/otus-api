package br.org.mongodb.codecs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bson.BsonBinary;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;
import org.ccem.otus.model.FileUploader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ObjectId.class)
public class FileUploaderCodecTest {
  private static final String OID_EXPECTED = "5915a80c7b65e45cda0d3e59";
  private static final String TEXT = "Otus";
  @InjectMocks
  private FileUploaderCodec fileUploaderCodec;
  @Mock
  private BsonWriter writer;
  @Mock
  private BsonReader reader;
  @Mock
  private BsonBinary bsonBinary;
  @Mock
  private ObjectId objectId;
  private byte[] file;
  private FileUploader fileUploader;
  private EncoderContext encoderContext;
  private DecoderContext decoderContext;

  @Before
  public void setUp() throws Exception {
    file = TEXT.getBytes();
    fileUploader = spy(new FileUploader(file));
  }

  @Test
  public void method_encode_should_evocate_functions_that_started_and_finished_BsonWriterDocument() throws Exception {
    encoderContext = EncoderContext.builder().build();
    fileUploaderCodec.encode(writer, fileUploader, encoderContext);
    verify(writer).writeStartDocument();
    verify(fileUploader).setOid(anyString());
    verify(writer).writeObjectId(anyString(), anyObject());
    verify(writer).writeBinaryData(anyString(), anyObject());
    verify(writer).writeEndDocument();
  }

  @Test
  public void method_Decode_should_return_FileUploaderObject() {
    decoderContext = DecoderContext.builder().build();
    when(reader.readObjectId()).thenReturn(objectId);
    when(objectId.toString()).thenReturn("5915a80c7b65e45cda0d3e59");
    when(reader.readBinaryData(anyString())).thenReturn(bsonBinary);
    when(bsonBinary.getData()).thenReturn(file);
    assertEquals(OID_EXPECTED, fileUploaderCodec.decode(reader, decoderContext).getOid());
    verify(reader).readStartDocument();
    verify(reader).readObjectId();
    verify(reader).readBinaryData(anyString());
    verify(reader).readEndDocument();
  }

}
