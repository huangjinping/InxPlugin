package com.inxpl.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlTag;
import com.inxpl.bean.DataBean;
import com.inxpl.utils.DirectoryHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class XMLAction extends AnAction {
    private Project project;
    private static final Logger logger = Logger.getInstance(ExtractTextAction.class);
    private List<DataBean> dataBeans;

    private List<PsiFile> mPsiFile;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showMessageDialog("开始变形了", "提示", Messages.getInformationIcon());
        project = e.getData(PlatformDataKeys.PROJECT);
        PsiFile psiFile = e.getData(PlatformDataKeys.PSI_FILE);
        mPsiFile = new ArrayList<>();


        PsiFile[] filesByName = FilenameIndex.getFilesByName(project, "activity_main.xml", GlobalSearchScope.projectScope(project));
        PsiFile file = filesByName[0];
        System.out.println(file.getText());
        dataBeans = new LinkedList<>();
        file.accept(new XmlRecursiveElementVisitor(true) {
            @Override
            public void visitXmlTag(XmlTag tag) {
                super.visitXmlTag(tag);
                String valueText = tag.getAttributeValue("android:text");
                String valueHint = tag.getAttributeValue("android:hint");
                if (valueText != null && !valueText.isEmpty() && !valueText.contains("@string")) {
                    String key = "layoutName_";
                    dataBeans.add(new DataBean(key, valueText));
                }
                if (valueHint != null && !valueHint.isEmpty() && !valueHint.contains("@string")) {
                    String key = "layoutName_";
                    dataBeans.add(new DataBean(key, valueHint));
                }
                logger.warn(valueText + "==valueText=============valueHint=====" + valueHint);
            }
        });
//        changeXml(file);
//        writeContentXml();


//        logger.warn(psiFile.getParent().getText()+"==psiFile.getParent()==================" + psiFile.getParent().getName());

//        PsiDirectory baseDir = PsiDirectoryFactory.getInstance(project).createDirectory(project.getProjectFile());
//                logger.warn("==baseDir.getParent()==================" + baseDir.getName());

        logger.warn("=============getBasePath=====" + project.getBasePath());
        logger.warn("=============getName=====" + project.getName());
        logger.warn("=============getProjectFilePath=====" + project.getProjectFilePath());
        logger.warn("=============getProjectFile=====" + project.getProjectFile().getName());


        PsiDirectory baseDir = PsiDirectoryFactory.getInstance(project).createDirectory(project.getBaseDir());
        PsiFile[] files = baseDir.getFiles();
        for (PsiFile item : files) {
            logger.warn("=======PsiDirectory======getFiles=====" + item.getName());
        }


//        PsiDirectory baseDir1 = PsiDirectoryFactory.getInstance(project).createDirectory(project.getBaseDir());
//        logger.warn("==baseDir1.getParent()==================" + baseDir1.getName());
//
//        PsiDirectory baseDir2 = PsiDirectoryFactory.getInstance(project).createDirectory(project.getWorkspaceFile());
//        logger.warn("==baseDir2.getParent()==================" + baseDir2.getName());

//        String stringPath = project.getBasePath() + "/app/src/main/src";
//        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(stringPath);
//
//        FileViewProvider viewProvider = PsiManager.getInstance(project).findViewProvider(virtualFile);

        DirectoryHelper directoryHelper = new DirectoryHelper();
        directoryHelper.getResList(project);
//        getJavaList();

    }


//
//
//
//    private void getJavaList() {
//        VirtualFile resBir = getAppJavaBaseDir(project);
//        PsiDirectory directory = PsiDirectoryFactory.getInstance(project).createDirectory(resBir);
////        PsiDirectory[] subdirectories = directory.getSubdirectories();
//
//        getPsiDirectoryClass(directory);
//        for (PsiFile file : mPsiFile) {
//            logger.warn(file.getText() + "========getJavaList=====getFiles=====" + file.getName());
//        }
//
//    }
//
//
//    private void getPsiDirectoryClass(PsiDirectory directory) {
//        PsiDirectory[] sub = directory.getSubdirectories();
//        PsiFile[] files = directory.getFiles();
//        if (files != null && files.length > 0) {
//            List<PsiFile> psiFiles = Arrays.asList(files);
//            mPsiFile.addAll(psiFiles);
//        }
//
//        if (sub != null && sub.length > 0) {
//            for (PsiDirectory psiDir : sub) {
//                getPsiDirectoryClass(psiDir);
//            }
//        }
//
//    }
//
//
//    private void getResList() {
//        VirtualFile resBir = getAppResBaseDir(project);
//        PsiDirectory directory = PsiDirectoryFactory.getInstance(project).createDirectory(resBir);
//        PsiDirectory[] subdirectories = directory.getSubdirectories();
//        logger.warn("==========1==psi=====");
//
//        for (PsiDirectory psi : subdirectories
//        ) {
//            PsiFile[] files = psi.getFiles();
//            logger.warn(psi.getText() + "=======PsiDirectory======psi=====" + psi.getName());
//            for (PsiFile file : files) {
//                logger.warn(file.getText() + "=======PsiFile======psi=====" + file.getName());
//            }
//
//        }
//        logger.warn("==========2==psi=====");
//
//    }
//
//
//    public static VirtualFile getAppJavaBaseDir(Project project) {
//        String path = project.getBasePath() + File.separator +
//                "app" + File.separator +
//                "src" + File.separator +
//                "main" + File.separator +
//                "java" + File.separator;
//
//        return LocalFileSystem.getInstance().findFileByPath(path);
//    }
//
//
//
//
//
//    public static VirtualFile getAppResBaseDir(Project project) {
//        String path = project.getBasePath() + File.separator +
//                "app" + File.separator +
//                "src" + File.separator +
//                "main" + File.separator +
//                "java" + File.separator;
//        logger.warn("==========getAppResBaseDir=====" + path);
//
//
//        return LocalFileSystem.getInstance().findFileByPath(path);
//    }


    private void getRootManager() {
        final ProjectRootManager rootManager = ProjectRootManager.getInstance(project);

        final ProjectFileIndex fileIndex = rootManager.getFileIndex();
        final List<VirtualFile> sourceRoots = rootManager.getModuleSourceRoots(JavaModuleSourceRootTypes.SOURCES);
        logger.warn("==getRootManager=============getRootManager===1==" + fileIndex);

        List<String> names = new ArrayList<>();
        for (final VirtualFile sourceRoot : sourceRoots) {
            if (sourceRoot.isDirectory()) {
                final String packageName = fileIndex.getPackageNameByDirectory(sourceRoot);
                logger.warn("==getRootManager=============getRootManager==2===" + packageName);
                if (packageName != null && !packageName.isEmpty()) {
                    names.add(packageName);

                }
            }
        }
        logger.warn("==getRootManager=============getRootManager==3===" + names.toString());

    }

    /**
     * 改布局文件里的映射
     */
    private void changeXml(PsiFile file) {
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


    /**
     * 往strings.xml中写内容
     */
    private void writeContentXml() {
        try {
            String stringPath = project.getBasePath() + "/app/src/main/res/values/strings.xml";
            VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(stringPath);

            if (virtualFile != null) {
                Runnable runnable = () -> {
                    Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
                    if (document != null) {
                        String text = document.getText();
                        text = text.replace("\"app_name1\"", "\"app_name11111\"");
                        document.setText(text);
                    }
                };
                WriteCommandAction.runWriteCommandAction(project, runnable);
            } else {
                Messages.showMessageDialog("strings.xml文件没找到", "提示", Messages.getInformationIcon());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
