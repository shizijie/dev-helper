package com.shizijie.dev.helper.web.drools;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

public class DroolsUtils {
    public static KieSession getKieSessionFromDrl(String drl) throws UnsupportedEncodingException {
        KieSession tempKieSession = null;
        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kb.add(ResourceFactory.newByteArrayResource(drl.getBytes("utf-8")), ResourceType.DRL);
        if (kb.hasErrors()) {
            String errorMessage = kb.getErrors().toString();
            System.out.println("规则语法异常---\n"+errorMessage);
            throw new UnsupportedEncodingException(errorMessage);
        }
        InternalKnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addPackages(kb.getKnowledgePackages());
        tempKieSession = kBase.newKieSession();
        return tempKieSession;
    }
    public static KieSession getKieSessionFromDrl(Collection<String> drl) throws UnsupportedEncodingException {
        KieSession tempKieSession = null;
        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
        for(String rule:drl){
            kb.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);
        }
        if (kb.hasErrors()) {
            String errorMessage = kb.getErrors().toString();
            System.out.println("规则语法异常---\n"+errorMessage);
            throw new UnsupportedEncodingException(errorMessage);
        }
        InternalKnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addPackages(kb.getKnowledgePackages());
        tempKieSession = kBase.newKieSession();
        return tempKieSession;
    }
}
