package com.shizijie.dev.helper.web.drools;

import org.drools.core.impl.KnowledgeBaseImpl;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.utils.KieHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    private static String rule="package rules.test;\n" +
            "import com.shizijie.dev.helper.web.drools.UserInfo;\n" +
            "import com.shizijie.dev.helper.web.drools.RuleInfo;\n" +
            "dialect  \"mvel\"\n" +
            "\n" +
            "rule \"test\"\n" +
            "    when\n" +
            "      $userInfo:UserInfo(num>=5);\n" +
            "      $ruleInfo:RuleInfo();\n" +
            "    then\n" +
            "     System.out.println(\"OK!\");\n" +
            "     $ruleInfo.setResultinfo(\"该用户证件下有5个电话号码！\");\n" +
            "end";

    private static String rule2="package rules.test;\n" +
            "import com.shizijie.dev.helper.web.drools.UserInfo;\n" +
            "import com.shizijie.dev.helper.web.drools.RuleInfo;\n" +
            "dialect  \"mvel\"\n" +
            "\n" +
            "rule \"test2\"\n" +
            "    when\n" +
            "      $userInfo:UserInfo(num==3);\n" +
            "      $ruleInfo:RuleInfo();\n" +
            "    then\n" +
            "     System.out.println(\"OKqqqqq!\");\n" +
            "     $ruleInfo.setResultinfo(\"该用户证件下有3个电话号码！\");\n" +
            "end";

    public static void main(String[] args) throws UnsupportedEncodingException {
//        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
//        KieHelper helper = new KieHelper();
//        helper.addContent(rule, ResourceType.DRL);
//        long startNewKiesession = System.currentTimeMillis();
//        KnowledgeBaseImpl kieBase = (KnowledgeBaseImpl) helper.build();
//        long endNewKiesession = System.currentTimeMillis();
//        System.out.println("创建规则库："+ (endNewKiesession - startNewKiesession));
//        KieSession kieSession = kieBase.newKieSession();
        long startNewKiesession = System.currentTimeMillis();
        List<String> list=new ArrayList<>();
        list.add(rule);
        list.add(rule2);
        KieSession kieSession = DroolsUtils.getKieSessionFromDrl(list);
        long endNewKiesession = System.currentTimeMillis();
        System.out.println("创建规则库："+ (endNewKiesession - startNewKiesession));
        UserInfo userInfo = new UserInfo();
        userInfo.setNum(3);
        RuleInfo ruleInfo = new RuleInfo();
        startNewKiesession = System.currentTimeMillis();
        int icount =   drools(kieSession,userInfo,ruleInfo);
        System.out.println("icount : "+icount +" 耗时："+(System.currentTimeMillis()-startNewKiesession));
        if(icount>0){
            System.out.println("输出：" + ruleInfo.getResultinfo());
        }
        //-----------------------------------------
        userInfo.setNum(5);
        startNewKiesession = System.currentTimeMillis();
        icount =   drools(kieSession,userInfo,ruleInfo);
        System.out.println("icount : "+icount +" 耗时："+(System.currentTimeMillis()-startNewKiesession));
        if(icount>0){
            System.out.println("输出：" + ruleInfo.getResultinfo());
        }
        //-----------------------------------------
        startNewKiesession = System.currentTimeMillis();
        check(userInfo,ruleInfo);
        System.out.println("jdk 耗时："+(System.currentTimeMillis()-startNewKiesession));
    }
    private static int drools(KieSession kieSession,UserInfo userInfo,RuleInfo ruleInfo){
        FactHandle handle = kieSession.insert(userInfo);
        kieSession.insert(ruleInfo);
        int icount=kieSession.fireAllRules();
        kieSession.delete(handle);
        return icount;
    }


    private static void check(UserInfo userInfo,RuleInfo ruleInfo){
        if(userInfo.getNum()>=5){
            System.out.println("OK!");
            ruleInfo.setResultinfo("该用户证件下有5个电话号码！");
        }
        if(userInfo.getNum()==3){
            System.out.println("OK!11111");
            ruleInfo.setResultinfo("该用户证件下有3个电话号码！");
        }
    }
}
