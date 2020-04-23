package githubsample_webapitesting.testclasses;

import core.base_action.RestAction;
import core.test_execution.BaseTest;

public abstract class GitHubWebAPITest extends BaseTest {
    public GitHubWebAPITest(){
        baseAction.setRestAction(new RestAction());

        setUserKeywordPackage("githubsample_webapitesting.keywords");
        testDataManager.setTestDataPath("src/test/java/githubsample_webapitesting/suites/");

        testVars.getRuntimeVars().put("githubapi","https://api.github.com");
        testVars.getRuntimeVars().put("authorization","Basic YXV0b21hdGlvbnRlc3RlcjMwNDpBdXRvVGVzdDMwNA==");
    }
}
