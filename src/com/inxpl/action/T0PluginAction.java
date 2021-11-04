package com.inxpl.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 1.包名乱序 （目前只能更换统一包名）
 * 2.更换类名 （DataBinding viewBing  处理 未实现）
 */
public class T0PluginAction extends BaseGenerateAction {


    public T0PluginAction() {
        super(null);
    }

    public T0PluginAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    private static final Logger logger = Logger.getInstance(T0PluginAction.class);

    private void createOnE(AnActionEvent e) {
        Project project = e.getProject();
        JavaDirectoryService directoryService = JavaDirectoryService.getInstance();
        IdeView ideView = e.getRequiredData(LangDataKeys.IDE_VIEW);
        PsiDirectory directory = ideView.getOrChooseDirectory();
        Map<String, String> map = new HashMap<>();
        PsiClass psiClass = directoryService.createClass(directory, "user", "first", false, map);
        writeField("ddd", project, psiClass);

    }

    private void writeField(String pasteStr, Project project, PsiClass psiClass) {
        PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiField field = elementFactory.createFieldFromText(pasteStr, psiClass);
        psiClass.add(field);
    }


    private void reNamePackage(PsiClass targetClass, Project project) {
        PsiJavaFile javaFile = (PsiJavaFile) targetClass.getContainingFile();
        PsiPackage psiPackage = JavaPsiFacade.getInstance(project)
                .findPackage(javaFile.getPackageName());

        RenameRefactoring rename = RefactoringFactory.getInstance(project).createRename(psiPackage, "renamePagecage");
        rename.setSearchInComments(false);
        rename.setSearchInNonJavaFiles(false);
        rename.run();
        logger.warn(targetClass.getQualifiedName() + "=======" + psiPackage.getText() + "==parent====2===" + rename.getNewNames());
        logger.warn(targetClass.getQualifiedName() + "====getQualifiedName=========");

    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showMessageDialog("我是To条消息", "提示", Messages.getInformationIcon());
        // 获取编辑器中的文件
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        // 获取当前类
        PsiClass targetClass = getTargetClass(editor, file);


//        PsiMethod[] onActions = targetClass.findMethodsByName("onAction", true);
//        logger.warn(targetClass.getQualifiedName() + "=========changeParamger====1===");
//        if (onActions.length > 0) {
//            logger.warn(targetClass.getQualifiedName() + "=========changeParamger====2===" + onActions[0].getName());
//            RenameRefactoring rename = RefactoringFactory.getInstance(project).createRename(onActions[0], "c_1");
//            rename.setSearchInComments(false);
//            rename.setSearchInNonJavaFiles(false);
//            rename.run();
//            logger.warn(targetClass.getQualifiedName() + "=========changeParamger====3===" + rename.getNewNames().toString());
//        }

//        logger.warn(targetClass.getQualifiedName() + "=========changeParamger====4===");
//        logger.warn(targetClass.getQualifiedName() + "=========createRename====before===" + targetClass.getName());
//        RenameRefactoring rename = RefactoringFactory.getInstance(project).createRename(targetClass, "c_" + targetClass.getName());
//        rename.setSearchInComments(false);
//        rename.setSearchInNonJavaFiles(false);
//        rename.run();
//        logger.warn(targetClass.getQualifiedName() + "=========createRename====after===" + targetClass.getName());


        // 获取元素操作的工厂类
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);

        // 生成代码
        new LayoutCreator(project, targetClass, factory, file).execute();

        PsiManager.getInstance(project).addPsiTreeChangeListener(new PsiTreeChangeListener() {
            @Override
            public void beforeChildAddition(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }

            @Override
            public void beforeChildRemoval(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }

            @Override
            public void beforeChildReplacement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }

            @Override
            public void beforeChildMovement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }

            @Override
            public void beforeChildrenChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }

            @Override
            public void beforePropertyChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }

            @Override
            public void childAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }

            @Override
            public void childRemoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }

            @Override
            public void childReplaced(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }

            @Override
            public void childrenChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }

            @Override
            public void childMoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }

            @Override
            public void propertyChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

            }
        });
    }


}
