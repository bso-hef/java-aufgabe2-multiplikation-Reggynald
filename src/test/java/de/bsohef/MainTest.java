package de.bsohef;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.Invocation;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainTest {
    static InputStream sysInBackup;
    static OutputStream sysOutBackup;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //Read output
    @Spy
    PrintStream out = new PrintStream(outputStream);
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    private ArgumentCaptor<byte[]> byteArrayCaptor;

    Main testObject = new Main();

    @BeforeAll
    static void beforeAll() {
        sysInBackup = System.in;
        sysOutBackup = System.out;
    }

    @AfterAll
    static void afterAll() {
        System.setIn(sysInBackup);
    }

    @Test
    void main() throws IOException {

        System.setOut(out);

        //Prepare console input
        ByteArrayInputStream in = new ByteArrayInputStream("3 4 ".getBytes());
        System.setIn(in);
        //Call Method under test
        testObject.main(null);

        // details of all invocations including methods and arguments
        Collection<Invocation> invocations = Mockito.mockingDetails(out).getInvocations();
        // just a number of calls of any mock's methods
        int numberOfCalls = invocations.size();
        invocations = invocations.stream().filter( invocation -> {
            if(invocation.getMethod().getName().contains("print")){
                return true;
            }
            return false;
        }).collect(Collectors.toCollection(ArrayList::new));
        assertTrue(3 == invocations.size(), "Es müssen drei Ausgaben verwendet werden. \n" +
                "1. Benutzer auffordern eine Zahl einzugeben\n" +
                "2. Benutzer auffordern eine zweite Zahl einzugeben \n" +
                "3. Ergebnis ausgeben");
        //Validate output
        String output = outputStream.toString();
        assertTrue(output.contains("12"), "Product of 3 and 4 is not 12; \noutput was: " + output);
    }
}