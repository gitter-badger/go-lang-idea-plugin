package ro.redeul.google.go.lang.psi;

import com.intellij.psi.PsiDirectoryContainer;

import java.util.Collection;

public interface GoPackage extends GoPsiElement, PsiDirectoryContainer {

    public String getImportPath();

    public String getName();

    GoFile[] getFiles();
}
