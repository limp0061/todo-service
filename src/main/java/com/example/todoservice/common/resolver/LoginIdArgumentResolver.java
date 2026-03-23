package com.example.todoservice.common.resolver;

import com.example.todoservice.common.annotation.LoginId;
import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.member.exception.MemberErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginIdArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * @LoginId 어노테이션이 붙은 Long 타입 파라미터를 처리
     * ex) @LoginId Long memberId
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginId.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Object memberId = request.getAttribute("memberId");

        if (memberId == null) {
            throw new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        return memberId;
    }
}
