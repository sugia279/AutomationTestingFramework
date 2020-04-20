package githubsample_webapitesting.testclasses.repositories;

import githubsample_webapitesting.testclasses.GitHubWebAPITest;
import org.testng.annotations.DataProvider;

public class RepositoriesTest extends GitHubWebAPITest {

    @DataProvider
    @Override
    protected Object[] testDataSet() {
        String[] paths = {
                "Repositories/CRUDRepository.json",
                "Repositories/Branches.json"
        };
        return fetchDataToDataSet(paths);
    }
}
