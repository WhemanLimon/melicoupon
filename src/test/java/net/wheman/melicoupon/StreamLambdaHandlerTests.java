package net.wheman.melicoupon;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StreamLambdaHandlerTests {
    
    @Test
    void checkLambdaSetupIsValid(){
        StreamLambdaHandler streamLambdaHandler = new StreamLambdaHandler();
        InputStream inputStream = new InputStream(){
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        OutputStream outputStream = new OutputStream(){

            @Override
            public void write(int b) throws IOException {
                
            }
            
        };
        Context context = new Context(){

            @Override
            public String getAwsRequestId() {
                return null;
            }

            @Override
            public ClientContext getClientContext() {
                return null;
            }

            @Override
            public String getFunctionName() {
                return null;
            }

            @Override
            public String getFunctionVersion() {
                return null;
            }

            @Override
            public CognitoIdentity getIdentity() {
                return null;
            }

            @Override
            public String getInvokedFunctionArn() {
                return null;
            }

            @Override
            public String getLogGroupName() {
                return null;
            }

            @Override
            public String getLogStreamName() {
                return null;
            }

            @Override
            public LambdaLogger getLogger() {
                return null;
            }

            @Override
            public int getMemoryLimitInMB() {
                return 0;
            }

            @Override
            public int getRemainingTimeInMillis() {
                return 0;
            }
            
        };
        assertDoesNotThrow(() -> streamLambdaHandler.handleRequest(inputStream, outputStream, context));
    }
}
