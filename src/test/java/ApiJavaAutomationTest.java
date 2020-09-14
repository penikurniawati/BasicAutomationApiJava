import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

public class ApiJavaAutomationTest {
    final static String firstUrl = "http://dummy.restapiexample.com";
    final static String secondUrl = "https://reqres.in";

    /*
        Hallo ini adalah initial state terkait java rest assured kita.
        Coba run class ini, dan pastikan program berjalan dan menghasilkan
        output "The response status is 200".
        Segera kontak tim Atlas jika menemui kendala!

        Nantikan berbagai latihan dan problem set di kelas nanti!
     */

    @Test
    public void firstTrial(){
        Response response = given().baseUri(firstUrl).basePath("/api/v1").contentType(ContentType.JSON)
                .get("/employees");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("The response status is " +statusCode);
        System.out.println("Response body = " + response.getBody().prettyPrint());
        System.out.println("First name employee = " + response.path("data.employee_name[0]"));
        System.out.println("Second age employee = " + response.path("data.employee_age[1]"));
    }

    @Test
    public void getResponseBody(){
        Response response =
                given().log().all(). //log().all() fungsinya untuk menampilkan seluruh request yang diminta untuk mengecek apakah sudah benar atau belum
                        baseUri(secondUrl).
                        basePath("/api/").
                        contentType(ContentType.JSON).
//                          fungsi untuk menampilkan halaman ke2, dengan jumlah data tiap halaman sebanyak 4, dan dicari data dengan id 8
                        param("page", 2).
                        param( "per_page", 4).
                        param("id", 8).
                        get("users");

        response.getBody().prettyPrint();
    }

    @Test
    public void getFirstEmployeeName(){
        Response response =
                given().log().all().
                        baseUri(secondUrl).
                        basePath("/api/").
                        contentType(ContentType.JSON).
                        param("page", 1).
                        param("per_page",1).
                        param("id",1).
                        get("users");

        String firstName = response.path("data.first_name");
        System.out.println("First name employee is = " + firstName);
        response.getBody().prettyPrint();
    }

    @Test
    public void tryQueryParameters(){

    }

    @Test
    public void tryPathParameters(){
//        Response response = given().log().all().
//                baseUri(secondUrl).
//                basePath("/api/").
//                contentType(ContentType.JSON).
//                pathParam("id", 1).
//                get("users/{id}");
//
//        Assert.assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 2000);
//        System.out.println("response time = " + response.getTimeIn(TimeUnit.MILLISECONDS));
//        response.getBody().prettyPrint();
    }

    @Test
    public void tryVerifyEmployee(){

    }

//    @Test
//    public  void postCreateEmployee(){
//        String bodyRequest = "{\"name\":\"test\",\"salary\":\"123\",\"age\":\"23\"}";
//
//        Response response =
//                given().baseUri(firstUrl).basePath("/api/v1")
//                .contentType(ContentType.JSON).
//                        body(bodyRequest).
//                        post("/create");
//
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.path("status"),"success");
//        Assert.assertEquals(response.getStatusCode(), 200);
//        Assert.assertEquals(response.path("data.name"), "Atlas");
//        Assert.assertEquals(response.path("data.salary"), "1000");
//        Assert.assertEquals(response.path("data.age"), "17");
//        int idEmployee = response.path("data.id");
//        System.out.println("id employee = " + idEmployee);
//    }
    @Test
    public void registerUserSuccessful(){
        String bodyRequest = "{\n" +
                " \"email\": \"eve.holt@reqres.in\",\n" +
                " \"password\": \"pistol\"\n" +
                " }";

        Response response = given().log().all().baseUri(secondUrl).basePath("/api").
                contentType(ContentType.JSON).
                body(bodyRequest).
                post("/register");

        response.getBody().prettyPrint();
//        email dan password tidak masuk dalam assert karena mereka tidak ada di dalam response
//        hanya data dalam response yang bisa diassert
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertNotNull("token");
        Assert.assertNotNull("id");
    }

    @Test
    public void registerUserUnsuccessful(){
        String bodyRequest = "{\n" +
                " \"email\": \"sydney@fife\"\n" + //jangan ada koma di akhir data
                " }";

        Response response = given().log().all().baseUri(secondUrl).basePath("/api").
                contentType(ContentType.JSON).
                body(bodyRequest).
                post("/register");

        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.path("error"),"Missing password");
    }

    @Test
    public void loginUserSuccessful(){
        String bodyRequest = "{\n" +
                " \"email\": \"eve.holt@reqres.in\",\n" +
                " \"password\": \"cityslicka\"\n" +
                " }";

        Response response = given().log().all().baseUri(secondUrl).basePath("/api").
                contentType(ContentType.JSON).
                body(bodyRequest).
                post("/login");

        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertNotNull("token");
    }

    @Test
    public void loginUserUnsuccessful(){
        String bodyRequest = "{\n" +
                " \"email\": \"peter@klaven\"\n" +
                " }";

        Response response = given().log().all().baseUri(secondUrl).basePath("/api").
                contentType(ContentType.JSON).
                body(bodyRequest).
                post("/login");

        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.path("error"),"Missing password");
    }

    @Test
    public void postCreateUser(){
        String bodyRequest = "{\n" +
                " \"name\": \"morpheus\",\n" +
                " \"job\": \"leader\"\n" +
                " }";

        Response response = given().log().all().baseUri(secondUrl).basePath("/api").
                contentType(ContentType.JSON).
                body(bodyRequest).
                post("/users");

        response.getBody().prettyPrint();
        Assert.assertEquals(response.path("name"),"morpheus");
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.path("job"), "leader");
        Assert.assertNotEquals(response.getStatusCode(), 404);
    }

