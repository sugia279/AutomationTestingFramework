package githubsample_webapitesting.testclasses;

import core.base_action.RestAction;
import core.test_execution.BaseTest;

public abstract class GitHubWebAPITest extends BaseTest {
    public GitHubWebAPITest(){
        baseAction.setRestAction(new RestAction());

        setHighLevelActionPackage("githubsample_webapitesting.keywords");
        testDataManager.setTestDataPath("src/test/java/githubsample_webapitesting/suites/");

        testVars.getRuntimeVars().put("githubapi","https://api.github.com");
        testVars.getRuntimeVars().put("authorization","token adbd023ee1a484a0878a7df920bc346f90ec56d1");
    }
}
