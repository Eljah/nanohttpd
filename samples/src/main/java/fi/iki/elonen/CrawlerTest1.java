package fi.iki.elonen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

        if (uri.equals("/favicon.ico")) {
            InputStream is;

            try {
                FileInputStream fis = new FileInputStream("favicon.ico");
                is=fis;
                System.out.println("Requesting /favicon.ico");
                fis.close();     //todo comment it; doesn't respond http correctly with this line
                Response resp = new Response(Response.Status.OK, "image/png", is);
                return resp;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } else {


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
                    "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css\">\n<link class=\"\" href=\"/favicon.ico\" rel=\"shortcut icon\" type=\"image/x-icon\">\n" +
                    "        <script src=\"https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js\"></script>\n" +
                    "        <script src=\"https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js\"></script>\n" +
                    "    <![endif]-->\n" +
                    "  </head><body><h1>Crawler Test1 - </h1> <body style=\"padding: 20px\"><!-- Navigation -->\n" +
                    "    <nav class=\"navbar navbar-inverse\" role=\"navigation\">\n" +
                    "      <div class=\"container\"><!-- Brand and toggle get grouped for better mobile display -->\n" +
                    "        <div class=\"navbar-header\">\n" +
                    "          <button class=\"navbar-toggle\" type=\"button\" data-toggle=\"collapse\" data-target=\"#bs-example-navbar-collapse-1\"><span class=\"sr-only\">Toggle navigation</span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span></button><a class=\"navbar-brand\" href=\"index\">Главная</a>\n" +
                    "        </div><!-- Collect the nav links, forms, and other content for toggling -->\n" +
                    "      </div><!-- /.container -->\n" +
                    "    </nav><!-- Page Content -->\n" +
                    "    <div class=\"container\">\n" +
                    "      <div>\n";


            Map<String, String> parms = session.getParms();
            if (parms.get("generation") == null) {
                msg +=
                        "<a href=\"/b?generation=1\">" + "Generation " + generation + "</a>";

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
            msg += "</div></div></body></html>\n";

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
            resp.addHeader("Cache-Control", "no-cache, must-revalidate, proxy-revalidate");
            resp.setChunkedTransfer(false);

            for (Map.Entry<String, String> entry : session.getHeaders().entrySet()) {
                System.out.println(entry.getKey() + "/" + entry.getValue());
            }

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
        return null;
    }


    public static void main(String[] args) {
        ServerRunner.run(CrawlerTest1.class);
    }

}
