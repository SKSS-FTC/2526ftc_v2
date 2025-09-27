package com.rframeworks.processor;

import com.google.auto.service.AutoService;
import com.rframeworks.di.Injectable;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class RFrameworksProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private final Set<TypeElement> injectableClasses = new HashSet<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Injectable.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            generateRegistry();
            return true;
        }

        for (Element e : roundEnv.getElementsAnnotatedWith(Injectable.class)) {
            if (e instanceof TypeElement) {
                injectableClasses.add((TypeElement) e);
            }
        }

        return true;
    }

    private void generateRegistry() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("registerAll")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class);

        ClassName supplier = ClassName.get("java.util.function", "Supplier");

        for (TypeElement clazz : injectableClasses) {
            Injectable ann = clazz.getAnnotation(Injectable.class);
            if (ann == null) continue;

            String key = ann.value();
            ClassName clazzName = ClassName.get(clazz);

            messager.printMessage(Diagnostic.Kind.NOTE,
                    "Registering @" + key + " -> " + clazzName);

            methodBuilder.addStatement("$T.register($S, ($T<$T>) $T::new)",
                    ClassName.get("com.rframeworks.di", "Container"),
                    key,
                    supplier,
                    ClassName.get(Object.class),
                    clazzName);
        }

        TypeSpec registryClass = TypeSpec.classBuilder("GeneratedRegistry")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodBuilder.build())
                .build();

        JavaFile javaFile = JavaFile.builder("com.rframeworks.di.generated", registryClass)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException ex) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "Failed to write GeneratedRegistry: " + ex.getMessage());
        }
    }
}
