package net.king2500.plugins.PhpAdvancedAutoComplete.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.impl.YAMLBlockMappingImpl;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;

import java.io.File;
import java.util.*;

public class YamlHelper {

    @NotNull
    public static String[] getYamlKeysFromDirectory(Project project) {
        String localeRelativePath = File.separator + "app" + File.separator + "locales";

        VirtualFile localeDirectory = LocalFileSystem.getInstance().findFileByIoFile(new File(project.getBasePath() + localeRelativePath));
        if (localeDirectory == null) {
            return new String[] {};
        }
        VirtualFile[] files = localeDirectory.getChildren();

        Set<String> uniqueResultElements = new HashSet<>();

        for (VirtualFile file : files) {
            if (file.getExtension() != null && file.getExtension().equals("yml")) {
                FileViewProvider fvp = PsiManager.getInstance(project).findViewProvider(file);

                YAMLFileImpl yamlFile;
                if (fvp != null) {
                    yamlFile = new YAMLFileImpl(fvp);

                    YAMLDocument document = yamlFile.getDocuments().get(0);
                    YAMLBlockMappingImpl blockMapping = (YAMLBlockMappingImpl) document.getTopLevelValue();

                    List<YAMLPsiElement> elements;
                    if (blockMapping != null) {
                        elements = blockMapping.getYAMLElements();

                        for (YAMLPsiElement element : elements) {
                            uniqueResultElements.add(element.getName());
                        }
                    }
                }
            }
        }

        List<String> uniqueResultElementsList = new ArrayList<>(uniqueResultElements);
        Collections.sort(uniqueResultElementsList);

        return uniqueResultElementsList.toArray(new String[uniqueResultElementsList.size()]);
    }
}
