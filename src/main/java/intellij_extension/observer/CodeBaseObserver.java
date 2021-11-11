package intellij_extension.observer;

import intellij_extension.models.CodeBase;

public interface CodeBaseObserver
{
    void refresh(CodeBase codeBase);
}
