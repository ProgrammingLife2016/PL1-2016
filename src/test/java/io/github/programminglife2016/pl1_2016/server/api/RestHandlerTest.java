package io.github.programminglife2016.pl1_2016.server.api;

import org.junit.Before;

/**
 * Run the ApiHandlerTest tests with a RestHandler object.
 */
public class RestHandlerTest extends ApiHandlerTest {
    /**
     * Use a RestHandler as test object.
     */
    @Before
    public void setUp() {
        setApiHandler(new RestHandler());
    }
}
