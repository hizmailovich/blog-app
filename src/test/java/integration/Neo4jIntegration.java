package integration;

import com.solvd.blog.BlogApplication;
import com.solvd.blog.fake.FkPost;
import com.solvd.blog.fake.FkUser;
import com.solvd.blog.model.Post;
import com.solvd.blog.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Query;
import org.neo4j.driver.Session;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@SuppressWarnings("JTCOP.RuleAllTestsHaveProductionClass")
@SpringBootTest(classes = BlogApplication.class)
public class Neo4jIntegration {

    protected static final Neo4jContainer container = new Neo4jContainer(
            DockerImageName.parse("neo4j:5.9.0")
    ).withoutAuthentication();

    @BeforeAll
    public static void init() {
        container.start();
    }

    @BeforeAll
    public static void setUp() {
        try (Driver driver = GraphDatabase.driver(container.getBoltUrl());
             Session session = driver.session()) {
            User user = new FkUser("Name", "Email");
            Post post = new FkPost();
            Query query = new Query(
                    "CREATE (u:User {name:$name, email:$email}) "
                            + "CREATE (p:Post {title:$title, content:$content, date:$date}) "
                            + "CREATE (p)-[:MAINTAINED]->(u)",
                    Map.of("name", user.name(),
                            "email", user.email(),
                            "title", post.title(),
                            "content", post.content(),
                            "date", post.date()
                    )
            );
            session.run(query);
        }
    }

    @AfterAll
    public static void stop() {
        container.stop();
    }

}
