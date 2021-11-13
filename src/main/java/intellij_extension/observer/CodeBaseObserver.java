package intellij_extension.observer;


import intellij_extension.models.redesign.CodebaseV2;

public interface CodeBaseObserver
{
    void refresh(CodebaseV2 codeBase);
}
