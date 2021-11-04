package com.inxpl.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.inxpl.utils.xml.Files;
import com.inxpl.utils.xml.Formatter;
import com.inxpl.utils.xml.HardDimensReplaceFromatter;
import com.inxpl.utils.xml.HardStringsReplaceFromatter;

//https://github.com/shang1101/LayoutFormat_androidstudioplugin   思路来源

/**
 * 1.接受一个插入关键字
 * 2.在1这个关键标签前面插入代码
 * 3.布局文件属性排序顺序更换
 * 4.生成修正后的代码layout
 */
public class JunkXMLAction extends AnAction {

    private static final Logger logger = Logger.getInstance(ExtractTextAction.class);

    @Override
    public void actionPerformed(final AnActionEvent event) {
        final Project project = getEventProject(event);
        VirtualFile file = event.getData(LangDataKeys.VIRTUAL_FILE);
        System.out.println("All formatted files:");
        HardStringsReplaceFromatter.getInstance().initKeyValuesMap(file);
        HardDimensReplaceFromatter.getInstance().initKeyValuesMap(file);
        execute(project, file);
        systemReformat(event);
    }


    private void systemReformat(final AnActionEvent event) {
        event.getActionManager()
                .getAction(IdeActions.ACTION_EDITOR_REFORMAT)
                .actionPerformed(event);
    }


    private void execute(Project project, final VirtualFile file) {
        VirtualFile[] files = file.getChildren();
        if (files.length > 0) {
            for (VirtualFile _file : files) {
                if (Files.isXmlFileOrDir(_file)) {
                    execute(project, _file);
                }
            }
        } else {
            System.out.println(file.getPath());
            final Document document = FileDocumentManager.getInstance().getDocument(file);

            WriteCommandAction.runWriteCommandAction(project, () -> {
                String txt = document.getText();
                document.setText(Formatter.apply(txt));
            });

        }
    }
//    /**
//     * 生成 contract类内容
//     * create Contract Model Presenter
//     */
//    private void setFileDocument() {
//
//
//        int lastIndex = _content.lastIndexOf("}");
//        _content = _content.substring(0, lastIndex);
//        MessagesCenter.showDebugMessage(_content, "debug");
//        final String content = setContractContent();
//        //wirte in runWriteAction
//        WriteCommandAction.runWriteCommandAction(_editor.getProject(),
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        _editor.getDocument().setText(content);
//                    }
//                });
//
//    }

    @Override
    public void update(AnActionEvent event) {
        super.update(event);
        final VirtualFile file = CommonDataKeys.VIRTUAL_FILE.getData(event.getDataContext());
        event.getPresentation().setVisible(Files.isXmlFileOrDir(file));
    }


}
