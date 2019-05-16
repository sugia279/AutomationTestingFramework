package core.uibase;

import core.actionbase.SoftAssertExt;

public class BaseWebUIValidator<M>  {
    private M map;
    protected SoftAssertExt softAssert;
    public BaseWebUIValidator() {
        softAssert = new SoftAssertExt();
    }

    public M Map() {
        return map;
    }
    public void setMap(M m) {
        map = m;
    }

    public SoftAssertExt getSoftAssert() {
        return softAssert;
    }
}
