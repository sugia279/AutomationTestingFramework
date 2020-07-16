package githubsample_webapitesting.testclasses;

import core.base_action.RestAction;
import core.test_execution.BaseTest;

public abstract class GitHubWebAPITest extends BaseTest {
    public GitHubWebAPITest(){
        actions.setRestAction(new RestAction());

        setUserKeywordPackage("githubsample_webapitesting.keywords");
        testDataManager.setTestDataPath("src/test/java/githubsample_webapitesting/suites/");

        testVars.getConfigVars().put("githubapi","https://api.github.com");
        testVars.getConfigVars().put("authorization","Basic YXV0b21hdGlvbnRlc3RlcjMwNDpBdXRvVGVzdDMwNA==");
    }
}