//  di bawah ini adalah update dengan PUT, PUT artinya kita harus menulis semua data baik yang diubah maupun tidak
    @Test
    public void updateUser(){
        String bodyRequest = "{\n" +
                " \"name\": \"morpheus\",\n" +
                " \"job\": \"leader\"\n" +
                " }";

        Response response = given().log().all().baseUri(secondUrl).basePath("/api").
                contentType(ContentType.JSON).
                body(bodyRequest).
                post("/users");

        response.getBody().prettyPrint();
//       response di sini tidak harus response tapi bebas karena dia sebuah variable
        Assert.assertEquals(response.path("name"),"morpheus");
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.path("job"), "leader");
        Assert.assertNotEquals(response.getStatusCode(), 404);
        String idUser = response.path("id");

        String updateRequest = "{\n" +
                " \"name\": \"morpheus\",\n" +
                " \"job\": \"CEO\"\n" +
                " }";

        Response updateResponse = given().log().all().baseUri(secondUrl).basePath("/api").
                contentType(ContentType.JSON).
                body(updateRequest).
                pathParam("id", idUser).
                put("users/{id}");
//              put("users/2");

        updateResponse.getBody().prettyPrint();
        Assert.assertEquals(updateResponse.path("name"),"morpheus");
        Assert.assertEquals(updateResponse.getStatusCode(), 200);
        Assert.assertEquals(updateResponse.path("job"), "CEO");
        Assert.assertNotEquals(response.getStatusCode(), 404);
    }

    @Test
    public void updateUserPatch(){
        String bodyRequest = "{\n" +
                " \"name\": \"morpheus\",\n" +
                " \"job\": \"leader\"\n" +
                " }";

        Response response = given().log().all().baseUri(secondUrl).basePath("/api").
                contentType(ContentType.JSON).
                body(bodyRequest).
                post("/users");

        response.getBody().prettyPrint();
//       response di sini tidak harus response tapi bebas karena dia sebuah variable
        Assert.assertEquals(response.path("name"),"morpheus");
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.path("job"), "leader");
        Assert.assertNotEquals(response.getStatusCode(), 404);
        String idUser = response.path("id");

        String updateRequest = "{\n" +
                " \"job\": \"CEO\"\n" +
                " }";

        Response updateResponse = given().log().all().baseUri(secondUrl).basePath("/api").
                contentType(ContentType.JSON).
                body(updateRequest).
                pathParam("id", idUser).
                patch("users/{id}");

        updateResponse.getBody().prettyPrint();
        Assert.assertEquals(response.path("name"),"morpheus");
        Assert.assertEquals(updateResponse.getStatusCode(), 200);
        Assert.assertEquals(updateResponse.path("job"), "CEO");
        Assert.assertNotEquals(response.getStatusCode(), 404);
    }

    @Test
    public void deleteEmployee(){
        String bodyRequest = "{\n" +
                " \"name\": \"morpheus\",\n" +
                " \"job\": \"leader\"\n" +
                " }";
        Response response = given().log().all().baseUri(secondUrl).basePath("/api").
                contentType(ContentType.JSON).
                body(bodyRequest).
                post("/users");

        response.getBody().prettyPrint();
        Assert.assertEquals(response.path("name"),"morpheus");
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.path("job"), "leader");
        Assert.assertNotEquals(response.getStatusCode(), 404);
        String idUser = response.path("id");

        Response deleteResponse = given().log().all().baseUri(secondUrl).basePath("/api").
                contentType(ContentType.JSON).
                pathParam("id", idUser).
                delete("users/{id}");

        deleteResponse.getBody().prettyPrint();
        Assert.assertEquals(deleteResponse.statusCode(),204);

    }

}
