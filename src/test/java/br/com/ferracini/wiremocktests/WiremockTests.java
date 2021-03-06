package br.com.ferracini.wiremocktests;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

/**
 * @author lferracini
 * @project = junit5-advanced
 * @since <pre>06/03/2021</pre>
 */
public class WiremockTests {

    private static final String PORT = "8148";
    private static final String BASE = "http://localhost";
    private static final String URL = BASE.concat(":").concat(PORT);
    static WireMockServer wireMockServer;


    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(Integer.parseInt(PORT));
        wireMockServer.start();
        setupStub();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    private static void setupStub() {
        wireMockServer.stubFor(get(urlEqualTo("/planets"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("responses/json/planets.json")
                )
        );
        wireMockServer.stubFor(
                get(urlEqualTo("/planets/tatooine"))
                        .willReturn(aResponse()
                                .withStatus(503)
                                .withHeader("Content-Type", "text/plain")
                                .withBody("Service is broken"))
        );

        //Setting for one endpoint and two different conditions
        wireMockServer.stubFor(post(urlEqualTo("/some/endpoint"))
                .withHeader("Accept", matching("text/plain"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withStatus(500)
                        .withBody("My bad"))
        );
        wireMockServer.stubFor(get(urlEqualTo("/some/endpoint"))
                .withHeader("Accept", matching("application/json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(201)
                        .withFixedDelay(1500))
        );
    }

    @Test
    public void getPlanetsStatusCodePositive() {
        given()
                .when()
                .get(URL.concat("/planets"))
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void getPlanetsWithIncorrectPathTest() {
        given()
                .when()
                .get(URL.concat("/some/endpoint"))
                .then()
                .assertThat().statusCode(404);
    }

    @Test
    public void getServiceUnavailableTest() {
        given()
                .when()
                .get(URL.concat("/planets/tatooine"))
                .then()
                .assertThat().statusCode(503)
                .and()
                .body(Matchers.equalTo("Service is broken"));
    }

    @Test
    public void someEndpointErrorTest() throws InterruptedException {

        given()
                .header("Accept", "text/plain")
                .when()
                .post(URL.concat("/some/endpoint"))
                .then()
                .assertThat()
                .statusCode(500);
    }

    @Test
    public void someEndpointDelayTest() throws InterruptedException {

        given()
                .header("Accept", "application/json")
                .when()
                .get(URL.concat("/some/endpoint"))
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .header("Content-Type", "application/json")
                .time(Matchers.greaterThan(1500L));
    }
}
