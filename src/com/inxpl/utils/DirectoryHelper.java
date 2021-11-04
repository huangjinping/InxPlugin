package com.inxpl.utils;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;
import com.inxpl.action.ExtractTextAction;
import com.inxpl.bean.DataBean;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class DirectoryHelper {
    private List<PsiFile> mPsiFile;

    private String[] xmlTagArray = {"string", "color", "style", "array", "dimen", "integer"};

    private static final Logger logger = Logger.getInstance(ExtractTextAction.class);

    public List<PsiFile> getResList(Project project) {
        VirtualFile resBir = getAppResBaseDir(project);
        PsiDirectory directory = PsiDirectoryFactory.getInstance(project).createDirectory(resBir);
        PsiDirectory[] subdirectories = directory.getSubdirectories();
        logger.warn("==========1==psi=====");

        List<PsiFile> psiFileList = new ArrayList<>();
        for (PsiDirectory psi : subdirectories
        ) {
            PsiFile[] files = psi.getFiles();
            logger.warn(psi.getText() + "=======PsiDirectory======psi=====" + psi.getName());
            for (PsiFile file : files) {
                psiFileList.add(file);
                logger.warn(file.getText() + "=======PsiFile======psi=====" + file.getName());
            }

        }
        return psiFileList;

    }

    public static VirtualFile getAppResBaseDir(Project project) {
        String path = project.getBasePath() + File.separator +
                "app" + File.separator +
                "src" + File.separator +
                "main" + File.separator +
                "res" + File.separator;

        return LocalFileSystem.getInstance().findFileByPath(path);
    }


    public static VirtualFile getAppJavaBaseDir(Project project) {
        String path = project.getBasePath() + File.separator +
                "app" + File.separator +
                "src" + File.separator +
                "main" + File.separator +
                "java" + File.separator;

        return LocalFileSystem.getInstance().findFileByPath(path);
    }

    public List<PsiFile> getJavaList(Project project) {
        VirtualFile resBir = getAppJavaBaseDir(project);
        PsiDirectory directory = PsiDirectoryFactory.getInstance(project).createDirectory(resBir);
        mPsiFile = new ArrayList<>();
        getPsiDirectoryClass(directory);
        for (PsiFile file : mPsiFile) {
            logger.warn(file.getText() + "========getJavaList=====getFiles=====" + file.getName());
        }
        return mPsiFile;
    }

    private void getPsiDirectoryClass(PsiDirectory directory) {
        PsiDirectory[] sub = directory.getSubdirectories();
        PsiFile[] files = directory.getFiles();
        if (files != null && files.length > 0) {
            List<PsiFile> psiFiles = Arrays.asList(files);
            mPsiFile.addAll(psiFiles);
        }

        if (sub != null && sub.length > 0) {
            for (PsiDirectory psiDir : sub) {
                getPsiDirectoryClass(psiDir);
            }
        }

    }


    public void changeXml(PsiFile file, Project project, List<DataBean> dataBeans) {
        Runnable runnable = () -> {
            VirtualFile virtualFile = file.getVirtualFile();
            Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
            if (document != null) {
                String text = document.getText();
                for (DataBean attributeValue : dataBeans) {
                    text = text.replace("\"" + attributeValue.getValue() + "\"", "\"" + "@string/" + attributeValue.getKey() + "\"");
                }
                document.setText(text);
            }
        };
        WriteCommandAction.runWriteCommandAction(project, runnable);
    }


    public void changeListFile(List<PsiFile> files, Project project, List<DataBean> dataBeans) {
        Runnable runnable = () -> {
            for (PsiFile file : files) {
                VirtualFile virtualFile = file.getVirtualFile();
                Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
                if (document != null) {
                    String text = document.getText();
                    for (DataBean attributeValue : dataBeans) {
                        text = text.replace(attributeValue.getKey(), attributeValue.getValue());
                    }
                    document.setText(text);
                }
            }
        };
        WriteCommandAction.runWriteCommandAction(project, runnable);
    }


    public PsiFile getMiniFast(Project project) {
        VirtualFile virtualFile = getMiniFastDir(project);
        return PsiManager.getInstance(project).findFile(virtualFile);
    }


    public static VirtualFile getMiniFastDir(Project project) {
        String path = project.getBasePath() + File.separator +
                "app" + File.separator +
                "src" + File.separator +
                "main" + File.separator + "AndroidManifest.xml";

        return LocalFileSystem.getInstance().findFileByPath(path);
    }


    public static VirtualFile getValuesDir(Project project) {
        String path = project.getBasePath() + File.separator +
                "app" + File.separator +
                "src" + File.separator +
                "main" + File.separator +
                "res" + File.separator +
                "values" + File.separator;

        return LocalFileSystem.getInstance().findFileByPath(path);
    }


    public static VirtualFile getLayoutDir(Project project) {
        String path = project.getBasePath() + File.separator +
                "app" + File.separator +
                "src" + File.separator +
                "main" + File.separator +
                "res" + File.separator +
                "layout" + File.separator;

        return LocalFileSystem.getInstance().findFileByPath(path);
    }


    public List<DataBean> handAllIds(Project project) {
        VirtualFile resBir = getLayoutDir(project);
        PsiDirectory directory = PsiDirectoryFactory.getInstance(project).createDirectory(resBir);
        PsiFile[] files = directory.getFiles();
        List<DataBean> dataBeans = new ArrayList<>();
        for (PsiFile file : files) {
            file.accept(new XmlRecursiveElementVisitor(true) {
                @Override
                public void visitXmlTag(XmlTag tag) {
                    super.visitXmlTag(tag);
                    String id = tag.getAttributeValue("android:id");
                    if (id != null && !id.isEmpty() && id.contains("@+id/")) {
                        id = id.replace("@+id/", "");
                        String reName = NameFactory.createName(id);
                        dataBeans.add(new DataBean("\"@+id/" + id + "\"", "\"@+id/" + reName + "\""));
                        dataBeans.add(new DataBean("\"@id/" + id + "\"", "\"@id/" + reName + "\""));
                        dataBeans.add(new DataBean("R.id." + id + "", "R.id." + reName + ""));

                    }
                }
            });
        }
        return dataBeans;
    }

    public List<DataBean> handAllValues(Project project) {
        VirtualFile resBir = getValuesDir(project);
        PsiDirectory directory = PsiDirectoryFactory.getInstance(project).createDirectory(resBir);
        PsiFile[] files = directory.getFiles();
        List<DataBean> dataBeans = new ArrayList<>();
        logger.warn(directory.getText() + "=======handAllValues======psi=====" + directory.getName());
        for (PsiFile file : files) {
            dataBeans.addAll(parseXml(file));
            logger.warn(file.getText() + "=======handAllValues======psi=====" + file.getName());
        }
        return dataBeans;
    }

    private List<DataBean> parseXml(@NotNull PsiFile valueFile) {
        final List<DataBean> dataBeanResult = new ArrayList<>();
        final XmlFile xmlFile = (XmlFile) valueFile;
        final XmlDocument document = xmlFile.getDocument();
        logger.warn("开始手机信息");
        if (document == null) {
            return dataBeanResult;
        }
        final XmlTag rootTag = document.getRootTag();
        if (rootTag == null) {
            return dataBeanResult;
        }

        for (String xmlTag : xmlTagArray) {
            final XmlTag[] stringTags = rootTag.findSubTags(xmlTag);
            if (stringTags != null) {
                for (XmlTag stringTag : stringTags) {
                    final String name = stringTag.getAttributeValue("name");
                    String reName = NameFactory.createName(name);
                    dataBeanResult.add(new DataBean("name=\"" + name + "\"", "name=\"" + reName + "\""));
                    dataBeanResult.add(new DataBean("\"@" + xmlTag + "/" + name + "\"", "\"@" + xmlTag + "/" + reName + "\""));
                    dataBeanResult.add(new DataBean("R." + xmlTag + "." + name, "R." + xmlTag + "." + reName));
                }
            }

        }
        logger.warn("名字手机结束");
        return dataBeanResult;
    }


    public static VirtualFile getAppResDrawableBaseDir(Project project) {
        String path = project.getBasePath() + File.separator +
                "app" + File.separator +
                "src" + File.separator +
                "main" + File.separator +
                "res" + File.separator +
                "drawable" + File.separator;

        return LocalFileSystem.getInstance().findFileByPath(path);
    }


    public List<DataBean> handDrawable(Project project) {
        VirtualFile resBir = getAppResBaseDir(project);

        PsiDirectory directory = PsiDirectoryFactory.getInstance(project).createDirectory(resBir);
        PsiDirectory[] subdirectories = directory.getSubdirectories();
        List<DataBean> dataBeans = new ArrayList<>();

        Map<String, String> changeReName = new HashMap<>();

        for (PsiDirectory psi : subdirectories
        ) {

            String psiName = psi.getName();
//            if (psiName.startsWith("drawable")|| psiName.startsWith("mipmap") || psiName.startsWith("xml")) {
            if (psiName.startsWith("mipmap")) {
                PsiFile[] files = psi.getFiles();
                logger.warn(psi.getText() + "=======PsiDirectory======psi=====" + psi.getName());
                for (PsiFile file : files) {
                    logger.warn(file.getText() + "=======PsiFile======psi=====" + file.getName());
                    String reName = NameFactory.createName(file.getName());
                    if (changeReName.get(reName) == null) {
                        changeReName.put(reName, file.getName());
                        logger.warn(file.getName() + "=======changeReName======psi=====" + reName);
                        dataBeans.add(new DataBean(file.getName(), reName));
                        reNamePsiFile(file, reName, project);
                    }

                }
            }


        }

//        PsiFile[] files = directory.getFiles();
//        for (PsiFile file : files) {
//            String reName = NameFactory.createName(file.getName());
//            dataBeans.add(new DataBean(file.getName(), reName));
//            reNamePsiFile(file, reName, project);
//            logger.warn(file.getText() + "=======PsiFile======psi=====" + file.getName());
//        }
        return dataBeans;
    }


    public void reNamePsiFile(PsiElement file, String reName, Project project) {
        logger.warn(file.getText() + "=======PsiFile======psi=====" + reName);
        RenameRefactoring rename = RefactoringFactory.getInstance(project).createRename(file, reName);
        rename.setSearchInComments(false);
        rename.setSearchInNonJavaFiles(false);
        rename.run();

    }


}
