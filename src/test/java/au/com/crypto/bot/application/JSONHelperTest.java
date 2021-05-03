package au.com.crypto.bot.application;

import au.com.crypto.bot.application.utils.JSONHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JSONHelperTest {

    public static String jsonString = """
                                        {
                                         "conditionsGroup":[
                                                {
                                                  "name":"sm_reddot_1h_BTC",
                                                  "last_observed": 300,
                                                  "time_frame": 300,
                                                  "symbol": "BTCUSDT",
                                                  "sequence": 1
                                                },
                                                {
                                                  "name":"lr_greendot_5m_BTC",
                                                  "last_observed": 300,
                                                  "time_frame": 500,
                                                  "symbol": "BTCUSDT",
                                                  "sequence": 2
                                                }
                                              ]
                                           }
                                    """;

    @Before
    public void init() {

    }

    @Test
    public void testJSONHelper() {
        Assert.assertEquals(JSONHelper.jsonToHash(jsonString), "e28cd5bf0e2364547d1f7bf2ee661ff5");
    }
 }
