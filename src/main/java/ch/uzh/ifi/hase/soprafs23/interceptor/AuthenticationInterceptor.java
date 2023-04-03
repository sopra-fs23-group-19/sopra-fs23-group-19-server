package ch.uzh.ifi.hase.soprafs23.interceptor;
import ch.uzh.ifi.hase.soprafs23.annotation.PassToken;
import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
//import io.jsonwebtoken.Jwts;

import ch.uzh.ifi.hase.soprafs23.entity.User;

@Component
public class AuthenticationInterceptor  implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("authToken");
        // Get token from http request header.
//        schema of Authorization header looks like this: Bearer <token>,
//        therefore we need to remove the "Bearer " part. What remains, which is token itself.

        // if there is no Annotation
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //check if annotated with passtoken，if so, skip authentication
        //Although this @PassToken is not yet used in controller, keep it here for further development.
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //Check if Annotated with @UserLoginToken
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // peform simple token based authentication
                if (token == null) {
                    String tokenNullMessage = "Please log in with correct credentials. Not AUTHORIZED.";
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(tokenNullMessage));
                }
                //remove the "Bearer " part.
//                token=token.substring(7);
//                long userid;
//                String userpassword;
//                try {
//                    //try to decode.
//                    byte[] decoded = Base64.getDecoder().decode(token);
//                    String decodedStr = new String(decoded, StandardCharsets.UTF_8);
//                    String[] parts = decodedStr.split(":");
//                    userid = Long.parseLong(parts[0],10);
//                    userpassword = parts[1];
//                } catch (IllegalArgumentException j) {
//                    String tokenWrongMessage = "Please log in with correct credentials. Not AUTHORIZED.";
//                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(tokenWrongMessage));
//                }
//                User user0 = userService.getUserById(userid);
//                if (user0 == null) {
//                    String userNullMessage = "User with this id not found. Please log in with correct credentials.";
//                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(userNullMessage));
//                }
//                // verify token
//                if (!user0.getPassword().equals(userpassword)) {
//                    String passwordWrongMessage = "Please log in with correct credentials. Not AUTHORIZED.";
//                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(passwordWrongMessage));
//                }
                return true;
            }
        }
        return true;
    }

}