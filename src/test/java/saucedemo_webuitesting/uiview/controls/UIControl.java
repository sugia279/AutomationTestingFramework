package saucedemo_webuitesting.uiview.controls;

import core.actionbase.WebAction;
import core.uibase.BaseWebUI;

public class UIControl<M,V> extends BaseWebUI<M,V> {

    public UIControl(M m, V v, WebAction action)
    {
        super(m,v, action);
    }
}
