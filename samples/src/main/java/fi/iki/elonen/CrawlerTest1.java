package fi.iki.elonen;

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

        String msg = "<html><body><h1>Crawler Test1</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("generation") == null)
            msg +=
                    "<a href='?generation=1'>" + "Generation " + generation + "</a>";
        else {
            generation = Integer.parseInt(parms.get("generation"));
            System.out.println(generation);
            if (generation < 5) {
                msg += (new Date()).toString() + ": <a href='?generation=" + (generation + 1) + "'>" + "Generation " + (generation) + "</a>";
            } else {
                msg += (new Date()).toString() + ": <a href='?generation=" + (generation) + "'>" + "Generation " + (generation) + "</a>";
            }


        }
        msg += "</body></html>\n";

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new NanoHTTPD.Response(msg);
    }


    public static void main(String[] args) {
        ServerRunner.run(CrawlerTest1.class);
    }

}
