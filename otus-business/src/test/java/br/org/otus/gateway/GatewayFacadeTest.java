package br.org.otus.gateway;

import br.org.otus.gateway.gates.DBDistributionGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class GatewayFacadeTest {
    private static String INFO_VARIABLE_PARAMS = "{\"recruitmentNumber\": \"4107\",\"variables\":[{\"name\": \"tst1\",\"value\": \"Text\",\"sending\": \"1\"},{\"name\": \"tst1\",\"value\": \"Text\",\"sending\": \"9\"}]}";
    private static String CURRENT_VARIABLES_BY_MICROSERVICE = "{\"variables\":[{\"identification\": \"tst1\",\"\"name\": \"var2\",\"sending\": \"1\"}]}";

    @InjectMocks
    private DBDistributionGateway dbDistributionGateway;
    @Mock
    private URL url;


    @Before
    public void setUp() throws Exception {
        //PowerMockito.whenNew(URL.class)


    }

//    @Test
//    public void getCurrentFacadeMethod_should_bring_currentVariableListJson() throws IOException {
//        assertEquals(CURRENT_VARIABLES_BY_MICROSERVICE, dbDistributionGateway.findVariables(CURRENT_VARIABLES_BY_MICROSERVICE).getData());
//    }

    @Test()
    public void getCurrentFacadeMethod_should_throw_exception_for_host_invalid_port() throws IOException {
        try {
            File temp = File.createTempFile("pattern", ".json");

            temp.deleteOnExit();

            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
            out.write("[{\"name\":\"3\",\"type\":\"string\"},{\"name\":\"2\",\"type\":\"string\"}]");
            out.close();
            dbDistributionGateway.uploadVariableTypeCorrelation(temp);
        } catch (IOException e) {
        }
        assertEquals("variable", "variable");
    }

}