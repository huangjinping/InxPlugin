package com.inxpl.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.psi.xml.XmlTag;
import com.inxpl.bean.DataBean;
import com.inxpl.utils.DirectoryHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//https://plugins.jetbrains.com/docs/intellij/modifying-psi.html#creating-the-new-psi

//Just as everywhere else in the IntelliJ Platform API, the text passed to createFileFromText() and other createFromText() methods must use only \n as line separators.

/**
 * 1.方法内插入代码 （有方法插入的思路）
 * 2.方法名称修改 （ todo继承和实现方法）
 * 3.插入成员变量并在方法体重引用  （未实现）
 */
public class JunkAction extends BaseGenerateAction {
    private static final Logger logger = Logger.getInstance(ExtractTextAction.class);

    public JunkAction() {
        this(null);
    }

    public JunkAction(CodeInsightActionHandler handler) {
        super(handler);
    }


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
//        // 获取当前类
        PsiClass targetClass = getTargetClass(editor, file);

        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
        DirectoryHelper directoryHelper = new DirectoryHelper();


//        logger.warn("=================12=================");
//        logger.warn(targetClass.getText());
//        logger.warn("=================13================");
//
//        PsiMethod[] onActions = targetClass.findMethodsByName("onAction", false);
//
//        if (onActions != null && onActions.length > 0) {
//            PsiMethod method = onActions[0];
//            directoryHelper.reNamePsiFile(method, "harrusOnacion", project);
//
//        }

//
//

//            logger.warn(method.getText());
//            logger.warn(method.getName());

//            innerMethod(method);
//
//            PsiMethod[] superMethods = method.findSuperMethods();
//            for (PsiMethod child : superMethods
//            ) {
//                logger.warn("=============child====method==1===============" + child.getName());
//                logger.warn("==============child===method==1===============" + child.getText());
//            }
//            final PsiClass superClass = method.getContainingClass();
        logger.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>stat>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        reNameClass(targetClass);
//        createText();
//        demo1();
//        createTemplate("TextView.ftl");
        logger.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>end>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

//        PsiField[] allFields = targetClass.getFields();
//        for (PsiField field : allFields) {
//            logger.warn("=================field==1===============");
//            logger.warn(field.getText());
//            logger.warn(field.getName());
//            directoryHelper.reNamePsiFile(field, NameFactory.createName(field.getName()), project);
//            logger.warn("=================field==2==============");
//        }
//        createOnE(e);


