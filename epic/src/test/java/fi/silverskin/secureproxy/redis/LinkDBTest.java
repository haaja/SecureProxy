package fi.silverskin.secureproxy.redis;

import static org.junit.Assert.*;
import org.junit.*;


public class LinkDBTest {

    private LinkDB linkDB;

    public LinkDBTest() {
        linkDB = new LinkDB();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        linkDB.flushAll();
    }

    @Test
    public void testGlobalLinkDB() {
        linkDB.addLink("first_key", "first_value");
        assertEquals("Link pair should have been set.",
                "first_key", linkDB.fetchKey("first_value"));

        linkDB.addLink("first_key", "modified_value");
        assertEquals("Already added link pair should not be modified.",
                "first_key", linkDB.fetchKey("first_value"));
        assertEquals("When adding with existing key, querying new value should "
                + "give empty string.",
                "", linkDB.fetchKey("modified_value"));

        assertEquals("Not existing value should give empty string",
                "", linkDB.fetchKey("not existing value"));
    }

    @Test
    public void testSessionLinkDB() {
        String session1 = "session00001";
        String session2 = "000002session";
        linkDB.addLink("first_key", "first_value", session1, 6000);
        linkDB.addLink("second_key", "second_value", session2, 6000);

        assertEquals("Link pair should have been set.",
                "first_key", linkDB.fetchKey("first_value", session1));
        assertEquals("Link pair should not be reached from global storage.",
                "", linkDB.fetchKey("first_value"));
        assertEquals("Link pair should not be reached from not existing session.",
                "", linkDB.fetchKey("first_value"));
        assertEquals("Link pair should not be reached from another session.",
                "", linkDB.fetchKey("first_value", session2));
    }

    @Test
    public void testSessionLinkDBTimeouts() throws InterruptedException {
        linkDB.addLink("first_key", "first_value", "sessionid", 1);
        Thread.sleep(2000);
        assertEquals("Session should have expired.",
                "", linkDB.fetchKey("first_value", "sessionid"));
    }


	@Test
	public void testAddLinkReturnValue() {
        boolean retval = linkDB.addLink("1_key", "first_value");
            assertTrue("Retval should be true when insert stores something.", retval);

        retval = linkDB.addLink("1_key", "first_value");
            assertFalse("Retval should be false when insert doesn't store..", retval);
	}


	@Test
	public void testFetchValue() {
        linkDB.addLink("first_key", "first_value");
            String actual = linkDB.fetchValue("first_key");

            assertEquals("first_value", actual);
	}
}
