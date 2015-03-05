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
    static ArrayList<String> alreadyAsked = new ArrayList<String>();

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


        String msg = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <meta name=\"description\" content=\"\">\n" +
                "    <meta name=\"author\" content=\"\">\n" +
                "    <title>Сайт о веломаршрутах Татарстана и окрестностей Казани</title><!-- Bootstrap Core CSS -->\n" +
                "    <link class=\"\" href=\"/favicon.ico\" rel=\"shortcut icon\" type=\"image/x-icon\">\n" +
                "    <link href=\"/css/bootstrap.min.css?1\" rel=\"stylesheet\"><!-- Custom CSS -->\n" +
                "    <link href=\"/css/blog-post.css?1\" rel=\"stylesheet\"><!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries --><!-- WARNING: Respond.js doesn't work if you view the page via file:// --><!--[if lt IE 9]>\n" +
                "        <script src=\"https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js\"></script>\n" +
                "        <script src=\"https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js\"></script>\n" +
                "    <![endif]-->\n" +
                "  </head><body><h1>Crawler Test1 - </h1>\n";


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
            session.getCookies().set("JSESSIONID", randomizer, 10000);
        }


        resp.addHeader("Last-Modified", "Sun, 22 Feb 2015 13:34:37 GMT");
        resp.addHeader("Server", "Jetty(9.1.4.v20140401)");
        resp.setChunkedTransfer(false);

        if (alreadyAsked.contains(session.getUri().toString())) {
            //resp.setStatus(Response.Status.NOT_MODIFIED);
            System.out.println("Already asked URI " + session.getUri().toString());
        } else {
            if (!session.getMethod().equals(Method.HEAD)) {
                alreadyAsked.add(session.getUri().toString());
                System.out.println("First time asked URI " + session.getUri().toString());
            }
        }


        return resp;
    }


    public static void main(String[] args) {
        ServerRunner.run(CrawlerTest1.class);
    }

}
