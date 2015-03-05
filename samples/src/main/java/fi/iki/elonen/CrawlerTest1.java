package fi.iki.elonen;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Ilya Evlampiev on 05.03.2015.
 */
public class CrawlerTest1 extends NanoHTTPD {
    static ArrayList<String> alreadyAsked=new ArrayList<String>();

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


        String msg = "<html><body><h1>Crawler Test1 - </h1>\n";


        Map<String, String> parms = session.getParms();
        if (parms.get("generation") == null) {
            msg +=
                    "<a href=\"/?generation=1\">" + "Generation " + generation + "</a>";

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
        Response resp = new NanoHTTPD.Response(msg);

        //Last-Modified:Sun, 22 Feb 2015 13:34:37 GMT
        //Server:Jetty(9.1.4.v20140401)
        if (parms.get("generation") == null) {
            //resp.addHeader("Set-Cookie", "JSESSIONID=" + randomizer);
            session.getCookies().set("JSESSIONID",randomizer,10000);
        }


        resp.addHeader("Last-Modified", "Sun, 22 Feb 2015 13:34:37 GMT");
        resp.addHeader("Server", "Jetty(9.1.4.v20140401)");
        resp.setChunkedTransfer(false);

        if (alreadyAsked.contains(session.getUri().toString()))
        {
           resp.setStatus(Response.Status.NOT_MODIFIED);
            System.out.println("Already asked URI "+session.getUri().toString());
        }
        else
        {
            alreadyAsked.add(session.getUri().toString());
            System.out.println("First time asked URI "+session.getUri().toString());

        }


        return resp;
    }


    public static void main(String[] args) {
        ServerRunner.run(CrawlerTest1.class);
    }

}