        List<DataBean> androidStrings = parseStringsNewsXml(project);
        logger.warn(androidStrings.toString());
        logger.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>end1>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


    }


    private List<DataBean> parseStringsNewsXml(Project project) {

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

//                    if (id != null && !id.isEmpty() && id.contains("@+id/")) {
//                        id = id.replace("@+id/", "");
//                        String reName = NameFactory.createName(id);
//                        dataBeans.add(new DataBean("\"@+id/" + id + "\"", "\"@+id/" + reName + "\""));
//                        dataBeans.add(new DataBean("\"@id/" + id + "\"", "\"@id/" + reName + "\""));
//                        dataBeans.add(new DataBean("R.id." + id + "", "R.id." + reName + ""));
//
//                    }
                }
            });
        }
        return null;
    }

    public VirtualFile getLayoutDir(Project project) {
        String path = project.getBasePath() + File.separator +
                "app" + File.separator +
                "src" + File.separator +
                "main" + File.separator +
                "res" + File.separator +
                "layout" + File.separator +
                "activity_user.xml";

        return LocalFileSystem.getInstance().findFileByPath(path);
    }


    private void createText() {
        try {
            String res = readResources("TextView.ftl");
            Map textParamter = getTextParamter();
            Template template = new Template("strTpl", res, new Configuration(new Version("2.3.23")));
            StringWriter result = new StringWriter();
            template.process(textParamter, result);
            System.out.println(result.toString());
            logger.warn(result.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.warn("==========IOException================" + ex.getMessage());
        }
    }


    public String readResources(String templateName) throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/ftl/" + templateName);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = "";
        String configContentStr = "";
        try {
            while ((s = br.readLine()) != null) {
                configContentStr = configContentStr + s;
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        logger.warn("=====readResources=======" + configContentStr);
        return configContentStr;
    }


    private void reNameClass(PsiClass targetClass) {

        PsiMethod[] methods = targetClass.getMethods();
        for (PsiMethod method : methods
        ) {
            renameMethod(targetClass, method);
        }
        PsiClass[] innerClasses = targetClass.getInnerClasses();
        if (innerClasses != null && innerClasses.length > 0) {
            for (PsiClass psiClass : innerClasses) {
                reNameClass(psiClass);
            }
        }


    }


//    public static void createJavaTemplate(String temp, String pathName) {
//        try {
//
//            Template template = getConfiguration().getTemplate("ftl/" + temp);
//            Map paramter = getJavaParamter();
//            StringWriter result = new StringWriter();
//            template.process(paramter, result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    private static Configuration getConfiguration() throws IOException {
        Configuration configuration = new Configuration(Configuration.getVersion());

        configuration.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir")));
        configuration.setDefaultEncoding("utf-8");
        return configuration;
    }


    private void renameMethod(PsiClass psiClass, PsiMethod psiMethod) {
        PsiCodeBlock body = psiMethod.getBody();
        PsiStatement[] statement = body.getStatements();
        logger.warn(psiClass.getName() + "======psiMethod.getName()===========" + psiMethod.getName());
        logger.warn(psiClass.getName() + "=psiMethod.getText()===========" + psiMethod.getText());
        for (PsiStatement psiStatement : psiMethod.getBody().getStatements()) {
            if (psiStatement instanceof PsiExpressionStatement) {
                PsiExpressionStatement psiExpressionStatement = (PsiExpressionStatement) psiStatement;
                PsiExpression expression = psiExpressionStatement.getExpression();
                if (expression instanceof PsiMethodCallExpression) {
                    PsiMethodCallExpression callExpression = (PsiMethodCallExpression) expression;
                    PsiExpressionList argumentList = callExpression.getArgumentList();
                    PsiExpression[] expressions = argumentList.getExpressions();
                    if (expressions.length > 0) {
                        if (expressions[0] instanceof PsiNewExpression) {
                            PsiNewExpression psiExpression = (PsiNewExpression) expressions[0];
                            PsiAnonymousClass anonymousClass = psiExpression.getAnonymousClass();
                            PsiMethod[] methods = anonymousClass.getMethods();
                            for (PsiMethod method : methods) {
                                logger.warn("=========method==========" + method.getName());
                            }
                        }
                    }

                }
            }

            logger.warn(psiClass.getName() + "=psiStatement.getFirstChild()===========" + psiStatement.getFirstChild());
            logger.warn(psiClass.getName() + "=psiStatement.getText()===========" + psiStatement.getText());

        }

    }

    private static Map getTextParamter() {
        Map map = new HashMap();
        map.put("width", "100");
        map.put("height", "100");
        return map;
    }


    public static void createTemplate(String temp) {
        try {


            Template template = getConfiguration().getTemplate("ftl/" + temp);
            Map paramter = getTextParamter();
            StringWriter result = new StringWriter();
            template.process(paramter, result);
            System.out.println(result.toString());
            logger.warn("===createTemplate===" + result.toString());
        } catch (Exception e) {
            logger.warn("Exception========" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void demo1() {
        try {
            try {
                Map map = new HashMap();
                map.put("name", "张三");
                map.put("money", 10.155);
                map.put("point", 10);
                Template template = new Template("strTpl", "您好${name}，晚上好！您目前余额：${money?string(\"#.##\")}元，积分：${point}", new Configuration(new Version("2.3.23")));
                StringWriter result = new StringWriter();
                template.process(map, result);
                System.out.println(result.toString());
                logger.warn(result.toString());
                //您好张三，晚上好！您目前余额：10.16元，积分：10
            } catch (Exception e) {
                e.printStackTrace();
            }
            //您好张三，晚上好！您目前余额：10.16元，积分：10
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 可以修改名称
     *
     * @param psiMethod
     * @param targetClass
     * @param factory
     * @param project
     */
    private void innerMethod(PsiMethod psiMethod, PsiClass targetClass, PsiElementFactory factory, Project project) {

        Runnable runnable = () -> {
            // 获取setContentView
            // onCreate是否存在initView方法
            boolean hasInitViewStatement = true;
            for (PsiStatement psiStatement : psiMethod.getBody().getStatements()) {
                logger.warn("=================psiStatement===============" + psiStatement.getText());
                PsiElement firstChild = psiStatement.getFirstChild();
                String text = "if (true){int a =12; User name= new User(); double age=1.00;}";
                text = "try {\n" +
                        "            User user = new User();\n" +
                        "            user.setHarrisage(1);\n" +
                        "            user.setName(\"name\");\n" +
                        "            user.setHarrisaddress(\"address\");\n" +
                        "            user.toString();\n" +
                        "        } catch (Exception e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }";
                text = text.replace("n\"", "");//切记这个操作否则异常
                PsiStatement statementFromText = factory.createStatementFromText(text, null);
//                PsiExpression expression = factory.createExpressionFromText(text, null);
                psiStatement.getParent().addBefore(statementFromText, psiStatement);
                logger.warn("=================firstChild===============" + firstChild.getText());

//                1
//                String text = "if (true){\n" +
//                        "                    \n" +
//                        "                }";
//                PsiStatement statementFromText = factory.createStatementFromText(text, null);
//                psiStatement.getParent().addBefore(statementFromText, psiStatement);


//                psiMethod.getBody().addAfter(factory.createStatementFromText(text, null), setContentViewStatement);
            }
        };
        WriteCommandAction.runWriteCommandAction(project, runnable);


    }


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


//    @Override
//    public void actionPerformed(AnActionEvent e) {
//        // TODO: insert action logic here
//        Project fatherProject = e.getProject();
//        Project project = e.getData(PlatformDataKeys.PROJECT);
//
//        logger.warn("======================" + fatherProject.getName());
//        if (fatherProject == null) {
//            return;
//        }
//        logger.warn("==============1========" + fatherProject.getName());
//
//        //获取所选的目录，即需要添加类的的包路径file
//        VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
//
//        if (virtualFile == null || !virtualFile.isDirectory()) {
//            return;
//        }
//        logger.warn("==============3=======" + fatherProject.getName());
//
//        //通过所选文件，获取包的directory
//        PsiDirectory directory = PsiDirectoryFactory.getInstance(fatherProject).createDirectory(virtualFile);
//        //添加类
//        logger.warn("==============4=======" + fatherProject.getName());
//
////        JavaDirectoryService.getInstance().createClass(directory, "", "MVPTemplateClass");
//        Map<String, String> mapParams = new HashMap<>();
//        mapParams.put("NAME", "MyClass");
//        mapParams.put("PACKAGE_NAME", "com.test.com");
//        mapParams.put("USER", "UserName");
//        mapParams.put("DATE", "2020---1029--10");
//        JavaDirectoryService.getInstance().createClass(directory, "MyTest", "MVPTemplateClass", false, mapParams);
//        logger.warn("==============5=======" + fatherProject.getName());
//
//    }


}
