package saucedemo_webuitesting.uiview.modals;

import core.actionbase.WebAction;
import core.uibase.BaseWebUI;

public class UIModalDialog<M,V> extends BaseWebUI<M,V> {

    public UIModalDialog(M m, V v, WebAction action)
    {
        super(m,v, action);
    }

}
