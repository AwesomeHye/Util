package kr.datasolution.practice.hyein.annotation;

import kr.datasolution.practice.hyein.HyeinPracticeApplication;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.CommandLineRunner;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class LogProcessTimeAspect {

    @Around("@annotation(LogProcessTime)")
    public Object logProcessTime(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime = System.currentTimeMillis();

        /*//클래스에 붙은 애노테이션 가져와서 LogProcessTime의 값 가져오기 - 되는데 클래스명을 지정해야해서 불편
        Method[] methods = HyeinPracticeApplication.class.getMethods();
        for(Method method: methods){
            if(method.isAnnotationPresent(LogProcessTime.class)){
                LogProcessTime logProcessTimes = method.getAnnotation(LogProcessTime.class);
                log.info("prefix: {}", logProcessTimes.prefix());
            }
        }*/



        /*
        //spEL - 안됨
        Object[] args = joinPoint.getArgs();
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(logProcessTime.prefix());
        String prefix = (String) expression.getValue(args);
        log.info("prefix2: {}", prefix);*/

        /*
        //변수 확인
        Signature signature = joinPoint.getSignature();
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();

        System.out.println("Signature : " + signature.toString());
        System.out.println("target : " + target.toString());
        System.out.println("name : " + signature.getName());
        System.out.println("longName : " + signature.toLongString());
        System.out.println("shortName : " + signature.toShortString());

        for(int i=0; i < args.length; i++){
            System.out.println("args[" + i + "] : " + args[i].toString());
        }*/

        //joinpoint의 메소드 가져오기
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        //joinpoint 메소드에 붙은 LogProcessTime 애노테이션 객체와 멤버필드 가져오기
        LogProcessTime logProcessTime = method.getAnnotation(LogProcessTime.class);
        String prefix = logProcessTime.prefix();

        Object proceed = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        log.info("{} process time: {}ms", prefix, (endTime-startTime)/1000.0);
        return proceed;
    }

}
