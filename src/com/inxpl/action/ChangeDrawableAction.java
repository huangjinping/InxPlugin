package com.inxpl.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.inxpl.utils.DirectoryHelper;
import org.jetbrains.annotations.NotNull;

/**
 * 1、更换图片名称
 * 2、更换资源文件名称
 */
public class ChangeDrawableAction extends AnAction {
    private Project project;
    private static final Logger logger = Logger.getInstance(ExtractTextAction.class);


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        project = e.getData(PlatformDataKeys.PROJECT);
        DirectoryHelper directoryHelper = new DirectoryHelper();
        directoryHelper.handDrawable(project);

    }
}
