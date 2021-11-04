package com.inxpl.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.*;
import com.inxpl.bean.DataBean;
import com.inxpl.utils.DirectoryHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.string.xml 等资源（name=id 更换）
 * 2.layout.xml 中id 更换(dataBing ,ViewBinding 没有更换)
 *
 */
public class StringAction extends AnAction {
    private Project project;
    private static final Logger logger = Logger.getInstance(ExtractTextAction.class);


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        project = e.getData(PlatformDataKeys.PROJECT);
//        String stringPath = project.getBasePath() + "/app/src/main/res/values/strings.xml";
//        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(stringPath);
//        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
//        List<DataBean> androidStrings = parseStringsNewsXml(psiFile);
//


        DirectoryHelper directoryHelper = new DirectoryHelper();
        logger.warn("===========directoryHelper=====收集信息开始==");
        List<DataBean> androidStrings = directoryHelper.handAllValues(project);
        logger.warn(androidStrings.toString());
        logger.warn("===========directoryHelper=====收集信息结束==");


        logger.warn("===========directoryHelper=====收集id开始==");
        List<DataBean> dataBeans = directoryHelper.handAllIds(project);
        androidStrings.addAll(dataBeans);
        logger.warn(dataBeans.toString());
        logger.warn("===========directoryHelper=====收集id结束==");

        List<PsiFile> resList = directoryHelper.getResList(project);
        resList.addAll(directoryHelper.getJavaList(project));
        resList.add(directoryHelper.getMiniFast(project));
        directoryHelper.changeListFile(resList, project, androidStrings);
    }


    private List<DataBean> parseStringsNewsXml(@NotNull PsiFile stringsFile) {
        final List<DataBean> androidStrings = new ArrayList<>();
        final XmlFile xmlFile = (XmlFile) stringsFile;
        final XmlDocument document = xmlFile.getDocument();


        if (document == null) {
            return androidStrings;
        }
        final XmlTag rootTag = document.getRootTag();
        if (rootTag == null) {
            return androidStrings;
        }
        final XmlTag[] stringTags = rootTag.findSubTags("string");
        for (XmlTag stringTag : stringTags) {


            final String name = stringTag.getAttributeValue("name");
            final String translatableStr = stringTag.getAttributeValue("translatable");
            String reName = "harris" + name;
            androidStrings.add(new DataBean("name=\"" + name + "\"", "name=\"" + reName + "\""));
            androidStrings.add(new DataBean("\"@string/" + name + "\"", "\"@string/" + reName + "\""));
            androidStrings.add(new DataBean("R.string." + name, "R.string." + reName));

        }
        return androidStrings;
    }


//    private List<AndroidString> parseStringsXml(@NotNull PsiFile stringsFile) {
//        final List<AndroidString> androidStrings = new ArrayList<>();
//        final XmlFile xmlFile = (XmlFile) stringsFile;
//        final XmlDocument document = xmlFile.getDocument();
//        if (document == null) {
//            return androidStrings;
//        }
//        final XmlTag rootTag = document.getRootTag();
//        if (rootTag == null) {
//            return androidStrings;
//        }
//        final XmlTag[] stringTags = rootTag.findSubTags("string");
//        for (XmlTag stringTag : stringTags) {
//            final String name = stringTag.getAttributeValue("name");
//            final String translatableStr = stringTag.getAttributeValue("translatable");
//            final boolean translatable = Boolean.parseBoolean(translatableStr == null ? "true" : translatableStr);
//
//            final List<AndroidString.Content> contents = new ArrayList<>();
//            final XmlTagChild[] contentTags = stringTag.getValue().getChildren();
//            for (XmlTagChild content : contentTags) {
//                if (content instanceof XmlText) {
//                    final XmlText xmlText = (XmlText) content;
//                    final String text = xmlText.getValue();
//                    contents.add(new AndroidString.Content(text));
//                } else if (content instanceof XmlTag) {
//                    final XmlTag xmlTag = (XmlTag) content;
//                    if (!xmlTag.getName().equals("xliff:g")) {
//                        continue;
//                    }
//                    final String text = xmlTag.getValue().getText();
//                    final String id = xmlTag.getAttributeValue("id");
//                    final String example = xmlTag.getAttributeValue("example");
//                    contents.add(new AndroidString.Content(text, id, example, true));
//                }
//            }
//            androidStrings.add(new AndroidString(name, contents, translatable));
//        }
//        return androidStrings;
//    }
}
