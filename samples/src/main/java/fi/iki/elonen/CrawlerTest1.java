package fi.iki.elonen;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;

/**
 * Created by Ilya Evlampiev on 05.03.2015.
 */
public class CrawlerTest1 extends NanoHTTPD {
    public CrawlerTest1() {
        super(8181);
    }

    @Override
    public Response serve(IHTTPSession session) {
        int generation = 0;
        Method method = session.getMethod();
        String uri = session.getUri();
        System.out.println(method + " '" + uri + "' ");

        SecureRandom random = new SecureRandom();
        String randomizer = new BigInteger(130, random).toString(32);

        String msg = "<html><body><h1>Crawler Test1 - " + randomizer + "</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("generation") == null) {
            msg +=
                    "<a href=\"/" + randomizer + "?generation=1&rand=" + randomizer + "\">" + "Generation " + generation + "</a>";
            msg += "<br>";
            msg += "<a href=\"/" + randomizer + "?generation=1&rand=" + randomizer + "\">" + "Generation " + generation + "</a>";
        } else {
            generation = Integer.parseInt(parms.get("generation"));
            System.out.println(generation);
            if (generation < 5) {
                msg += (new Date()).toString() + ": <a href='/" + randomizer + "?generation=" + (generation + 1) + "&rand=" + randomizer + "'>" + "Generation " + (generation) + "</a>";
                msg += "<br>";
                msg += (new Date()).toString() + ": <a href='/" + randomizer + "?generation=" + (generation + 1) + "&rand=" + randomizer + "'>" + "Generation " + (generation) + "</a>";
            } else {
                msg += (new Date()).toString() + ": <a href='/a?generation=" + (generation) + "'>" + "Generation " + (generation) + "</a>";
            }


        }
        msg += "</body></html>\n";

        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Last-Modified:Sun, 22 Feb 2015 13:34:37 GMT
        //Server:Jetty(9.1.4.v20140401)
        Response resp=new NanoHTTPD.Response(msg);
        resp.addHeader("Last-Modified","Sun, 22 Feb 2015 13:34:37 GMT");
        resp.addHeader("Server","Jetty(9.1.4.v20140401)");
        resp.setChunkedTransfer(false);
        return resp ;
    }


    public static void main(String[] args) {
        ServerRunner.run(CrawlerTest1.class);
    }

}
