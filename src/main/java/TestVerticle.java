import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestVerticle extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(TestVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        Router router = Router.router(vertx);
        router.get("/api/baeldung/articles/article/:id")
                .handler(this::getArticles);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                startPromise.complete();
                            } else {
                                startPromise.fail(result.cause());
                            }
                        });
    }

    @Override
    public void stop() throws Exception {
        logger.info("Stop");
    }

    private void getArticles(RoutingContext routingContext) {
        String articleId = routingContext.request()
                .getParam("id");
        Article article = new Article(articleId,
                "This is an intro to vertx", "baeldung", "01-02-2017", 1578);

        routingContext.response()
                .putHeader("content-type", "application/json")
                .setStatusCode(200)
                .end(Json.encodePrettily(article));
    }
}
